package com.fullwall.Citizens.NPCTypes.Wizards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler;
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
	private int mana;

	/**
	 * Wizard NPC object
	 * 
	 * @param npc
	 */
	public WizardNPC(HumanNPC npc) {
		this.npc = npc;
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
	 * Purchase a teleport
	 * 
	 * @param player
	 * @param wizard
	 * @param op
	 */
	public void buyTeleport(Player player, WizardNPC wizard, Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				double paid = EconomyHandler.pay(op, player);
				if (paid > 0) {
					player.sendMessage(ChatColor.GREEN
							+ "Paid "
							+ StringUtils.wrap(EconomyHandler.getPaymentType(
									op, "" + paid, ChatColor.YELLOW))
							+ " for a teleport to "
							+ StringUtils.wrap(wizard.getCurrentLocationName())
							+ ".");
					player.teleport(wizard.getCurrentLocation());
				}
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
					+ StringUtils.wrap(wizard.getCurrentLocationName()) + ".");
		}
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		// TODO Auto-generated method stub
		
	}
}