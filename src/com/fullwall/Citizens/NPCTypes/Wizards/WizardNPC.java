package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Enums.WizardMode;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardNPC implements Toggleable, Clickable {
	private HumanNPC npc;
	private String locations = "";
	private int currentLocation = 0;
	private int numberOfLocations = 0;
	private Location currentLoc;
	private WizardMode mode = WizardMode.TELEPORT;
	private int mana = 10;
	private String time = "morning";
	private CreatureType mob = CreatureType.CHICKEN;

	/**
	 * Wizard NPC object
	 * 
	 * @param npc
	 */
	public WizardNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Addds a location to the main location sting.
	 * 
	 * @param location
	 * @param locName
	 */
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

	/**
	 * Returns the main location string.
	 * 
	 * @return
	 */
	public String getLocations() {
		return locations;
	}

	/**
	 * Sets all the locations of the wizard (one big combined string)
	 * 
	 * @param locationsinc
	 */
	public void setLocations(String locationsinc) {
		locations = locationsinc;
		numberOfLocations = locations.split(":").length;
		if (locations.split(":")[0].isEmpty()) {
			numberOfLocations = 0;
		}
	}

	/**
	 * Sets the next location in the list as active.
	 * 
	 * @return
	 */
	public void cycleLocation() {
		currentLocation++;
		if (currentLocation >= numberOfLocations) {
			currentLocation = 0;
		}
	}

	/**
	 * Returns the total amount of locations bound to the wizard.
	 * 
	 * @return
	 */
	public int getNumberOfLocations() {
		return numberOfLocations;
	}

	/**
	 * Return the current active teleport location for the wizard.
	 * 
	 * @return
	 */
	public Location getCurrentLocation() {
		String locs[] = locations.split(":")[currentLocation].split(",");
		this.currentLoc = new Location(Bukkit.getServer().getWorld(locs[1]),
				Double.parseDouble(locs[2]), Double.parseDouble(locs[3]),
				Double.parseDouble(locs[4]), Float.parseFloat(locs[5]),
				Float.parseFloat(locs[6].replace(")", "")));
		return this.currentLoc;
	}

	/**
	 * Return the current active teleport location name for the wizard.
	 * 
	 * @return
	 */
	public String getCurrentLocationName() {
		String locs[] = locations.split(":")[currentLocation].split(",");
		return locs[0].replace("(", "");
	}

	/**
	 * Get the mana that a wizard NPC has remaining
	 * 
	 * @return
	 */
	public int getMana() {
		return mana;
	}

	/**
	 * Set the mana of a wizard NPC
	 * 
	 * @param mana
	 */
	public void setMana(int mana) {
		this.mana = mana;
	}

	/**
	 * Get the mode of a wizard
	 * 
	 * @return
	 */
	public WizardMode getMode() {
		return mode;
	}

	/**
	 * Set the mode of a wizard
	 * 
	 * @param mode
	 */
	public void setMode(WizardMode mode) {
		this.mode = mode;
	}

	/**
	 * Get the time setting of a wizard
	 * 
	 * @return
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Set the time setting for a wizard
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	public CreatureType getMob() {
		return mob;
	}

	public void setMob(CreatureType mob) {
		this.mob = mob;
	}

	@Override
	public void toggle() {
		npc.setWizard(!npc.isWizard());
	}

	@Override
	public boolean getToggle() {
		return npc.isWizard();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "wizard";
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
		if (Permission.canUse(player, npc, getType())) {
			if (player.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
				WizardMode mode = npc.getWizard().getMode();
				switch (mode) {
				case TELEPORT:
					if (npc.getWizard().getNumberOfLocations() > 0) {
						npc.getWizard().cycleLocation();
						player.sendMessage(ChatColor.GREEN
								+ "Location set to "
								+ StringUtils.wrap(npc.getWizard()
										.getCurrentLocationName()));
					}
					break;
				case SPAWN:
				case TIME:
				default:
					player.sendMessage(ChatColor.RED
							+ "No valid mode selected.");
					break;
				}
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (Permission.canUse(player, npc, getType())) {
			if (player.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
				WizardMode mode = npc.getWizard().getMode();
				switch (mode) {
				case TELEPORT:
					if (npc.getWizard().getNumberOfLocations() > 0) {
						WizardManager.buy(player, npc,
								Operation.WIZARD_TELEPORT);
					}
					break;
				case SPAWN:
				case TIME:
				case WEATHER:
					WizardManager
							.buy(player, npc, Operation.WIZARD_TOGGLESTORM);
					break;
				default:
					player.sendMessage(ChatColor.RED
							+ "No valid mode selected.");
					break;
				}
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}
}