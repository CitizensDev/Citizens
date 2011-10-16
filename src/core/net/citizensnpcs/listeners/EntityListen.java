package net.citizensnpcs.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.event.npc.NPCRightClickEvent;
import net.citizensnpcs.api.event.npc.NPCTalkEvent;
import net.citizensnpcs.api.event.npc.NPCTargetEvent;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.PluginManager;

public class EntityListen extends EntityListener implements Listener {

	@Override
	public void registerEvents(Citizens plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Type.ENTITY_DAMAGE, this, Priority.Normal, plugin);
		pm.registerEvent(Type.ENTITY_TARGET, this, Priority.Normal, plugin);
		pm.registerEvent(Type.ENTITY_DEATH, this, Priority.Normal, plugin);
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled())
			return;
		CreatureTask.onDamage(event.getEntity(), event);
		HumanNPC npc = NPCManager.get(event.getEntity());
		if (npc != null) {
			npc.callDamageEvent(event);
		}
		if (!event.isCancelled() && event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if (npc != null) {
				if (e.getDamager() instanceof Player) {
					Player player = (Player) e.getDamager();
					npc.callLeftClick(player, npc);
				}
			} else if (e.getDamager() instanceof Player) {
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
			if (!NPCManager.get(event.getTarget()).callTargetEvent(event)) {
				event.setCancelled(true);
			}
		}
		NPCTargetEvent e = (NPCTargetEvent) event;
		HumanNPC npc = NPCManager.get(e.getEntity());
		if (npc != null && event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			if (npc.getNPCData().isLookClose()) {
				NPCManager.faceEntity(npc, player);
			}
			if (UtilityProperties.isHoldingTool("SelectItems", player)) {
				if (!NPCManager.hasSelected(player, npc.getUID())) {
					NPCDataManager.selectNPC(player, npc);
					Messaging.send(player, npc,
							SettingsManager.getString("SelectionMessage"));
					if (!SettingsManager.getBoolean("QuickSelect")) {
						return;
					}
				}
			}
			// Call NPC talk event
			if (UtilityProperties.isHoldingTool("TalkItems", player)) {
				Player target = (Player) e.getTarget();
				NPCTalkEvent talkEvent = new NPCTalkEvent(npc, target,
						MessageUtils.getText(npc, target));
				Bukkit.getServer().getPluginManager().callEvent(talkEvent);
				if (!talkEvent.isCancelled()) {
					if (!talkEvent.getText().isEmpty()) {
						Messaging.send(target, npc, talkEvent.getText());
					}
				}
			}
			NPCRightClickEvent rightClickEvent = new NPCRightClickEvent(npc,
					player);
			Bukkit.getServer().getPluginManager().callEvent(rightClickEvent);
			if (!rightClickEvent.isCancelled()) {
				NPCDataManager.handleEquipmentEditor(rightClickEvent);
				if (npc.getWaypoints().isStarted()
						&& npc.getWaypoints().current() != null) {
					npc.getWaypoints().scheduleDelay(npc,
							npc.getWaypoints().current().getLocation(),
							SettingsManager.getInt("RightClickPause"));
				}
				npc.callRightClick(player, npc);
			}
		}
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		CreatureTask.onEntityDeath(event.getEntity());
		if (NPCManager.isNPC(event.getEntity())) {
			NPCManager.get(event.getEntity()).callDeathEvent(event);
		}
	}
}