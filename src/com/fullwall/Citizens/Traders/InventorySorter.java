package com.fullwall.Citizens.Traders;

import org.bukkit.inventory.ItemStack;

public class InventorySorter {
	/**
	 * Sorts an inventory. Used for trader npcs.
	 * 
	 * @param stack
	 * @return
	 */
	public static ItemStack[] sortItemStack(ItemStack[] stack) {
		return sortItemStack(stack, 0, stack.length);
	}

	public static ItemStack[] sortItemStack(ItemStack[] stack, int start,
			int end) {
		stack = stackItems(stack, start, end);
		recQuickSort(stack, start, end - 1);
		return stack;
	}

	private static ItemStack[] stackItems(ItemStack[] items, int start, int end) {
		for (int i = start; i < end; i++) {
			ItemStack item = items[i];
			// Avoid infinite stacks and stacks with durability
			if (item == null || item.getAmount() <= 0
					|| ItemType.shouldNotStack(item.getTypeId())) {
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
							|| ItemType.shouldNotStack(item.getTypeId())) {
						continue;
					}
					// Same type?
					// Blocks store their color in the damage value
					if (item2.getTypeId() == item.getTypeId()
							&& (!ItemType.usesDamageValue(item.getTypeId()) || item
									.getDurability() == item2.getDurability())) {
						// This stack won't fit in the parent stack
						if (item2.getAmount() > needed) {
							item.setAmount(64);
							item2.setAmount(item2.getAmount() - needed);
							break;
							// This stack will
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

	private static void swap(ItemStack[] list, int first, int second) {
		ItemStack temp;

		temp = list[first];
		list[first] = list[second];
		list[second] = temp;
	}

	private static class ComparableIS {
		private ItemStack item;

		public ComparableIS(ItemStack item) {
			this.item = item;
		}

		public int compareTo(ItemStack check) {
			if (check == null)
				return 0;
			if (item == null)
				return 0;
			// Type ID first
			if (item.getTypeId() > check.getTypeId()) {
				return 1;
			} else if (item.getTypeId() < check.getTypeId()) {
				return -1;
			} else if (item.getTypeId() == check.getTypeId()) {
				// Wool, dye, slabs, and logs next
				if (ItemType.usesDamageValue(item.getTypeId())) {
					if (item.getDurability() < check.getDurability()) {
						return 1;
					} else if (item.getDurability() > check.getDurability()) {
						return -1;
					}
				}
				// Stack size
				if (item.getAmount() < check.getAmount()) {
					return -1;
				} else if (item.getAmount() > check.getAmount()) {
					return 1;
				}
			}
			return 0;
		}
	}

	private static int partition(ItemStack[] list, int first, int last) {
		ItemStack pivot;
		int smallIndex;
		swap(list, first, (first + last) / 2);
		pivot = list[first];
		smallIndex = first;
		for (int index = first + 1; index <= last; index++) {
			ComparableIS compElem = new ComparableIS(list[index]);
			if (compElem.compareTo(pivot) < 0) {
				smallIndex++;
				swap(list, smallIndex, index);
			}
		}
		swap(list, first, smallIndex);
		return smallIndex;

	}

	private static void recQuickSort(ItemStack[] list, int first, int last) {
		if (first < last) {
			int pivotLocation = partition(list, first, last);
			recQuickSort(list, first, pivotLocation - 1);
			recQuickSort(list, pivotLocation + 1, last);
		}
	}

}
