package com.fullwall.Citizens.Commands.CommandExecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.fullwall.Citizens.Citizens;

public class QuestsExecutor implements CommandExecutor {
	private final Citizens plugin;

	public QuestsExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		return false;
	}
}
