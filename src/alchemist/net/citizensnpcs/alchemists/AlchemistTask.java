package net.citizensnpcs.alchemists;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.resources.npclib.HumanNPC;

public class AlchemistTask implements Runnable {
	private HumanNPC npc;
	private int id;

	public AlchemistTask(HumanNPC npc) {
		this.npc = npc;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Alchemist alchemist = npc.getType("alchemist");
		ArrayList<ItemStack> required = new ArrayList<ItemStack>();
		for (String item : alchemist.getRecipe(alchemist.getCurrentRecipeID())
				.split(",")) {
			required.add(AlchemistManager.getStackByString(item));
		}
		ArrayList<ItemStack> npcInv = new ArrayList<ItemStack>();
		for (ItemStack i : npc.getInventory().getContents()) {
			npcInv.add(i);
		}
		if (npcInv.containsAll(required)) {
			npc.getInventory().addItem(
					new ItemStack(alchemist.getCurrentRecipeID()));
			npc.getPlayer().updateInventory();
			kill();
		}
		// make sure lists are clear for next iteration
		required.clear();
		npcInv.clear();
	}

	public void addID(int id) {
		this.id = id;
	}

	private void kill() {
		run();
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
}