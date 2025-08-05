package net.echo.echotweaks.block;

import java.util.Optional;
import java.util.function.Function;

import net.echo.echotweaks.EchoTweaks;
import net.echo.echotweaks.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {

	public static class ItemSettingsHelper {
		public static final Optional<Item.Settings>
			  DEFAULT_ITEM = Optional.of(new Item.Settings())
			, NO_ITEM = Optional.empty();
	} 

	public static final Block CHARCOAL_BLOCK = registerBasic(
		"charcoal_block",
		AbstractBlock.Settings.create()
			.requiresTool()
			.strength(5.0F, 6.0F)
	);



	public static void init() {
		// Charcoal Block as fuel
		FuelRegistryEvents.BUILD.register((builder, context) -> {
			builder.add(ModBlocks.CHARCOAL_BLOCK.asItem(), 16_000);
		});
		
		// Charcoal Block flammable
		FlammableBlockRegistry.getDefaultInstance().add(CHARCOAL_BLOCK, 5, 5);

		registerInventoryEntries();
	}
	
	private static Block registerBasic(String name, AbstractBlock.Settings blockSettings) {
		return register(name, blockSettings, ItemSettingsHelper.DEFAULT_ITEM, Block::new);
	}
	private static Block register(String name, AbstractBlock.Settings blockSettings, Optional<Item.Settings> itemSettings, Function<AbstractBlock.Settings, Block> blockFactory) {
		RegistryKey<Block> blockKey = keyOf(name);
		Block block = blockFactory.apply(blockSettings.registryKey(blockKey));

		if(itemSettings.isPresent()) {
			RegistryKey<Item> itemKey = ModItems.keyOf(name);
			BlockItem blockItem = new BlockItem(block, itemSettings.get().registryKey(itemKey));
			Registry.register(Registries.ITEM, itemKey, blockItem);
		}

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	private static void registerInventoryEntries() {
		registerInventoryEntry(CHARCOAL_BLOCK, ItemGroups.BUILDING_BLOCKS, Items.COAL_BLOCK);
	}
	private static void registerInventoryEntry(Block modBlock, RegistryKey<ItemGroup> group, Item addAfterItem) {
		ItemGroupEvents.modifyEntriesEvent(group)
			.register((itemGroup) -> itemGroup.addAfter(addAfterItem, modBlock.asItem()));
	}
	public static RegistryKey<Block> keyOf(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(EchoTweaks.MOD_ID, name));
	}
}
