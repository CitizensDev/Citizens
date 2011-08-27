package net.citizensnpcs.wizards;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.SettingsManager;

import org.bukkit.Bukkit;

public class WizardTask implements Runnable {
	private Wizard wizard;
	private int taskID = 0;
	private static final List<Integer> tasks = new ArrayList<Integer>();

	public WizardTask(Wizard wizard) {
		this.wizard = wizard;
	}

	@Override
	public void run() {
		if (wizard.getMana() + 1 < SettingsManager.getInt("WizardMaxMana")) {
			wizard.setMana(wizard.getMana() + 1);
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