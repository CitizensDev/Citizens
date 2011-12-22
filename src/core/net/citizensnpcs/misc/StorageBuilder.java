package net.citizensnpcs.misc;

import net.citizensnpcs.properties.DataKey;

public interface StorageBuilder<T> {
	T create(DataKey root);
}
