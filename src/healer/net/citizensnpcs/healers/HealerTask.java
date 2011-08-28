package net.citizensnpcs.healers;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;

public class HealerTask implements Runnable {
	private final HumanNPC npc;
	private int taskID;
	private static final Map<Integer, HealerTask> tasks = new HashMap<Integer, HealerTask>();

	public HealerTask(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		Healer healer = npc.getType("healer");
		if (healer.getHealth() < healer.getMaxHealth()) {
			healer.setHealth(healer.getHealth() + 1);
		}
	}

	public void addID(int taskID) {
		this.taskID = taskID;
		tasks.put(npc.getUID(), this);
	}

	public static void cancelTasks() {
		for (HealerTask entry : tasks.values()) {
			Bukkit.getServer().getScheduler().cancelTask(entry.taskID);
		}
	}

	public static HealerTask getTask(int UID) {
		return tasks.get(UID);
	}

	public void cancel() {
		Bukkit.getServer().getScheduler()
				.cancelTask(getTask(npc.getUID()).taskID);
		tasks.remove(npc.getUID());
	}
}