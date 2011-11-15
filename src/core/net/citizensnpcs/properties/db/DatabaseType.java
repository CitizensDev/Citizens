package net.citizensnpcs.properties.db;

import java.util.Map;

import com.google.common.collect.Maps;

public enum DatabaseType {
	MYSQL("com.mysql.jdbc.Driver", "mysql"),
	SQLITE("", "sqlite");
	private final String driver;
	private final String[] aliases;

	DatabaseType(String driver, String... aliases) {
		this.driver = driver;
		this.aliases = aliases;
	}

	public String getDriver() {
		return driver;
	}

	public static DatabaseType fromName(String alias) {
		return lookup.get(alias.toLowerCase());
	}

	private static final Map<String, DatabaseType> lookup = Maps.newHashMap();
	static {
		for (DatabaseType type : values()) {
			if (type.aliases == null)
				continue;
			for (String alias : type.aliases)
				lookup.put(alias, type);
		}
	}
}
