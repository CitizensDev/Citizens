package net.citizensnpcs.wizards;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;

public class WizardTask implements Runnable {
	private final HumanNPC npc;
	private int taskID;
	private static final Map<Integer, WizardTask> tasks = new HashMap<Integer, WizardTask>();

	public WizardTask(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		Wizard wizard = npc.getType("wizard");
		if (wizard.getMana() + 1 < SettingsManager.getInt("WizardMaxMana")) {
			wizard.setMana(wizard.getMana() + 1);
		}
	}

	public void addID(int taskID) {
		this.taskID = taskID;
		tasks.put(npc.getUID(), this);
	}

	public static void cancelTasks() {
		for (WizardTask entry : tasks.values()) {
			Bukkit.getServer().getScheduler().cancelTask(entry.taskID);
		}
		tasks.clear();
	}

	public static WizardTask getTask(int UID) {
		return tasks.get(UID);
	}

	public void cancel() {
		Bukkit.getServer().getScheduler()
				.cancelTask(getTask(npc.getUID()).taskID);
		tasks.remove(npc.getUID());
	}
}