/* TODO
 * figure out how the hell to not show 2 levels deep of hinters
 */

package net.echo.echotweaks.mixin.hiddenAdvancement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementDisplays;

@Mixin(AdvancementDisplays.class)
public abstract class Revealer {

	@Redirect(
		  method = "getStatus(Lnet/minecraft/advancement/Advancement;Z)Lnet/minecraft/advancement/AdvancementDisplays$Status;"
		, at = @At(
			  value = "INVOKE"
			, target = "Lnet/minecraft/advancement/AdvancementDisplay;isHidden()Z"
		)
	)
	private static boolean getRidOfAnnoyingHideyThing(AdvancementDisplay ignored) {
		return false;
	}
}
