package net.citizensnpcs.healers;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Healer extends CitizensNPC {
	// TODO: unlimited health option
	private int health = 10;
	private int level = 1;

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	// Get the maximum health of a healer NPC
	public int getMaxHealth() {
		return level * 10;
	}

	// Get the level of a healer NPC
	public int getLevel() {
		return level;
	}

	// Set the level of a healer NPC
	public void setLevel(int level) {
		this.level = level;
	}

	// TODO Make this less ugly to look at
	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (PermissionManager.hasPermission(player, "citizens.healer.use.heal")) {
			Healer healer = npc.getType("healer");
			int playerHealth = player.getHealth();
			int healerHealth = healer.getHealth();
			if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("HealerTakeHealthItem")) {
				if (playerHealth == 20) {
					player.sendMessage(ChatColor.GREEN
							+ "You are fully healed.");
					return;
				}
				if (healerHealth == 0) {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " does not have enough health remaining for you to take.");
					return;
				}
				if (EconomyManager.useEconPlugin()) {
					if (EconomyManager.hasEnough(player,
							UtilityProperties.getPrice("healer.heal"))) {
						double paid = EconomyManager.pay(player,
								UtilityProperties.getPrice("healer.heal"));
						if (paid >= 0) {
							player.sendMessage(StringUtils.wrap(npc
									.getStrippedName())
									+ " has healed you for "
									+ StringUtils.wrap(EconomyManager
											.format(paid)) + ".");
						}
					} else {
						player.sendMessage(MessageUtils.getNoMoneyMessage(
								player, "healer.heal"));
						return;
					}
				} else {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " has healed you.");
				}
				player.setHealth(player.getHealth() + 1);
				healer.setHealth(healer.getHealth() - 1);
			} else if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("HealerGiveHealthItem")) {
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

	// Get the health regeneration rate for a healer based on its level
	public int getHealthRegenRate() {
		return SettingsManager.getInt("HealerHealthRegenIncrement")
				* (11 - (getLevel()));
	}

	@Override
	public CitizensNPCType getType() {
		return new HealerType();
	}

	@Override
	public void save(Storage profiles, int UID) {
		profiles.setInt(UID + ".healer.health", health);
		profiles.setInt(UID + ".healer.level", level);

	}

	@Override
	public void load(Storage profiles, int UID) {
		health = profiles.getInt(UID + ".healer.health", 10);
		level = profiles.getInt(UID + ".healer.level", 1);
	}
}