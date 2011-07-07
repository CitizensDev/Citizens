package com.citizens.resources.register.methods;

import org.bukkit.plugin.Plugin;

import com.citizens.resources.register.Method;
import com.iConomy.iConomy;
import com.iConomy.system.Account;
import com.iConomy.system.BankAccount;
import com.iConomy.system.Holdings;
import com.iConomy.util.Constants;

public class iCo5 implements Method {
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
		return "5";
	}

	@Override
	public String format(double amount) {
		return iConomy.format(amount);
	}

	@Override
	public boolean hasBanks() {
		return Constants.Banking;
	}

	@Override
	public boolean hasBank(String bank) {
		return (hasBanks()) && iConomy.Banks.exists(bank);
	}

	@Override
	public boolean hasAccount(String name) {
		return iConomy.hasAccount(name);
	}

	@Override
	public boolean hasBankAccount(String bank, String name) {
		return (hasBank(bank)) && iConomy.getBank(bank).hasAccount(name);
	}

	@Override
	public MethodAccount getAccount(String name) {
		return new iCoAccount(iConomy.getAccount(name));
	}

	@Override
	public MethodBankAccount getBankAccount(String bank, String name) {
		return new iCoBankAccount(iConomy.getBank(bank).getAccount(name));
	}

	@Override
	public boolean isCompatible(Plugin plugin) {
		return plugin.getDescription().getName().equalsIgnoreCase("iconomy")
				&& plugin.getClass().getName().equals("com.iConomy.iConomy")
				&& plugin instanceof iConomy;
	}

	@Override
	public void setPlugin(Plugin plugin) {
		iCoPlugin = (iConomy) plugin;
	}

	public class iCoAccount implements MethodAccount {
		private final Account account;
		private final Holdings holdings;

		public iCoAccount(Account account) {
			this.account = account;
			this.holdings = account.getHoldings();
		}

		public Account getiCoAccount() {
			return account;
		}

		@Override
		public double balance() {
			return this.holdings.balance();
		}

		@Override
		public boolean set(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.set(amount);
			return true;
		}

		@Override
		public boolean add(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.add(amount);
			return true;
		}

		@Override
		public boolean subtract(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.subtract(amount);
			return true;
		}

		@Override
		public boolean multiply(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.multiply(amount);
			return true;
		}

		@Override
		public boolean divide(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.divide(amount);
			return true;
		}

		@Override
		public boolean hasEnough(double amount) {
			return this.holdings.hasEnough(amount);
		}

		@Override
		public boolean hasOver(double amount) {
			return this.holdings.hasOver(amount);
		}

		@Override
		public boolean hasUnder(double amount) {
			return this.holdings.hasUnder(amount);
		}

		@Override
		public boolean isNegative() {
			return this.holdings.isNegative();
		}

		@Override
		public boolean remove() {
			if (this.account == null)
				return false;
			this.account.remove();
			return true;
		}
	}

	public class iCoBankAccount implements MethodBankAccount {
		private final BankAccount account;
		private final Holdings holdings;

		public iCoBankAccount(BankAccount account) {
			this.account = account;
			this.holdings = account.getHoldings();
		}

		public BankAccount getiCoBankAccount() {
			return account;
		}

		@Override
		public String getBankName() {
			return this.account.getBankName();
		}

		@Override
		public int getBankId() {
			return this.account.getBankId();
		}

		@Override
		public double balance() {
			return this.holdings.balance();
		}

		@Override
		public boolean set(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.set(amount);
			return true;
		}

		@Override
		public boolean add(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.add(amount);
			return true;
		}

		@Override
		public boolean subtract(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.subtract(amount);
			return true;
		}

		@Override
		public boolean multiply(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.multiply(amount);
			return true;
		}

		@Override
		public boolean divide(double amount) {
			if (this.holdings == null)
				return false;
			this.holdings.divide(amount);
			return true;
		}

		@Override
		public boolean hasEnough(double amount) {
			return this.holdings.hasEnough(amount);
		}

		@Override
		public boolean hasOver(double amount) {
			return this.holdings.hasOver(amount);
		}

		@Override
		public boolean hasUnder(double amount) {
			return this.holdings.hasUnder(amount);
		}

		@Override
		public boolean isNegative() {
			return this.holdings.isNegative();
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