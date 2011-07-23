package com.citizens.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConstructableLocation {
	private String world;
	private double x, y, z;
	private float pitch, yaw;

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public Location construct() {
		return new Location(Bukkit.getServer().getWorld(world), x, y, z, pitch,
				yaw);
	}
}
