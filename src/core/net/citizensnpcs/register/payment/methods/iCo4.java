package net.citizensnpcs.register.payment.methods;

import net.citizensnpcs.register.payment.Method;

import org.bukkit.plugin.Plugin;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

/**
 * iConomy 4 Implementation of Method
 * 
 * @author Nijikokun <nijikokun@shortmail.com> (@nijikokun)
 * @copyright (c) 2011
 * @license AOL license <http://aol.nexua.org>
 */
public class iCo4 implements Method {
	private iConomy iConomy;

	@Override
	public String format(double amount) {
		return com.nijiko.coelho.iConomy.iConomy.getBank().format(amount);
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public MethodAccount getAccount(String name) {
		return new iCoAccount(com.nijiko.coelho.iConomy.iConomy.getBank()
				.getAccount(name));
	}

	@Override
	public MethodBankAccount getBankAccount(String bank, String name) {
		return null;
	}

	@Override
	public String getName() {
		return "iConomy";
	}

	@Override
	public iConomy getPlugin() {
		return this.iConomy;
	}

	@Override
	public String getVersion() {
		return "4";
	}

	@Override
	public boolean hasAccount(String name) {
		return com.nijiko.coelho.iConomy.iConomy.getBank().hasAccount(name);
	}

	@Override
	public boolean hasBank(String bank) {
		return false;
	}

	@Override
	public boolean hasBankAccount(String bank, String name) {
		return false;
	}

	@Override
	public boolean hasBanks() {
		return false;
	}

	@Override
	public boolean isCompatible(Plugin plugin) {
		return plugin.getDescription().getName().equalsIgnoreCase("iconomy")
				&& plugin.getClass().getName()
						.equals("com.nijiko.coelho.iConomy.iConomy")
				&& plugin instanceof iConomy;
	}

	@Override
	public void setPlugin(Plugin plugin) {
		iConomy = (iConomy) plugin;
	}

	public class iCoAccount implements MethodAccount {
		private final Account account;

		public iCoAccount(Account account) {
			this.account = account;
		}

		@Override
		public boolean add(double amount) {
			if (this.account == null)
				return false;
			this.account.add(amount);
			return true;
		}

		@Override
		public double balance() {
			return this.account.getBalance();
		}

		@Override
		public boolean divide(double amount) {
			if (this.account == null)
				return false;
			this.account.divide(amount);
			return true;
		}

		public Account getiCoAccount() {
			return account;
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
		public boolean multiply(double amount) {
			if (this.account == null)
				return false;
			this.account.multiply(amount);
			return true;
		}

		@Override
		public boolean remove() {
			if (this.account == null)
				return false;
			this.account.remove();
			return true;
		}

		@Override
		public boolean set(double amount) {
			if (this.account == null)
				return false;
			this.account.setBalance(amount);
			return true;
		}

		@Override
		public boolean subtract(double amount) {
			if (this.account == null)
				return false;
			this.account.subtract(amount);
			return true;
		}
	}
}
