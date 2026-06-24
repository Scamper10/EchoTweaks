package net.echo.echotweaks.criterion;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class LeadSnapsCriterion extends AbstractCriterion<LeadSnapsCriterion.Conditions> {
	
	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public void trigger(ServerPlayerEntity player, Entity entity) {
		LootContext entityContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		trigger(player, conditions -> conditions.requirementsMet(entityContext));
	}

	public record Conditions(
		  Optional<LootContextPredicate> player
		, LootContextPredicate entity
	) implements AbstractCriterion.Conditions {
		public static Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			  EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player)
			, EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.fieldOf("entity").forGetter(Conditions::entity)
		).apply(instance, Conditions::new));

		public boolean requirementsMet(LootContext entity) {
			return this.entity.test(entity);
		}
	}
}
