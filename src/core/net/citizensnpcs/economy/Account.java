package net.citizensnpcs.economy;

public interface Account {
	void add(double amount);

	double balance();

	boolean hasEnough(double amount);

	void set(double balance);

	void subtract(double amount);
}
