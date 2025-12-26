package net.echo.echotweaks.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.echo.echotweaks.statistic.ModStats;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(AxolotlEntity.class)
public abstract class AxolotlBlueChance extends AnimalEntity {
	private static final @Unique int MAX_BONUS_BREEDS = AxolotlEntity.BLUE_BABY_CHANCE / 2;
	private static final @Unique int BREEDS_FOR_BEST_CHANCE = MAX_BONUS_BREEDS;
	private static final @Unique int BEST_INVERSE_CHANCE = 10;

	// Makes the inverse chance equal AxolotlEntity.BLUE_BABY_CHANCE at zero breeds
	// while preserving BEST_INVERSE_CHANCE
	private static final @Unique double MATCH_VANILLA_MULTIPLIER = (AxolotlEntity.BLUE_BABY_CHANCE-BEST_INVERSE_CHANCE)/Math.pow(BREEDS_FOR_BEST_CHANCE, 2);

	protected AxolotlBlueChance(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(
		method="createChild("
				+ "Lnet/minecraft/server/world/ServerWorld;"
				+ "Lnet/minecraft/entity/passive/PassiveEntity;"
			+ ")Lnet/minecraft/entity/passive/PassiveEntity;"
		, at=@At(value="INVOKE", target="shouldBabyBeDifferent(Lnet/minecraft/util/math/random/Random;)Z")
	)
	private boolean shouldBeDifferent(Random random, ServerWorld world, PassiveEntity other) {
		final int randomRange = getInverseChance();
		final int randomChosen = random.nextInt(randomRange);
		return randomChosen == 0;
	}

	@Unique private int getInverseChance() {
		Optional<ServerPlayerEntity> loveCause = Optional.ofNullable(this.getLovingPlayer());
		if(loveCause.isEmpty()) 
			return AxolotlEntity.BLUE_BABY_CHANCE;

		final int baseFromStat = loveCause.get().getStatHandler().getStat(ModStats.BRED, EntityType.AXOLOTL);
		return toInverseChance(baseFromStat);
	}

	@Unique private int toInverseChance(int source) {
		final int clamped = Math.clamp(source, 0, MAX_BONUS_BREEDS);

		// return AxolotlEntity.BLUE_BABY_CHANCE - clamped;
		return (int) (Math.pow(clamped-BREEDS_FOR_BEST_CHANCE, 2) * MATCH_VANILLA_MULTIPLIER + BEST_INVERSE_CHANCE);
	}
}
