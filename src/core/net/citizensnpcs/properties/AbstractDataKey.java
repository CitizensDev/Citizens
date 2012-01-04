package net.citizensnpcs.properties;

public abstract class AbstractDataKey implements DataKey {
	@Override
	public boolean getBoolean(String key, boolean value) {
		if (valueExists(key))
			return getBoolean(key);
		setBoolean(key, value);
		return value;
	}

	@Override
	public double getDouble(String key, double value) {
		if (valueExists(key))
			return getDouble(key);
		setDouble(key, value);
		return value;
	}

	@Override
	public int getInt(String key, int value) {
		if (valueExists(key))
			return getInt(key);
		setInt(key, value);
		return value;
	}

	@Override
	public long getLong(String key, long value) {
		if (valueExists(key))
			return getLong(key);
		setLong(key, value);
		return value;
	}

	@Override
	public String getString(String key, String value) {
		if (valueExists(key))
			return getString(key);
		setString(key, value);
		return value;
	}

	abstract boolean valueExists(String key);
}
