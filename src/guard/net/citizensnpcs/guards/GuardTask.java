package net.citizensnpcs.guards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.event.npc.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.npc.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.misc.NPCLocation;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.PathUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GuardTask implements Runnable {
	private final static Map<String, NPCLocation> toRespawn = new HashMap<String, NPCLocation>();

	@Override
	public void run() {
		for (HumanNPC npc : CitizensManager.getList().values()) {
			if (!npc.isType("guard")) {
				continue;
			}
			Guard guard = npc.getType("guard");
			if (guard.isAttacking()) {
				boolean cancel = false;
				if (!npc.getHandle().hasTarget() || !guard.isAggressive()) {
					cancel = true;
				} else if (npc.getHandle().hasTarget()
						&& !LocationUtils.withinRange(npc.getBaseLocation(),
								npc.getHandle().getTarget().getLocation(),
								guard.getProtectionRadius())) {
					cancel = true;
				} else if (npc.getHandle().hasTarget() && guard.isBodyguard()
						&& Bukkit.getServer().getPlayer(npc.getOwner()) != null) {
					Player player = Bukkit.getServer()
							.getPlayer(npc.getOwner());
					if (npc.getHandle().getTarget() != player
							&& !LocationUtils.withinRange(
									npc.getBaseLocation(),
									player.getLocation(),
									guard.getProtectionRadius())) {
						cancel = true;
					}
				}
				if (cancel) {
					npc.getHandle().cancelTarget();
					GuardManager.returnToBase(guard, npc);
					guard.setAttacking(false);
				}
			}

			if (LocationUtils.withinRange(npc.getLocation(),
					npc.getBaseLocation(), 3.5)) {
				if (guard.isReturning()) {
					guard.setReturning(false);
				}
				if (!guard.isAttacking() && npc.isPaused()) {
					npc.setPaused(false);
				}
			} else if (guard.isReturning()
					&& npc.getHandle().getStationaryTicks() > SettingsManager
							.getInt("MaxStationaryReturnTicks")) {
				npc.teleport(npc.getBaseLocation());
				guard.setReturning(false);
			}
			if (guard.isAttacking() || guard.isReturning()) {
				continue;
			}
			if (npc.isPaused()) {
				npc.setPaused(false);
			}
			if (guard.isBouncer()) {
				handleTarget(npc.getPlayer(), npc, guard);
			} else if (guard.isBodyguard()) {
				Player player = Bukkit.getServer().getPlayer(npc.getOwner());
				if (player != null) {
					if (!LocationUtils.withinRange(npc.getLocation(),
							player.getLocation(), guard.getProtectionRadius())) {
						double range = SettingsManager
								.getDouble("PathfindingRange");
						if (!LocationUtils.withinRange(npc.getLocation(),
								player.getLocation(), range))
							npc.teleport(player.getLocation());
						else
							PathUtils.target(npc, player, false, -1, -1, range);
					} else
						handleTarget(player, npc, guard);
				} else {
					if (CitizensManager.getNPC(npc.getUID()) != null) {
						toRespawn.put(npc.getOwner(),
								new NPCLocation(npc.getLocation(),
										npc.getUID(), npc.getOwner()));
						NPCManager.despawn(npc.getUID(), NPCRemoveReason.DEATH);
					}
				}
			}
		}
	}

	private void handleTarget(Entity entity, HumanNPC npc, Guard guard) {
		if (!guard.isAggressive()) {
			return;
		}
		FlagList flags = guard.getFlags();
		flags.processEntities(npc, entity.getLocation(),
				getNearby(entity, guard));
		if (flags.getResult() != null) {
			guard.target(flags.getResult(), npc);
			npc.setPaused(true);
		}
	}

	private List<Entity> getNearby(Entity entity, Guard guard) {
		return entity.getNearbyEntities(guard.getHalvedProtectionRadius(),
				guard.getProtectionRadius(), guard.getHalvedProtectionRadius());
	}

	public static void checkRespawn(Player player) {
		String owner = player.getName();
		if (toRespawn.containsKey(owner)) {
			NPCManager.register(toRespawn.get(owner).getUID(), owner,
					NPCCreateReason.RESPAWN);
			CitizensManager.getNPC(toRespawn.get(owner).getUID()).teleport(
					player.getLocation());
		}
	}
}