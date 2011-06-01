package com.fullwall.Citizens.NPCTypes.Healers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerNPC implements Toggleable, Clickable {
	private HumanNPC npc;
	private int health = 10;
	private int level = 1;

	/**
	 * Healer NPC object
	 * 
	 * @param npc
	 */
	public HealerNPC(HumanNPC npc) {
		this.npc = npc;
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
	public void toggle() {
		npc.setHealer(!npc.isHealer());
	}

	@Override
	public boolean getToggle() {
		return npc.isHealer();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "healer";
	}

	@Override
	public void saveState() {
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		int playerHealth = player.getHealth();
		int healerHealth = npc.getHealer().getHealth();
		if (Permission.canUse(player, getType())) {
			if (player.getItemInHand().getTypeId() == Constants.healerTakeHealthItem) {
				if (playerHealth < 20) {
					if (healerHealth > 0) {
						player.setHealth(playerHealth + 1);
						npc.getHealer().setHealth(healerHealth - 1);
						player.sendMessage(ChatColor.GREEN
								+ "You drained health from the healer "
								+ StringUtils.wrap(npc.getStrippedName()) + ".");
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
					if (healerHealth < npc.getHealer().getMaxHealth()) {
						player.setHealth(playerHealth - 1);
						npc.getHealer().setHealth(healerHealth + 1);
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
				if (healerHealth != npc.getHealer().getMaxHealth()) {
					npc.getHealer().setHealth(npc.getHealer().getMaxHealth());
					player.sendMessage(ChatColor.GREEN + "You restored all of "
							+ StringUtils.wrap(npc.getStrippedName())
							+ "'s health with a magical block of diamond.");
					int amountInHand = player.getItemInHand().getAmount();
					if (amountInHand == 1) {
						ItemStack emptyStack = null;
						player.setItemInHand(emptyStack);
					} else {
						player.setItemInHand(new ItemStack(
								Material.DIAMOND_BLOCK, amountInHand - 1));
					}
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