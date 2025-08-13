/* TODO
 * figure out how the hell to not show 2 levels deep of hinters
 */

package net.echo.echotweaks.mixin.hiddenAdvancement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancement.AdvancementDisplays;

@Mixin(AdvancementDisplays.class)
public abstract class Revealer {

	@Unique private static final Object STATUS_NO_CHANGE = getEnumConst("NO_CHANGE");

	@Inject(
		  method = "getStatus(Lnet/minecraft/advancement/Advancement;Z)Ljdk/internal/org/jline/utils/Status;"
		, at = @At(
			  value = "RETURN"
			, ordinal = 2
		)
		, cancellable = true
	)
	private static void getRidOfAnnoyingHideyThing(CallbackInfoReturnable<Object> cir) {
		cir.setReturnValue(STATUS_NO_CHANGE);
		cir.cancel();
	}

	@SuppressWarnings("unchecked") // We know exactly which class we're accessing
	@Unique
	private static <E extends Enum<E>> E getEnumConst(String name) {
		final String STATUS_BINARY_NAME = "net.minecraft.advancement.AdvancementDisplays$Status";
		try {
			// I hate reflection
			Class<?> enumClass = Class.forName(STATUS_BINARY_NAME);
			return Enum.valueOf((Class<E>)enumClass, name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to access private inner class " + STATUS_BINARY_NAME, e);
		}
	}
}
