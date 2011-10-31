package net.citizensnpcs.misc;

import net.citizensnpcs.properties.Storage;

public interface StorageBuilder<T> {
	T create(Storage storage, String root);
}
