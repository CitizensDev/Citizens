package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class NPCReward implements Reward {
	private final String name;
	private final String[] toggles;
	private final Location at;

	public NPCReward(String name, String[] toggles, Location at) {
		this.name = name;
		this.at = at;
		for (String type : toggles) {
			if (!NPCTypeManager.validType(type))
				throw new IllegalArgumentException(
						"Invalid toggle type specified in NPC reward: " + type);
		}
		this.toggles = toggles;
	}

	@Override
	public void grant(Player player, int UID) {
		HumanNPC spawned = NPCManager
				.register(name, at != null ? at : player.getLocation(),
						NPCCreateReason.COMMAND);
		spawned.getNPCData().setOwner(player.getName());
		for (String type : toggles) {
			spawned.addType(type);
		}
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public void save(DataKey root) {
		root = root.getRelative("npc");
		root.setString("name", name);
		root.setString("types", Joiner.on(",").join(toggles));
	}

	public static class NPCRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(DataKey root, boolean take) {
			if (root.keyExists("npcid"))
				return new NPCGiveReward(root.getInt("npcid"));
			return new NPCReward(root.getString("name"), root
					.getString("types").split(","), LocationUtils.loadLocation(
					root, false));
		}
	}

	private static class NPCGiveReward implements Reward {
		private final int UID;

		private NPCGiveReward(int UID) {
			this.UID = UID;
		}

		@Override
		public void grant(Player player, int UID) {
			NPCManager.get(UID).getNPCData().setOwner(player.getName());
		}

		@Override
		public boolean isTake() {
			return false;
		}

		@Override
		public void save(DataKey root) {
			root.setInt("npcid", UID);
		}
	}
}
