package com.fullwall.Citizens.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.CreatureTask;
import com.fullwall.Citizens.Events.NPCDisplayTextEvent;
import com.fullwall.Citizens.Events.NPCRightClickEvent;
import com.fullwall.Citizens.Events.NPCTargetEvent;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Questers.QuesterNPC;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.Messaging;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Entity Listener
 */
public class EntityListen extends EntityListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.ENTITY_DEATH, this, Event.Priority.Normal,
				Citizens.plugin);
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		CreatureTask.onDamage(event.getEntity(), event);
		HumanNPC npc = NPCManager.get(event.getEntity());
		if (npc != null && !npc.isType("guard")) {
			event.setCancelled(true);
		}
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if (npc != null) {
				if (e.getEntity() instanceof Player) {
					e.setCancelled(true);
				}
				if (e.getDamager() instanceof Player) {
					Player player = (Player) e.getDamager();
					npc.callLeftClick(player, npc);
				}
			} else if (e.getDamager() instanceof Player) {
				if (((LivingEntity) e.getEntity()).getHealth() - e.getDamage() <= 0) {
					QuestManager.incrementQuest((Player) e.getDamager(),
							new EntityDeathEvent(e.getEntity(), null));
				}
				CreatureTask.onDamage(e.getEntity(), event);
			}
		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (!(event instanceof NPCTargetEvent)) {
			return;
		}
		if (CreatureTask.getCreature(event.getEntity()) != null) {
			CreatureTask.getCreature(event.getEntity()).onRightClick(
					(Player) event.getTarget());
		}
		if (NPCManager.isNPC(event.getTarget())) {
			event.setCancelled(true);
		}
		NPCTargetEvent e = (NPCTargetEvent) event;
		HumanNPC npc = NPCManager.get(e.getEntity());
		if (npc != null && event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			if (Citizens.plugin.validateTool("items.basic.select-items", player
					.getItemInHand().getTypeId(), player.isSneaking())) {
				if (!NPCManager.validateSelected(player, npc.getUID())) {
					NPCManager.selectedNPCs.put(player.getName(), npc.getUID());
					player.sendMessage(ChatColor.GREEN + "You selected "
							+ StringUtils.wrap(npc.getStrippedName()) + " (ID "
							+ StringUtils.wrap(npc.getUID()) + ").");
					return;
				}
			}
			// Call text-display event
			if (Citizens.plugin.validateTool("items.basic.talk-items", player
					.getItemInHand().getTypeId(), player.isSneaking())) {

				Player target = (Player) e.getTarget();
				NPCDisplayTextEvent textEvent = new NPCDisplayTextEvent(npc,
						target, MessageUtils.getText(npc, target));
				Bukkit.getServer().getPluginManager().callEvent(textEvent);
				QuesterNPC quester = npc.getToggleable("quester");
				if (quester != null && quester.hasQuests()) {
				} else if (textEvent.isCancelled()) {
				} else if (!textEvent.getNPC().getNPCData().isLookClose()) {
					NPCManager.facePlayer(npc, target);
				} else if (!textEvent.getText().isEmpty()) {
					Messaging.send(target, textEvent.getText());
				}
			}
			NPCRightClickEvent rightClickEvent = new NPCRightClickEvent(npc,
					player);
			Bukkit.getServer().getPluginManager().callEvent(rightClickEvent);
			if (!rightClickEvent.isCancelled()) {
				if (npc.getWaypoints().isStarted()) {
					Bukkit.getServer()
							.getScheduler()
							.scheduleSyncDelayedTask(
									Citizens.plugin,
									new RestartPathTask(npc, npc.getWaypoints()
											.get(npc.getWaypoints().getIndex())),
									Constants.rightClickPause);
					npc.getHandle().cancelPath();
					npc.setPaused(true);
				}
				npc.callRightClick(player, rightClickEvent.getNPC());
			}
		}
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		CreatureTask.onEntityDeath(event.getEntity());
	}

	private class RestartPathTask implements Runnable {
		private final Location point;
		private final HumanNPC npc;

		public RestartPathTask(HumanNPC npc, Location point) {
			this.npc = npc;
			this.point = point;
		}

		@Override
		public void run() {
			this.npc.createPath(point, -1, -1, Constants.pathFindingRange);
			npc.setPaused(false);
		}
	}
}