package net.echo.echotweaks;

import java.util.function.Function;

import net.echo.echotweaks.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {
	public static void init() {
		FuelRegistryEvents.BUILD.register((builder, context) -> {
			builder.add(ModBlocks.CHARCOAL_BLOCK.asItem(), 16_000);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
			.register((itemGroup) -> itemGroup.addAfter(Items.COAL_BLOCK, CHARCOAL_BLOCK.asItem()));

		FlammableBlockRegistry.getDefaultInstance().add(CHARCOAL_BLOCK, 5, 5);
	}
	
	private static Block register(String name, AbstractBlock.Settings blockSettings, Item.Settings itemSettings, Boolean shouldRegisterItem, Function<AbstractBlock.Settings, Block> blockFactory) {
		RegistryKey<Block> blockKey = keyOf(name);
		Block block = blockFactory.apply(blockSettings.registryKey(blockKey));

		if(shouldRegisterItem) {
			RegistryKey<Item> itemKey = ModItems.keyOf(name);
			BlockItem blockItem = new BlockItem(block, itemSettings.registryKey(itemKey));
			Registry.register(Registries.ITEM, itemKey, blockItem);
		}

		return Registry.register(Registries.BLOCK, blockKey, block);
	}
	private static Block register(String name) {
		return register(
			name,
			AbstractBlock.Settings.create(),
			new Item.Settings(),
			true,
			Block::new
		);
	}
	public static RegistryKey<Block> keyOf(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(EchoTweaks.MOD_ID, name));
	}

	public static final Block CHARCOAL_BLOCK = register("charcoal_block");
}
