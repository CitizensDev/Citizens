package net.citizensnpcs.npcs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.events.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

public class NPCDataManager {
	public static final Map<String, Integer> pathEditors = Maps.newHashMap();
	public static final Map<Integer, ArrayDeque<String>> NPCTexts = new MapMaker()
			.makeMap();
	static final Map<String, Integer> selectedNPCs = new MapMaker().makeMap();

	public static void handlePathEditor(PlayerInteractEvent event) {
		String name = event.getPlayer().getName();
		if (pathEditors.get(name) != null) {
			HumanNPC npc = NPCManager.get(pathEditors.get(name));
			if (npc == null) {
				pathEditors.remove(name);
				event.getPlayer()
						.sendMessage(
								ChatColor.GRAY
										+ "Something went wrong (NPC is dead?).");
				return;
			}
			switch (event.getAction()) {
			case LEFT_CLICK_BLOCK:
				Location loc = event.getClickedBlock().getLocation();
				if (!(npc.getWorld().getName().equals(event.getPlayer()
						.getWorld().getName()))) {
					event.getPlayer()
							.sendMessage(
									ChatColor.GRAY
											+ "Waypoints must be in the same world as the npc.");
					break;
				}
				if (npc.getWaypoints().size() > 0
						&& npc.getWaypoints().getLast().getLocation()
								.distance(loc) > SettingsManager
								.getDouble("PathfindingRange")) {
					event.getPlayer().sendMessage(
							ChatColor.GRAY
									+ "Points can't be more than "
									+ StringUtils.wrap(SettingsManager
											.getDouble("PathfindingRange"),
											ChatColor.GRAY)
									+ " blocks away from each other.");
					break;
				}
				npc.getWaypoints().add(new Waypoint(loc));
				event.getPlayer().sendMessage(
						StringUtils.wrap("Added")
								+ " waypoint at ("
								+ StringUtils.wrap(loc.getBlockX())
								+ ", "
								+ StringUtils.wrap(loc.getBlockY())
								+ ", "
								+ StringUtils.wrap(loc.getBlockZ())
								+ ") ("
								+ StringUtils.wrap(npc.getWaypoints().size())
								+ " "
								+ StringUtils.pluralise("waypoint", npc
										.getWaypoints().size()) + ")");
				break;
			case RIGHT_CLICK_BLOCK:
			case RIGHT_CLICK_AIR:
				if (npc.getWaypoints().size() > 0) {
					npc.getWaypoints().removeLast();
					event.getPlayer().sendMessage(
							StringUtils.wrap("Undid")
									+ " the last waypoint ("
									+ StringUtils.wrap(npc.getWaypoints()
											.size()) + " remaining)");

				} else
					event.getPlayer().sendMessage(
							ChatColor.GRAY + "No more waypoints.");
				break;
			}
		}
	}

	/**
	 * Adds items to an npc so that they are visible.
	 * 
	 * @param npc
	 * @param items
	 */
	public static void addItems(HumanNPC npc, ArrayList<Integer> items) {
		if (items != null) {
			Material matHelm = Material.getMaterial(items.get(1));
			Material matTorso = Material.getMaterial(items.get(2));
			Material matLegs = Material.getMaterial(items.get(3));
			Material matBoots = Material.getMaterial(items.get(4));

			// TODO: reduce the long if-tree.
			if (matHelm != null && matHelm != Material.AIR) {
				npc.getInventory().setHelmet(new ItemStack(matHelm, 1));
			} else {
				npc.getInventory().setHelmet(null);
			}
			if (matBoots != null && matBoots != Material.AIR) {
				npc.getInventory().setBoots(new ItemStack(matBoots, 1));
			} else {
				npc.getInventory().setBoots(null);
			}
			if (matLegs != null && matLegs != Material.AIR) {
				npc.getInventory().setLeggings(new ItemStack(matLegs, 1));
			} else {
				npc.getInventory().setLeggings(null);
			}
			if (matTorso != null && matTorso != Material.AIR) {
				npc.getInventory().setChestplate(new ItemStack(matTorso, 1));
			} else {
				npc.getInventory().setChestplate(null);
			}
			npc.getNPCData().setItems(items);
		}
	}

	/**
	 * Sets the in-hand item of an npc.
	 * 
	 * @param player
	 * @param npc
	 * @param material
	 */
	public static void setItemInHand(Player player, HumanNPC npc,
			String material) {
		Material mat = StringUtils.parseMaterial(material);
		if (mat == null) {
			player.sendMessage(ChatColor.RED + "Incorrect item name.");
			return;
		}
		if (mat != Material.AIR && !player.getInventory().contains(mat)) {
			player.sendMessage(ChatColor.RED
					+ "You need to have at least 1 of the item in your inventory to add it to the NPC.");
			return;
		}
		if (npc.isType("trader")) {
			player.sendMessage(ChatColor.GRAY
					+ "That NPC is a trader. Please put the item manually in the first slot of the trader's inventory instead.");
			return;
		}
		int slot = player.getInventory().first(mat);
		ItemStack item = InventoryUtils.decreaseItemStack(player.getInventory()
				.getItem(slot));
		player.getInventory().setItem(slot, item);

		ArrayList<Integer> items = npc.getNPCData().getItems();

		int olditem = items.get(0);
		items.set(0, mat.getId());

		npc.getNPCData().setItems(items);

		if (mat != null && mat != Material.AIR) {
			npc.getInventory().setItem(0, new ItemStack(mat, 1));
		} else {
			npc.getInventory().setItem(0, null);
		}

		NPCDataManager.addItems(npc, items);

		if ((olditem != 0 && items.get(0) == 0)) {
			NPCManager.removeForRespawn(npc.getUID());
			NPCManager.register(npc.getUID(), npc.getOwner(), NPCCreateReason.RESPAWN);
		}
		player.sendMessage(StringUtils.wrap(npc.getStrippedName())
				+ "'s in-hand item was set to "
				+ StringUtils.wrap(MessageUtils.getMaterialName(mat.getId()))
				+ ".");
	}

	/**
	 * Adds to an npc's text.
	 * 
	 * @param UID
	 * @param text
	 */
	public static void addText(int UID, String text) {
		ArrayDeque<String> texts = NPCDataManager.getText(UID);
		if (texts == null) {
			texts = new ArrayDeque<String>();
		}
		texts.add(text);
		NPCDataManager.setText(UID, texts);
	}

	public static int getSelected(Player player) {
		return selectedNPCs.get(player.getName());
	}

	public static void selectNPC(Player player, HumanNPC npc) {
		selectedNPCs.put(player.getName(), npc.getUID());
	}

	public static void deselectNPC(Player player) {
		selectedNPCs.remove(player.getName());
	}

	/**
	 * Returns an npc's text.
	 * 
	 * @param UID
	 * @return
	 */
	public static ArrayDeque<String> getText(int UID) {
		return NPCTexts.get(UID);
	}

	/**
	 * Sets an npc's text to the given texts.
	 * 
	 * @param UID
	 * @param text
	 */
	public static void setText(int UID, ArrayDeque<String> text) {
		text = StringUtils.colourise(text);
		NPCTexts.put(UID, text);
		NPCManager.get(UID).getNPCData().setTexts(text);
	}

	/**
	 * Resets an NPC's text.
	 * 
	 * @param UID
	 */
	public static void resetText(int UID) {
		setText(UID, new ArrayDeque<String>());
	}
}