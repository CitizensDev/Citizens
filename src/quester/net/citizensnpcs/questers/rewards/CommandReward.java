package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.DataKey;
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
	public void save(DataKey root) {
		root.setString("command", command);
		root.setBoolean("server", isServerCommand);
	}

	public static class CommandRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(DataKey root, boolean take) {
			return new CommandReward(root.getString("command"),
					root.getBoolean("server"));
		}
	}
}
