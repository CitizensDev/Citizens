package net.citizensnpcs.guards;

import java.util.List;

import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Targeter {
	public static LivingEntity findTarget(List<Entity> possible, HumanNPC npc) {
		FlagList flags = ((Guard) npc.getType("guard")).getFlags();
		flags.processEntities(npc.getLocation(), possible);
		return flags.getResult();
	}

	public static List<Entity> getNearby(Entity entity, double range) {
		return entity.getNearbyEntities(range / 2, range, range / 2);
	}
}
