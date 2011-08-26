package net.citizensnpcs.wizards;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.wizards.WizardManager.WizardMode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class Wizard extends CitizensNPC {
	private String locations = "";
	private int currentLocation = 0;
	private int numberOfLocations = 0;
	private Location currentLoc;
	private WizardMode mode = WizardMode.TELEPORT;
	private int mana = 10;
	private String time = "morning";
	private CreatureType mob = CreatureType.CHICKEN;
	private boolean unlimitedMana = false;
	private String command = "";

	// Adds a location to the main location sting.
	public void addLocation(Location location, String locName) {
		String addedLoc = "";
		addedLoc = "(" + locName + "," + location.getWorld().getName() + ","
				+ location.getX() + "," + location.getY() + ","
				+ location.getZ() + "," + location.getYaw() + ","
				+ location.getPitch() + "):";
		locations = locations + addedLoc;
		numberOfLocations = locations.split(":").length;
		if (locations.split(":")[0].isEmpty()) {
			numberOfLocations = 0;
		}
	}

	// Returns the main location string
	public String getLocations() {
		return locations;
	}

	// Sets all the locations of the wizard (one big combined string)
	public void setLocations(String locationsinc) {
		locations = locationsinc;
		numberOfLocations = locations.split(":").length;
		if (locations.split(":")[0].isEmpty()) {
			numberOfLocations = 0;
		}
	}

	// Sets the next location in the list as active.
	public void cycle(HumanNPC npc, WizardMode mode) {
		Wizard wizard = npc.getType("wizard");
		switch (mode) {
		case TELEPORT:
			currentLocation++;
			if (currentLocation >= numberOfLocations) {
				currentLocation = 0;
			}
			break;
		case SPAWN:
			CreatureType type = wizard.getMob();
			CreatureType newType = null;
			switch (type) {
			case PIG:
				newType = CreatureType.WOLF;
				break;
			case WOLF:
				newType = CreatureType.COW;
				break;
			case COW:
				newType = CreatureType.CHICKEN;
				break;
			case CHICKEN:
				newType = CreatureType.SHEEP;
				break;
			case SHEEP:
				newType = CreatureType.SQUID;
				break;
			case SQUID:
				newType = CreatureType.PIG_ZOMBIE;
				break;
			case PIG_ZOMBIE:
				newType = CreatureType.GHAST;
				break;
			case GHAST:
				newType = CreatureType.GIANT;
				break;
			case GIANT:
				newType = CreatureType.CREEPER;
				break;
			case CREEPER:
				newType = CreatureType.ZOMBIE;
				break;
			case ZOMBIE:
				newType = CreatureType.SPIDER;
				break;
			case SPIDER:
				newType = CreatureType.SKELETON;
				break;
			case SKELETON:
				newType = CreatureType.PIG;
				break;
			}
			wizard.setMob(newType);
			break;
		case TIME:
			String time = wizard.getTime();
			String newTime = "";
			if (time.equals("day")) {
				newTime = "afternoon";
			} else if (time.equals("night")) {
				newTime = "morning";
			} else if (time.equals("morning")) {
				newTime = "day";
			} else if (time.equals("afternoon")) {
				newTime = "night";
			}
			wizard.setTime(newTime);
			break;
		}
	}

	// Returns the total amount of locations bound to the wizard.
	public int getNumberOfLocations() {
		return numberOfLocations;
	}

	// Return the current active teleport location for the wizard.
	public Location getCurrentLocation() {
		String locs[] = locations.split(":")[currentLocation].split(",");
		this.currentLoc = new Location(Bukkit.getServer().getWorld(locs[1]),
				Double.parseDouble(locs[2]), Double.parseDouble(locs[3]),
				Double.parseDouble(locs[4]), Float.parseFloat(locs[5]),
				Float.parseFloat(locs[6].replace(")", "")));
		return this.currentLoc;
	}

	// Return the current active teleport location name for the wizard.
	public String getCurrentLocationName() {
		String locs[] = locations.split(":")[currentLocation].split(",");
		return locs[0].replace("(", "");
	}

	// Get the mana that a wizard NPC has remaining
	public int getMana() {
		return mana;
	}

	// Set the mana of a wizard NPC
	public void setMana(int mana) {
		this.mana = mana;
	}

	// Get the mode of a wizard
	public WizardMode getMode() {
		return mode;
	}

	// Set the mode of a wizard
	public void setMode(WizardMode mode) {
		this.mode = mode;
	}

	// Get the time setting of a wizard
	public String getTime() {
		return time;
	}

	// Set the time setting for a wizard
	public void setTime(String time) {
		this.time = time;
	}

	// Get the mob that a wizard will spawn
	public CreatureType getMob() {
		return mob;
	}

	// Set the mob that a wizard will spawn
	public void setMob(CreatureType mob) {
		this.mob = mob;
	}

	// Get whether a wizard has unlimited mana
	public boolean hasUnlimitedMana() {
		return unlimitedMana;
	}

	// Set whether a wizard has unlimited mana
	public void setUnlimitedMana(boolean unlimitedMana) {
		this.unlimitedMana = unlimitedMana;
	}

	// Get a wizard's current command to execute
	public String getCommand() {
		return command;
	}

	// Set the command for a wizard to execute
	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		if (PermissionManager.generic(player, "citizens.wizard.use.interact")) {
			if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("WizardInteractItem")) {
				Wizard wizard = npc.getType("wizard");
				String msg = ChatColor.GREEN + "";
				switch (wizard.getMode()) {
				case COMMAND:
					return;
				case TELEPORT:
					if (wizard.getNumberOfLocations() > 0) {
						wizard.cycle(npc, WizardMode.TELEPORT);
						msg += "Location set to "
								+ StringUtils.wrap(wizard
										.getCurrentLocationName());
					} else {
						msg += ChatColor.RED + npc.getStrippedName()
								+ " has no locations.";
					}
					break;
				case SPAWN:
					wizard.cycle(npc, WizardMode.SPAWN);
					msg += "Mob to spawn set to "
							+ StringUtils.wrap(StringUtils.format(wizard
									.getMob()));
					break;
				case TIME:
					wizard.cycle(npc, WizardMode.TIME);
					msg += "Time setting set to "
							+ StringUtils.wrap(wizard.getTime());
					break;
				case WEATHER:
					return;
				default:
					msg = ChatColor.RED + "No valid mode selected.";
					break;
				}
				player.sendMessage(msg);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (PermissionManager.generic(player, "citizens.wizard.use.interact")) {
			Wizard wizard = npc.getType("wizard");
			if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("WizardInteractItem")) {
				String op = "";
				switch (wizard.getMode()) {
				case COMMAND:
					op = "executecommand";
					break;
				case TELEPORT:
					op = "teleport";
					break;
				case TIME:
					op = "changetime";
					break;
				case SPAWN:
					op = "spawnmob";
					break;
				case WEATHER:
					op = "togglestorm";
					break;
				}
				WizardManager.handleRightClick(player, npc, "wizard." + op);
			} else if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("WizardManaRegenItem")) {
				String msg = StringUtils.wrap(npc.getStrippedName() + "'s");
				int mana = 0;
				if (wizard.getMana() + 10 < SettingsManager
						.getInt("WizardMaxMana")) {
					mana = wizard.getMana() + 10;
					msg += " mana has been increased to "
							+ StringUtils.wrap(mana) + ".";
				} else if (wizard.getMana() + 10 == SettingsManager
						.getInt("WizardMaxMana")) {
					mana = SettingsManager.getInt("WizardMaxMana");
					msg += " mana has been fully replenished.";
				} else {
					msg += " mana cannot be regenerated with that item any further.";
					return;
				}
				InventoryUtils.decreaseItemInHand(player);
				player.sendMessage(msg);
				wizard.setMana(mana);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public CitizensNPCType getType() {
		return new WizardType();
	}
}