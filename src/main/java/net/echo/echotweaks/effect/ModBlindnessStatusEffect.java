package net.echo.echotweaks.effect;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;

public class ModBlindnessStatusEffect extends StatusEffect {
	private static final int LOCATE_RADIUS = 100;
	private static final int
	  MIN_COUNT = 6
	, COUNT_RANGE = 44;
	private static final double MAX_COUNT_DIST_SQ = 1e4;
	
	public ModBlindnessStatusEffect(StatusEffect vanillaBlindness) {
		super(vanillaBlindness.getCategory(), vanillaBlindness.getColor());
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		// exit silently if no city found

		// find nearest
		Registry<Structure> structureRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE);
		RegistryEntryList<Structure> structureList = RegistryEntryList.of(structureRegistry.getOptional(StructureKeys.ANCIENT_CITY).get());
		@Nullable Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager().getChunkGenerator()
		.locateStructure(world, structureList, entity.getBlockPos(), LOCATE_RADIUS, false);
		
		// scale count based on distance
		BlockPos cityPos = pair.getFirst();
		Vec3d entityPos = entity.getEntityPos();
		double distSq = cityPos.getSquaredDistance(entityPos);
		int count = (int)(Math.min(distSq / MAX_COUNT_DIST_SQ, 1) * COUNT_RANGE) + MIN_COUNT;

		for(int i = 0; i < count; i++) {
			// choose a random angle (uniform)
			// choose a distance (weighted around like 1.5 blocks away?)
			// scale particle (or some other transform) based on how close the angle is
			world.spawnParticles(
				  ParticleTypes.SCULK_CHARGE_POP
				, entityPos.x, entityPos.y, entityPos.z
				, 1 // count:1 uses <delta> as position variance
				, 0, 0.1, 0
				, 0 // <speed> does nothing as <count> more than zero?
			);
		}

		return true;
	}
}
