package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemReward implements Reward {
	private final ItemStack reward;
	private final boolean take;

	public ItemReward(ItemStack reward, boolean take) {
		this.reward = reward;
		this.take = take;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		if (this.take) {
			removeItems(player);
		} else {
			player.getWorld().dropItemNaturally(player.getLocation(), reward);
		}
	}

	private void removeItems(Player player) {
		int remaining = reward.getAmount();
		ItemStack[] contents = player.getInventory().getContents();
		for (int i = 0; i != contents.length; ++i) {
			ItemStack item = contents[i];
			if (item.getType() == reward.getType()) {
				if (remaining - item.getAmount() < 0) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
					item = null;
				}
				if (item.getAmount() == 0)
					item = null;
				contents[i] = item;
				if (remaining <= 0)
					break;
			}
		}
		player.getInventory().setContents(contents);
	}

	private int getRemainder(Player player) {
		int remaining = reward.getAmount();
		for (ItemStack item : player.getInventory().getContents()) {
			if (item.getType() == reward.getType()) {
				if (remaining - item.getAmount() < 0) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
				}
				if (remaining <= 0)
					break;
			}
		}
		return remaining;
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return take ? getRemainder(player) <= 0 : true;
	}

	@Override
	public String getRequiredText(Player player) {
		int remainder = getRemainder(player);
		return ChatColor.GRAY
				+ "You need "
				+ StringUtils.wrap(remainder, ChatColor.GRAY)
				+ " more "
				+ StringUtils.pluralise(StringUtils.format(reward.getType()),
						remainder) + ".";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setInt(root + ".id", reward.getTypeId());
		storage.setInt(root + ".amount", reward.getAmount());
		storage.setInt(root + ".data", reward.getDurability());
	}
}