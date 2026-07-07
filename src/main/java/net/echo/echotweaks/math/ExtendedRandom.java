package net.echo.echotweaks.math;

import net.minecraft.util.math.random.Random;

public abstract class ExtendedRandom {
	private static final double TWO_PI = 2 * Math.PI;
	
	public static double nextGaussian(Random random, double mean, double deviation) {
		return random.nextGaussian() * deviation + mean;
	}

	public static double nextYawUniform(Random random) {
		return random.nextFloat() * TWO_PI - Math.PI;
	}
}
