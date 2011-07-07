package com.citizens.resources.register.methods;

import org.bukkit.plugin.Plugin;

import com.citizens.resources.register.Method;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class EE17 implements Method {
	private Essentials Essentials;

	@Override
	public Essentials getPlugin() {
		return this.Essentials;
	}

	@Override
	public String getName() {
		return "EssentialsEco";
	}

	@Override
	public String getVersion() {
		return "2.2";
	}

	@Override
	public String format(double amount) {
		return Economy.format(amount);
	}

	@Override
	public boolean hasBanks() {
		return false;
	}

	@Override
	public boolean hasBank(String bank) {
		return false;
	}

	@Override
	public boolean hasAccount(String name) {
		return Economy.playerExists(name);
	}

	@Override
	public boolean hasBankAccount(String bank, String name) {
		return false;
	}

	@Override
	public MethodAccount getAccount(String name) {
		if (!hasAccount(name))
			return null;
		return new EEcoAccount(name);
	}

	@Override
	public MethodBankAccount getBankAccount(String bank, String name) {
		return null;
	}

	@Override
	public boolean isCompatible(Plugin plugin) {
		try {
			Class.forName("com.earth2me.essentials.api.Economy");
		} catch (Exception e) {
			return false;
		}

		return plugin.getDescription().getName().equalsIgnoreCase("essentials")
				&& plugin instanceof Essentials;
	}

	@Override
	public void setPlugin(Plugin plugin) {
		Essentials = (Essentials) plugin;
	}

	public class EEcoAccount implements MethodAccount {
		private final String name;

		public EEcoAccount(String name) {
			this.name = name;
		}

		@Override
		public double balance() {
			Double balance = 0.0;

			try {
				balance = Economy.getMoney(this.name);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] Failed to grab balance in Essentials Economy: "
								+ ex.getMessage());
			}

			return balance;
		}

		@Override
		public boolean set(double amount) {
			try {
				Economy.setMoney(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
				return false;
			} catch (NoLoanPermittedException ex) {
				System.out
						.println("[REGISTER] No loan permitted in Essentials Economy: "
								+ ex.getMessage());
				return false;
			}

			return true;
		}

		@Override
		public boolean add(double amount) {
			try {
				Economy.add(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
				return false;
			} catch (NoLoanPermittedException ex) {
				System.out
						.println("[REGISTER] No loan permitted in Essentials Economy: "
								+ ex.getMessage());
				return false;
			}

			return true;
		}

		@Override
		public boolean subtract(double amount) {
			try {
				Economy.subtract(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
				return false;
			} catch (NoLoanPermittedException ex) {
				System.out
						.println("[REGISTER] No loan permitted in Essentials Economy: "
								+ ex.getMessage());
				return false;
			}

			return true;
		}

		@Override
		public boolean multiply(double amount) {
			try {
				Economy.multiply(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
				return false;
			} catch (NoLoanPermittedException ex) {
				System.out
						.println("[REGISTER] No loan permitted in Essentials Economy: "
								+ ex.getMessage());
				return false;
			}

			return true;
		}

		@Override
		public boolean divide(double amount) {
			try {
				Economy.divide(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
				return false;
			} catch (NoLoanPermittedException ex) {
				System.out
						.println("[REGISTER] No loan permitted in Essentials Economy: "
								+ ex.getMessage());
				return false;
			}

			return true;
		}

		@Override
		public boolean hasEnough(double amount) {
			try {
				return Economy.hasEnough(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
			}

			return false;
		}

		@Override
		public boolean hasOver(double amount) {
			try {
				return Economy.hasMore(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
			}

			return false;
		}

		@Override
		public boolean hasUnder(double amount) {
			try {
				return Economy.hasLess(name, amount);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
			}

			return false;
		}

		@Override
		public boolean isNegative() {
			try {
				return Economy.isNegative(name);
			} catch (UserDoesNotExistException ex) {
				System.out
						.println("[REGISTER] User does not exist in Essentials Economy: "
								+ ex.getMessage());
			}

			return false;
		}

		@Override
		public boolean remove() {
			return false;
		}
	}
}