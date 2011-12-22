package net.citizensnpcs.properties.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.DataSource;
import net.citizensnpcs.utils.Messaging;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DatabaseStorage implements DataSource {
	private final ConnectionInfo info;
	private Connection connection;

	public DatabaseStorage(String driver, ConnectionInfo info)
			throws ClassNotFoundException, SQLException {
		this.info = info;
		if (!loadedDrivers.contains(driver)) {
			Class.forName(driver);
			loadedDrivers.add(driver);
		}
		this.connect();
	}

	private void ensureConnection() throws SQLException {
		if (this.connection.getClass().getName().startsWith("org.sqlite"))
			return;

		if (!this.connection.isValid(0)) {
			Messaging.log("Lost connection with database, reopening...");
			this.connect();
		}
	}

	private void connect() throws SQLException {
		this.connection = DriverManager.getConnection(info.url, info.username,
				info.password);
	}

	public boolean tableExists(String table) {
		if (aliases.containsKey(table))
			table = aliases.get(table);
		try {
			this.ensureConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs = meta.getTables(null, null, table, null);
			return rs.next();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean columnExists(String table, String column) {
		if (aliases.containsKey(table))
			table = aliases.get(table);
		try {
			this.ensureConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs = meta.getColumns(null, null, table, column);
			return rs.next();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void performSQL(String sql) throws SQLException {
		this.connection.prepareStatement(sql).execute();
	}

	@Override
	public DataKey getKey(String root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load() {
	}

	@Override
	public void save() {
		try {
			this.ensureConnection();
			this.connection.commit();
		} catch (SQLException ex) {
			Messaging.log("Unable to save database, error: " + ex.getMessage());
		}
	}

	public static void registerTableAlias(String alias, String table) {
		if (alias == null || table == null)
			throw new IllegalArgumentException("arguments can't be null");
		aliases.put(alias, table);
	}

	private static final Set<String> loadedDrivers = Sets.newHashSet();
	private static final Map<String, String> aliases = Maps.newHashMap();
}
