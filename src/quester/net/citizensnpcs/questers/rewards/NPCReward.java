package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class NPCReward implements Reward {
	private final Location at;
	private final String name;
	private final String[] toggles;

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
		HumanNPC spawned = NPCManager.get(NPCManager.register(name,
				at != null ? at : player.getLocation(), player.getName(),
				NPCCreateReason.COMMAND));
		for (String type : toggles) {
			spawned.addType(type);
		}
	}

	@Override
	public boolean isTake() {
		return false;
	}

	@Override
	public void save(Storage storage, String root) {
		root += ".npc";
		storage.setString(root + ".name", name);
		storage.setString(root + ".types", Joiner.on(",").join(toggles));
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
		public void save(Storage storage, String root) {
			storage.setInt(root + ".npcid", UID);
		}
	}

	public static class NPCRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			if (storage.keyExists(root + ".npcid"))
				return new NPCGiveReward(storage.getInt(root + ".npcid"));
			return new NPCReward(storage.getString(root + ".name"), storage
					.getString(root + ".types").split(","),
					LocationUtils.loadLocation(storage, root, false));
		}
	}
}
