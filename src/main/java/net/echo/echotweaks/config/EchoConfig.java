package net.echo.echotweaks.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "echotweaks")
public class EchoConfig implements ConfigData{

	@Comment("Select what /platform does with no arguments")
	public DefaultPlatformBehavior platformNoArgBehavior = DefaultPlatformBehavior.CUSTOM;
	
	@Comment("Platform size when none is specified")
	public int defaultPlatformSize = 1;

	@Comment("Main block for Custom mode")
	public String customPlatformBlock = "minecraft:glass";

	@Comment("Optional different center block (empty string to disable)")
	public String customPlatformCenterBlock = "minecraft:white_stained_glass";

	

	public static EchoConfig getConfig() {
		return AutoConfig.getConfigHolder(EchoConfig.class).getConfig();
	}
	public static void save() {
		AutoConfig.getConfigHolder(EchoConfig.class).save();
	}
}
