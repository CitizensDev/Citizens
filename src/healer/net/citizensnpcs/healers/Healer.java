package net.citizensnpcs.healers;

import net.citizensnpcs.Economy;
import net.citizensnpcs.Settings;
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
			if (UtilityProperties.isHoldingTool("HealerTakeHealthItem", player)) {
				if (playerHealth == 20) {
					player.sendMessage(ChatColor.GREEN
							+ "Twoje zycie jest juz pelne!");
					return;
				}
				if (healerHealth == 0) {
					player.sendMessage(StringUtils.wrap(npc.getName())
							+ " Niestety ale skonczyly mi sie apteczki.");
					return;
				}
				if (Economy.useEconPlugin()) {
					if (Economy.hasEnough(player,
							UtilityProperties.getPrice("healer.heal"))) {
						double paid = Economy.pay(player,
								UtilityProperties.getPrice("healer.heal"));
						if (paid >= 0) {
							player.sendMessage(StringUtils.wrap(npc.getName())
									+ " uzdrowil cie za "
									+ StringUtils.wrap(Economy.format(paid))
									+ ".");
						}
					} else {
						player.sendMessage(MessageUtils.getNoMoneyMessage(
								player, "healer.heal"));
						return;
					}
				} else {
					player.sendMessage(StringUtils.wrap(npc.getName())
							+ " uzdrowil cie.");
				}
				player.setHealth(player.getHealth() + 1);
				healer.setHealth(healer.getHealth() - 1);
			} else if (UtilityProperties.isHoldingTool("HealerGiveHealthItem",
					player)) {
				if (playerHealth >= 1) {
					if (healerHealth < healer.getMaxHealth()) {
						player.setHealth(playerHealth - 1);
						healer.setHealth(healerHealth + 1);
						player.sendMessage(ChatColor.GREEN
								+ "Dziekuje ze oddales pol twojego serca. "
								+ StringUtils.wrap(npc.getName()) + ".");
					} else {
						player.sendMessage(StringUtils.wrap(npc.getName())
								+ " ma juz duzo apteczek.");
					}
				} else {
					player.sendMessage(ChatColor.GREEN
							+ "You do not have enough health remaining to heal "
							+ StringUtils.wrap(npc.getName()));
				}
			} else if (player.getItemInHand().getType() == Material.DIAMOND_BLOCK) {
				if (healerHealth != healer.getMaxHealth()) {
					healer.setHealth(healer.getMaxHealth());
					player.sendMessage(ChatColor.GREEN + "Dales blok diamentu  "
							+ StringUtils.wrap(npc.getName())
							+ " i otrzymal maksymalna ilosc apteczek.");
					InventoryUtils.decreaseItemInHand(player);
				} else {
					player.sendMessage(StringUtils.wrap(npc.getName())
							+ " ma juz maksymalna ilosc apteczek.");
				}
			}
		}
	}

	// Get the health regeneration rate for a healer based on its level
	public int getHealthRegenRate() {
		return Settings.getInt("HealerHealthRegenIncrement")
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