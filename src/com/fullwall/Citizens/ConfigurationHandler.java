package com.fullwall.Citizens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;

import com.fullwall.Citizens.Interfaces.Storage;

public class ConfigurationHandler implements Storage {
	private Configuration config;
	private String fileName;

	public ConfigurationHandler(String fileName) {
		this.fileName = fileName;

		File file = getFile();

		if (file.exists()) {
			load();
		} else {
			create();
			save();
		}
		if (fileName.contains("Citizens.settings")) {
			loadRenames(Defaults.settingsRenames);
			loadDefaults(Defaults.settingsDefaults);
			loadDeletes(Defaults.settingsDeletes);
		} else if (fileName.contains("Citizens.economy")) {
			loadRenames(Defaults.economyRenames);
			loadDefaults(Defaults.economyDefaults);
			loadDeletes(Defaults.economyDeletes);
		} else if (fileName.contains("Citizens.itemlookups")) {
			loadLookups(Defaults.lookupsDefaults);
		}
	}

	private void loadLookups(HashMap<String, String> nodes) {
		boolean told = false;
		for (Entry<String, String> entry : nodes.entrySet()) {
			if (!pathExists(entry.getKey())) {
				if (!told) {
					Citizens.log
							.info("[Citizens]: Missing entry in Citizens.itemlookups - validating file.");
					told = true;
				}
				setString(entry.getKey(), entry.getValue());
			}
		}
	}

	private void loadDeletes(ArrayList<String> nodes) {
		for (String node : nodes) {
			Citizens.log.info("[Citizens]: Deleting outdated setting " + node
					+ ".");
			removeKey(node);
		}
	}

	private void loadDefaults(HashMap<String, String> nodes) {
		for (Entry<String, String> entry : nodes.entrySet()) {
			if (!pathExists(entry.getKey())) {
				Citizens.log.info("[Citizens]: Writing missing setting "
						+ entry.getKey() + " as " + entry.getValue() + ".");
				setString(entry.getKey(), entry.getValue());
			}
		}
	}

	private void loadRenames(HashMap<String, String> nodes) {
		for (Entry<String, String> entry : nodes.entrySet()) {
			if (pathExists(entry.getKey())) {
				String key = entry.getValue();
				String value = getString(entry.getKey());
				Citizens.log.info("[Citizens]: Renaming setting "
						+ entry.getKey() + " to " + key + ".");
				removeKey(entry.getKey());
				setString(key, value);
			}
		}
	}

	public void load() {
		File file = getFile();
		this.config = new Configuration(file);
	}

	public void save() {
		this.config.save();
	}

	private void create() {
		File file = getFile();
		try {
			Citizens.log.info("[Citizens]: Creating new config file at "
					+ fileName + ".");
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Citizens.log.log(Level.SEVERE, "[Citizens]: Unable to create "
					+ file.getPath(), ex);
		}
	}

	private File getFile() {
		return new File(this.fileName);
	}

	@Override
	public void removeKey(String path) {
		this.config.removeProperty(path);
		if (Constants.saveOften)
			save();
	}

	@Override
	public void removeKey(int path) {
		removeKey("" + path);
	}

	public boolean pathExists(String path) {
		return this.config.getString(path) != null;
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
		} else
			setString(path, value);
		return value;
	}

	@Override
	public String getString(int path, String value) {
		return getString("" + path, value);
	}

	@Override
	public void setString(String path, String value) {
		this.config.setProperty(path, value);
		if (Constants.saveOften)
			save();
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
		if (Constants.saveOften)
			save();
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
		if (Constants.saveOften)
			save();
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
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setLong(int path, long value) {
		setLong("" + path, value);
	}

	@Override
	public boolean getBoolean(String path) {
		if (pathExists(path)) {
			return Boolean.parseBoolean(this.config.getString(path));
		}

		return false;
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
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setBoolean(int path, boolean value) {
		setBoolean("" + path, value);
	}
}