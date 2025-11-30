package com.mycologycraft_devs.mycologycraft.utility;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;


public class bezierCurves {
	//list of control points
	public List<Vec3> controlPoints = List.of();

	//initialises our list, should rly call this instead of asign directly but being quick and dirty rn
	public void BezierND(List<Vec3> controlPoints) {
		if (controlPoints.size() < 2)
			throw new IllegalArgumentException("A Bezier curve needs at least 2 points.");
		this.controlPoints = controlPoints;
	}

	// Computes the point on the curve using de Casteljau’s algorithm
	public Vec3 getPoint(double t) {
		List<Vec3> temp = new ArrayList<>(controlPoints);

		int n = temp.size();
		for (int r = 1; r < n; r++) {
			for (int i = 0; i < n - r; i++) {
				Vec3 a = temp.get(i);
				Vec3 b = temp.get(i + 1);

				// linear interpolation
				temp.set(i, a.multiply(new Vec3(1-t,1-t,1-t)).add(b.multiply(new Vec3(t,t,t))));
			}
		}
		return temp.get(0);  // final point
	}
}
