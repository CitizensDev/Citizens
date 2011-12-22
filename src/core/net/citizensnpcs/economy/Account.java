package net.citizensnpcs.economy;

public interface Account {
	void add(double amount);

	void subtract(double amount);

	void set(double balance);

	boolean hasEnough(double amount);

	double balance();
}
