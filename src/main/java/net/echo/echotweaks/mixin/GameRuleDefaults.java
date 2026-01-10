package net.echo.echotweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.world.rule.GameRules;

/** Due to the use of `ordinal`, these will be extremely volatile on game updates */
@Mixin(GameRules.class)
public abstract class GameRuleDefaults {

	@ModifyArg(method = "<clinit>", at = @At(
		value = "INVOKE"
		, target = "Lnet/minecraft/world/rule/GameRules;registerBooleanRule("
			+ "Ljava/lang/String;"
			+ "Lnet/minecraft/world/rule/GameRuleCategory;"
			+ "Z"
			+ ")Lnet/minecraft/world/rule/GameRule;"
		, ordinal = 6
	), index = 2)
	private static boolean commandBlockOutput(boolean vanillaDefault) {
		return false;
	}

	@ModifyArg(method = "<clinit>", at = @At(
		value = "INVOKE"
		, target = "Lnet/minecraft/world/rule/GameRules;registerBooleanRule("
			+ "Ljava/lang/String;"
			+ "Lnet/minecraft/world/rule/GameRuleCategory;"
			+ "Z"
			+ ")Lnet/minecraft/world/rule/GameRule;"
		, ordinal = 20
	), index = 2)
	private static boolean locatorBar(boolean vanillaDefault) {
		return false;
	}
	


	@ModifyArg(method = "<clinit>", at = @At(
		value = "INVOKE"
		, target = "Lnet/minecraft/world/rule/GameRules;registerIntRule("
			+ "Ljava/lang/String;"
			+ "Lnet/minecraft/world/rule/GameRuleCategory;"
			+ "III"
			+ ")Lnet/minecraft/world/rule/GameRule;"
		, ordinal = 0
	), index = 2)
	private static int maxSnowAccumulationHeight(int vanillaDefault) {
		return 3;
	}

	@ModifyArg(method = "<clinit>", at = @At(
		value = "INVOKE"
		, target = "Lnet/minecraft/world/rule/GameRules;registerIntRule("
			+ "Ljava/lang/String;"
			+ "Lnet/minecraft/world/rule/GameRuleCategory;"
			+ "II"
			+ ")Lnet/minecraft/world/rule/GameRule;"
		, ordinal = 5
	), index = 2)
	private static int playersNetherPortalCreativeDelay(int vanillaDefault) {
		return 10;
	}

}
