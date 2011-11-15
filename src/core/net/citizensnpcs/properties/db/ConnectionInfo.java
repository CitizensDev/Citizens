package net.citizensnpcs.properties.db;

public class ConnectionInfo {
	public final String url;
	public final String username;
	public final String password;

	public ConnectionInfo(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
}
