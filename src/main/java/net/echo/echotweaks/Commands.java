package net.echo.echotweaks;

import java.util.Collection;
import java.util.function.UnaryOperator;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

public class Commands {
	private static int returnValue = 0; // scope workaround, volatile

	public static void registerAll() {
		// heal
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("heal")
				.then(CommandManager.argument("targets", EntityArgumentType.entities())
					.then(CommandManager.argument("amount", IntegerArgumentType.integer())
						.executes(context -> { // selector, amount
						Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
						int amount = IntegerArgumentType.getInteger(context, "amount");
						ServerCommandSource source = context.getSource();
							return healEntities(source, targets, amount);
						}))
					.executes(context -> { // selector only
						Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
						ServerCommandSource source = context.getSource();
						return healEntities(source, targets);
					}))
				.executes(context -> { // no args
					ServerCommandSource source = context.getSource();
					if(source.getEntity() instanceof LivingEntity target) {
						healEntity(source, target);
					} else {
						return 0;
					}
					return 1;
				})
				.requires(source -> source.hasPermissionLevel(2))
			);
		});
		
		// unbreakable
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("unbreakable")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(context -> {
					ServerCommandSource source = context.getSource();
					source.getEntity().getWeaponStack().apply(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE, UnaryOperator.identity());
					source.sendFeedback(() -> Text.literal("It will never be broken."), false);
					return 1;
				})
			);
		});
	}

	public static int healEntities(ServerCommandSource source, Collection<? extends Entity> targets, int amount) {
		returnValue = 0;
			targets.forEach(entity -> {
				if(entity instanceof LivingEntity target) {
					healEntity(source, target, amount);
					returnValue = 1;
				}
			});
		return returnValue;
	}

	public static int healEntities(ServerCommandSource source, Collection<? extends Entity> targets) {
		returnValue = 0;
			targets.forEach(entity -> {
				if(entity instanceof LivingEntity target) {
					healEntity(source, target);
					returnValue = 1;
				}
			});
		return returnValue;
	}

	public static void healEntity(ServerCommandSource source, LivingEntity target, int amount) {
		target.setHealth(target.getHealth() + amount);
		source.sendFeedback(() -> Text.literal("Healed %s by %s points".formatted(target.getName().getString(), amount)), false);
	}

	public static void healEntity(ServerCommandSource source, LivingEntity target) {
		target.setHealth(target.getMaxHealth());
		source.sendFeedback(() -> Text.literal("Healed %s".formatted(target.getName().getString())), false);
	}
}
