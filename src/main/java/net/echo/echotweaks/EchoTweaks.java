package net.echo.echotweaks;

import net.echo.echotweaks.command.ModCommands;
import net.echo.echotweaks.config.EchoConfig;
import net.echo.echotweaks.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class EchoTweaks implements ModInitializer {
	public static final String MOD_ID = "echotweaks";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		AutoConfig.register(EchoConfig.class, Toml4jConfigSerializer::new);

		ModItems.init();
		ModBlocks.init();
		ModCommands.registerAll();

		LOGGER.log("My Mod Loaded :3");
		// LOGGER.log("IT COMPILES"); // toggle this to check
	}

	public class LOGGER {
		private static final Logger l = LoggerFactory.getLogger(MOD_ID);

		public static void log(String s) {
			l.info(s);
		}
		public static void log(int i) {
			l.info(((Integer)i).toString());
		}
		public static void err(String s) {
			l.error(s);
		}
	}
}
