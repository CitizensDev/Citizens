package net.citizensnpcs.alchemists;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Alchemist extends CitizensNPC {
    private Map<Integer, String> recipes = new HashMap<Integer, String>();
    private int currentRecipeID = 0;

    public Map<Integer, String> getRecipes() {
        return recipes;
    }

    public void setRecipes(Map<Integer, String> recipes) {
        this.recipes = recipes;
    }

    public String getRecipe(int itemID) {
        return recipes.get(itemID);
    }

    public void addRecipe(int itemID, String recipe) {
        this.recipes.put(itemID, recipe);
    }

    public int getCurrentRecipeID() {
        return this.currentRecipeID;
    }

    public void setCurrentRecipeID(int currentRecipeID) {
        this.currentRecipeID = currentRecipeID;
    }

    @Override
    public void onRightClick(Player player, HumanNPC npc) {
        if (!PermissionManager.hasPermission(player, "citizens.alchemist.use.interact")) {
            Messaging.sendError(player, MessageUtils.noPermissionsMessage);
            return;
        }
        if (!AlchemistManager.hasClickedOnce(player.getName())) {
            if (recipes.size() == 0) {
                Messaging.sendError(player, npc.getName() + " has no recipes.");
                return;
            }
            if (AlchemistManager.sendRecipeMessage(player, npc, 1)) {
                player.sendMessage(ChatColor.GREEN + "Type " + StringUtils.wrap("/alchemist view (page)")
                        + " for more ingredients.");
                player.sendMessage(ChatColor.GREEN + "Right-click again to craft.");
            }
            AlchemistManager.setClickedOnce(player.getName(), true);
            return;
        }
        AlchemistTask task = new AlchemistTask(npc);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Citizens.plugin, task, 2, 1);
        player.openInventory(npc.getInventory());
        AlchemistManager.setClickedOnce(player.getName(), false);
        // TODO requires inventory events to cancel tasks when inventory closes
    }

    @Override
    public CitizensNPCType getType() {
        return new AlchemistType();
    }

    @Override
    public void save(Storage profiles, int UID) {
        profiles.setInt(UID + ".alchemist.recipes.current", currentRecipeID);
    }

    @Override
    public void load(Storage profiles, int UID) {
        currentRecipeID = profiles.getInt(UID + ".alchemist.recipes.current", 0);

    }
}