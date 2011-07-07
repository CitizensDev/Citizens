package com.citizens.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import com.citizens.Constants;
import com.citizens.interfaces.Storage;
import com.citizens.utils.Messaging;

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
	private final Properties properties;
	private final String fileName;

	public PropertyHandler(String fileName, boolean create) {
		this.fileName = fileName;
		this.properties = new Properties();
		File file = new File(fileName);
		if (file.exists()) {
			load();
		} else if (create) {
			createFile(file);
		}
	}

	public PropertyHandler(String fileName) {
		this(fileName, true);
	}

	private void createFile(File file) {
		try {
			Messaging.log("Creating missing file at " + fileName + ".");
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Messaging.log("Unable to create " + file.getPath() + ".",
					Level.SEVERE);
		}
	}

	@Override
	public void load() {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(this.fileName);
			this.properties.load(stream);
		} catch (IOException ex) {
			Messaging.log("Unable to load " + this.fileName, Level.SEVERE);
		} finally {
			try {
				if (stream != null) {
					Messaging.debug("Load stream closed.");
					stream.close();
				}
			} catch (IOException e) {
				Messaging.log("Unable to close " + this.fileName, Level.SEVERE);
			}
		}
	}

	@Override
	public void save() {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(this.fileName);
			this.properties.store(stream, "Citizens File");
		} catch (IOException ex) {
			Messaging.log("Unable to save " + this.fileName, Level.SEVERE);
		} finally {
			try {
				if (stream != null) {
					Messaging.debug("Save stream closed.");
					stream.close();
				}
			} catch (IOException e) {
				Messaging.log("Unable to close " + this.fileName, Level.SEVERE);
			}
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
		if (Constants.saveOften) {
			save();
		}
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
		} else {
			setString(key, value);
		}
		return value;
	}

	@Override
	public String getString(int key, String value) {
		return getString("" + key, value);
	}

	@Override
	public void setString(String key, String value) {
		this.properties.setProperty(key, value);
		if (Constants.saveOften) {
			save();
		}
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
		if (this.properties.containsKey(key)) {
			return Integer.parseInt(this.properties.getProperty(key));
		} else {
			setInt(key, value);
		}
		return value;
	}

	@Override
	public int getInt(int key, int value) {
		return getInt("" + key, value);
	}

	@Override
	public void setInt(String key, int value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
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
		} else {
			setDouble(key, value);
		}
		return value;
	}

	@Override
	public double getDouble(int key, double value) {
		return getDouble("" + key, value);
	}

	@Override
	public void setDouble(String key, double value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
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
		} else {
			setLong(key, value);
		}
		return value;
	}

	@Override
	public long getLong(int key, long value) {
		return getLong("" + key, value);
	}

	@Override
	public void setLong(String key, long value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setLong(int key, long value) {
		setLong("" + key, value);
	}

	@Override
	public boolean getBoolean(String key) {
		return this.properties.containsKey(key)
				&& Boolean.parseBoolean(this.properties.getProperty(key));
	}

	@Override
	public boolean getBoolean(int key) {
		return getBoolean("" + key);
	}

	@Override
	public boolean getBoolean(String key, boolean value) {
		if (this.properties.containsKey(key)) {
			return Boolean.parseBoolean(this.properties.getProperty(key));
		} else {
			setBoolean(key, value);
		}
		return value;
	}

	@Override
	public boolean getBoolean(int key, boolean value) {
		return getBoolean("" + key, value);
	}

	@Override
	public void setBoolean(String key, boolean value) {
		this.properties.setProperty(key, String.valueOf(value));
		if (Constants.saveOften) {
			save();
		}
	}

	@Override
	public void setBoolean(int key, boolean value) {
		setBoolean("" + key, value);
	}

	public void clear() {
		this.properties.clear();
	}
}