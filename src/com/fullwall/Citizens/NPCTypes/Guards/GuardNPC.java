package com.fullwall.Citizens.NPCTypes.Guards;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class GuardNPC implements Toggleable {
	private HumanNPC npc;
	private boolean isBodyguard = true;
	private boolean isBouncer = false;
	private String guardType = "";

	/**
	 * Guard NPC object
	 * 
	 * @param npc
	 */
	public GuardNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Get whether or not a guard NPC is a bodyguard
	 * 
	 * @return
	 */
	public boolean isBodyguard() {
		return isBodyguard;
	}

	/**
	 * Set whether or not a guard NPC is a bodyguard
	 * 
	 * @param state
	 */
	public void setBodyguard(boolean state) {
		this.isBodyguard = state;
	}

	/**
	 * Get whether or not a guard NPC is a bouncer
	 * 
	 * @return
	 */
	public boolean isBouncer() {
		return isBouncer;
	}

	/**
	 * Set whether or not a guard NPC is a bouncer
	 * 
	 * @param state
	 */
	public void setBouncer(boolean state) {
		this.isBouncer = state;
	}

	/**
	 * Get the type of guard that a guard NPC is
	 * 
	 * @return
	 */
	public String getGuardType() {
		if (isBodyguard()) {
			guardType = "bodyguard";
		} else if (isBouncer()) {
			guardType = "bouncer";
		} else {
			guardType = "null";
		}
		return guardType;
	}

	/**
	 * Set the type of a guard that a guard NPC is
	 * 
	 * @param guardType
	 */
	public void setGuardType(String guardType) {
		this.guardType = guardType;
	}

	@Override
	public void toggle() {
		npc.setGuard(!npc.isGuard());
	}

	@Override
	public boolean getToggle() {
		return npc.isGuard();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "guard";
	}

	@Override
	public void saveState() {
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}
}