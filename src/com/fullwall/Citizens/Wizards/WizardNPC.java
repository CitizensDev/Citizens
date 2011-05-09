package com.fullwall.Citizens.Wizards;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Utils.WizardPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Wizard-NPC object
 */
public class WizardNPC implements Toggleable {
	private HumanNPC npc;
	private String locations = "";
	private int currentLocation = 0;
	private int nrOfLocations = 0;
	private Location currentLoc;
	
	/**
	 * NPC Wizard object
	 * 
	 * @param npc
	 */
	public WizardNPC(HumanNPC npc){
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
	
	/**
	 * Returns the main location string.
	 * @return
	 */
	public String getLocations() {
		return locations;
	}

	@Override
	public void saveState() {
		WizardPropertyPool.saveState(npc);
	}

	@Override
	public void registerState() {
		WizardPropertyPool.saveWizard(npc.getUID(), true);
	}

	/**
	 * Addds a location to the main location sting.
	 * 
	 * @param location
	 * @param locName
	 */
	public void addLocation(Location location, String locName) {
		String addedLoc = "";
		addedLoc = "(" + locName + "," + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch() + "):";
		locations = locations + addedLoc;
		nrOfLocations = locations.split(":").length;
		if(locations.split(":")[0].isEmpty()){
			nrOfLocations = 0;
		}
		WizardPropertyPool.saveLocations(npc.getUID(), locations);
	}
	
	/**
	 * Sets all the locations of the wizard (one big combined string)
	 * @param locationsinc
	 */
	public void setLocations(String locationsinc) {
		locations = locationsinc;
		nrOfLocations = locations.split(":").length;
		if(locations.split(":")[0].isEmpty()){
			nrOfLocations = 0;
		}
		WizardPropertyPool.saveLocations(npc.getUID(), locations);
	}

	/**
	 * Sets the next location in the list as active.
	 */
	public void nextLocation() {
		currentLocation++;
		if(currentLocation >= nrOfLocations) currentLocation = 0;
	}
	
	/**
	 * Returns the total amount of locations bound to the wizard.
	 * 
	 * @return
	 */
	public int getNrOfLocations(){
		return nrOfLocations;
	}

	/**
	 * Return the current active teleport location for the wizard.
	 * 
	 * @return
	 */
	public Location getCurrentLocation() {
		String locs[]= locations.split(":")[currentLocation].split(",");
		this.currentLoc = new Location(Bukkit.getServer().getWorld(locs[1]),
				Double.parseDouble(locs[2]),
				Double.parseDouble(locs[3]),
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
		String locs[]= locations.split(":")[currentLocation].split(",");
		return locs[0].replace("(", "");
	}
}