package net.citizensnpcs.lib.pathfinding;

import java.util.Random;

import net.citizensnpcs.lib.CraftNPC;
import net.citizensnpcs.lib.HumanNPC;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;
import net.minecraft.server.Vec3D;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MinecraftPathingStrategy implements PathingStrategy {
	private final CraftNPC handle;
	private final PathEntity path;

	MinecraftPathingStrategy(CraftNPC handle, PathEntity path) {
		this.handle = handle;
		this.path = path;
	}

	public MinecraftPathingStrategy(HumanNPC npc, Location destination) {
		this.handle = npc.getHandle();
		this.path = npc.getHandle().world.a(npc.getHandle(),
				destination.getBlockX(), destination.getBlockY(),
				destination.getBlockZ(), 16F);
	}

	@Override
	public boolean update() {
		if (handle.dead)
			return true;
		Vec3D vector = getVector();
		if (vector == null)
			return true;
		int yHeight = MathHelper.floor(handle.boundingBox.b + 0.5D);
		boolean inWater = ((Player) handle.getBukkitEntity()).getRemainingAir() < 20;
		boolean onFire = handle.fireTicks > 0;
		double diffX = vector.a - handle.locX;
		double diffZ = vector.c - handle.locZ;

		handle.yaw += handle.getYawDifference(diffZ, diffX);
		if (vector.b - yHeight > 0.0D) {
			handle.jump();
		}
		// TODO: adjust pitch.
		// Walk.
		handle.walkOnCurrentHeading();

		if (handle.positionChanged) {
			handle.jump();
		}
		if (new Random().nextFloat() < 0.8F && (inWater || onFire)) {
			handle.motY += 0.04D;
		}
		return false;
	}

	private Vec3D getVector() {
		Vec3D vec3d = path.a(handle);
		double lengthSq = (handle.width * 2.0F);
		lengthSq *= lengthSq;
		while (vec3d != null
				&& vec3d.d(handle.locX, vec3d.b, handle.locZ) < lengthSq) {
			this.path.a(); // Increment path index.
			if (this.path.b()) { // finished.
				return null;
			} else {
				vec3d = this.path.a(handle);
			}
		}
		return vec3d;
	}
}
