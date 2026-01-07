package net.echo.echotweaks.command;

import java.util.Collection;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class HealCommand {
	public static void register() {
		ModCommands.register("heal", argBuilder -> {
			return argBuilder
				.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
				.executes(context -> {
					ServerCommandSource source = context.getSource();
					if(!(source.getEntity() instanceof LivingEntity target)) return 0;
					healEntity(source, target);
					return SINGLE_SUCCESS;
				})
				.then(CommandManager.argument("targets", EntityArgumentType.entities())
					.executes(context -> {
						Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
						ServerCommandSource source = context.getSource();
						return healEntities(source, targets);
					})
					.then(CommandManager.argument("amount", IntegerArgumentType.integer())
						.executes(context -> {
							Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
							int amount = IntegerArgumentType.getInteger(context, "amount");
							ServerCommandSource source = context.getSource();
							return healEntities(source, targets, amount);
						})
					)
				);
		});
	}

	private static int healEntities(ServerCommandSource source, Collection<? extends Entity> targets, int amount) {
		int success = 0;
			for(Entity entity : targets) {
				if(entity instanceof LivingEntity target) {
					healEntity(source, target, amount);
					success = SINGLE_SUCCESS;
				}
			}
		return success;
	}

	private static int healEntities(ServerCommandSource source, Collection<? extends Entity> targets) {
		int success = 0;
			for(Entity entity : targets) {
				if(entity instanceof LivingEntity target) {
					healEntity(source, target);
					success = SINGLE_SUCCESS;
				}
			}
		return success;
	}

	private static void healEntity(ServerCommandSource source, LivingEntity target, int amount) {
		target.setHealth(target.getHealth() + amount);
		source.sendFeedback(() -> Text.translatable("commands.echotweaks.heal.specific", target.getDisplayName(), amount), true);
	}

	private static void healEntity(ServerCommandSource source, LivingEntity target) {
		target.setHealth(target.getMaxHealth());
		source.sendFeedback(() -> Text.translatable("commands.echotweaks.heal.maximum", target.getDisplayName()), true);
	}

}
