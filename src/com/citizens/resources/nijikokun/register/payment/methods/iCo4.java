package com.citizens.Resources.nijikokun.register.payment.methods;

import org.bukkit.plugin.Plugin;

import com.citizens.Resources.nijikokun.register.payment.Method;
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

public class iCo4 implements Method {
	private iConomy iCoPlugin;

	@Override
	public iConomy getPlugin() {
		return this.iCoPlugin;
	}

	@Override
	public String getName() {
		return "iConomy";
	}

	@Override
	public String getVersion() {
		return "4";
	}

	@Override
	public String format(double amount) {
		return iConomy.getBank().format(amount);
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
		return iConomy.getBank().hasAccount(name);
	}

	@Override
	public boolean hasBankAccount(String bank, String name) {
		return false;
	}

	@Override
	public MethodAccount getAccount(String name) {
		return new iCoAccount(iConomy.getBank().getAccount(name));
	}

	@Override
	public MethodBankAccount getBankAccount(String bank, String name) {
		return null;
	}

	@Override
	public boolean isCompatible(Plugin plugin) {
		return plugin.getDescription().getName().equalsIgnoreCase("iconomy")
				&& !plugin.getClass().getName().equals("com.iConomy.iConomy")
				&& plugin instanceof iConomy;
	}

	@Override
	public void setPlugin(Plugin plugin) {
		iCoPlugin = (iConomy) plugin;
	}

	public class iCoAccount implements MethodAccount {
		private final Account account;

		public iCoAccount(Account account) {
			this.account = account;
		}

		public Account getiCoAccount() {
			return account;
		}

		@Override
		public double balance() {
			return this.account.getBalance();
		}

		@Override
		public boolean set(double amount) {
			if (this.account == null)
				return false;
			this.account.setBalance(amount);
			return true;
		}

		@Override
		public boolean add(double amount) {
			if (this.account == null)
				return false;
			this.account.add(amount);
			return true;
		}

		@Override
		public boolean subtract(double amount) {
			if (this.account == null)
				return false;
			this.account.subtract(amount);
			return true;
		}

		@Override
		public boolean multiply(double amount) {
			if (this.account == null)
				return false;
			this.account.multiply(amount);
			return true;
		}

		@Override
		public boolean divide(double amount) {
			if (this.account == null)
				return false;
			this.account.divide(amount);
			return true;
		}

		@Override
		public boolean hasEnough(double amount) {
			return this.account.hasEnough(amount);
		}

		@Override
		public boolean hasOver(double amount) {
			return this.account.hasOver(amount);
		}

		@Override
		public boolean hasUnder(double amount) {
			return (this.balance() < amount);
		}

		@Override
		public boolean isNegative() {
			return this.account.isNegative();
		}

		@Override
		public boolean remove() {
			if (this.account == null)
				return false;
			this.account.remove();
			return true;
		}
	}
}