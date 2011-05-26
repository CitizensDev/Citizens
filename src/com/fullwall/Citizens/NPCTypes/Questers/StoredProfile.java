package com.fullwall.Citizens.NPCTypes.Questers;

import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Properties.PropertyHandler;

public class StoredProfile {
	public Storage profile;

	public StoredProfile(String name) {
		String directory = "plugins/Citizens/Profiles/";
		profile = new PropertyHandler(directory + name + ".profile");
	}
}