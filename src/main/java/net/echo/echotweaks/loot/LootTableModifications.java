package net.echo.echotweaks.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;

public class LootTableModifications {

	private static final RegistryKey<LootTable> CREEPER_LOOT_TABLE_KEY = EntityType.CREEPER.getLootTableKey().get();

	public static void apply() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

			// Don't touch datapacks
			if(!source.isBuiltin())
				return;

			if(key.getValue() == CREEPER_LOOT_TABLE_KEY.getValue()) {
				CreeperLootTableModification.addPools(tableBuilder);
			}

		});
	}

}
