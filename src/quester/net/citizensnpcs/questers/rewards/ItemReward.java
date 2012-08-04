package net.citizensnpcs.questers.rewards;

import java.util.ArrayList;
import java.util.Collection;

import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemReward implements Requirement, Reward {
    private final int amount;
    private final short durability;
    private final Material material;
    private final boolean take;

    ItemReward(Material mat, int amount, short durability, boolean take) {
        this.material = mat;
        this.amount = amount;
        this.durability = durability;
        this.take = take;
    }

    @Override
    public boolean fulfilsRequirement(Player player) {
        return InventoryUtils.has(player, material, amount);
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

    @SuppressWarnings("deprecation")
    @Override
    public void grant(Player player, int UID) {
        if (amount <= 0 || material == null || material == Material.AIR)
            return;
        if (this.take) {
            InventoryUtils.removeItems(player, material, amount);
        } else {
            int temp = this.amount, other = temp;
            Collection<ItemStack> unadded = new ArrayList<ItemStack>();
            while (temp > 0) {
                other = temp > material.getMaxStackSize() ? material
                        .getMaxStackSize() : temp;
                unadded.addAll(player.getInventory()
                        .addItem(new ItemStack(material, other, durability))
                        .values());
                temp -= other;
            }
            for (ItemStack stack : unadded) {
                player.getWorld()
                        .dropItemNaturally(player.getLocation(), stack);
            }
            player.updateInventory();
        }
    }

    @Override
    public boolean isTake() {
        return take;
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
            int id;
            short data;
            if (storage.getString("id").contains(":")) {
                String[] split = storage.getString(root + ".id").split(":");
                id = Integer.parseInt(split[0]);
                data = Short.parseShort(split[1]);
            } else {
                id = storage.getInt(root + ".id");
                data = (short) storage.getInt(root + ".data");
            }
            int amount = storage.keyExists(root + ".amount") ? storage
                    .getInt(root + ".amount") : 1;
            return new ItemReward(Material.getMaterial(id), amount, data, take);
        }
    }
}