package net.echo.echotweaks.math;

import net.minecraft.util.math.Position;

public abstract class Angle {
	public static double calculateYaw(double x, double z) {
		double smallest = Math.atan(x / z);
		if(z > 0)
			return -smallest;

		if(x > 0) 
			return -(Math.PI + smallest);
		
		return Math.PI - smallest;
	}

	public static double calculateYaw(Position pos) {
		return calculateYaw(pos.getX(), pos.getZ());
	}

	public static double calculateYaw(Position pos1, Position pos2) {
		double deltaX = pos2.getX() - pos1.getX();
		double deltaZ = pos2.getZ() - pos1.getZ();
		return calculateYaw(deltaX, deltaZ);
	}
}