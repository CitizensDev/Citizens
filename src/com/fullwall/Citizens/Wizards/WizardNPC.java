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
	private String locations;
	private int currentLocation = 0;
	private int nrOfLocations = 1;
	private Location currentLoc;
	
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


	public void addLocation(Location location, String locName) {
		String addedLoc = "";
		addedLoc = "(" + locName + "," + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch() + "):";
		locations = locations + addedLoc;
		nrOfLocations = locations.split(":").length;
	}
	
	public void setLocations(String locationsinc) {
		locations = locationsinc;
		nrOfLocations = locations.split(":").length;
	}

	public void nextLocation() {
		currentLocation++;
		if(currentLocation >= nrOfLocations) currentLocation = 0;
	}
	
	public int getNrOfLocations(){
		return nrOfLocations;
	}

	public Location getCurrentLocation() {
		String locs[]= locations.split(":")[currentLocation].split(",");
		this.currentLoc = new Location(Bukkit.getServer().getWorld(locs[1]),
				Double.parseDouble(locs[2]),
				Double.parseDouble(locs[3]),
				Double.parseDouble(locs[4]), Float.parseFloat(locs[5]),
				Float.parseFloat(locs[6].replace(")", "")));
		return this.currentLoc;
	}
	
	public String getCurrentLocationName() {
		String locs[]= locations.split(":")[currentLocation].split(",");
		return locs[0].replace("(", "");
	}
}