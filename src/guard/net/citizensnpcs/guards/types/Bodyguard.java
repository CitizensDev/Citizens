package net.citizensnpcs.guards.types;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.Settings;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.guards.Guard;
import net.citizensnpcs.guards.GuardUpdater;
import net.citizensnpcs.guards.Targeter;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.utils.LocationUtils;
import net.citizensnpcs.utils.PathUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Bodyguard implements GuardUpdater {
	public Bodyguard() {
		NPCTypeManager.registerEvent(Type.PLAYER_LOGIN, new PlayerListener() {
			@Override
			public void onPlayerLogin(PlayerLoginEvent event) {
				Player player = event.getPlayer();
				String owner = player.getName();
				if (!toRespawn.containsKey(owner))
					return;
				NPCManager.register(toRespawn.remove(owner),
						NPCCreateReason.RESPAWN).teleport(player.getLocation());
			}
		});
	}

	@Override
	public GuardStatus updateStatus(GuardStatus current, HumanNPC npc) {
		if (npc.getHandle().hasTarget() && current == GuardStatus.NORMAL)
			current = GuardStatus.ATTACKING;
		switch (current) {
		case NORMAL:
			if (findTarget(npc))
				return GuardStatus.ATTACKING;
			break;
		case ATTACKING:
			if (!keepAttacking(npc)) {
				teleportHome(npc);
				return GuardStatus.NORMAL;
			}
			break;
		}
		return current;
	}

	private boolean keepAttacking(HumanNPC npc) {
		Player owner = Bukkit.getPlayerExact(npc.getOwner());
		if (owner == null) {
			despawn(npc);
			return false;
		}
		Guard guard = npc.getType("guard");
		return npc.getHandle().hasTarget()
				&& LocationUtils.withinRange(owner.getLocation(),
						npc.getLocation(), guard.getProtectionRadius());
	}

	private void despawn(HumanNPC npc) {
		toRespawn.put(npc.getOwner(), npc);
		NPCManager.despawn(npc.getUID());
	}

	private boolean findTarget(HumanNPC npc) {
		Guard guard = npc.getType("guard");
		if (!guard.isAggressive())
			return false;
		Player player = Bukkit.getPlayerExact(npc.getOwner());
		if (player == null) {
			despawn(npc);
			return false;
		}
		if (!LocationUtils.withinRange(npc.getLocation(), player.getLocation(),
				guard.getProtectionRadius())) {
			double range = Settings.getDouble("PathfindingRange");
			if (!LocationUtils.withinRange(npc.getLocation(),
					player.getLocation(), range))
				npc.teleport(player.getLocation());
			else
				PathUtils.target(npc, player, false, -1, -1, range);
			return false;
		} else {
			LivingEntity entity = Targeter.findTarget(
					Targeter.getNearby(player, guard.getProtectionRadius()),
					npc);
			if (entity != null)
				guard.target(entity, npc);
			return entity != null;
		}
	}

	private void teleportHome(HumanNPC npc) {
		Player owner = Bukkit.getServer().getPlayerExact(npc.getOwner());
		if (owner != null) {
			npc.teleport(owner.getLocation());
		} else
			despawn(npc);
	}

	private final static Map<String, HumanNPC> toRespawn = new HashMap<String, HumanNPC>();

	@Override
	public void onDamage(HumanNPC npc, LivingEntity attacker) {
	}
}
