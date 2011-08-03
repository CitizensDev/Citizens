package net.citizensnpcs.npctypes.guards;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.TickTask;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.commands.commands.GuardCommands;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.guards.GuardManager.GuardType;
import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Saveable;
import net.citizensnpcs.properties.SettingsManager;
import net.citizensnpcs.properties.SettingsManager.SettingsType;
import net.citizensnpcs.properties.properties.GuardProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.PathUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

public class Guard extends CitizensNPC {
	private boolean isAggressive = false;
	private boolean isAttacking = false;
	private GuardType guardType = GuardType.NULL;
	private final FlagList flags = new FlagList();
	private double radius = 10;

	/**
	 * Get whether a guard NPC is a bodyguard
	 * 
	 * @return
	 */
	public boolean isBodyguard() {
		return guardType == GuardType.BODYGUARD;
	}

	/**
	 * Set whether a guard NPC is a bodyguard
	 * 
	 * @param state
	 */
	public void setBodyguard() {
		guardType = GuardType.BODYGUARD;
	}

	/**
	 * Get whether a guard NPC is a bouncer
	 * 
	 * @return
	 */
	public boolean isBouncer() {
		return guardType == GuardType.BOUNCER;
	}

	/**
	 * Set whether a guard NPC is a bouncer
	 * 
	 * @param state
	 */
	public void setBouncer() {
		guardType = GuardType.BOUNCER;
	}

	public void clear() {
		guardType = GuardType.NULL;
	}

	/**
	 * Get whether a bodyguard NPC kills on sight
	 * 
	 * @return
	 */

	public boolean isAggressive() {
		return isAggressive;
	}

	/**
	 * Set whether a bodyguard kills on sight
	 * 
	 * @param state
	 */
	public void setAggressive(boolean state) {
		this.isAggressive = state;
	}

	/**
	 * Get the type of guard that a guard NPC is
	 * 
	 * @return
	 */
	public GuardType getGuardType() {
		return guardType;
	}

	/**
	 * Set the type of a guard that a guard NPC is
	 * 
	 * @param guardType
	 */
	public void setGuardType(GuardType guardType) {
		this.guardType = guardType;
	}

	/**
	 * Get a guard's blacklist
	 * 
	 * @return
	 */
	public FlagList getFlags() {
		return flags;
	}

	/**
	 * Get the protection radius for a bouncer
	 * 
	 * @return
	 */
	public double getProtectionRadius() {
		return radius;
	}

	/**
	 * Get the halved protection radius for a bouncer
	 * 
	 * @return
	 */
	public double getHalvedProtectionRadius() {
		return this.radius / 2;
	}

	/**
	 * Set the protection radius for a bouncer
	 * 
	 * @param radius
	 */
	public void setProtectionRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public String getType() {
		return "guard";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
	}

	public void setAttacking(boolean attacking) {
		this.isAttacking = attacking;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof HumanNPC)) {
			return;
		}
		HumanNPC npc = (HumanNPC) event.getEntity();
		if (npc.getPlayer().getHealth() - event.getDamage() <= 0) {
			return;
		}
		if (isAggressive() && event.getCause() == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
			if (!isOwner(ev.getDamager(), npc)) {
				target((LivingEntity) ev.getDamager(), npc);
			}
		}
	}

	private boolean isOwner(Entity damager, HumanNPC npc) {
		return damager instanceof Player ? NPCManager.validateOwnership(
				(Player) damager, npc.getUID(), false) : false;
	}

	@Override
	public void onDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof HumanNPC)) {
			return;
		}
		HumanNPC npc = (HumanNPC) event.getEntity();
		Player player = Bukkit.getServer().getPlayer(npc.getOwner());
		if (player != null) {
			player.sendMessage(ChatColor.GRAY + "Your guard NPC "
					+ StringUtils.wrap(npc.getStrippedName(), ChatColor.GRAY)
					+ " died.");
		}
		event.getDrops().clear();
		TickTask.scheduleRespawn(npc,
				SettingsManager.getInt("GuardRespawnDelay"));
	}

	public void target(LivingEntity entity, HumanNPC npc) {
		npc.setPaused(true);
		this.setAttacking(true);
		PathUtils.target(npc, entity, true, -1, -1,
				SettingsManager.getDouble("PathfindingRange"));
	}

	@Override
	public Saveable getProperties() {
		return new GuardProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new GuardCommands();
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("GuardRespawnDelay", SettingsType.GENERAL,
				"ticks.guards.respawn-delay", 100));
		nodes.add(new Node("DefaultBouncerProtectionRadius",
				SettingsType.GENERAL,
				"range.guards.default-bouncer-protection-radius", 10));
		return nodes;
	}
}