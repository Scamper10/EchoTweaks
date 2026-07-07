package net.echo.echotweaks.effect;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.echo.echotweaks.math.Angle;
import net.echo.echotweaks.math.ExtendedRandom;
import net.echo.echotweaks.math.ExtendedVec3d;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;

public class ModBlindnessStatusEffect extends StatusEffect {
	private static final int LOCATE_RADIUS = 100;


	// applied before randomness, to stop insane counts at very low distances
	private static final int MAX_COUNT = 10;

	private static final double
		  MIN_AVERAGE_COUNT = -0.5
		, MAX_COUNT_MULTIPLIER = 0
		, COUNT_DEVIATION = 1;

	private static final double FOUND_CUTOFF_DIST_SQ = 60 * 60;
	private static final double MIN_COUNT_DIST_SQ = 250 * 250;

	private static final double
		  PARTICLE_DISTANCE_AVERAGE = 3
		, PARTICLE_DISTANCE_DEVIATION = 0.5
		, ANGLE_DEVIATION = MathHelper.PI / 3;
	
	public ModBlindnessStatusEffect(StatusEffect vanillaBlindness) {
		super(vanillaBlindness.getCategory(), vanillaBlindness.getColor());
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	/** @returns If the effect should persist */
	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		Random random = world.getRandom();

		Vec3d entityPos = entity.getEyePos();
		
		Optional<BlockPos> optionalCityPos = getNearestCityPos(world, BlockPos.ofFloored(entityPos));
		if(optionalCityPos.isEmpty())
			return true;

		BlockPos cityPos = optionalCityPos.get();
		double cityDistSq = cityPos.getSquaredDistance(
			new Vec3d(entityPos.getX(), cityPos.getY(), entityPos.getZ())
		);

		int averageCount = getCountByDistance(cityDistSq);
		int randomizedCount = (int) ExtendedRandom.nextGaussian(random, averageCount, COUNT_DEVIATION);

		double cityAngle = Angle.calculateYaw(entityPos, Vec3d.of(cityPos));

		for(int i = 0; i < randomizedCount; i++) {
			double angle = cityDistSq < FOUND_CUTOFF_DIST_SQ ?
				  ExtendedRandom.nextYawUniform(random)
				: ExtendedRandom.nextGaussian(random, cityAngle, ANGLE_DEVIATION);
			double distance = ExtendedRandom.nextGaussian(random, PARTICLE_DISTANCE_AVERAGE, PARTICLE_DISTANCE_DEVIATION);

			Vec3d entityToParticle = ExtendedVec3d.fromYaw(angle)
				.multiply(distance);
			Vec3d particlePos = entityPos.add(entityToParticle);

			// choose a distance (weighted around like 1.5 blocks away?)
			// scale particle (or some other transform) based on how close the angle is
			world.spawnParticles(
				  ParticleTypes.SCULK_CHARGE_POP
				, particlePos.getX(), particlePos.getY(), particlePos.getZ()
				, 1
				, 0, 1, 0
				, 0
			);
		}

		return true;
	}

	private static Optional<BlockPos> getNearestCityPos(ServerWorld world, BlockPos pos) {
		Registry<Structure> structureRegistry = world
			.getRegistryManager()
			.getOrThrow(RegistryKeys.STRUCTURE);

		RegistryEntryList<Structure> structureList = RegistryEntryList.of(
			structureRegistry.getOptional(StructureKeys.ANCIENT_CITY).get()
		);

		Pair<BlockPos, RegistryEntry<Structure>> nearestCity = world
			.getChunkManager()
			.getChunkGenerator()
			.locateStructure(world, structureList, pos, LOCATE_RADIUS, false);

		if(nearestCity == null)
			return Optional.empty();

		return Optional.of(nearestCity.getFirst());
	}


	private static int getCountByDistance(double distanceSquared) {
		double distanceScale = MIN_COUNT_DIST_SQ / distanceSquared;
		int calculatedCount = (int) MathHelper.lerp(distanceScale, MIN_AVERAGE_COUNT, MAX_COUNT_MULTIPLIER);
		return Math.min(calculatedCount, MAX_COUNT);
	}
}
