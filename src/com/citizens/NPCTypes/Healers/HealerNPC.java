package com.citizens.NPCTypes.Healers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.citizens.Constants;
import com.citizens.Permission;
import com.citizens.Economy.EconomyHandler;
import com.citizens.Economy.EconomyHandler.Operation;
import com.citizens.Interfaces.Clickable;
import com.citizens.Interfaces.Toggleable;
import com.citizens.NPCTypes.Healers.HealerNPC;
import com.citizens.Resources.NPClib.HumanNPC;
import com.citizens.Utils.InventoryUtils;
import com.citizens.Utils.MessageUtils;
import com.citizens.Utils.StringUtils;

public class HealerNPC extends Toggleable implements Clickable {
	private int health = 10;
	private int level = 1;

	/**
	 * Healer NPC object
	 * 
	 * @param npc
	 */
	public HealerNPC(HumanNPC npc) {
		super(npc);
	}

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
	private void buyHeal(Player player, HumanNPC npc, Operation op,
			boolean healPlayer) {
		HealerNPC healer = npc.getToggleable("healer");
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			double paid = EconomyHandler.pay(op, player);
			if (paid > 0) {
				int playerHealth = 0;
				int healerHealth = 0;
				String msg = StringUtils.wrap(npc.getStrippedName());
				if (healPlayer) {
					playerHealth = player.getHealth() + 1;
					healerHealth = healer.getHealth() - 1;
					msg += " healed you for "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									op, "" + paid)) + ".";
				} else {
					playerHealth = player.getHealth();
					healerHealth = healer.getHealth() + 1;
					msg += " has been healed for "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									op, "" + paid)) + ".";
				}
				player.setHealth(playerHealth);
				healer.setHealth(healerHealth);
				player.sendMessage(msg);
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(
					Operation.HEALER_HEAL, player));
		}
	}

	// TODO Make this less ugly to look at
	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		HealerNPC healer = npc.getToggleable("healer");
		int playerHealth = player.getHealth();
		int healerHealth = healer.getHealth();
		if (Permission.canUse(player, npc, getType())) {
			if (player.getItemInHand().getTypeId() == Constants.healerTakeHealthItem) {
				if (playerHealth < 20) {
					if (healerHealth > 0) {
						if (Constants.payForHealerHeal) {
							buyHeal(player, npc, Operation.HEALER_HEAL, true);
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
			} else if (player.getItemInHand().getTypeId() == Constants.healerGiveHealthItem) {
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
					InventoryUtils.decreaseItemInHand(player,
							Material.DIAMOND_BLOCK);
					// TODO Uncomment after BukkitContrib is updated to MC 1.7.2
					// Achievements.award(player, Achievement.HEALER_REVIVE);
				} else {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " is fully healed.");
				}
			}
		}
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
	}
}