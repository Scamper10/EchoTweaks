package net.echo.echotweaks.command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class NameCommand {
	private static final String NAME_TEXT_ARG = "name";

	public static void register() {
		ModCommands.register("name", (argBuilder, registryAccess) -> {
			return argBuilder
				.then(CommandManager.literal("item").then(CommandManager.argument(NAME_TEXT_ARG, TextArgumentType.text(registryAccess))
				.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						ItemStack heldStack = source.getEntity().getWeaponStack();
						heldStack.set(DataComponentTypes.ITEM_NAME, Text.of(TextArgumentType.parseTextArgument(context, NAME_TEXT_ARG)));
						source.sendFeedback(() -> Text.translatable("commands.echotweaks.name.success"), false);
						return SINGLE_SUCCESS;
					})
				));
				//TODO .then(CommandManager.literal("entity"));
		});
	}
}
