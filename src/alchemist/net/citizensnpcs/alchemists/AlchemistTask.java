package net.citizensnpcs.alchemists;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.citizensnpcs.SettingsManager;
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
		PlayerInventory npcInv = npc.getInventory();
		for (ItemStack i : required) {
			if (!npcInv.contains(i)) {
				return;
			}
		}
		// clear all ingredients from the inventory
		for (ItemStack toRemove : required) {
			npcInv.remove(toRemove);
		}
		// make sure list is clear for next iteration
		required.clear();
		// add the resulting item into the inventory
		int result;
		if (new Random().nextInt(100) <= SettingsManager
				.getInt("AlchemistFailedCraftChance")) {
			result = alchemist.getCurrentRecipeID();
		} else {
			result = SettingsManager.getInt("AlchemistFailedCraftItem");
		}
		npcInv.setItem(npcInv.first(result), new ItemStack(result, 1));
		npc.getPlayer().updateInventory();
		kill();
	}

	public void addID(int id) {
		this.id = id;
	}

	private void kill() {
		run();
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
}