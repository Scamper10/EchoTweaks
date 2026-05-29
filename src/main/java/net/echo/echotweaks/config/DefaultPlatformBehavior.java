package net.echo.echotweaks.config;

public enum DefaultPlatformBehavior {
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
