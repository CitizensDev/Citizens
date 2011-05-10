package com.fullwall.Citizens.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent.Reason;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Traders.TraderInterface;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
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
			if (e.getDamager() instanceof Player && npc.isHealer()) {
				Entity entity = e.getDamager();
				if (entity instanceof Player) {
					Player player = (Player) entity;
					if (Permission.hasPermission("citizens.healer.interact",
							(CommandSender) player)) {
						int playerHealth = player.getHealth();
						int healerHealth = HealerPropertyPool.getStrength(npc
								.getUID());
						if (player.getItemInHand().getTypeId() == Citizens.healerTakeHealthItem) {
							if (playerHealth <= 19) {
								if (healerHealth >= 1) {
									player.setHealth(playerHealth + 1);
									HealerPropertyPool.saveStrength(
											npc.getUID(), healerHealth - 1);
									player.sendMessage(ChatColor.GREEN
											+ "You drained health from the healer "
											+ StringUtils.yellowify(npc
													.getStrippedName()) + ".");
								} else {
									player.sendMessage(StringUtils
											.yellowify(npc.getStrippedName())
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
									HealerPropertyPool.saveStrength(
											npc.getUID(), healerHealth + 1);
									player.sendMessage(ChatColor.GREEN
											+ "You donated some health to the healer "
											+ StringUtils.yellowify(npc
													.getStrippedName()) + ".");
								} else {
									player.sendMessage(StringUtils
											.yellowify(npc.getStrippedName())
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
												.getStrippedName())
										+ "'s health.");
								int x = player.getItemInHand().getAmount();
								ItemStack diamondBlock = new ItemStack(
										Material.DIAMOND_BLOCK, x - 1);
								player.setItemInHand(diamondBlock);
							} else {
								player.sendMessage(StringUtils.yellowify(npc
										.getStrippedName())
										+ " is fully healed.");
							}
						}
					} else {
						player.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
			}
			if (e.getDamager() instanceof Player && npc.isWizard()) {
				Entity entity = e.getDamager();
				if (entity instanceof Player) {
					Player player = (Player) entity;
					if (Permission.hasPermission(
							"citizens.wizard.changeteleport",
							(CommandSender) player)) {
						if (player.getItemInHand().getTypeId() == Citizens.wizardInteractItem) {
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
		if (!(event instanceof NPCEntityTargetEvent))
			return;
		NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
		HumanNPC npc = NPCManager.getNPC(e.getEntity());
		if (NPCManager.getNPC(e.getTarget()) != null) {
			if (e.getEntity() instanceof Monster
					&& !(e.getEntity() instanceof Player))
				e.setCancelled(true);
		}
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
				if (npc.isWizard()) {
					if (Permission.hasPermission("citizens.wizard.useteleport",
							(CommandSender) p)) {
						if (p.getItemInHand().getTypeId() == Citizens.wizardInteractItem) {
							if (npc.getWizard().getNrOfLocations() > 0) {
								this.buyTeleport(p, npc.getWizard(),
										Operation.WIZARD_TELEPORT);
								found = true;
							}
						}
					} else {
						p.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
				if (npc.isBlacksmith()) {
					if (Permission.hasPermission("citizens.blacksmith.repair",
							(CommandSender) p)) {
						String item = getToolType(p.getItemInHand());
						if (item.equals("cheap")) {
							buyRepair(p, npc,
									Operation.BLACKSMITH_TOOL_REPAIR);
							found = true;
						}
					} else {
						p.sendMessage(MessageUtils.noPermissionsMessage);
					}
				}
				if (found && !plugin.canSelectAny())
					return;
				// If we're using a selection tool, select the NPC as well.
				// Check if we haven't already selected the NPC too.
				if (plugin.validateTool("select-item", p.getItemInHand()
						.getTypeId()) == true) {
					if (!NPCManager.validateSelected(p, npc.getUID())) {
						NPCManager.NPCSelected.put(p.getName(), npc.getUID());
						p.sendMessage(ChatColor.GREEN + "You selected NPC "
								+ StringUtils.yellowify(npc.getStrippedName())
								+ ", ID "
								+ StringUtils.yellowify("" + npc.getUID())
								+ ".");
					} else if (!found && plugin.canSelectAny()) {
						p.sendMessage(ChatColor.GREEN
								+ "That NPC is already selected!");
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
							+ EconomyHandler.getPaymentType(op, "" + paid,
									ChatColor.YELLOW)
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
					+ StringUtils.yellowify(wizard.getCurrentLocationName()) + ".");
		}
	}

	/**
	 * Purchase a tool repair
	 * 
	 * @param player
	 * @param npc
	 * @param op
	 */
	private void buyRepair(Player player, HumanNPC npc, Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0) {
					if (player.getItemInHand().getDurability() > 0) {
						player.getItemInHand().setDurability((short) 0);
						player.sendMessage(StringUtils.yellowify(npc
								.getStrippedName())
								+ " repaired your tool for "
								+ EconomyHandler.getPaymentType(op, "" + paid,
										ChatColor.YELLOW) + ".");
					} else {
						player.sendMessage(ChatColor.RED
								+ "Your tool is already fully repaired.");
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
			return;
		}
	}

	/**
	 * Get a string of a tool type
	 * 
	 * @param item
	 * @return
	 */
	private String getToolType(ItemStack item) {
		String type = "";
		switch (item.getTypeId()) {
		case 256:
			type = "exp";
		case 257:
			type = "exp";
		case 258:
			type = "exp";
		case 259:
			type = "cheap";
		case 267:
			type = "exp";
		case 268:
			type = "cheap";
		case 269:
			type = "cheap";
		case 270:
			type = "cheap";
		case 271:
			type = "cheap";
		case 272:
			type = "cheap";
		case 273:
			type = "cheap";
		case 274:
			type = "cheap";
		case 275:
			type = "cheap";
		case 276:
			type = "exp";
		case 277:
			type = "exp";
		case 278:
			type = "exp";
		case 279:
			type = "exp";
		case 283:
			type = "cheap";
		case 284:
			type = "cheap";
		case 285:
			type = "cheap";
		case 286:
			type = "cheap";
		case 290:
			type = "cheap";
		case 291:
			type = "cheap";
		case 292:
			type = "exp";
		case 293:
			type = "exp";
		case 294:
			type = "cheap";
		case 346:
			type = "cheap";
		}
		return type;
	}
}