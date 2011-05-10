package com.fullwall.Citizens.NPCs;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Blacksmiths.BlacksmithNPC;
import com.fullwall.Citizens.Healers.HealerNPC;
import com.fullwall.Citizens.Traders.TraderNPC;
import com.fullwall.Citizens.Utils.BlacksmithPropertyPool;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.Citizens.Utils.WizardPropertyPool;
import com.fullwall.Citizens.Wizards.WizardNPC;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCList;
import com.fullwall.resources.redecouverte.NPClib.NPCSpawner;

public class NPCManager {

	@SuppressWarnings("unused")
	private Citizens plugin;
	public static ConcurrentHashMap<Integer, String> GlobalUIDs = new ConcurrentHashMap<Integer, String>();
	public static ConcurrentHashMap<Integer, ArrayList<String>> BasicNPCTexts = new ConcurrentHashMap<Integer, ArrayList<String>>();
	public static ConcurrentHashMap<String, Integer> NPCSelected = new ConcurrentHashMap<String, Integer>();
	public Random ran = new Random(new Random(new Random(new Random(
			System.currentTimeMillis()).nextLong()).nextLong()).nextLong());
	private static NPCList list;

	public NPCManager(Citizens plugin) {
		this.plugin = plugin;
		NPCManager.list = new NPCList();
	}

	/**
	 * Spawns a new npc and registers it.
	 * 
	 * @param name
	 * @param UID
	 * @param owner
	 */
	public void registerNPC(String name, int UID, String owner) {
		Location loc = PropertyPool.getLocationFromID(UID);
		// String uniqueID = generateID(NPCType.BASIC);

		String colour = PropertyPool.getColour(UID);// StringUtils.getColourFromString(name);
		name = ChatColor.stripColor(name);
		String npcName = name;
		if (!colour.isEmpty() && !colour.equals("§f"))
			npcName = colour + name;
		if (Citizens.convertSlashes == true) {
			String[] brokenName = npcName.split(Citizens.convertToSpaceChar);
			for (int i = 0; i < brokenName.length; i++) {
				if (i == 0)
					npcName = brokenName[i];
				else
					npcName += " " + brokenName[i];
			}
		}
		HumanNPC npc = NPCSpawner.SpawnBasicHumanNpc(UID, npcName,
				loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), 0.0F);

		ArrayList<Integer> items = PropertyPool.getItems(UID);

		loadBasic(npc, UID, items);
		if (TraderPropertyPool.isTrader(UID)) {
			loadTrader(npc, npc.getTrader(), UID);
		}
		if (HealerPropertyPool.isHealer(UID)) {
			loadHealer(npc, npc.getHealer(), UID);
		}
		if (WizardPropertyPool.isWizard(UID)) {
			loadWizard(npc, npc.getWizard(), UID);
		}
		if (BlacksmithPropertyPool.isBlacksmith(UID)) {
			loadBlacksmith(npc, npc.getBlacksmith(), UID);
		}
		npc.setNPCData(new NPCData(name, UID, loc, colour, items, BasicNPCTexts
				.get(UID), Citizens.defaultFollowingEnabled,
				Citizens.defaultTalkWhenClose, owner, npc.getBalance()));
		PropertyPool.saveState(UID, npc.getNPCData());
		registerUID(UID, name);
		list.put(UID, npc);
		TraderPropertyPool.saveState(npc);
		HealerPropertyPool.saveState(npc);
		WizardPropertyPool.saveState(npc);
		BlacksmithPropertyPool.saveState(npc);
		npc.getPlayer().setSleepingIgnored(true);
	}

	/**
	 * Registers a new npc.
	 * 
	 * @param name
	 * @param loc
	 * @param owner
	 * @return
	 */
	public int registerNPC(String name, Location loc, String owner) {
		int UID = PropertyPool.getNewNpcID();
		PropertyPool.saveLocation(name, loc, UID);
		PropertyPool.setLookWhenClose(UID, Citizens.defaultFollowingEnabled);
		PropertyPool.setTalkWhenClose(UID, Citizens.defaultTalkWhenClose);
		registerNPC(name, UID, owner);
		return UID;
	}

	/**
	 * Loads trader data for an npc.
	 * 
	 * @param npc
	 * @param trader
	 * @param UID
	 */
	private void loadTrader(HumanNPC npc, TraderNPC trader, int UID) {
		npc.setTrader(TraderPropertyPool.getTraderState(UID));
		npc.getInventory().setContents(
				TraderPropertyPool.getInventory(UID).getContents());
		npc.setBalance(PropertyPool.getBalance(UID));
		trader.setUnlimited(TraderPropertyPool.getUnlimited(UID));
		trader.setStocking(TraderPropertyPool.getStockables(UID));
		TraderPropertyPool.saveState(npc);
	}

	/**
	 * Loads basic data for an npc.
	 * 
	 * @param npc
	 * @param UID
	 * @param items
	 */
	private void loadBasic(HumanNPC npc, int UID, ArrayList<Integer> items) {
		NPCDataManager.addItems(npc, items);
		PropertyPool.getSetText(UID);
		npc.setBalance(PropertyPool.getBalance(UID));
	}

	/**
	 * Loads healer data for an npc.
	 * 
	 * @param npc
	 * @param healer
	 * @param UID
	 */
	public void loadHealer(HumanNPC npc, HealerNPC healer, int UID) {
		npc.setHealer(HealerPropertyPool.getHealerState(UID));
		healer.setStrength(HealerPropertyPool.getStrength(UID));
		HealerPropertyPool.saveState(npc);
	}

	/**
	 * Loads wizard data for an npc.
	 * 
	 * @param npc
	 * @param wizard
	 * @param UID
	 */
	public void loadWizard(HumanNPC npc, WizardNPC wizard, int UID) {
		npc.setWizard(WizardPropertyPool.getWizardState(UID));
		wizard.setLocations(WizardPropertyPool.getLocations(UID));
		WizardPropertyPool.saveState(npc);
	}

	/**
	 * Loads blacksmith data for an npc.
	 * 
	 * @param npc
	 * @param blacksmith
	 * @param UID
	 */
	public void loadBlacksmith(HumanNPC npc, BlacksmithNPC blacksmith, int UID) {
		npc.setBlacksmith(BlacksmithPropertyPool.getBlacksmithState(UID));
		BlacksmithPropertyPool.saveState(npc);
	}

	/**
	 * Sets an npc's text to the given texts.
	 * 
	 * @param UID
	 * @param text
	 */
	public static void setBasicNPCText(int UID, ArrayList<String> text) {
		BasicNPCTexts.put(UID, text);
		PropertyPool.saveText(UID, text);
	}

	/**
	 * Returns an npc's text.
	 * 
	 * @param UID
	 * @return
	 */
	public static ArrayList<String> getBasicNPCText(int UID) {
		return BasicNPCTexts.get(UID);
	}

	/**
	 * Gets an npc from a UID.
	 * 
	 * @param UID
	 * @return
	 */
	public static HumanNPC getNPC(int UID) {
		return list.get(UID);
	}

	/**
	 * Gets an npc from an entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static HumanNPC getNPC(Entity entity) {
		return list.getBasicHumanNpc(entity);
	}

	/**
	 * Gets the list of npcs.
	 * 
	 * @return
	 */
	public static NPCList getNPCList() {
		return list;
	}

	/**
	 * Moves an npc to a location.
	 * 
	 * @param npc
	 * @param loc
	 */
	public void moveNPC(HumanNPC npc, Location loc) {
		String location = loc.getWorld().getName() + "," + loc.getX() + ","
				+ loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + ","
				+ loc.getPitch();
		PropertyPool.locations.setString(npc.getUID(), location);
		npc.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F);
	}

	/**
	 * Rotates an npc.
	 * 
	 * @param npc
	 * @param player
	 */
	public static void rotateNPCToPlayer(HumanNPC npc, Player player) {
		Location loc = npc.getLocation();
		double xDiff = player.getLocation().getX() - loc.getX();
		double yDiff = player.getLocation().getY() - loc.getY();
		double zDiff = player.getLocation().getZ() - loc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
		double yaw = (Math.acos(xDiff / DistanceXZ) * 180 / Math.PI);
		double pitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;
		if (zDiff < 0.0) {
			yaw = yaw + (Math.abs(180 - yaw) * 2);
		}
		npc.moveTo(loc.getX(), loc.getY(), loc.getZ(), (float) yaw - 90,
				(float) pitch);
	}

	/**
	 * Despawns an npc.
	 * 
	 * @param UID
	 */
	public void despawnNPC(int UID) {
		GlobalUIDs.remove(UID);
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
		list.remove(UID);
	}

	/**
	 * Removes an npc.
	 * 
	 * @param UID
	 */
	public void removeNPC(int UID) {
		GlobalUIDs.remove(UID);
		String actualName = NPCManager.getNPC(UID).getName();
		String playerName = NPCManager.getNPC(UID).getOwner();
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
		list.remove(UID);
		PropertyPool.removeFromFiles(actualName, playerName, UID);
		TraderPropertyPool.removeFromFiles(UID);
		HealerPropertyPool.removeFromFiles(UID);
	}

	/**
	 * Removes an npc, but not from the properties.
	 * 
	 * @param UID
	 */
	public static void removeNPCForRespawn(int UID) {
		NPCSpawner.RemoveBasicHumanNpc(list.get(UID));
	}

	/**
	 * Gets the global list of UIDs.
	 * 
	 * @return
	 */
	public ConcurrentHashMap<Integer, String> getBasicUIDs() {
		return GlobalUIDs;
	}

	/**
	 * Registers a UID in the global list.
	 * 
	 * @param UID
	 * @param name
	 */
	private void registerUID(int UID, String name) {
		GlobalUIDs.put(UID, name);
	}

	@SuppressWarnings("unused")
	private String generateUID() {
		boolean found = false;
		// Change this to an integer return?
		String UID = "";
		while (found != true) {
			UID = "" + ran.nextInt();
			if (!GlobalUIDs.containsKey(UID)) {
				found = true;
				break;
			}
		}
		return UID;
	}

	/**
	 * Checks if a given entity is an npc.
	 * 
	 * @param entity
	 * @return
	 */
	public static boolean isNPC(Entity entity) {
		return list.getBasicHumanNpc(entity) != null;
	}

	/**
	 * Checks if a player has an npc selected.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean validateSelected(Player p) {
		if (NPCSelected.get(p.getName()) != null
				&& !NPCSelected.get(p.getName()).toString().isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has selected the given npc.
	 * 
	 * @param p
	 * @param npc
	 * @return
	 */
	public static boolean validateSelected(Player p, int UID) {
		if (NPCSelected.get(p.getName()) != null
				&& !NPCSelected.get(p.getName()).toString().isEmpty()) {
			if (NPCSelected.get(p.getName()).equals(UID))
				return true;
		}
		return false;
	}

	// Overloaded method to add an optional permission string parameter (admin
	// overrides).
	public static boolean validateOwnership(Player p, int UID, String permission) {
		if (Permission.generic(p,
				permission.replace("citizens.", "citizens.admin.")))
			return true;
		String[] npcOwners = PropertyPool.getOwner(UID).split(",");
		for (int i = 0; i < npcOwners.length; i++) {
			if (npcOwners[i].equals(p.getName()))
				return true;
		}
		return false;
	}

	/**
	 * Checks if a player owns a given npc.
	 * 
	 * @param UID
	 * @param p
	 * @return
	 */
	public static boolean validateOwnership(Player p, int UID) {
		String[] npcOwners = PropertyPool.getOwner(UID).split(",");
		for (int i = 0; i < npcOwners.length; i++) {
			if (npcOwners[i].equals(p.getName()))
				return true;
		}
		return false;
	}
}
