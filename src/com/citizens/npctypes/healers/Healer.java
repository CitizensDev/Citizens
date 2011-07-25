package com.citizens.npctypes.healers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.SettingsManager.Constant;
import com.citizens.commands.CommandHandler;
import com.citizens.commands.commands.HealerCommands;
import com.citizens.economy.EconomyManager;
import com.citizens.economy.EconomyOperation;
import com.citizens.interfaces.Saveable;
import com.citizens.npctypes.CitizensNPC;
import com.citizens.properties.properties.HealerProperties;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.InventoryUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.StringUtils;

public class Healer extends CitizensNPC {
	private int health = 10;
	private int level = 1;

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Get the maximum health of a healer NPC
	 * 
	 * @return
	 */
	public int getMaxHealth() {
		return level * 10;
	}

	/**
	 * Get the level of a healer NPC
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the level of a healer NPC
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String getType() {
		return "healer";
	}

	/**
	 * Purchase a heal from a healer
	 * 
	 * @param player
	 * @param npc
	 * @param op
	 */
	private void buyHeal(Player player, HumanNPC npc, EconomyOperation op,
			boolean healPlayer) {
		Healer healer = npc.getType("healer");
		if (!EconomyManager.useEconomy() || op.canBuy(player)) {
			double paid = op.pay(player);
			if (paid > 0) {
				int playerHealth = 0;
				int healerHealth = 0;
				String msg = StringUtils.wrap(npc.getStrippedName());
				if (healPlayer) {
					playerHealth = player.getHealth() + 1;
					healerHealth = healer.getHealth() - 1;
					msg += " healed you for "
							+ StringUtils.wrap(EconomyManager.getPaymentType(
									player, op, "" + paid)) + ".";
				} else {
					playerHealth = player.getHealth();
					healerHealth = healer.getHealth() + 1;
					msg += " has been healed for "
							+ StringUtils.wrap(EconomyManager.getPaymentType(
									player, op, "" + paid)) + ".";
				}
				player.setHealth(playerHealth);
				healer.setHealth(healerHealth);
				player.sendMessage(msg);
			}
		} else if (EconomyManager.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
		}
	}

	// TODO Make this less ugly to look at
	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		Healer healer = npc.getType("healer");
		int playerHealth = player.getHealth();
		int healerHealth = healer.getHealth();
		if (Permission.generic(player, "citizens.healer.use.heal")) {
			if (player.getItemInHand().getTypeId() == Constant.HealerTakeHealthItem
					.toInt()) {
				if (playerHealth < 20) {
					if (healerHealth > 0) {
						if (Constant.PayForHealerHeal.toBoolean()) {
							buyHeal(player, npc,
									EconomyManager.getOperation("healer-heal"),
									true);
						} else {
							player.setHealth(playerHealth + 1);
							healer.setHealth(healerHealth - 1);
							player.sendMessage(ChatColor.GREEN
									+ "You drained health from the healer "
									+ StringUtils.wrap(npc.getStrippedName())
									+ ".");
						}
					} else {
						player.sendMessage(StringUtils.wrap(npc
								.getStrippedName())
								+ " does not have enough health remaining for you to take.");
					}
				} else {
					player.sendMessage(ChatColor.GREEN
							+ "You are fully healed.");
				}
			} else if (player.getItemInHand().getTypeId() == Constant.HealerGiveHealthItem
					.toInt()) {
				if (playerHealth >= 1) {
					if (healerHealth < healer.getMaxHealth()) {
						player.setHealth(playerHealth - 1);
						healer.setHealth(healerHealth + 1);
						player.sendMessage(ChatColor.GREEN
								+ "You donated some health to the healer "
								+ StringUtils.wrap(npc.getStrippedName()) + ".");
					} else {
						player.sendMessage(StringUtils.wrap(npc
								.getStrippedName()) + " is fully healed.");
					}
				} else {
					player.sendMessage(ChatColor.GREEN
							+ "You do not have enough health remaining to heal "
							+ StringUtils.wrap(npc.getStrippedName()));
				}
			} else if (player.getItemInHand().getType() == Material.DIAMOND_BLOCK) {
				if (healerHealth != healer.getMaxHealth()) {
					healer.setHealth(healer.getMaxHealth());
					player.sendMessage(ChatColor.GREEN + "You restored all of "
							+ StringUtils.wrap(npc.getStrippedName())
							+ "'s health with a magical block of diamond.");
					InventoryUtils.decreaseItemInHand(player);
				} else {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " is fully healed.");
				}
			}
		}
	}

	@Override
	public Saveable getProperties() {
		return new HealerProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new HealerCommands();
	}

	@Override
	public Map<String, Object> getDefaultSettings() {
		Map<String, Object> nodes = new HashMap<String, Object>();
		nodes.put("prices.healer.levelup.item", 10);
		nodes.put("prices.healer.levelup.item-currency-id", 37);
		nodes.put("prices.healer.levelup.econplugin", 100);
		nodes.put("prices.healer.heal.item", 10);
		nodes.put("prices.healer.heal.item-currency-id", 37);
		nodes.put("prices.healer.heal.econplugin", 100);
		return nodes;
	}
}