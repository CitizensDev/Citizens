package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.utils.ServerUtils;

import org.bukkit.entity.Player;

public class CommandReward implements Reward {
	private final String command;
	private final boolean isServerCommand;

	CommandReward(String command, boolean isServerCommand) {
		this.command = command;
		this.isServerCommand = isServerCommand;
	}

	@Override
	public void grant(Player player, int UID) {
		String localCommand = command.replaceAll("<player>", player.getName())
				.replaceAll("<world>", player.getWorld().getName());
		if (isServerCommand) {
			ServerUtils.dispatchCommandWithEvent(localCommand);
		} else {
			player.performCommand(localCommand);
		}
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".command", command);
		storage.setBoolean(root + ".server", isServerCommand);
	}

	public static class CommandRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			return new CommandReward(storage.getString(root + ".command"),
					storage.getBoolean(root + ".server"));
		}
	}
}
