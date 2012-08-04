package net.citizensnpcs.wizards;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.citizensnpcs.Settings;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.wizards.WizardManager.WizardMode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class Wizard extends CitizensNPC {
    private int currentLocation = 0;
    // TODO: using a string as storage for locations is baaaaad.
    private final List<String> locations = Lists.newArrayList();
    private int mana = 10;
    private EntityType mob = EntityType.CHICKEN;
    private int mobIndex = 0;
    private WizardMode mode = WizardMode.TELEPORT;
    private String time = "morning";
    private boolean unlimitedMana = false;

    // Adds a location to the main location sting.
    public void addLocation(Location location, String locName) {
        String addedLoc = locName + "," + location.getWorld().getName() + "," + location.getX() + "," + location.getY()
                + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
        locations.add(addedLoc);
    }

    // Sets the next location in the list as active.
    public void cycle() {
        switch (mode) {
        case TELEPORT:
            ++currentLocation;
            if (currentLocation >= locations.size()) {
                currentLocation = 0;
            }
            break;
        case SPAWN:
            this.mob = EntityType.values()[mobIndex >= EntityType.values().length ? (mobIndex = 0) : ++mobIndex];
            break;
        case TIME:
            String time = this.time;
            String newTime = "";
            if (time.equals("morning")) {
                newTime = "day";
            } else if (time.equals("day")) {
                newTime = "afternoon";
            } else if (time.equals("afternoon")) {
                newTime = "night";
            } else if (time.equals("night")) {
                newTime = "morning";
            }
            this.time = newTime;
            break;
        }
    }

    // Return the current active teleport location for the wizard.
    public Location getCurrentLocation() {
        if (currentLocation >= locations.size())
            currentLocation = 0;
        String locs[] = locations.get(currentLocation).split(",");
        return new Location(Bukkit.getServer().getWorld(locs[1]), Double.parseDouble(locs[2]),
                Double.parseDouble(locs[3]), Double.parseDouble(locs[4]), Float.parseFloat(locs[5]),
                Float.parseFloat(locs[6]));
    }

    // Return the current active teleport location name for the wizard.
    public String getCurrentLocationName() {
        if (currentLocation >= locations.size())
            currentLocation = 0;
        return locations.get(currentLocation).split(",")[0];
    }

    // Returns the main location string
    public Collection<String> getLocations() {
        return locations;
    }

    // Get the mana that a wizard NPC has remaining
    public int getMana() {
        return mana;
    }

    // Get the mob that a wizard will spawn
    public EntityType getMob() {
        return mob;
    }

    // Get the mode of a wizard
    public WizardMode getMode() {
        return mode;
    }

    // Returns the total amount of locations bound to the wizard.
    public int getNumberOfLocations() {
        return locations.size();
    }

    // Get the time setting of a wizard
    public String getTime() {
        return time;
    }

    @Override
    public CitizensNPCType getType() {
        return new WizardType();
    }

    // Get whether a wizard has unlimited mana
    public boolean hasUnlimitedMana() {
        return unlimitedMana;
    }

    @Override
    public void load(Storage profiles, int UID) {
        unlimitedMana = profiles.getBoolean(UID + ".wizard.unlimited-mana");
        time = profiles.getString(UID + ".wizard.time", "morning");
        mode = (profiles.keyExists(UID + ".wizard.mode") && WizardMode.parse(profiles.getString(UID + ".wizard.mode")) != null) ? WizardMode
                .parse(profiles.getString(UID + ".wizard.mode")) : WizardMode.TELEPORT;
        mana = profiles.getInt(UID + ".wizard.mana", 10);
        locations.clear();
        for (String location : splitter.split(profiles.getString(UID + ".wizard.locations"))) {
            locations.add(location.replace("(", "").replace(")", ""));
        }
        mob = (EntityType.fromName(profiles.getString(UID + ".wizard.mob")) != null) ? EntityType.fromName(profiles
                .getString(UID + ".wizard.mob")) : EntityType.CREEPER;
        mobIndex = mob.ordinal();
    }

    @Override
    public void onLeftClick(Player player, HumanNPC npc) {
        if (PermissionManager.hasPermission(player, "citizens.wizard.use.interact")) {
            if (UtilityProperties.isHoldingTool("WizardInteractItem", player)) {
                String msg = ChatColor.GREEN.toString();
                switch (mode) {
                case TELEPORT:
                    if (locations.size() > 0) {
                        cycle();
                        msg += "Location set to " + StringUtils.wrap(getCurrentLocationName());
                    } else {
                        msg += ChatColor.RED + npc.getName() + " has no locations.";
                    }
                    break;
                case SPAWN:
                    cycle();
                    msg += "Mob to spawn set to " + StringUtils.wrap(StringUtils.format(mob));
                    break;
                case TIME:
                    cycle();
                    msg += "Time setting set to " + StringUtils.wrap(time);
                    break;
                case WEATHER:
                    return;
                default:
                    msg = ChatColor.RED + "No valid mode selected.";
                }
                player.sendMessage(msg);
            }
        } else {
            player.sendMessage(MessageUtils.noPermissionsMessage);
        }
    }

    @Override
    public void onRightClick(Player player, HumanNPC npc) {
        if (PermissionManager.hasPermission(player, "citizens.wizard.use.interact")) {
            if (UtilityProperties.isHoldingTool("WizardInteractItem", player)) {
                WizardManager.handleRightClick(player, npc, "wizard." + mode.toString());
            } else if (UtilityProperties.isHoldingTool("WizardManaRegenItem", player)) {
                String msg = StringUtils.wrap(npc.getName() + "'s");
                int mana = 0;
                if (mana + 10 < Settings.getInt("WizardMaxMana")) {
                    mana = mana + 10;
                    msg += " mana has been increased to " + StringUtils.wrap(mana) + ".";
                } else if (mana + 10 == Settings.getInt("WizardMaxMana")) {
                    mana = Settings.getInt("WizardMaxMana");
                    msg += " mana has been fully replenished.";
                } else {
                    msg += " mana cannot be regenerated with that item any further.";
                    return;
                }
                InventoryUtils.decreaseItemInHand(player);
                player.sendMessage(msg);
                this.mana = mana;
            }
        } else {
            player.sendMessage(MessageUtils.noPermissionsMessage);
        }
    }

    public boolean removeLocation(String string) {
        Iterator<String> iter = locations.iterator();
        while (iter.hasNext()) {
            if (iter.next().split(",")[0].equalsIgnoreCase(string)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Storage profiles, int UID) {
        profiles.setBoolean(UID + ".wizard.unlimited-mana", unlimitedMana);
        profiles.setString(UID + ".wizard.time", time);
        profiles.setString(UID + ".wizard.mode", mode.name());
        profiles.setInt(UID + ".wizard.mana", mana);
        profiles.setString(UID + ".wizard.locations", Joiner.on(":").skipNulls().join(locations));
        profiles.setString(UID + ".wizard.mob", mob.name());
    }

    // Set the mana of a wizard NPC
    public void setMana(int mana) {
        this.mana = mana;
    }

    // Set the mode of a wizard
    public void setMode(WizardMode mode) {
        this.mode = mode;
    }

    // Set the time setting for a wizard
    public void setTime(String time) {
        this.time = time;
    }

    // Set whether a wizard has unlimited mana
    public void setUnlimitedMana(boolean unlimitedMana) {
        this.unlimitedMana = unlimitedMana;
    }

    private static final Splitter splitter = Splitter.on(":").omitEmptyStrings().trimResults();
}