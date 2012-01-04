package net.citizensnpcs.lib.pathfinding;

import net.citizensnpcs.lib.CraftNPC;
import net.citizensnpcs.lib.NPCManager;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class TargetPathingStrategy implements PathingStrategy {
	private final CraftNPC handle;
	private final net.minecraft.server.Entity target;
	private final boolean aggro;
	private PathingStrategy current = null;

	public TargetPathingStrategy(CraftNPC handle, Entity target, boolean aggro) {
		this.handle = handle;
		this.target = ((CraftEntity) target).getHandle();
		this.aggro = aggro;
	}

	@Override
	public boolean update() {
		if (target == null || target.dead)
			return true;
		current = new MinecraftPathingStrategy(handle, handle.world.findPath(
				handle, target, 16F));
		NPCManager.faceEntity(handle.getBukkitEntity(),
				target.getBukkitEntity());
		if (aggro)
			handle.tryAttack(target.getBukkitEntity());
		current.update();
		return false;
	}
}
