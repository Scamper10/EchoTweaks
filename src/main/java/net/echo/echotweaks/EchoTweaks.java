/* TODO
 * Move ToDo to its own file
 * Advancements:
 * 	- Ripped Genes		- Get a Variant 4 axolotl in a bucket
 * 	-- Fix `Caves & Cliffs` so breaking bedrock is not sometimes needed
 * 	-- Fix `This Boat has Legs` so it doesn't require a carrot-stick click, just holding one
 * 	-- move appropriate 100% advancements to zoology
 * Way to view completed requirements/criteria like in BaC (with translations keys?)
 * Villager inventory management
 * 	- gifting/stealing (with gossips) :
 *		add button to trading menu? (would require villager to have a profession)
 * 		or gift via bundle? how steal?
 * Crop replanting
 * Button to toggle tick pausing in the pause/advancement menus
 * Quick composting
 * More jungle saplings, dark oak too
 * Something about accidental peaceful difficulty when ticking is not paused
 * Gold bars? (like iron or copper bars) ((yeah they did an entire copper update))
 * Creative instakill weapon
 * Creative insta-tame
 * Creative block updater tool
 * Datapack modifiers, rather than replacing whole files
 * 	- Use datapaack directory structure (eg `<some modifier folder>/advancement/husbandry/<some vanilla advancement>.json`)
 * 	- Also add a namedModifiers file, which lists paths relative to `<modifier folder>` to a `<some named modifier>.json`
 * 	- Need options for append ( {} or [] or maybe "" ) or replace (any type)
 * 	- Possible format example: `{  "criteria/<criterion>/conditions/damage/dealt/min": 50.0  ,  "echotweaks:append": true  }`
 * 	- Append would default false
 * 	- Might need to implement `\/` -> "/" and `\\` -> "\" 
 * README:
 * 	- Change advancement titles into nice images
 * 	- Advancements: Include namespaced id and parent's
 * 	- Document advancement triggers (look for other similar undocumented things)
 * Re-add roses?
 * Implement zombie horses? (nvm, planned for 1.21.11)
 * Oozing potion somehow detect silme chunks?
 * LANG:
 * 	- Split advancement tabs
 * 	- Swap title-description
 * Fix superflat world creation
 * 	- Every world feature togglable
 * 	- Buttons for add/remove layers (remove already exists)
 * 	- Presets from files: already exists in datapacks, but all `features` are a single toggle
 * Fix creative default skeleton horse spawning {Tame and PersistenceRequired nbt tags}
 * 	- Maybe change command trap spawning to protect instant-activation?
 * 	- Maybe add egg for trap
 * BugFix - Ocelots sometimes take fish from client when feed requirements not met
 * /butcher [<entity selector>] - shorthand for `/kill @e[type=]`, no args uses !player, also disable dropped items?
 * /whereis <player> [<face towards>] - get coords and optionally tp ~ ~ ~ facing (need cheats)
 * 	- config: give compass? display coords? client-only glowing? display distance? (compass tells distance?)
 * 	- could maybe also be /locate player ... but that might be harder/impossible
 * Grey out completed advancement tabs and move them to the right side of the list
 * Sound for when pig zombs no longer angry (or all killed)
 * Remove item cooldowns in creative (insta crossbow?)
 * Something something https://minecraft.wiki/w/Old_Customized#Presets
 * Fix jukebox spitting out duplicate discs in creative (don't spit if held is same?)
 * F3+H shows hovered advancement ids like item ids
 * Spore blossoms renewable
 * 	- Particles drop straight down onto certain blocks -> one generates underneath?
 * Investigate 1.21.6 fog
 * Undo 1.21.6 lead and saddle crafts? (Nerf trader llama leads?)
 * Locator Bar ewww (make gamerule false by default)
 * Revert 'Mobs' to 'Creatures' in sound settings
 * Option to hide worldgen progress bar
 */

package net.echo.echotweaks;

import net.echo.echotweaks.block.ModBlocks;
import net.echo.echotweaks.command.ModCommands;
import net.echo.echotweaks.config.EchoConfig;
import net.echo.echotweaks.criterion.ModCriteria;
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
		ModCriteria.init();

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
