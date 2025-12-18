package net.echo.echotweaks.command;

import java.util.function.UnaryOperator;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

public class UnbreakableCommand {
	public static void register() {
		ModCommands.register("unbreakable", argBuilder -> {
			return argBuilder
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
				.executes(context -> {
					// ... maybe check with itemstack.isdamageable
					ServerCommandSource source = context.getSource();
					source.getEntity().getWeaponStack().apply(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE, UnaryOperator.identity());
					source.sendFeedback(() -> Text.translatable("commands.echotweaks.unbreakable.success"), false);
					return SINGLE_SUCCESS;
				});
		});
	}
}
