package com.fullwall.Citizens.NPCTypes.Questers;

import com.fullwall.Citizens.PropertyHandler;

public class StoredProfile {
	public PropertyHandler profile;

	public StoredProfile(String name) {
		String directory = "plugins/Citizens/Profiles/";
		profile = new PropertyHandler(directory + name + ".profile");
	}
}
