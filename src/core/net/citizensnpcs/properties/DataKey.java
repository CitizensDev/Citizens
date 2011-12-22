package net.citizensnpcs.properties;

import java.util.List;

public interface DataKey {
	void copy(String to);

	boolean getBoolean(String keyExt);

	boolean getBoolean(String keyExt, boolean value);

	double getDouble(String keyExt);

	double getDouble(String keyExt, double value);

	int getInt(String keyExt);

	int getInt(String keyExt, int value);

	List<DataKey> getIntegerSubKeys();

	Iterable<DataKey> getSubKeys();

	long getLong(String keyExt);

	long getLong(String keyExt, long value);

	Object getRaw(String keyExt);

	DataKey getRelative(String relative);

	String getString(String keyExt);

	String getString(String keyExt, String value);

	boolean keyExists(String keyExt);

	void removeKey(String keyExt);

	void setBoolean(String keyExt, boolean value);

	void setDouble(String keyExt, double value);

	void setInt(String keyExt, int value);

	void setLong(String keyExt, long value);

	void setRaw(String path, Object value);

	void setString(String keyExt, String value);

	String name();
}
