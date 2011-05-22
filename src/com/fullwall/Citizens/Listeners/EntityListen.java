package com.fullwall.Citizens.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent.Reason;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Bandits.BanditInterface;
import com.fullwall.Citizens.NPCTypes.Traders.TraderInterface;
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
	private final Citizens plugin;
	private PluginManager pm;

	public EntityListen(final Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.ENTITY_DEATH, this, Event.Priority.Normal,
				plugin);
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			HumanNPC npc = NPCManager.get(e.getEntity());
			if (e.getEntity() instanceof Player && npc != null) {
				e.setCancelled(true);
			}
			if (npc != null && e.getDamager() instanceof Player) {
				Entity entity = e.getDamager();
				if (entity instanceof Player) {
					Player player = (Player) entity;
					if (npc.isHealer()) {
						int playerHealth = player.getHealth();
						int healerHealth = npc.getHealer().getHealth();
						if (player.getItemInHand().getTypeId() == Constants.healerTakeHealthItem) {
							if (playerHealth < 20) {
								if (healerHealth > 0) {
									player.setHealth(playerHealth + 1);
									npc.getHealer().setHealth(healerHealth - 1);
									player.sendMessage(ChatColor.GREEN
											+ "You drained health from the healer "
											+ StringUtils.wrap(npc
													.getStrippedName()) + ".");
								} else {
									player.sendMessage(StringUtils.wrap(npc
											.getStrippedName())
											+ " does not have enough health remaining for you to take.");
								}
							} else {
								player.sendMessage(ChatColor.GREEN
										+ "You are fully healed.");
							}
						} else if (player.getItemInHand().getTypeId() == Constants.healerGiveHealthItem) {
							if (playerHealth >= 1) {
								if (healerHealth < npc.getHealer()
										.getMaxHealth()) {
									player.setHealth(playerHealth - 1);
									npc.getHealer().setHealth(healerHealth + 1);
									player.sendMessage(ChatColor.GREEN
											+ "You donated some health to the healer "
											+ StringUtils.wrap(npc
													.getStrippedName()) + ".");
								} else {
									player.sendMessage(StringUtils.wrap(npc
											.getStrippedName())
											+ " is fully healed.");
								}
							} else {
								player.sendMessage(ChatColor.GREEN
										+ "You do not have enough health remaining to heal "
										+ StringUtils.wrap(npc
												.getStrippedName()));
							}
						} else if (player.getItemInHand().getType() == Material.DIAMOND_BLOCK) {
							if (healerHealth != npc.getHealer().getMaxHealth()) {
								npc.getHealer().setHealth(
										npc.getHealer().getMaxHealth());
								player.sendMessage(ChatColor.GREEN
										+ "You restored all of "
										+ StringUtils.wrap(npc
												.getStrippedName())
										+ "'s health with a magical block of diamond.");
								int amountInHand = player.getItemInHand()
										.getAmount();
								if (amountInHand == 1) {
									ItemStack emptyStack = null;
									player.setItemInHand(emptyStack);
								} else {
									player.setItemInHand(new ItemStack(
											Material.DIAMOND_BLOCK,
											amountInHand - 1));
								}
							} else {
								player.sendMessage(StringUtils.wrap(npc
										.getStrippedName())
										+ " is fully healed.");
							}
						}
					}
					if (npc.isWizard()) {
						if (Permission.hasPermission(
								"citizens.wizard.changeteleport",
								(CommandSender) player)) {
							if (player.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
								if (npc.getWizard().getNumberOfLocations() > 0) {
									npc.getWizard().cycleLocation();
									player.sendMessage(ChatColor.GREEN
											+ "Location set to "
											+ StringUtils.wrap(npc.getWizard()
													.getCurrentLocationName()));
								}
							}
						} else {
							player.sendMessage(MessageUtils.noPermissionsMessage);
						}
					}
				}
			}
		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (NPCManager.isNPC(event.getTarget())) {
			event.setCancelled(true);
		}
		if (!(event instanceof NPCEntityTargetEvent)) {
			return;
		}
		NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
		HumanNPC npc = NPCManager.get(e.getEntity());
		if (npc != null && event.getTarget() instanceof Player) {
			// The NPC lib handily provides a right click event.
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
				Player p = (Player) event.getTarget();
				if (plugin.validateTool("select-item", p.getItemInHand()
						.getTypeId(), p.isSneaking()) == true) {
					if (!NPCManager.validateSelected(p, npc.getUID())) {
						NPCManager.selectedNPCs.put(p.getName(), npc.getUID());
						p.sendMessage(ChatColor.GREEN + "You selected NPC "
								+ StringUtils.wrap(npc.getStrippedName())
								+ ", ID " + StringUtils.wrap("" + npc.getUID())
								+ ".");
						return;
					}
				}
				// Dispatch text event / select NPC.
				if (plugin.validateTool("items", p.getItemInHand().getTypeId(),
						p.isSneaking()) == true) {
					CitizensBasicNPCEvent ev = new CitizensBasicNPCEvent(
							npc.getName(), MessageUtils.getText(npc,
									(Player) e.getTarget(), plugin), npc,
							Reason.RIGHT_CLICK, (Player) e.getTarget());
					plugin.getServer().getPluginManager().callEvent(ev);
				}
				if (npc.isTrader()) {
					TraderInterface.handleRightClick(npc, p);
				}
				if (npc.isWizard()) {
					if (Permission.hasPermission("citizens.wizard.useteleport",
							(CommandSender) p)) {
						if (p.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
							if (npc.getWizard().getNumberOfLocations() > 0) {
								npc.getWizard().buyTeleport(p, npc.getWizard(),
										Operation.WIZARD_TELEPORT);
							}
						}
					} else {
						p.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
				if (npc.isBlacksmith()) {
					if (Permission.hasPermission("citizens.blacksmith.repair",
							(CommandSender) p)) {
						if (npc.getBlacksmith().getToolType(p.getItemInHand())
								.equals("tool")) {
							npc.getBlacksmith().buyItemRepair(p, npc,
									p.getItemInHand(),
									Operation.BLACKSMITH_TOOL_REPAIR);
						} else if (npc.getBlacksmith()
								.getToolType(p.getItemInHand()).equals("armor")) {
							npc.getBlacksmith().buyItemRepair(p, npc,
									p.getItemInHand(),
									Operation.BLACKSMITH_ARMOR_REPAIR);
						}
					} else {
						p.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
				if (npc.isBandit()) {
					BanditInterface.handleRightClick(npc, p);
				}
			}
		}
	}
}