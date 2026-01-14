package net.echo.echotweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.spawner.PhantomSpawner;

@Mixin(PhantomSpawner.class)
public abstract class NoCreativePhantoms {
	@WrapOperation(
		method = "spawn"
		, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z")
	)
	private boolean shouldNotSpawnPhantoms(ServerPlayerEntity player, Operation<Boolean> isSpectator) {
		return player.isCreative() || isSpectator.call(player);
	}
}
