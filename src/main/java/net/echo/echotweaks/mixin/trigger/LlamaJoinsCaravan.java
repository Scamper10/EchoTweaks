package net.echo.echotweaks.mixin.trigger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.echo.echotweaks.criterion.ModCriteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(LlamaEntity.class)
public abstract class LlamaJoinsCaravan extends AbstractDonkeyEntity {
	LlamaJoinsCaravan(EntityType<? extends AbstractDonkeyEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "follow(Lnet/minecraft/entity/passive/LlamaEntity;)V", at = @At("TAIL"))
	private void trigger(LlamaEntity llama, CallbackInfo ci) {

		LlamaEntity current = (LlamaEntity)(Object)this;

		int length = 1;
		boolean
		  hasCreamy = false
		, hasWhite = false
		, hasBrown = false
		, hasGray = false;
		do {
			switch (current.getVariant()) {
				case LlamaEntity.Variant.CREAMY	-> hasCreamy = true;
				case LlamaEntity.Variant.WHITE	-> hasWhite = true;
				case LlamaEntity.Variant.BROWN	-> hasBrown = true;
				case LlamaEntity.Variant.GRAY	-> hasGray = true;
			}
			length++;

			current = current.getFollowing();
		} while (current.isFollowing());

		Entity entity = current.getLeashHolder();
		if(!(entity instanceof ServerPlayerEntity player)) return;

		ModCriteria.LLAMA_JOINS_CARAVAN.trigger(player, length, hasCreamy, hasWhite, hasBrown, hasGray);
	}
}
