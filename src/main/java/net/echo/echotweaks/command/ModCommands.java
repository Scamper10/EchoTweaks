package net.echo.echotweaks.command;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ModCommands {

	public static void registerAll() {
		HealCommand.register();
		UnbreakableCommand.register();
		PlatformCommand.register();
	}

	public static void register(String command, Function<LiteralArgumentBuilder<ServerCommandSource>, LiteralArgumentBuilder<ServerCommandSource>> listener) {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(listener.apply(CommandManager.literal(command)));
		});
	}
	public static void register(String command, BiFunction<LiteralArgumentBuilder<ServerCommandSource>, CommandRegistryAccess, LiteralArgumentBuilder<ServerCommandSource>> listener) {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(listener.apply(CommandManager.literal(command), registryAccess));
		});
	}

	public static void sendFeedback(ServerCommandSource source, boolean broadcast, String translationKey, Object... args) {
		source.sendFeedback(() -> Text.translatable(translationKey, args), broadcast);
	}
}
