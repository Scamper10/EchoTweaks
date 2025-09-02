package net.echo.echotweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.echo.echotweaks.criterion.ModCriteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(TurtleEggBlock.class)
public abstract class CrushTurtleEggTrigger extends Block {
	CrushTurtleEggTrigger(AbstractBlock.Settings settings) {
		super(settings);
	}
	
	@Inject(
		method = "tryBreakEgg("
					+ "Lnet/minecraft/world/World;"
					+ "Lnet/minecraft/block/BlockState;"
					+ "Lnet/minecraft/util/math/BlockPos;"
					+ "Lnet/minecraft/entity/Entity;"
					+ "I"
			   + ")V"
		, at = @At(
			  value = "INVOKE"
			, target = "Lnet/minecraft/block/TurtleEggBlock;breakEgg("
						+ "Lnet/minecraft/world/World;"
						+ "Lnet/minecraft/util/math/BlockPos;"
						+ "Lnet/minecraft/block/BlockState;"
					 + ")V"
			, shift = At.Shift.AFTER
		)
	)
	private void trigger(CallbackInfo ci, @Local Entity entity) {
		if(!(entity instanceof ServerPlayerEntity player)) return;
		ModCriteria.CRUSH_TURTLE_EGG.trigger(player);
	}
}
