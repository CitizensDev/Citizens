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
	private final Material material;
	private final int amount;
	private final short durability;
	private final boolean take;

	public ItemReward(Material mat, int amount, short durability, boolean take) {
		this.material = mat;
		this.amount = amount;
		this.durability = durability;
		this.take = take;
	}

	@Override
	public void grant(Player player, HumanNPC npc) {
		if (amount == 0 || material == Material.AIR)
			return;
		if (this.take) {
			InventoryUtils.removeItems(player, material, amount);
		} else {
			int temp = this.amount, other = temp;
			while (temp > 0) {
				other = temp > material.getMaxStackSize() ? material
						.getMaxStackSize() : temp;
				player.getWorld().dropItemNaturally(player.getLocation(),
						new ItemStack(material, other, durability));
				temp -= other;
			}
		}
	}

	@Override
	public boolean isTake() {
		return take;
	}

	@Override
	public boolean canTake(Player player) {
		return take ? InventoryUtils.has(player, material, amount) : true;
	}

	@Override
	public String getRequiredText(Player player) {
		int remainder = InventoryUtils.getRemainder(player, material, amount);
		return ChatColor.GRAY
				+ "You need "
				+ StringUtils.wrap(remainder, ChatColor.GRAY)
				+ " more "
				+ StringUtils
						.pluralise(StringUtils.format(material), remainder)
				+ ".";
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setInt(root + ".id", material.getId());
		storage.setInt(root + ".amount", amount);
		storage.setInt(root + ".data", durability);
	}

	public static class ItemRewardBuilder implements RewardBuilder {
		@Override
		public Reward build(Storage storage, String root, boolean take) {
			int id = storage.getInt(root + ".id");
			int amount = storage.getInt(root + ".amount");
			short data = 0;
			if (storage.keyExists(root + ".data"))
				data = (short) storage.getInt(root + ".data");
			return new ItemReward(Material.getMaterial(id), amount, data, take);
		}
	}
}