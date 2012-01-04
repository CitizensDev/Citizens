package net.citizensnpcs.guards.types;

import net.citizensnpcs.guards.Guard;
import net.citizensnpcs.guards.GuardUpdater;
import net.citizensnpcs.guards.Targeter;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.entity.LivingEntity;

public class Bouncer implements GuardUpdater {

	private boolean continueReturn(HumanNPC npc) {
		return !LocationUtils.withinRange(npc.getLocation(),
				npc.getBaseLocation(), 3.5);
	}

	private boolean findTarget(HumanNPC npc) {
		Guard guard = npc.getType("guard");
		if (!guard.isAggressive())
			return false;
		LivingEntity entity = Targeter.findTarget(Targeter.getNearby(
				npc.getPlayer(), guard.getProtectionRadius()), npc);
		if (entity != null
				&& LocationUtils.withinRange(entity.getLocation(),
						npc.getBaseLocation(), guard.getProtectionRadius())) {
			guard.target(entity, npc);
			return true;
		}
		return false;
	}

	private boolean keepAttacking(HumanNPC npc) {
		Guard guard = npc.getType("guard");
		/*	if (npc.getHandle().getStationaryTicks() > Settings
					.getInt("MaxStationaryReturnTicks")) {
				npc.teleport(npc.getBaseLocation());
				npc.getHandle().cancelTarget();
			}TODO*/
		return npc.getPathController().isPathing()
				&& LocationUtils.withinRange(npc.getLocation(),
						npc.getBaseLocation(), guard.getProtectionRadius());
	}

	@Override
	public void onDamage(HumanNPC npc, LivingEntity attacker) {
		Guard guard = npc.getType("guard");
		guard.target(attacker, npc);
	}

	private boolean startReturning(HumanNPC npc) {
		if (findTarget(npc))
			return false;
		npc.getPathController().pathTo(npc.getBaseLocation());
		// Settings.getInt("MaxStationaryReturnTicks")); TODO
		return true;
	}

	@Override
	public GuardStatus updateStatus(GuardStatus current, HumanNPC npc) {
		if (npc.getPathController().isPathing()
				&& current == GuardStatus.NORMAL)
			current = GuardStatus.ATTACKING;
		switch (current) {
		case NORMAL:
			if (findTarget(npc)) {
				return GuardStatus.ATTACKING;
			}
			break;
		case ATTACKING:
			if (!keepAttacking(npc)) {
				npc.getPathController().cancel();
				if (startReturning(npc)) {
					return GuardStatus.RETURNING;
				} else {
					return GuardStatus.ATTACKING;
				}
			}
			break;
		case RETURNING:
			if (!continueReturn(npc)) {
				return GuardStatus.NORMAL;
			}
			break;
		}
		return current;
	}
}
