package net.citizensnpcs.healers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

public class HealerTask implements Runnable {
	private Healer healer;
	private int taskID;
	private static final List<Integer> tasks = new ArrayList<Integer>();

	public HealerTask(Healer healer) {
		this.healer = healer;
	}

	@Override
	public void run() {
		if (healer.getHealth() < healer.getMaxHealth()) {
			healer.setHealth(healer.getHealth() + 1);
		}
	}

	public void addID(int id) {
		this.taskID = id;
		tasks.add(taskID);
	}

	public static void cancelTasks() {
		for (int task : tasks) {
			Bukkit.getServer().getScheduler().cancelTask(task);
		}
	}
	
	public boolean isActiveTask() {
		return taskID != 0;
	}

	public void cancel() {
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}
}