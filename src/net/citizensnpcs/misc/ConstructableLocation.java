package net.citizensnpcs.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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

	public void setWorld(World world) {
		this.world = world.getName();
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public Location construct() {
		return new Location(Bukkit.getServer().getWorld(world), x, y, z, pitch,
				yaw);
	}

	public void setValues(Location other, boolean useWorld) {
		if (useWorld)
			this.setWorld(other.getWorld());
		this.setX(other.getX());
		this.setY(other.getY());
		this.setZ(other.getZ());
		this.setPitch(other.getPitch());
		this.setYaw(other.getYaw());
	}
}
