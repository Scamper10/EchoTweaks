package net.echo.echotweaks.command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Unit;

public class UnbreakableCommand {
	public static void register() {
		ModCommands.register("unbreakable", argBuilder -> {
			return argBuilder
				.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
				.executes(context -> {
					// ... maybe check with itemstack.isdamageable
					ServerCommandSource source = context.getSource();
					source.getEntity().getWeaponStack().set(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE);
					ModCommands.sendFeedback(source, false, "commands.echotweaks.unbreakable.success");
					return SINGLE_SUCCESS;
				});
		});
	}
}
