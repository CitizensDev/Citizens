package net.citizensnpcs.properties;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import net.citizensnpcs.Settings;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ConfigurationHandler implements Storage {
	private final FileConfiguration config;
	private final File file;

	public ConfigurationHandler(String fileName) {
		this.file = new File(fileName);
		this.config = new YamlConfiguration();
		if (!file.exists()) {
			create();
			save();
		} else {
			load();
		}
	}

	@Override
	public void load() {
		try {
			this.config.load(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			this.config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void create() {
		try {
			Messaging
					.log("Creating new config file at " + file.getName() + ".");
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Messaging.log("Unable to create " + file.getPath() + ".",
					Level.SEVERE);
		}
	}

	@Override
	public void removeKey(String path) {
		this.config.set(path, null);
		if (Settings.getBoolean("SaveOften")) {
			save();
		}
	}

	public boolean pathExists(String path) {
		return this.config.get(path) != null;
	}

	public boolean pathExists(int path) {
		return pathExists("" + path);
	}

	@Override
	public String getString(String path) {
		if (pathExists(path)) {
			return this.config.get(path).toString();
		}
		return "";
	}

	@Override
	public String getString(String path, String value) {
		if (pathExists(path)) {
			return this.config.getString(path);
		} else {
			setString(path, value);
		}
		return value;
	}

	@Override
	public void setString(String path, String value) {
		this.config.set(path, value);
		if (Settings.getBoolean("SaveOften")) {
			save();
		}
	}

	@Override
	public int getInt(String path) {
		if (pathExists(path)) {
			if (config.getString(path) == null)
				return config.getInt(path);
			return Integer.parseInt(this.config.getString(path));
		}
		return 0;
	}

	@Override
	public int getInt(String path, int value) {
		return this.config.getInt(path, value);
	}

	@Override
	public void setInt(String path, int value) {
		this.config.set(path, value);
		if (Settings.getBoolean("SaveOften")) {
			save();
		}
	}

	@Override
	public double getDouble(String path) {
		if (pathExists(path)) {
			if (config.getString(path) == null) {
				if (config.get(path) instanceof Integer)
					return config.getInt(path);
				return config.getDouble(path);
			}
			return Double.parseDouble(this.config.getString(path));
		}
		return 0;
	}

	@Override
	public double getDouble(String path, double value) {
		return this.config.getDouble(path, value);
	}

	@Override
	public void setDouble(String path, double value) {
		this.config.set(path, String.valueOf(value));
		if (Settings.getBoolean("SaveOften")) {
			save();
		}
	}

	@Override
	public long getLong(String path) {
		if (pathExists(path)) {
			if (config.getString(path) == null) {
				if (config.get(path) instanceof Integer)
					return config.getInt(path);
				return config.getLong(path);
			}
			return Long.parseLong(this.config.getString(path));
		}
		return 0;
	}

	@Override
	public long getLong(String path, long value) {
		return this.config.getInt(path, (int) value);
	}

	@Override
	public void setLong(String path, long value) {
		this.config.set(path, value);
		if (Settings.getBoolean("SaveOften")) {
			save();
		}
	}

	@Override
	public boolean getBoolean(String path) {
		if (pathExists(path)) {
			if (config.getString(path) == null)
				return config.getBoolean(path);
			return Boolean.parseBoolean(this.config.getString(path));
		}
		return false;
	}

	@Override
	public boolean getBoolean(String path, boolean value) {
		return this.config.getBoolean(path, value);
	}

	@Override
	public void setBoolean(String path, boolean value) {
		this.config.set(path, value);
		if (Settings.getBoolean("SaveOften")) {
			save();
		}
	}

	@Override
	public Set<String> getKeys(String path) {
		if (path == null || path.isEmpty())
			return this.config.getRoot().getKeys(false);
		if (config.getConfigurationSection(path) == null) {
			return Sets.newHashSet();
		}
		return this.config.getConfigurationSection(path).getKeys(false);
	}

	@Override
	public Object getRaw(String path) {
		return config.get(path);
	}

	@Override
	public void setRaw(String path, Object value) {
		config.set(path, value);
	}

	@Override
	public boolean keyExists(String path) {
		return pathExists(path);
	}

	@Override
	public List<Integer> getIntegerKeys(String string) {
		if (config.getConfigurationSection(string) == null) {
			return Lists.newArrayList();
		}
		Set<String> keys = config.getConfigurationSection(string)
				.getKeys(false);
		List<Integer> parsed = Lists.newArrayList();
		for (String key : keys) {
			if (!StringUtils.isNumber(key)) {
				continue;
			}
			parsed.add(Integer.parseInt(key));
		}
		return parsed;
	}
}