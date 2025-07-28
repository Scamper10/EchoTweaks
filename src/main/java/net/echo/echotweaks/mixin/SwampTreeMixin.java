/* TODO
 * make size configurable
 */

package net.echo.echotweaks.mixin;

import java.util.Iterator;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;

import net.echo.echotweaks.EchoTweaks;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

@Mixin(SaplingGenerator.class)
public abstract class SwampTreeMixin {
	
	@Shadow Optional<RegistryKey<ConfiguredFeature<?, ?>>> regularVariant;

	@ModifyVariable(
		method = "generate("
				+ "Lnet/minecraft/server/world/ServerWorld;"
				+ "Lnet/minecraft/world/gen/chunk/ChunkGenerator;"
				+ "Lnet/minecraft/util/math/BlockPos;"
				+ "Lnet/minecraft/block/BlockState;"
				+ "Lnet/minecraft/util/math/random/Random;"
				+ ")Z"
		, at = @At("STORE")
		, ordinal = 1
	)
	public RegistryKey<ConfiguredFeature<?, ?>> swampTreeMixin(RegistryKey<ConfiguredFeature<?, ?>> originalTree, @Local ServerWorld world, @Local BlockPos pos) {
		if(!regularVariant.equals(Optional.of(TreeConfiguredFeatures.OAK))) return originalTree;
		if(!areSwampBlocksNearby(world, pos)) return originalTree;
		return TreeConfiguredFeatures.SWAMP_OAK;
	}

	private boolean areSwampBlocksNearby(WorldAccess world, BlockPos pos) {
		Iterator<BlockPos> posIterator = Mutable.iterate(pos.down().north(2).west(2), pos.up().south(2).east(2)).iterator();

		BlockPos blockPos;
		do {
			if (!posIterator.hasNext()) {
				return false;
			}

			blockPos = (BlockPos) posIterator.next();
		} while (!world.getBlockState(blockPos).isIn(TagKey.of(RegistryKeys.BLOCK, Identifier.of(EchoTweaks.MOD_ID, "grows_swamp_oaks"))));

		return true;
	}
}
