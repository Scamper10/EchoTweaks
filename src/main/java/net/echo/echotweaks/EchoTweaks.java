/* TODO
 * When Pigs Fly advancement
 */

package net.echo.echotweaks;

import net.echo.echotweaks.block.ModBlocks;
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

		public static void log(Object... stuff) {
			l.info(formString(stuff));
		}
		public static void err(Object... stuff) {
			l.error(formString(stuff));
		}

		private static String formString(Object[] stuff) {
			String s = "";
			for(int i = 0; i < stuff.length - 1; i++) {
				s += stuff[i].toString() + ", ";
			}
			return s + stuff[stuff.length-1];
		}
	}
}
