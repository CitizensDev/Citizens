package com.fullwall.Citizens.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import com.fullwall.Citizens.CreatureTask;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent.NpcTargetReason;

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
		if (npc != null && !npc.isGuard()) {
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
					if (npc.isHealer()) {
						npc.getHealer().onLeftClick(player, npc);
					}
					if (npc.isWizard()) {
						npc.getWizard().onLeftClick(player, npc);
					}
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
		if (!(event instanceof NPCEntityTargetEvent)) {
			return;
		}
		NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
		if (CreatureTask.getCreature(event.getEntity()) != null) {
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED)
				CreatureTask.getCreature(event.getEntity()).onRightClick(
						(Player) event.getTarget());
		}
		if (NPCManager.isNPC(event.getTarget())) {
			event.setCancelled(true);
		}
		HumanNPC npc = NPCManager.get(e.getEntity());
		if (npc != null && event.getTarget() instanceof Player) {
			// The NPC lib handily provides a right click event.
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
				Player player = (Player) event.getTarget();
				if (Citizens.plugin
						.validateTool("items.basic.select-items", player
								.getItemInHand().getTypeId(), player
								.isSneaking())) {
					if (!NPCManager.validateSelected(player, npc.getUID())) {
						NPCManager.selectedNPCs.put(player.getName(),
								npc.getUID());
						player.sendMessage(ChatColor.GREEN + "You selected "
								+ StringUtils.wrap(npc.getStrippedName())
								+ " (ID " + StringUtils.wrap(npc.getUID())
								+ ").");
						return;
					}
				}
				// Dispatch text event / select NPC.
				if (Citizens.plugin
						.validateTool("items.basic.talk-items", player
								.getItemInHand().getTypeId(), player
								.isSneaking())) {
					CitizensBasicNPCEvent ev = new CitizensBasicNPCEvent(npc,
							(Player) e.getTarget(), MessageUtils.getText(npc,
									(Player) e.getTarget()));
					Bukkit.getServer().getPluginManager().callEvent(ev);
				}
				if (npc.isTrader()) {
					npc.getTrader().onRightClick(player, npc);
				}
				if (npc.isWizard()) {
					npc.getWizard().onRightClick(player, npc);
				}
				if (npc.isBlacksmith()) {
					npc.getBlacksmith().onRightClick(player, npc);
				}
				if (npc.isQuester()) {
					npc.getQuester().onRightClick(player, npc);
				}
			}
		}
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		CreatureTask.onEntityDeath(event.getEntity());
	}
}