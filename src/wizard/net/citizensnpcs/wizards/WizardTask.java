package net.citizensnpcs.wizards;

import net.citizensnpcs.Settings;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.utils.ByIdArray;

import org.bukkit.Bukkit;

public class WizardTask implements Runnable {
	private final HumanNPC npc;
	private int taskID;

	public WizardTask(HumanNPC npc) {
		this.npc = npc;
	}

	public void addID(int taskID) {
		this.taskID = taskID;
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

	private static final ByIdArray<WizardTask> tasks = ByIdArray.create();

	public static void cancelTasks() {
		for (WizardTask entry : tasks) {
			Bukkit.getServer().getScheduler().cancelTask(entry.taskID);
		}
		tasks.clear();
	}

	public static WizardTask getTask(int UID) {
		return tasks.get(UID);
	}
}