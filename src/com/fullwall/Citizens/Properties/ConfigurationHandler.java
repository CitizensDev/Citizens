package com.fullwall.Citizens.Properties;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Defaults;
import com.fullwall.Citizens.Interfaces.Storage;
import com.fullwall.Citizens.Utils.Messaging;

public class ConfigurationHandler implements Storage {
	private final Configuration config;
	private final String fileName;

	public ConfigurationHandler(String fileName) {
		this.fileName = fileName;
		File file = getFile();
		this.config = new Configuration(file);
		if (!file.exists()) {
			create();
			save();
		} else {
			load();
		}
		if (fileName.contains("citizens.yml")) {
			loadRenames(Defaults.settingsRenames);
			loadDefaults(Defaults.settingsDefaults);
			loadDeletes(Defaults.settingsDeletes);
		} else if (fileName.contains("economy.yml")) {
			loadRenames(Defaults.economyRenames);
			loadDefaults(Defaults.economyDefaults);
			loadDeletes(Defaults.economyDeletes);
		} else if (fileName.contains("mobs.yml")) {
			loadRenames(Defaults.mobRenames);
			loadDefaults(Defaults.mobDefaults);
			loadDeletes(Defaults.mobDeletes);
		}
		save();
	}

	private void loadDeletes(List<String> nodes) {
		boolean found = false;
		for (String node : nodes) {
			Messaging.log("Deleting outdated setting " + node + ".");
			removeKey(node);
			if (!found) {
				found = true;
			}
		}
		if (found) {
			save();
		}
	}

	private void loadDefaults(Map<String, String> nodes) {
		boolean found = false;
		for (Entry<String, String> node : nodes.entrySet()) {
			if (!pathExists(node.getKey())) {
				Messaging.log("Writing default setting " + node.getKey() + ".");
				setString(node.getKey(), node.getValue());
				if (!found) {
					found = true;
				}
			}
		}
		if (found) {
			save();
		}
	}

	private void loadRenames(Map<String, String> nodes) {
		boolean found = false;
		for (Entry<String, String> node : nodes.entrySet()) {
			if (pathExists(node.getKey())) {
				String key = node.getValue();
				String value = getString(node.getKey());
				Messaging.log("Renaming setting " + node.getKey() + ".");
				removeKey(node.getKey());
				setString(key, value);
				if (!found) {
					found = true;
				}
			}
		}
		if (found) {
			save();
		}
	}

	@Override
	public void load() {
		config.load();
	}

	@Override
	public void save() {
		this.config.save();
	}

	private void create() {
		File file = getFile();
		try {
			Messaging.log("Creating new config file at " + fileName + ".");
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Messaging.log("Unable to create " + file.getPath() + ".",
					Level.SEVERE);
		}
	}

	private File getFile() {
		return new File(this.fileName);
	}

	@Override
	public void removeKey(String path) {
		this.config.removeProperty(path);
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void removeKey(int path) {
		removeKey("" + path);
	}

	public boolean pathExists(String path) {
		return this.config.getProperty(path) != null;
	}

	public boolean pathExists(int path) {
		return pathExists("" + path);
	}

	@Override
	public String getString(String path) {
		if (pathExists(path)) {
			return this.config.getString(path);
		}
		return "";
	}

	@Override
	public String getString(int path) {
		return getString("" + path);
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
	public String getString(int path, String value) {
		return getString("" + path, value);
	}

	@Override
	public void setString(String path, String value) {
		this.config.setProperty(path, value);
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setString(int path, String value) {
		setString("" + path, value);
	}

	@Override
	public int getInt(String path) {
		if (pathExists(path)) {
			return Integer.parseInt(this.config.getString(path));
		}
		return 0;
	}

	@Override
	public int getInt(int path) {
		return getInt("" + path);
	}

	@Override
	public int getInt(String path, int value) {
		return this.config.getInt(path, value);
	}

	@Override
	public int getInt(int path, int value) {
		return getInt("" + path, value);
	}

	@Override
	public void setInt(String path, int value) {
		this.config.setProperty(path, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setInt(int path, int value) {
		setInt("" + path, value);
	}

	@Override
	public double getDouble(String path) {
		if (pathExists(path)) {
			return Double.parseDouble(this.config.getString(path));
		}
		return 0;
	}

	@Override
	public double getDouble(int path) {
		return getDouble("" + path);
	}

	@Override
	public double getDouble(String path, double value) {
		return this.config.getDouble(path, value);
	}

	@Override
	public double getDouble(int path, double value) {
		return getDouble("" + path, value);
	}

	@Override
	public void setDouble(String path, double value) {
		this.config.setProperty(path, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setDouble(int path, double value) {
		setDouble("" + path, value);
	}

	@Override
	public long getLong(String path) {
		if (pathExists(path)) {
			return Long.parseLong(this.config.getString(path));
		}
		return 0;
	}

	@Override
	public long getLong(int path) {
		return getLong("" + path);
	}

	@Override
	public long getLong(String path, long value) {
		return this.config.getInt(path, (int) value);
	}

	@Override
	public long getLong(int path, long value) {
		return getLong("" + path, value);
	}

	@Override
	public void setLong(String path, long value) {
		this.config.setProperty(path, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setLong(int path, long value) {
		setLong("" + path, value);
	}

	@Override
	public boolean getBoolean(String path) {
        return pathExists(path) && Boolean.parseBoolean(this.config.getString(path));
    }

	@Override
	public boolean getBoolean(int path) {
		return getBoolean("" + path);
	}

	@Override
	public boolean getBoolean(String path, boolean value) {
		return this.config.getBoolean(path, value);
	}

	@Override
	public boolean getBoolean(int path, boolean value) {
		return getBoolean("" + path, value);
	}

	@Override
	public void setBoolean(String path, boolean value) {
		this.config.setProperty(path, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setBoolean(int path, boolean value) {
		setBoolean("" + path, value);
	}

	public List<String> getKeys(String path) {
		return this.config.getKeys(path);
	}

	public ConfigurationNode getNode(String path) {
		return this.config.getNode(path);
	}
}