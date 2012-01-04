package net.citizensnpcs.guards;

import net.citizensnpcs.guards.types.GuardStatus;
import net.citizensnpcs.lib.HumanNPC;

import org.bukkit.entity.LivingEntity;

public interface GuardUpdater {
	void onDamage(HumanNPC npc, LivingEntity attacker);

	GuardStatus updateStatus(GuardStatus current, HumanNPC npc);
}
