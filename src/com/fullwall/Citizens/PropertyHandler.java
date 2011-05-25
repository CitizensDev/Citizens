package com.fullwall.Citizens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import com.fullwall.Citizens.Interfaces.Storage;

/**
 * iConomy v1.x
 * Copyright (C) 2010  Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * PropertyHandler
 * 
 * Reads & Writes properties files.
 * 
 * @author Nijiko
 */
public final class PropertyHandler implements Storage {
	private Properties properties;
	private String fileName;

	public PropertyHandler(String fileName) {
		this.fileName = fileName;
		this.properties = new Properties();
		File file = new File(fileName);

		if (file.exists()) {
			load();
		} else {
			createFile(file);
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

	private void createFile(File file) {
		try {
			if (!fileName.contains("profile"))
				Citizens.log.info(fileName
						+ " not found! Creating empty file at " + fileName
						+ ".");
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Citizens.log.log(Level.SEVERE,
					"Unable to create " + file.getPath(), ex);
		}
	}

	private void loadDefaults(HashMap<String, String> nodes) {
		for (Entry<String, String> entry : nodes.entrySet()) {
			if (!keyExists(entry.getKey())) {
				Citizens.log.info("Missing setting " + entry.getKey() + " in "
						+ this.fileName + "! Writing value as "
						+ entry.getValue() + ".");
				setString(entry.getKey(), entry.getValue());
			}
		}
	}

	private void loadLookups(HashMap<String, String> nodes) {
		boolean told = false;
		for (Entry<String, String> entry : nodes.entrySet()) {
			if (!keyExists(entry.getKey())) {
				if (!told)
					Citizens.log
							.info("Missing entry in Citizens.itemlookups - restoring settings");
				told = true;
				setString(entry.getKey(), entry.getValue());
			}
		}
	}

	private void loadDeletes(ArrayList<String> nodes) {
		for (String entry : nodes) {
			if (keyExists(entry)) {
				Citizens.log.info("Deleting unused setting " + entry + ".");
				removeKey(entry);
			}
		}
	}

	private void loadRenames(HashMap<String, String> nodes) {
		try {
			for (Entry<String, String> entry : returnMap().entrySet()) {
				for (Entry<String, String> secondEntry : nodes.entrySet()) {
					if (entry.getKey().contains(secondEntry.getKey())) {
						String key = entry.getKey().replace(
								secondEntry.getKey(), secondEntry.getValue());
						String value = entry.getValue();
						Citizens.log.info("Renaming setting " + entry.getKey()
								+ " to " + key + ".");
						removeKey(entry.getKey());
						setString(key, value);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fullwall.Citizens.Storage#load()
	 */
	@Override
	public void load() {
		try {
			this.properties.load(new FileInputStream(this.fileName));
		} catch (IOException ex) {
			Citizens.log.log(Level.SEVERE, "Unable to load " + this.fileName,
					ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fullwall.Citizens.Storage#save()
	 */
	@Override
	public void save() {
		try {
			this.properties.store(new FileOutputStream(this.fileName),
					"Citizens File");
		} catch (IOException ex) {
			Citizens.log.log(Level.SEVERE, "Unable to save " + this.fileName,
					ex);
		}
	}

	public Map<String, String> returnMap() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(
				new FileReader(this.fileName));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().length() == 0) {
				continue;
			}
			if (line.charAt(0) == '#') {
				continue;
			}
			int delimPosition = line.indexOf('=');
			String key = line.substring(0, delimPosition).trim();
			String value = line.substring(delimPosition + 1).trim();
			map.put(key, value);
		}
		reader.close();
		return map;
	}

	@Override
	public void removeKey(String key) {
		this.properties.remove(key);
		if (Constants.saveOften)
			save();
	}

	@Override
	public void removeKey(int key) {
		removeKey("" + key);
	}

	public boolean keyExists(String key) {
		return this.properties.containsKey(key);
	}

	public boolean keyExists(int key) {
		return keyExists("" + key);
	}

	@Override
	public String getString(String key) {
		if (this.properties.containsKey(key)) {
			return this.properties.getProperty(key);
		}

		return "";
	}

	@Override
	public String getString(int key) {
		return getString("" + key);
	}

	@Override
	public String getString(String key, String value) {
		if (this.properties.containsKey(key)) {
			return this.properties.getProperty(key);
		} else
			setString(key, value);
		return value;
	}

	@Override
	public String getString(int key, String value) {
		return getString("" + key, value);
	}

	@Override
	public void setString(String key, String value) {
		this.properties.setProperty(key, value);
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setString(int key, String value) {
		setString("" + key, value);
	}

	@Override
	public int getInt(String key) {
		if (this.properties.containsKey(key)) {
			return Integer.parseInt(this.properties.getProperty(key));
		}

		return 0;
	}

	@Override
	public int getInt(int key) {
		return getInt("" + key);
	}

	@Override
	public int getInt(String key, int value) {
		if (this.properties.containsKey(key))
			return Integer.parseInt(this.properties.getProperty(key));
		else
			setInt(key, value);
		return value;
	}

	@Override
	public int getInt(int key, int value) {
		return getInt("" + key, value);
	}

	@Override
	public void setInt(String key, int value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setInt(int key, int value) {
		setInt("" + key, value);
	}

	@Override
	public double getDouble(String key) {
		if (this.properties.containsKey(key)) {
			return Double.parseDouble(this.properties.getProperty(key));
		}

		return 0;
	}

	@Override
	public double getDouble(int key) {
		return getDouble("" + key);
	}

	@Override
	public double getDouble(String key, double value) {
		if (this.properties.containsKey(key)) {
			return Double.parseDouble(this.properties.getProperty(key));
		} else
			setDouble(key, value);
		return value;
	}

	@Override
	public double getDouble(int key, double value) {
		return getDouble("" + key, value);
	}

	@Override
	public void setDouble(String key, double value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setDouble(int key, double value) {
		setDouble("" + key, value);
	}

	@Override
	public long getLong(String key) {
		if (this.properties.containsKey(key)) {
			return Long.parseLong(this.properties.getProperty(key));
		}

		return 0;
	}

	@Override
	public long getLong(int key) {
		return getLong("" + key);
	}

	@Override
	public long getLong(String key, long value) {
		if (this.properties.containsKey(key)) {
			return Long.parseLong(this.properties.getProperty(key));
		} else
			setLong(key, value);
		return value;
	}

	@Override
	public long getLong(int key, long value) {
		return getLong("" + key, value);
	}

	@Override
	public void setLong(String key, long value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setLong(int key, long value) {
		setLong("" + key, value);
	}

	@Override
	public boolean getBoolean(String key) {
		if (this.properties.containsKey(key)) {
			return Boolean.parseBoolean(this.properties.getProperty(key));
		}

		return false;
	}

	@Override
	public boolean getBoolean(int key) {
		return getBoolean("" + key);
	}

	@Override
	public boolean getBoolean(String key, boolean value) {
		if (this.properties.containsKey(key)) {
			return Boolean.parseBoolean(this.properties.getProperty(key));
		} else
			setBoolean(key, value);
		return value;
	}

	@Override
	public boolean getBoolean(int key, boolean value) {
		return getBoolean("" + key, value);
	}

	@Override
	public void setBoolean(String key, boolean value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften)
			save();
	}

	@Override
	public void setBoolean(int key, boolean value) {
		setBoolean("" + key, value);
	}

	public void clear() {
		this.properties.clear();
	}
}