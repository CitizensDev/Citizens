package net.citizensnpcs.guards;

import net.citizensnpcs.Settings;
import net.citizensnpcs.TickTask;
import net.citizensnpcs.guards.flags.FlagInfo;
import net.citizensnpcs.guards.flags.FlagList;
import net.citizensnpcs.guards.flags.FlagList.FlagType;
import net.citizensnpcs.guards.types.GuardStatus;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Guard extends CitizensNPC {
	private boolean isAggressive = true;
	private GuardState guardState = GuardState.NULL;
	private final FlagList flags = new FlagList();
	private double radius = 10;

	// Get a guard's blacklist
	public FlagList getFlags() {
		return flags;
	}

	// Get the protection radius for a bouncer
	public double getProtectionRadius() {
		return radius;
	}

	@Override
	public CitizensNPCType getType() {
		return new GuardType();
	}

	// Get whether a bodyguard NPC kills on sight
	public boolean isAggressive() {
		return isAggressive;
	}

	private boolean isCoOwned(Entity damager, HumanNPC npc) {
		HumanNPC other = NPCManager.get(damager);
		return other != null
				&& other.getOwner().equalsIgnoreCase(npc.getOwner());
	}

	private boolean isOwner(Entity damager, HumanNPC npc) {
		return damager instanceof Player ? NPCManager.isOwner((Player) damager,
				npc.getUID()) : false;
	}

	public boolean isState(GuardState state) {
		return guardState == state;
	}

	@Override
	public void load(DataKey root) {
		guardState = GuardState.parse(root.getString("type"));
		isAggressive = root.getBoolean("aggressive");
		radius = root.getDouble("radius",
				Settings.getDouble("DefaultBouncerProtectionRadius"));
		if (!root.keyExists("flags")) {
			return;
		}
		for (DataKey key : root.getRelative("flags").getSubKeys()) {
			boolean isSafe = key.getBoolean("safe");
			int priority = key.getInt("priority");
			flags.addFlag(FlagType.parse(key.getString("type")),
					FlagInfo.newInstance(key.name(), priority, isSafe));
		}
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
		if (!(event instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
		if (!(ev.getDamager() instanceof LivingEntity))
			return;
		HumanNPC npc = NPCManager.get(event.getEntity());
		if (isOwner(ev.getDamager(), npc) || isCoOwned(ev.getDamager(), npc)) {
			event.setCancelled(true);
			return;
		}
		if (guardState != GuardState.NULL)
			guardState.getUpdater().onDamage(npc,
					(LivingEntity) ev.getDamager());
		else if (this.isAggressive) {
			target((LivingEntity) ev.getDamager(), npc);
		}
	}

	@Override
	public void onDeath(EntityDeathEvent event) {
		HumanNPC npc = NPCManager.get(event.getEntity());
		Player player = Bukkit.getServer().getPlayer(npc.getOwner());
		if (player != null) {
			player.sendMessage(ChatColor.GRAY + "Your guard NPC "
					+ StringUtils.wrap(npc.getName(), ChatColor.GRAY)
					+ " died.");
		}
		event.getDrops().clear();
		TickTask.scheduleRespawn(npc, Settings.getInt("GuardRespawnDelay"));
	}

	@Override
	public void save(DataKey root) {
		root.setString("type", guardState.name());
		root.setBoolean("aggressive", isAggressive);
		root.setDouble("radius", radius);
		root = root.getRelative("flags");
		for (FlagType type : FlagType.values()) {
			for (FlagInfo info : flags.getFlags(type).values()) {
				DataKey key = root.getRelative(info.getName());
				key.setString("type", type.name());
				key.setBoolean("safe", info.isSafe());
				key.setInt("priority", info.priority());
			}
		}
	}

	// Set whether a bodyguard kills on sight
	public void setAggressive(boolean state) {
		this.isAggressive = state;
	}

	// Set the type of a guard that a guard NPC is
	public void setGuardState(GuardState guardState) {
		this.guardState = guardState;
	}

	// Set the protection radius for a bouncer
	public void setProtectionRadius(double radius) {
		this.radius = radius;
	}

	public void target(LivingEntity entity, HumanNPC npc) {
		if (isOwner(entity, npc) || isCoOwned(entity, npc))
			return;
		npc.getPathController().target(entity, true);
	}

	public GuardStatus updateStatus(GuardStatus guardStatus, HumanNPC npc) {
		return guardState == GuardState.NULL ? GuardStatus.NORMAL : guardState
				.getUpdater().updateStatus(guardStatus, npc);
	}
}