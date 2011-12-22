package net.citizensnpcs.lib.pathfinding;

import net.citizensnpcs.lib.CraftNPC;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class TargetPathingStrategy implements PathingStrategy {
	private final CraftNPC handle;
	private final net.minecraft.server.Entity target;
	private PathingStrategy current = null;

	public TargetPathingStrategy(HumanNPC npc, Entity target) {
		this.handle = npc.getHandle();
		this.target = ((CraftEntity) target).getHandle();
	}

	@Override
	public boolean update() {
		if (target == null || target.dead)
			return true;
		current = new MinecraftPathingStrategy(handle, handle.world.findPath(
				handle, target, 16F));
		NPCManager.faceEntity(handle.getBukkitEntity(),
				target.getBukkitEntity());
		handle.tryAttack(target.getBukkitEntity()); // TODO: respect aggro
		current.update();
		return false;
	}
}
