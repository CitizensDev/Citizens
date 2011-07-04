package com.citizens.Listeners;

import org.bukkit.Bukkit;
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

import com.citizens.Citizens;
import com.citizens.Constants;
import com.citizens.CreatureTask;
import com.citizens.Events.NPCDisplayTextEvent;
import com.citizens.Events.NPCRightClickEvent;
import com.citizens.Events.NPCTargetEvent;
import com.citizens.Interfaces.Listener;
import com.citizens.NPCTypes.Questers.QuesterNPC;
import com.citizens.NPCTypes.Questers.Quests.QuestManager;
import com.citizens.NPCs.NPCManager;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Utils.MessageUtils;
import com.citizens.Utils.Messaging;
import com.citizens.Utils.PathUtils;

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
		if (npc != null && !npc.callDamageEvent(event)) {
			event.setCancelled(true);
		}
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if (npc != null) {
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
			if (!NPCManager.get(event.getTarget()).callTargetEvent(event))
				event.setCancelled(true);
		}
		NPCTargetEvent e = (NPCTargetEvent) event;
		HumanNPC npc = NPCManager.get(e.getEntity());
		if (npc != null && event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			if (Citizens.plugin.validateTool("items.basic.select-items", player
					.getItemInHand().getTypeId(), player.isSneaking())) {
				if (!NPCManager.validateSelected(player, npc.getUID())) {
					NPCManager.selectNPC(player, npc);
					Messaging.send(player, npc, Constants.selectionMessage);
					if (!Constants.quickSelect)
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
					Messaging.send(target, npc, textEvent.getText());
				}
			}
			NPCRightClickEvent rightClickEvent = new NPCRightClickEvent(npc,
					player);
			Bukkit.getServer().getPluginManager().callEvent(rightClickEvent);
			if (!rightClickEvent.isCancelled()) {
				if (npc.getWaypoints().isStarted()
						&& npc.getWaypoints().currentIndex() <= npc
								.getWaypoints().size()) {
					Bukkit.getServer()
							.getScheduler()
							.scheduleSyncDelayedTask(
									Citizens.plugin,
									new RestartPathTask(npc, npc.getWaypoints()
											.current()),
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
			PathUtils
					.createPath(npc, point, -1, -1, Constants.pathFindingRange);
			npc.setPaused(false);
		}
	}
}