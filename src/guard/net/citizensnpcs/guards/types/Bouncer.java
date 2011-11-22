package net.citizensnpcs.guards.types;

import net.citizensnpcs.Settings;
import net.citizensnpcs.guards.Guard;
import net.citizensnpcs.guards.GuardUpdater;
import net.citizensnpcs.guards.Targeter;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.PathUtils;

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
		if (npc.getHandle().getStationaryTicks() > Settings
				.getInt("MaxStationaryReturnTicks")) {
			npc.teleport(npc.getBaseLocation());
			npc.getHandle().cancelTarget();
		}
		return npc.getHandle().hasTarget()
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
		PathUtils.createPath(npc, npc.getBaseLocation(), -1,
				Settings.getInt("MaxStationaryReturnTicks"));
		return true;
	}

	@Override
	public GuardStatus updateStatus(GuardStatus current, HumanNPC npc) {
		if (npc.getHandle().hasTarget() && current == GuardStatus.NORMAL)
			current = GuardStatus.ATTACKING;
		switch (current) {
		case NORMAL:
			if (findTarget(npc)) {
				npc.setPaused(true);
				return GuardStatus.ATTACKING;
			}
			break;
		case ATTACKING:
			if (!keepAttacking(npc)) {
				npc.getHandle().cancelTarget();
				if (startReturning(npc)) {
					return GuardStatus.RETURNING;
				} else {
					return GuardStatus.ATTACKING;
				}
			}
			break;
		case RETURNING:
			if (!continueReturn(npc)) {
				npc.setPaused(false);
				return GuardStatus.NORMAL;
			}
			break;
		}
		return current;
	}
}
