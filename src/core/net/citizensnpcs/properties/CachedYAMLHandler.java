package net.citizensnpcs.properties;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class CachedYAMLHandler implements DataSource {
	private final SettingsTree tree = new SettingsTree();

	private final FileConfiguration config;

	private final File file;

	public CachedYAMLHandler(String fileName) {
		this.file = new File(fileName);
		this.config = new YamlConfiguration();
		if (!file.exists()) {
			create();
			save();
		} else {
			load();
		}
	}

	private void clear() {
		for (String path : config.getKeys(true)) {
			config.set(path, null);
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

	private String get(String path) {
		return this.tree.get(path);
	}

	@Override
	public CachedYAMLKey getKey(String root) {
		return new CachedYAMLKey(root);
	}

	@Override
	public void load() {
		clear();
		try {
			config.load(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (String entry : this.config.getKeys(true)) {
			if (!(config.get(entry) instanceof MemorySection))
				tree.set(entry, config.get(entry).toString());
		}
		clear();
	}

	@Override
	public void save() {
		clear();
		for (Entry<String, String> entry : tree.getTree().entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isEmpty()
					&& !StringUtils.isNumber(entry.getKey())) {
				this.config.set(entry.getKey(), entry.getValue());
			}
		}
		try {
			this.config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clear();
	}

	public class CachedYAMLKey extends AbstractDataKey {
		private final String current;

		private CachedYAMLKey(String root) {
			this.current = root;
		}

		@Override
		public void copy(String to) {
			tree.copy(current, to);
		}

		@Override
		public boolean getBoolean(String keyExt) {
			return valueExists(keyExt)
					&& Boolean.parseBoolean(get(getKeyExt(keyExt)));
		}

		@Override
		public double getDouble(String keyExt) {
			if (valueExists(keyExt)) {
				return Double.parseDouble(get(getKeyExt(keyExt)));
			}
			return 0;
		}

		@Override
		public int getInt(String keyExt) {
			if (valueExists(keyExt)) {
				return Integer.parseInt(get(getKeyExt(keyExt)));
			}
			return 0;
		}

		@Override
		public List<DataKey> getIntegerSubKeys() {
			List<Integer> subPaths = Lists.newArrayList();
			for (String key : getKeys()) {
				if (StringUtils.isNumber(key))
					subPaths.add(Integer.parseInt(key));
			}
			Collections.sort(subPaths);
			List<DataKey> res = Lists.newArrayList();
			for (int key : subPaths) {
				res.add(getRelative(Integer.toString(key)));
			}
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

		private Set<String> getKeys() {
			String path = current;
			if (path == null || path.isEmpty())
				path = "";
			else
				path += ".";
			Set<String> keys = Sets.newHashSet();
			for (String key : tree.getTree().keySet()) {
				if (key.startsWith(path) && key.length() > path.length()) {
					key = key.replace(path, "");
					int index = key.contains(".") ? key.indexOf('.') : key
							.length();
					key = key.substring(0, index);
					if (!keys.contains(key))
						keys.add(key);
				}
			}
			return keys;
		}

		@Override
		public long getLong(String keyExt) {
			if (valueExists(keyExt)) {
				return Long.parseLong(get(getKeyExt(keyExt)));
			}
			return 0;
		}

		@Override
		public Object getRaw(String string) {
			return config.get(getKeyExt(string));
		}

		@Override
		public DataKey getRelative(String relative) {
			return new CachedYAMLKey(getKeyExt(relative));
		}

		@Override
		public String getString(String keyExt) {
			if (valueExists(keyExt)) {
				return get(getKeyExt(keyExt));
			}
			return "";
		}

		@Override
		public List<DataKey> getSubKeys() {
			List<DataKey> res = Lists.newArrayList();
			for (String key : getKeys()) {
				res.add(getRelative(key));
			}
			return res;
		}

		@Override
		public boolean keyExists(String path) {
			return tree.get(getKeyExt(path)) != null;
		}

		@Override
		public String name() {
			int last = current.lastIndexOf('.');
			return current.substring(last == 0 ? 0 : last + 1);
		}

		@Override
		public void removeKey(String keyExt) {
			tree.remove(getKeyExt(keyExt));
		}

		@Override
		public void setBoolean(String keyExt, boolean value) {
			tree.set(getKeyExt(keyExt), Boolean.toString(value));
		}

		@Override
		public void setDouble(String keyExt, double value) {
			tree.set(getKeyExt(keyExt), Double.toString(value));
		}

		@Override
		public void setInt(String keyExt, int value) {
			tree.set(getKeyExt(keyExt), Integer.toString(value));
		}

		@Override
		public void setLong(String keyExt, long value) {
			tree.set(getKeyExt(keyExt), Long.toString(value));
		}

		@Override
		public void setRaw(String path, Object value) {
			config.set(getKeyExt(path), value);
		}

		@Override
		public void setString(String keyExt, String value) {
			tree.set(getKeyExt(keyExt), value);
		}

		@Override
		public boolean valueExists(String path) {
			return keyExists(path) && !tree.get(getKeyExt(path)).isEmpty();
		}
	}
}