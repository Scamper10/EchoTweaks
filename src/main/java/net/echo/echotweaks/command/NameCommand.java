package net.echo.echotweaks.command;

import static net.echo.echotweaks.EchoTweaks.MOD_ID;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class NameCommand {
	private static final String
		COMMAND = "name"
		, ITEM_SUBCOMMAND = "item"
		, ENTITY_SUBCOMMAND = "entity"
		, TARGET_ENTITY_ARG = "target"
		, NAME_TEXT_ARG = "name";

	private static final String TRANSLATE_PREFIX = "commands."+MOD_ID+"."+COMMAND+".";

	private static final SimpleCommandExceptionType PROHIBIT_PLAYERS = new SimpleCommandExceptionType(Text.translatable(TRANSLATE_PREFIX.concat(ENTITY_SUBCOMMAND+".fail.prohibit_players")));

	public static void register() {
		ModCommands.register(COMMAND, (argBuilder, registryAccess) -> {
			return argBuilder
				.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
				.then(CommandManager.literal(ITEM_SUBCOMMAND).then(createNameTextArgument(registryAccess)
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						ItemStack heldStack = source.getEntity().getWeaponStack();
						heldStack.set(DataComponentTypes.ITEM_NAME, getNameTextArgumentValue(context));
						source.sendFeedback(() -> Text.translatable(TRANSLATE_PREFIX.concat(ITEM_SUBCOMMAND+".success"), heldStack.getFormattedName()), false);
						return SINGLE_SUCCESS;
					})
				))
				.then(CommandManager.literal(ENTITY_SUBCOMMAND)
					.then(CommandManager.argument(TARGET_ENTITY_ARG, EntityArgumentType.entity())
						.then(createNameTextArgument(registryAccess).executes(context -> {
							Entity targeted = EntityArgumentType.getEntity(context, TARGET_ENTITY_ARG);

							if(targeted instanceof PlayerEntity)
								throw PROHIBIT_PLAYERS.create();

							if(!targeted.getType().isSaveable())
								throw new SimpleCommandExceptionType(Text.of("(echotweaks) IDK how you get this")).create();

							targeted.setCustomName(getNameTextArgumentValue(context));

							return SINGLE_SUCCESS;
						}))
					)
				);
		});
	}

	private static RequiredArgumentBuilder<ServerCommandSource, Text> createNameTextArgument(CommandRegistryAccess registryAccess) {
		return CommandManager.argument(NAME_TEXT_ARG, TextArgumentType.text(registryAccess));
	}
	private static Text getNameTextArgumentValue(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return TextArgumentType.parseTextArgument(context, NAME_TEXT_ARG);
	}
}
