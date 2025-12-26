package net.echo.echotweaks.mixin;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.echo.echotweaks.statistic.ModStats;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(AnimalEntity.class)
public abstract class PerEntityBredStat {

	// Injecting into lambda
	@Inject(
		method="method_49795("
			+ "Lnet/minecraft/entity/passive/AnimalEntity;"
			+ "Lnet/minecraft/entity/passive/PassiveEntity;"
			+ "Lnet/minecraft/server/network/ServerPlayerEntity;"
			+ ")V"
		, at=@At(
			value="INVOKE"
			, target="Lnet/minecraft/server/network/ServerPlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"
			, shift=At.Shift.AFTER
		)
	)
	private void incrementSpecificBredStat(
			// locals outside lambda
			AnimalEntity other
			, @Nullable PassiveEntity baby

			// arg for lambda
			, ServerPlayerEntity player

			, CallbackInfo ci
		) {
		if(baby == null) 
			return;
		
		player.incrementStat(ModStats.BRED.getOrCreateStat(baby.getType()));	
	}
}
