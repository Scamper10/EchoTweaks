package net.echo.echotweaks.loot;

import net.echo.echotweaks.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.registry.Registries;

public class CreeperLootTableModification {
	private static EntityPredicate.Builder IS_WITCH = EntityPredicate.Builder.create()
		.type(EntityTypePredicate.create(Registries.ENTITY_TYPE, EntityType.WITCH))
		;
	
	private static DamageSourcePredicate.Builder BY_WITCH = new DamageSourcePredicate.Builder().sourceEntity(IS_WITCH);

	public static void addPools(LootTable.Builder tableBuilder) {
		LootPool.Builder poolBuilder = LootPool.builder()
			.conditionally(DamageSourcePropertiesLootCondition.builder(BY_WITCH))
			.with(ItemEntry.builder(ModItems.MUSIC_DISC_EVENING_SHIFTS))
			;
		tableBuilder.pool(poolBuilder);
	}
}
