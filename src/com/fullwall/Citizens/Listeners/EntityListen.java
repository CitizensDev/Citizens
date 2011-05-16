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
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent.Reason;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Traders.TraderInterface;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.Citizens.Wizards.WizardNPC;
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
	private PluginManager pm;

	public EntityListen(final Citizens plugin) {
		this.plugin = plugin;
	}

	/**
	 * Register entity events
	 */
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this, Event.Priority.Normal,
				plugin);
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			HumanNPC npc = NPCManager.getNPC(e.getEntity());
			if (e.getEntity() instanceof Player && npc != null) {
				e.setCancelled(true);
			}
			if (npc != null && e.getDamager() instanceof Player
					&& npc.isHealer()) {
				Entity entity = e.getDamager();
				if (entity instanceof Player) {
					Player player = (Player) entity;
					int playerHealth = player.getHealth();
					int healerHealth = npc.getHealer().getStrength();
					if (player.getItemInHand().getTypeId() == Constants.healerTakeHealthItem) {
						if (playerHealth <= 19) {
							if (healerHealth >= 1) {
								player.setHealth(playerHealth + 1);
								npc.getHealer().setStrength(healerHealth - 1);
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
					} else if (player.getItemInHand().getTypeId() == Constants.healerGiveHealthItem) {
						if (playerHealth >= 1) {
							if (healerHealth < npc.getHealer().getMaxStrength()) {
								player.setHealth(playerHealth - 1);
								npc.getHealer().setStrength(healerHealth + 1);
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
						if (healerHealth != npc.getHealer().getMaxStrength()) {
							npc.getHealer().setStrength(
									npc.getHealer().getMaxStrength());
							player.sendMessage(ChatColor.GREEN
									+ "You restored all of "
									+ StringUtils.yellowify(npc
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
							player.sendMessage(StringUtils.yellowify(npc
									.getStrippedName()) + " is fully healed.");
						}
					}
				}
			}
			if (npc != null && e.getDamager() instanceof Player
					&& npc.isWizard()) {
				Entity entity = e.getDamager();
				if (entity instanceof Player) {
					Player player = (Player) entity;
					if (Permission.hasPermission(
							"citizens.wizard.changeteleport",
							(CommandSender) player)) {
						if (player.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
							if (!npc.isWizard()) {
								return;
							}
							if (npc.getWizard().getNrOfLocations() > 0) {
								npc.getWizard().nextLocation();
								player.sendMessage(ChatColor.GREEN
										+ "Location set to "
										+ StringUtils.yellowify(npc.getWizard()
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

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (NPCManager.getNPC(event.getTarget()) != null) {
			event.setCancelled(true);
		}
		if (!(event instanceof NPCEntityTargetEvent))
			return;
		NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
		HumanNPC npc = NPCManager.getNPC(e.getEntity());
		if (npc != null && event.getTarget() instanceof Player) {
			// The NPC lib handily provides a right click event.
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
				Player p = (Player) event.getTarget();
				if (plugin.validateTool("select-item", p.getItemInHand()
						.getTypeId(), p.isSneaking()) == true) {
					if (!NPCManager.validateSelected(p, npc.getUID())) {
						NPCManager.NPCSelected.put(p.getName(), npc.getUID());
						p.sendMessage(ChatColor.GREEN + "You selected NPC "
								+ StringUtils.yellowify(npc.getStrippedName())
								+ ", ID "
								+ StringUtils.yellowify("" + npc.getUID())
								+ ".");
						return;
					}
				} // Dispatch text event.
					// If we're using a selection tool, select the NPC as well.
					// Check if we haven't already selected the NPC too.
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
							if (npc.getWizard().getNrOfLocations() > 0) {
								this.buyTeleport(p, npc.getWizard(),
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
						buyItemRepair(p, npc, p.getItemInHand());
					} else {
						p.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
			}
		}
	}

	/**
	 * Purchase a teleport
	 * 
	 * @param player
	 * @param wizard
	 * @param op
	 */
	private void buyTeleport(Player player, WizardNPC wizard, Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0)
					player.sendMessage(ChatColor.GREEN
							+ "Paid "
							+ StringUtils.yellowify(EconomyHandler
									.getPaymentType(op, "" + paid,
											ChatColor.YELLOW))
							+ " for a teleport to "
							+ StringUtils.yellowify(wizard
									.getCurrentLocationName()) + ".");
				player.teleport(wizard.getCurrentLocation());

			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
			return;
		} else {
			player.teleport(wizard.getCurrentLocation());
			player.sendMessage(ChatColor.GREEN + "You got teleported to "
					+ StringUtils.yellowify(wizard.getCurrentLocationName())
					+ ".");
		}
	}

	/**
	 * Purchase a item repair
	 * 
	 * @param player
	 * @param npc
	 * @param op
	 */
	private void buyItemRepair(Player player, HumanNPC npc, ItemStack item) {
		if (!EconomyHandler.useEconomy()
				|| EconomyHandler.canBuy(Operation.BLACKSMITH_TOOL_REPAIR,
						player)) {
			if (EconomyHandler.useEconomy()) {
				if (npc.getBlacksmith().validateItemToRepair(item)) {
					if (item.getDurability() > 0) {
						double paid = EconomyHandler.pay(
								Operation.BLACKSMITH_TOOL_REPAIR, player);
						if (paid > 0) {
							item.setDurability((short) 0);
							player.setItemInHand(item);
							player.sendMessage(StringUtils.yellowify(npc
									.getStrippedName())
									+ " repaired your item for "
									+ StringUtils.yellowify(EconomyHandler
											.getPaymentType(
													Operation.BLACKSMITH_TOOL_REPAIR,
													"" + paid, ChatColor.YELLOW))
									+ ".");
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "Your item is already fully repaired.");
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(
					Operation.BLACKSMITH_TOOL_REPAIR, player));
			return;
		}
	}
}