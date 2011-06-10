package com.fullwall.Citizens.NPCTypes.Questers;

import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Properties.ConfigurationHandler;

public class StoredProfile {
	public Storage profile;

	public StoredProfile(String name) {
		String directory = "plugins/Citizens/Profiles/";
		profile = new ConfigurationHandler(directory + name + ".profile");
	}

	public void save() {
		this.profile.save();
	}
}