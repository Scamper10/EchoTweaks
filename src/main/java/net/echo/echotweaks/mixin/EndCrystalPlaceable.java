package net.echo.echotweaks.mixin;

import static net.echo.echotweaks.EchoTweaks.MOD_ID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;


/**
 * Original check:<br>
 * <pre><code>
 * if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK)) {
 * 	return ActionResult.FAIL;
 * }
 * </code></pre>
 * 
 * Each <code>isOf</code> indicates a correct block.<br>
 * We can use the first one to check the custom block tag by replacing it with <code>isIn</code><br>
 * If the second <code>isOf</code> is ignored, bedrock will still be checked for even if it isn't in the tag.<br>
 * Hence, we replace it with <code>false</code>, to make the expression <code>&& true</code>, which does nothing.
 */
@Mixin(EndCrystalItem.class)
public abstract class EndCrystalPlaceable {
	@Redirect(
		method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"
		, at = @At(
			value = "INVOKE"
			, target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
			, ordinal = 0
		)
	)
	private boolean isInCrystalTag(BlockState blockState, Block obsidian) {
		return blockState.isIn(TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "end_crystal_placeable")));
	}

	@Redirect(
		method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"
		, at = @At(
			value = "INVOKE"
			, target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
			, ordinal = 1
		)
	)
	private boolean alwaysFalse(BlockState blockState, Block bedrock) {
		return false;
	}
}
