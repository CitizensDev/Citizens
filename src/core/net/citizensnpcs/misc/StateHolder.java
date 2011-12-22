package net.citizensnpcs.misc;

import net.citizensnpcs.properties.DataKey;

public interface StateHolder {
	void load(DataKey root);

	void save(DataKey root);
}
