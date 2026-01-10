package net.echo.echotweaks.criterion;

import net.echo.echotweaks.EchoTweaks;
import net.minecraft.advancement.criterion.Criteria;

public class ModCriteria {
	public static final GenericEventCriterion CRUSH_TURTLE_EGG	= Criteria.register(EchoTweaks.MOD_ID+":crush_turtle_egg",	new GenericEventCriterion());
	public static final GenericEventCriterion GAIN_OCELOT_TRUST	= Criteria.register(EchoTweaks.MOD_ID+":gain_ocelot_trust",	new GenericEventCriterion());
	public static final LlamaJoinsCaravanCriterion LLAMA_JOINS_CARAVAN = Criteria.register(EchoTweaks.MOD_ID+":llama_joins_caravan", new LlamaJoinsCaravanCriterion());

	public static void init() {}
}
