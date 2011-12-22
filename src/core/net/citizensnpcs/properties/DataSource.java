package net.citizensnpcs.properties;

public interface DataSource {
	public void load();

	public void save();

	public DataKey getKey(String root);
}
