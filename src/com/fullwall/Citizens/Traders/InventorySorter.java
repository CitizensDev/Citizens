package com.fullwall.Citizens.Traders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventorySorter {
	public static ItemStack[] sortInventory(ItemStack[] stack) {
		int end = stack.length - 1;
		int start = 0;
		stack = stackItems(stack, start, end);
		boolean doMore = true;
		int n = end;
		while (doMore) {
			n--;
			doMore = false; // assume this is our last pass over the array
			for (int i = start; i < n; i++) {
				if (stack[i].getTypeId() > stack[i + 1].getTypeId()) {
					// exchange elements
					ItemStack temp = stack[i];
					stack[i] = stack[i + 1];
					stack[i + 1] = temp;
					doMore = true; // after an exchange, must look again
				}
			}
		}
		return stack;
	}

	private static ItemStack[] stackItems(ItemStack[] items, int start, int end) {
		for (int i = start; i < end; i++) {
			ItemStack item = items[i];

			// Avoid infinite stacks and stacks with durability
			if (item == null || item.getAmount() <= 0
					|| item.getType().equals(Material.AIR)) {
				continue;
			}

			// Ignore buckets
			if (item.getTypeId() >= 325 && item.getTypeId() <= 327) {
				continue;
			}

			if (item.getAmount() < 64) {
				int needed = 64 - item.getAmount(); // Number of needed items
													// until 64

				// Find another stack of the same type
				for (int j = i + 1; j < end; j++) {
					ItemStack item2 = items[j];

					// Avoid infinite stacks and stacks with durability
					if (item2 == null || item2.getAmount() <= 0
							|| item2.getType() == Material.AIR) {
						continue;
					}

					// Same type?
					// Blocks store their color in the damage value
					if (item2.getTypeId() == item.getTypeId()
							&& (item.getData() == item2.getData())
							&& (item.getDurability() == item2.getDurability())) {
						// This stack won't fit in the parent stack
						if (item2.getAmount() > needed) {
							item.setAmount(64);
							item2.setAmount(item2.getAmount() - needed);
							break;
						} else {
							item.setAmount(item.getAmount() + item2.getAmount());
							needed = 64 - item.getAmount();
							items[j].setTypeId(0);
						}
					}
				}
			}
		}
		return items;
	}
}
