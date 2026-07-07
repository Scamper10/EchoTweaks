package net.echo.echotweaks.math;

import net.minecraft.util.math.Vec3d;

public abstract class ExtendedVec3d {
	/** @param yaw In radians, clockwise from +Z */
	public static Vec3d fromYaw(double yaw) {
		double x = -Math.sin(yaw);
		double z = Math.cos(yaw);
		return new Vec3d(x, 0, z);
	}
}
