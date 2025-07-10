package net.echo.echotweaks.config;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable;

public enum DefaultPlatformBehavior implements Translatable {
	USE_HELD,
	VOID_WORLD,
	CUSTOM;

	private final String translationKey;

	DefaultPlatformBehavior() {
		this.translationKey = "echotweaks.enum.DefaultPlatformBehavior." + this.name();
	}

	public String getKey() {
		return translationKey;
	}
}
