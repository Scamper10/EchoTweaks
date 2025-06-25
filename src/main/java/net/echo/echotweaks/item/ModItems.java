package net.echo.echotweaks.item;

import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.echo.echotweaks.EchoTweaks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public class ModItems {

	public static void init() {
		FuelRegistryEvents.BUILD.register((builder, context) -> {
			builder.add(ModItems.BIG_STICK, 150);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
			.register((itemGroup) -> itemGroup.add(ModItems.BIG_STICK));
	}

	private static Item register(String name, Item.Settings settings, Function<Item.Settings, Item> itemFactory) {
		RegistryKey<Item> itemKey = keyOf(name);
		Item item = itemFactory.apply(settings.registryKey(itemKey));
		Registry.register(Registries.ITEM, itemKey, item);
		return item;
	}
	private static Item register(String name, Item.Settings settings) {
		return register(name, settings, Item::new);
	}

	public static RegistryKey<Item> keyOf(String name) {
		return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(EchoTweaks.MOD_ID, name));
	}

	public static final Item BIG_STICK = ModItems.register(
		"big_stick",
		BigStickItem.createSettings(),
		BigStickItem::new
	);

}
