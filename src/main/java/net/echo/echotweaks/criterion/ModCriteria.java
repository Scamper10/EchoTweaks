package net.echo.echotweaks.criterion;

import net.echo.echotweaks.EchoTweaks;
import net.minecraft.advancement.criterion.Criteria;

public class ModCriteria {
	public static final CrushTurtleEggCriterion CRUSH_TURTLE_EGG = Criteria.register(EchoTweaks.MOD_ID+":crush_turtle_egg", new CrushTurtleEggCriterion());

	public static void init() {}
}
