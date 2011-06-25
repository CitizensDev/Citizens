package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.NPCTypes.Wizards.WizardManager.WizardMode;
import com.fullwall.Citizens.Utils.InventoryUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardNPC extends Toggleable implements Clickable {
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
		super(npc);
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
	public void cycle(HumanNPC npc, WizardMode mode) {
		WizardNPC wizard = npc.getToggleable("wizard");
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
	public String getType() {
		return "wizard";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		if (Permission.canUse(player, npc, getType())) {
			if (player.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
				WizardNPC wizard = npc.getToggleable("wizard");
				WizardMode mode = wizard.getMode();
				String msg = ChatColor.GREEN + "";
				switch (mode) {
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
							+ StringUtils.wrap(wizard.getMob().name()
									.toLowerCase().replace("_", " "));
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
		if (Permission.canUse(player, npc, getType())) {
			WizardNPC wizard = npc.getToggleable("wizard");
			if (player.getItemInHand().getTypeId() == Constants.wizardInteractItem) {
				WizardMode mode = wizard.getMode();
				switch (mode) {
				case TELEPORT:
					if (wizard.getNumberOfLocations() > 0) {
						WizardManager.buy(player, npc,
								Operation.WIZARD_TELEPORT);
					}
					break;
				case SPAWN:
					WizardManager.buy(player, npc, Operation.WIZARD_SPAWNMOB);
					break;
				case TIME:
					WizardManager.buy(player, npc, Operation.WIZARD_CHANGETIME);
					break;
				case WEATHER:
					WizardManager
							.buy(player, npc, Operation.WIZARD_TOGGLESTORM);
					break;
				default:
					player.sendMessage(ChatColor.RED
							+ "No valid mode selected.");
					break;
				}
			} else if (player.getItemInHand().getTypeId() == Constants.wizardManaRegenItem) {
				String msg = StringUtils.wrap(npc.getStrippedName() + "'s");
				int mana = 0;
				if (wizard.getMana() + 10 < Constants.maxWizardMana) {
					mana = wizard.getMana() + 10;
					msg += " mana has been increased to "
							+ StringUtils.wrap(mana) + ".";
				} else if (wizard.getMana() + 10 == Constants.maxWizardMana) {
					mana = Constants.maxWizardMana;
					msg += " mana has been fully replenished.";
				} else {
					msg += " mana cannot be regenerated with that item any further.";
					return;
				}
				InventoryUtils.decreaseItemInHand(player,
						Material.getMaterial(Constants.wizardManaRegenItem));
				player.sendMessage(msg);
				wizard.setMana(mana);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}
}