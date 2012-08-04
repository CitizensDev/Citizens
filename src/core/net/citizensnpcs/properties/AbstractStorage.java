package net.citizensnpcs.properties;

public abstract class AbstractStorage implements Storage {
	@Override
	public boolean getBoolean(String key, boolean value) {
		if (keyExists(key))
			return getBoolean(key);
		setBoolean(key, value);
		return value;
	}

	@Override
	public double getDouble(String key, double value) {
		if (keyExists(key))
			return getDouble(key);
		setDouble(key, value);
		return value;
	}

	@Override
	public int getInt(String key, int value) {
		if (keyExists(key))
			return getInt(key);
		setInt(key, value);
		return value;
	}

	@Override
	public long getLong(String key, long value) {
		if (keyExists(key))
			return getLong(key);
		setLong(key, value);
		return value;
	}

	@Override
	public String getString(String key, String value) {
		if (keyExists(key))
			return getString(key);
		setString(key, value);
		return value;
	}
}
