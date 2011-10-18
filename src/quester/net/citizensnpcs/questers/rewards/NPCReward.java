package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.api.event.npc.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class NPCReward implements Reward {
	private final String name;
	private final String[] toggles;

	public NPCReward(String name, String[] toggles) {
		this.name = name;
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
				.get(NPCManager.register(name, player.getLocation(),
						player.getName(), NPCCreateReason.COMMAND));
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

	public static class NPCRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			root += ".npc";
			return new NPCReward(storage.getString(root + ".name"), storage
					.getString(root + ".types").split(","));
		}
	}
}
