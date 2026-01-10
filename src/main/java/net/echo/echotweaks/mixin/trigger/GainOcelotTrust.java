package net.echo.echotweaks.mixin.trigger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.echo.echotweaks.criterion.ModCriteria;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(OcelotEntity.class)
public abstract class GainOcelotTrust {
	
	@Inject(
		method = "interactMob("
					+ "Lnet/minecraft/entity/player/PlayerEntity;"
					+ "Lnet/minecraft/util/Hand;"
			   + ")Lnet/minecraft/util/ActionResult;"
		, at = @At(
			  value = "INVOKE"
			, target = "Lnet/minecraft/entity/passive/OcelotEntity;setTrusting(Z)V"
			, shift = At.Shift.AFTER
		)
	)
	private void trigger(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if(!(player instanceof ServerPlayerEntity serverPlayer)) return;
		ModCriteria.GAIN_OCELOT_TRUST.trigger(serverPlayer);
	}
}
