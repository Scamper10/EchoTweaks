package net.echo.echotweaks.statistic;

import static net.echo.echotweaks.EchoTweaks.MOD_ID;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModStats {
	public static final StatType<EntityType<?>> BRED = registerType("bred", Registries.ENTITY_TYPE);

	private static <T> StatType<T> registerType(String id, Registry<T> registry) {
		Text text = Text.translatable("stat_type."+MOD_ID+"."+id);
		return Registry.register(Registries.STAT_TYPE, Identifier.of(MOD_ID, id), new StatType<T>(registry, text));
	}

	public static void init() {}
}
