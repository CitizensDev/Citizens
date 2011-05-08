package com.fullwall.Citizens.Interfaces;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public interface Saveable {

	public void saveFiles();

	public void saveState(HumanNPC npc);

	public void removeFromFiles(HumanNPC npc);

	public void setType(HumanNPC npc);

	public PropertyHandler getFile(String name);

	public boolean is(HumanNPC npc);

	public String getType();
}
