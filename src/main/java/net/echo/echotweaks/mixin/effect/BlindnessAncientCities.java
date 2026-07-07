package net.echo.echotweaks.mixin.effect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.echo.echotweaks.effect.ModBlindnessStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

@Mixin(StatusEffects.class)
public abstract class BlindnessAncientCities {
	@ModifyArg(
		  method = "<clinit>"
		, at = @At(
			  value="INVOKE"
			, target="Lnet/minecraft/entity/effect/StatusEffects;register("
					+ "Ljava/lang/String;"
					+ "Lnet/minecraft/entity/effect/StatusEffect;"
				+ ")Lnet/minecraft/registry/entry/RegistryEntry;"
			, ordinal = 14 // ordinal is unstable between updates
			)
		, index = 1
	)
	private static StatusEffect createModdedBlindnessEffect(StatusEffect vanillaBlindness) {
		return new ModBlindnessStatusEffect(vanillaBlindness);
	}
}
