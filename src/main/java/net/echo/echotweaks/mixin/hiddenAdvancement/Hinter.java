package net.echo.echotweaks.mixin.hiddenAdvancement;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.echo.echotweaks.EchoTweaks.MOD_ID;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.util.Identifier;

@Mixin(AdvancementWidget.class)
public abstract class Hinter {
	@Unique private static final String METHOD_DESCRIPTOR = "renderWidgets(Lnet/minecraft/client/gui/DrawContext;II)V";
	
	@Unique private boolean hasDisplayed;
	@Unique private final Identifier TEXTURE_ID = Identifier.of(MOD_ID, "hinter");
	@Unique private final int SIZE = 13;
	
	@Shadow private @Final AdvancementDisplay display;
	@Shadow private @Final int x;
	@Shadow private @Final int y;

	@Inject(method = METHOD_DESCRIPTOR, at = @At("HEAD"))
	private void setFieldFalse(DrawContext context, int x, int y, CallbackInfo ci) {
		hasDisplayed = false;
	}

	@Inject(
		  method = METHOD_DESCRIPTOR
		, at = @At(
			  value = "INVOKE"
			, target = "Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;II)V"
			, shift = At.Shift.AFTER
		  )
	)
	private void conditionWasSuccessful(DrawContext context, int x, int y, CallbackInfo ci) {
		hasDisplayed = true;
	}

	@Inject(
		  method = METHOD_DESCRIPTOR
		, at = @At(
			  value = "INVOKE"
			, target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
			, shift = At.Shift.BEFORE
		)
	)
	private void drawHinter(DrawContext context, int x, int y, CallbackInfo ci) {
		if(hasDisplayed) return;
		int relX = x + this.x + 9
		,	relY = y + this.y + 7;
		context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE_ID, relX, relY, SIZE, SIZE);
	}
}
