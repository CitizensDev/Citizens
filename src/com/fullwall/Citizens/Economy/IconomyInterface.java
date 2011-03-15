package com.fullwall.Citizens.Economy;

import com.fullwall.Citizens.Citizens;
import com.nijiko.coelho.iConomy.iConomy;

public class IconomyInterface {

	private Citizens plugin;

	public IconomyInterface(Citizens plugin) {
		this.plugin = plugin;
	}

	public double getBalance(String name) {
		if (iConomy.getBank().hasAccount(name))
			return iConomy.getBank().getAccount(name).getBalance();
		else
			return -1;
	}
}
