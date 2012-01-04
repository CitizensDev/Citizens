package net.citizensnpcs.properties;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import net.citizensnpcs.Settings;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

public class ConfigurationHandler implements DataSource {
	private final FileConfiguration config;

	private final File file;

	public ConfigurationHandler(String fileName) {
		this.file = new File(fileName);
		config = new YamlConfiguration();
		if (!file.exists()) {
			create();
			save();
		} else {
			load();
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
	public DataKey getKey(String root) {
		return new ConfigKey(root);
	}

	@Override
	public void load() {
		try {
			config.load(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean pathExists(String path) {
		return config.get(path) != null;
	}

	@Override
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class ConfigKey extends AbstractDataKey {
		private final String current;

		private ConfigKey(String root) {
			this.current = root;
		}

		@Override
		public void copy(String to) {
			ConfigurationSection root = config.getConfigurationSection(current);
			if (root == null)
				return;
			config.createSection(to, root.getValues(true));
		}

		@Override
		public boolean getBoolean(String keyExt) {
			String path = getKeyExt(keyExt);
			if (pathExists(path)) {
				if (config.getString(path) == null)
					return config.getBoolean(path);
				return Boolean.parseBoolean(config.getString(path));
			}
			return false;
		}

		@Override
		public boolean getBoolean(String keyExt, boolean def) {
			return config.getBoolean(getKeyExt(keyExt), def);
		}

		@Override
		public double getDouble(String keyExt) {
			String path = getKeyExt(keyExt);
			if (pathExists(path)) {
				if (config.getString(path) == null) {
					if (config.get(path) instanceof Integer)
						return config.getInt(path);
					return config.getDouble(path);
				}
				return Double.parseDouble(config.getString(path));
			}
			return 0;
		}

		@Override
		public double getDouble(String keyExt, double def) {
			return config.getDouble(getKeyExt(keyExt), def);
		}

		@Override
		public int getInt(String keyExt) {
			String path = getKeyExt(keyExt);
			if (pathExists(path)) {
				if (config.getString(path) == null)
					return config.getInt(path);
				return Integer.parseInt(config.getString(path));
			}
			return 0;
		}

		@Override
		public int getInt(String path, int value) {
			return config.getInt(getKeyExt(path), value);
		}

		@Override
		public List<DataKey> getIntegerSubKeys() {
			List<DataKey> res = Lists.newArrayList();
			ConfigurationSection section = config
					.getConfigurationSection(current);
			if (section == null)
				return res;
			List<Integer> keys = Lists.newArrayList();
			for (String key : section.getKeys(false)) {
				if (StringUtils.isNumber(key))
					keys.add(Integer.parseInt(key));
			}
			Collections.sort(keys);
			for (int key : keys)
				res.add(getRelative(Integer.toString(key)));
			return res;
		}

		private String getKeyExt(String from) {
			if (from.isEmpty())
				return current;
			if (from.charAt(0) == '.')
				return current.isEmpty() ? from.substring(1, from.length())
						: current + from;
			return current.isEmpty() ? from : current + "." + from;
		}

		@Override
		public long getLong(String keyExt) {
			String path = getKeyExt(keyExt);
			if (pathExists(path)) {
				if (config.getString(path) == null) {
					if (config.get(path) instanceof Integer)
						return config.getInt(path);
					return config.getLong(path);
				}
				return Long.parseLong(config.getString(path));
			}
			return 0;
		}

		@Override
		public long getLong(String keyExt, long def) {
			return config.getLong(getKeyExt(keyExt), def);
		}

		@Override
		public Object getRaw(String keyExt) {
			return config.get(getKeyExt(keyExt));
		}

		@Override
		public DataKey getRelative(String relative) {
			return new ConfigKey(getKeyExt(relative));
		}

		@Override
		public String getString(String keyExt) {
			String path = getKeyExt(keyExt);
			if (pathExists(path)) {
				return config.get(path).toString();
			}
			return "";
		}

		@Override
		public List<DataKey> getSubKeys() {
			List<DataKey> res = Lists.newArrayList();
			ConfigurationSection section = config
					.getConfigurationSection(current);
			if (section == null)
				return res;
			for (String key : section.getKeys(false)) {
				res.add(getRelative(key));
			}
			return res;
		}

		@Override
		public boolean keyExists(String keyExt) {
			return config.get(getKeyExt(keyExt)) != null;
		}

		@Override
		public String name() {
			int last = current.lastIndexOf('.');
			return current.substring(last == 0 ? 0 : last + 1);
		}

		@Override
		public void removeKey(String keyExt) {
			config.set(getKeyExt(keyExt), null);
			trySave();
		}

		@Override
		public void setBoolean(String path, boolean value) {
			config.set(getKeyExt(path), value);
			trySave();
		}

		@Override
		public void setDouble(String path, double value) {
			config.set(getKeyExt(path), String.valueOf(value));
			trySave();
		}

		@Override
		public void setInt(String path, int value) {
			config.set(getKeyExt(path), value);
			trySave();
		}

		@Override
		public void setLong(String path, long value) {
			config.set(getKeyExt(path), value);
			trySave();
		}

		@Override
		public void setRaw(String path, Object value) {
			config.set(getKeyExt(path), value);
		}

		@Override
		public void setString(String path, String value) {
			config.set(getKeyExt(path), value);
			trySave();
		}

		private void trySave() {
			if (Settings.getBoolean("SaveOften"))
				save();
		}

		@Override
		boolean valueExists(String key) {
			return keyExists(key);
		}
	}
}