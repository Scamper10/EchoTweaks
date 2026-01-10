package net.echo.echotweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;

@Mixin(targets = "net.minecraft.entity.effect.OozingStatusEffect")
public abstract class OozingSlimeChunks extends StatusEffect {
	protected OozingSlimeChunks(StatusEffectCategory category, int color) {
		super(category, color);
	}

	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		Random random = world.getRandom();
		boolean shouldSkip = random.nextInt(3) == 0;

		if(shouldSkip)
			return true;

		ChunkPos chunkPos = entity.getChunkPos();
		boolean isInSlimeChunk = ChunkRandom.getSlimeRandom(
			chunkPos.x
			, chunkPos.z
			, ((StructureWorldAccess)world).getSeed()
			, 987234911L
		).nextInt(10) == 0;

		if(!isInSlimeChunk)
			return true;
		
		Vec3d pos = entity.getEntityPos();
		float entityHeight = entity.getHeight();
		world.spawnParticles(
			ParticleTypes.ITEM_SLIME
			, pos.x, pos.y+entityHeight*random.nextDouble(), pos.z
			, 1
			, 0.15, 0.1, 0.15
			, 0 // <speed> does nothing as <count> more than zero?
		);
		
		return true;
	}
}
