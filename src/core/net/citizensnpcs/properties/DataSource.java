package net.citizensnpcs.properties;

public interface DataSource {
	public DataKey getKey(String root);

	public void load();

	public void save();
}
