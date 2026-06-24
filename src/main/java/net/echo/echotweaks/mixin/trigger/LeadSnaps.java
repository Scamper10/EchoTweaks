package net.echo.echotweaks.mixin.trigger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.echo.echotweaks.criterion.ModCriteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.Leashable.LeashData;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(Leashable.class)
public interface LeadSnaps {
	
	@Inject(
		method = "snapLongLeash()V"
		, at = @At(
			  value = "INVOKE"
			, target = "Lnet/minecraft/entity/Leashable;detachLeash()V"
			, shift = At.Shift.BEFORE
		)
	)
	private void trigger(CallbackInfo ci) {
		LeashData leashData = getLeashData();
		Entity leashHolder = leashData.leashHolder;
		if(leashHolder instanceof ServerPlayerEntity player) {
			Entity leashedEntity = (Entity)this; // `this` will be <Entity implements Leashable>
			ModCriteria.LEAD_SNAPS.trigger(player, leashedEntity);
		}
	}

	@Shadow abstract LeashData getLeashData();
}
