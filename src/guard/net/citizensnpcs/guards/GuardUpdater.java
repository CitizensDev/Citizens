package net.citizensnpcs.guards;

import net.citizensnpcs.guards.types.GuardStatus;
import net.citizensnpcs.lib.HumanNPC;

import org.bukkit.entity.LivingEntity;

public interface GuardUpdater {
	GuardStatus updateStatus(GuardStatus current, HumanNPC npc);

	void onDamage(HumanNPC npc, LivingEntity attacker);
}
