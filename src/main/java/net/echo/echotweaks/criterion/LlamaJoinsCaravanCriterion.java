package net.echo.echotweaks.criterion;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class LlamaJoinsCaravanCriterion extends AbstractCriterion<LlamaJoinsCaravanCriterion.Conditions> {
	
	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public void trigger(ServerPlayerEntity player, int caravanLength, boolean hasCreamy, boolean hasWhite, boolean hasBrown, boolean hasGray) {
		trigger(player, conditions -> conditions.requirementsMet(caravanLength, hasCreamy, hasWhite, hasBrown, hasGray));
	}

	public record Conditions(
		  Optional<LootContextPredicate> playerPredicate
		, Optional<Integer> requiredLength
		, boolean requiresCreamy
		, boolean requiresWhite
		, boolean requiresBrown
		, boolean requiresGray
	) implements AbstractCriterion.Conditions {
		public static Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			  LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player)
			, Codec.INT.optionalFieldOf("min_length").forGetter(Conditions::requiredLength)
			, Codec.BOOL.optionalFieldOf("requires_creamy", false).forGetter(Conditions::requiresCreamy)
			, Codec.BOOL.optionalFieldOf("requires_white", false).forGetter(Conditions::requiresWhite)
			, Codec.BOOL.optionalFieldOf("requires_brown", false).forGetter(Conditions::requiresBrown)
			, Codec.BOOL.optionalFieldOf("requires_gray", false).forGetter(Conditions::requiresGray)
		).apply(instance, Conditions::new));

		@Override
		public Optional<LootContextPredicate> player() {
			return playerPredicate;
		}

		public boolean requirementsMet(int caravanLength, boolean hasCreamy, boolean hasWhite, boolean hasBrown, boolean hasGray) {

			if(requiredLength.isPresent() && caravanLength < requiredLength.get()) return false;
			
			if(requiresCreamy && !hasCreamy) return false;
			if(requiresWhite && !hasWhite) return false;
			if(requiresBrown && !hasBrown) return false;
			if(requiresGray && !hasGray) return false;

			return true;
		}
	}
}
