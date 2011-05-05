package com.fullwall.Citizens.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent.Reason;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Traders.TraderInterface;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent.NpcTargetReason;

/**
 * Listener
 * 
 * @author fullwall
 */
public class EntityListen extends EntityListener {
	private final Citizens plugin;

	public EntityListen(final Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			HumanNPC npc = NPCManager.getNPC(e.getEntity());
			if (e.getEntity() instanceof Player
					&& e.getDamager() instanceof Player && npc != null) {
				e.setCancelled(true);
			}
			if(e.getEntity() instanceof Player && e.getDamager() instanceof Monster) {
				return;
			}
			if (npc.isHealer()) {
				Entity entity = e.getDamager();
				if (entity instanceof Player) {
					Player player = (Player) entity;
					int playerHealth = player.getHealth();
					int healerHealth = HealerPropertyPool.getStrength(npc
							.getUID());
					if (player.getItemInHand().getTypeId() == Citizens.healerTakeHealthItem) {
						if (playerHealth <= 19) {
							if (healerHealth >= 1) {
								player.setHealth(playerHealth + 1);
								HealerPropertyPool.saveStrength(npc.getUID(),
										healerHealth - 1);
								player.sendMessage(ChatColor.GREEN
										+ "You drained health from the healer "
										+ StringUtils.yellowify(npc
												.getStrippedName()) + ".");
							} else {
								player.sendMessage(StringUtils.yellowify(npc
										.getStrippedName())
										+ " does not have enough health remaining for you to take.");
							}
						} else {
							player.sendMessage(ChatColor.GREEN
									+ "You are fully healed.");
						}
					} else if (player.getItemInHand().getTypeId() == Citizens.healerGiveHealthItem) {
						if (playerHealth >= 1) {
							if (healerHealth < HealerPropertyPool
									.getMaxStrength(npc.getUID())) {
								player.setHealth(playerHealth - 1);
								HealerPropertyPool.saveStrength(npc.getUID(),
										healerHealth + 1);
								player.sendMessage(ChatColor.GREEN
										+ "You donated some health to the healer "
										+ StringUtils.yellowify(npc
												.getStrippedName()) + ".");
							} else {
								player.sendMessage(StringUtils.yellowify(npc
										.getStrippedName())
										+ " is fully healed.");
							}
						} else {
							player.sendMessage(ChatColor.GREEN
									+ "You do not have enough health remaining to heal "
									+ StringUtils.yellowify(npc
											.getStrippedName()));
						}
					} else if (player.getItemInHand().getType() == Material.DIAMOND_BLOCK) {
						if (healerHealth != HealerPropertyPool
								.getMaxStrength(npc.getUID())) {
							HealerPropertyPool.saveStrength(npc.getUID(),
									HealerPropertyPool.getMaxStrength(npc
											.getUID()));
							player.sendMessage(ChatColor.GREEN
									+ "You restored all of "
									+ StringUtils.yellowify(npc
											.getStrippedName()) + "'s health.");
							player.setItemInHand(null);
						} else {
							player.sendMessage(StringUtils.yellowify(npc
									.getStrippedName()) + " is fully healed.");
						}
					}
				}
			}
		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (!(event instanceof NPCEntityTargetEvent))
			return;
		NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
		HumanNPC npc = NPCManager.getNPC(e.getEntity());

		if (npc != null && event.getTarget() instanceof Player) {
			// The NPC lib handily provides a right click event.
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
				Player p = (Player) event.getTarget();
				boolean found = false;
				// Dispatch text event.
				if (plugin.validateTool("items", p.getItemInHand().getTypeId()) == true) {
					CitizensBasicNPCEvent ev = new CitizensBasicNPCEvent(
							npc.getName(), MessageUtils.getText(npc,
									(Player) e.getTarget(), plugin), npc,
							Reason.RIGHT_CLICK, (Player) e.getTarget());
					plugin.getServer().getPluginManager().callEvent(ev);
					found = true;
				}
				if (npc.isTrader()) {
					TraderInterface.handleRightClick(npc, p);
					found = true;
				}
				if (found && !plugin.canSelectAny())
					return;
				// If we're using a selection tool, select the NPC as well.
				// Check if we haven't already selected the NPC too.
				if (plugin.validateTool("select-item", p.getItemInHand()
						.getTypeId()) == true) {
					if (!NPCManager.validateSelected(p, npc.getUID())) {
						NPCManager.NPCSelected.put(p.getName(), npc.getUID());
						p.sendMessage(ChatColor.GREEN + "You selected NPC ["
								+ StringUtils.yellowify(npc.getStrippedName())
								+ "], ID ["
								+ StringUtils.yellowify("" + npc.getUID())
								+ "]");
					} else if (!found && plugin.canSelectAny()) {
						p.sendMessage(ChatColor.GREEN
								+ "That NPC is already selected!");
					}
				}
			}
		}
	}
}
