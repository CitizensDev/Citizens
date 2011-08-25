package net.citizensnpcs.questers.rewards;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.questers.Reward;
import net.citizensnpcs.questers.RewardBuilder;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
		if (reward == null || reward.getAmount() == 0
				|| reward.getType() == Material.AIR)
			return;
		if (this.take) {
			InventoryUtils.removeItems(player, reward);
		} else {
			player.getWorld().dropItemNaturally(player.getLocation(), reward);
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return take ? InventoryUtils.getRemainder(player, reward) <= 0 : true;
	}

	@Override
	public String getRequiredText(Player player) {
		int remainder = InventoryUtils.getRemainder(player, reward);
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

	public static class ItemRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			int id = storage.getInt(root + ".id");
			int amount = storage.getInt(root + ".amount");
			short data = 0;
			if (storage.keyExists(root + ".data"))
				data = (short) storage.getInt(root + ".data");
			return new ItemReward(new ItemStack(id, amount, data), take);
		}
	}
}