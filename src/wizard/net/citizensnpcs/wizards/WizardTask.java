package net.citizensnpcs.wizards;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.Settings;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;

public class WizardTask implements Runnable {
	private final HumanNPC npc;
	private int taskID;
	public WizardTask(HumanNPC npc) {
		this.npc = npc;
	}

	public void addID(int taskID) {
		this.taskID = taskID;
		tasks.put(npc.getUID(), this);
	}

	public void cancel() {
		Bukkit.getServer().getScheduler()
				.cancelTask(getTask(npc.getUID()).taskID);
		tasks.remove(npc.getUID());
	}

	@Override
	public void run() {
		Wizard wizard = npc.getType("wizard");
		if (wizard.getMana() + 1 < Settings.getInt("WizardMaxMana")) {
			wizard.setMana(wizard.getMana() + 1);
		}
	}

	private static final Map<Integer, WizardTask> tasks = new HashMap<Integer, WizardTask>();

	public static void cancelTasks() {
		for (WizardTask entry : tasks.values()) {
			Bukkit.getServer().getScheduler().cancelTask(entry.taskID);
		}
		tasks.clear();
	}

	public static WizardTask getTask(int UID) {
		return tasks.get(UID);
	}
}