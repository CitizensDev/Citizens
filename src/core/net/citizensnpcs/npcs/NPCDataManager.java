package net.citizensnpcs.npcs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.event.npc.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.npc.NPCRightClickEvent;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;
import net.citizensnpcs.waypoints.Waypoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

public class NPCDataManager {
	public static final Map<String, Integer> pathEditors = Maps.newHashMap();
	public static final Map<String, Integer> equipmentEditors = Maps
			.newHashMap();
	public static final Map<Integer, Deque<String>> NPCTexts = new MapMaker()
			.makeMap();
	public static final Map<String, Integer> selectedNPCs = new MapMaker()
			.makeMap();

	public static void handleEquipmentEditor(NPCRightClickEvent event) {
		Player player = event.getPlayer();
		if (equipmentEditors.get(player.getName()) != null) {
			HumanNPC npc = NPCManager
					.get(equipmentEditors.get(player.getName()));
			if (npc == null) {
				equipmentEditors.remove(player.getName());
				player.sendMessage(ChatColor.GRAY
						+ "Something went wrong (NPC is dead?).");
				return;
			}
			equip(player, npc);
		}
	}

	// equip an NPC based on a player's item-in-hand
	@SuppressWarnings("deprecation")
	private static void equip(Player player, HumanNPC npc) {
		ItemStack hand = player.getItemInHand();
		PlayerInventory inv = player.getInventory();
		List<Integer> items = new ArrayList<Integer>();
		items.add(npc.getInventory().getItemInHand().getTypeId());
		for (ItemStack i : npc.getInventory().getArmorContents()) {
			items.add(i.getTypeId());
		}
		boolean success = false;
		if (player.getItemInHand().getType() == Material.AIR) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i) != 0) {
					inv.addItem(new ItemStack(items.get(i), 1));
					items.set(i, 0);
					player.updateInventory();
				}
			}
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is now naked. Here are the items!");
			success = true;
		} else {
			int itemID = hand.getTypeId();
			String error = npc.getStrippedName() + " is already equipped with "
					+ MessageUtils.getMaterialName(itemID) + ".";
			String slot = "";
			if (player.isSneaking()) {
				if (Material.getMaterial(items.get(0)) == Material
						.getMaterial(itemID)) {
					Messaging.sendError(player, error);
					return;
				}
				items.set(0, itemID);
				if (npc.getInventory().getItemInHand().getType() != Material.AIR) {
					inv.addItem(npc.getInventory().getItemInHand());
				}
				slot = "item-in-hand";
			} else {
				if (InventoryUtils.isHelmet(itemID)) {
					if (Material.getMaterial(items.get(1)) == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					slot = "helmet";
					if (npc.getInventory().getBoots().getType() != Material.AIR) {
						inv.addItem(npc.getInventory().getHelmet());
					}
					items.set(1, itemID);
				} else if (InventoryUtils.isChestplate(itemID)) {
					if (Material.getMaterial(items.get(2)) == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					slot = "chestplate";
					if (npc.getInventory().getChestplate().getType() != Material.AIR) {
						inv.addItem(npc.getInventory().getChestplate());
					}
					items.set(2, itemID);
				} else if (InventoryUtils.isLeggings(itemID)) {
					if (Material.getMaterial(items.get(3)) == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					slot = "leggings";
					if (npc.getInventory().getLeggings().getType() != Material.AIR) {
						inv.addItem(npc.getInventory().getLeggings());
					}
					items.set(3, itemID);
				} else if (InventoryUtils.isBoots(itemID)) {
					if (Material.getMaterial(items.get(4)) == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					slot = "boots";
					if (npc.getInventory().getBoots().getType() != Material.AIR) {
						inv.addItem(npc.getInventory().getBoots());
					}
					items.set(4, itemID);
				} else {
					if (Material.getMaterial(items.get(0)) == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					items.set(0, itemID);
					if (npc.getInventory().getItemInHand().getType() != Material.AIR) {
						inv.addItem(npc.getInventory().getItemInHand());
					}
					slot = "item-in-hand";
				}
			}
			player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s ")
					+ slot + " was set to "
					+ StringUtils.wrap(MessageUtils.getMaterialName(itemID))
					+ ".");
			success = true;
		}
		InventoryUtils.decreaseItemInHand(player);

		if (success) {
			npc.getNPCData().setItems(items);
			addItems(npc, items);
			NPCManager.removeForRespawn(npc.getUID());
			NPCManager.register(npc.getUID(), npc.getOwner(),
					NPCCreateReason.RESPAWN);
		}
	}

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

	// Adds items to an npc so that they are visible.
	public static void addItems(HumanNPC npc, List<Integer> items) {
		if (items != null) {
			Material matHand = Material.getMaterial(items.get(0));
			Material matHelm = Material.getMaterial(items.get(1));
			Material matTorso = Material.getMaterial(items.get(2));
			Material matLegs = Material.getMaterial(items.get(3));
			Material matBoots = Material.getMaterial(items.get(4));

			PlayerInventory npcInv = npc.getInventory();

			if (matHand != null && matHand != Material.AIR) {
				npcInv.setItemInHand(new ItemStack(matHand, 1));
			} else {
				npcInv.setItemInHand(null);
			}
			if (matHelm != null && matHelm != Material.AIR) {
				npcInv.setHelmet(new ItemStack(matHelm, 1));
			} else {
				npcInv.setHelmet(null);
			}
			if (matBoots != null && matBoots != Material.AIR) {
				npcInv.setBoots(new ItemStack(matBoots, 1));
			} else {
				npcInv.setBoots(null);
			}
			if (matLegs != null && matLegs != Material.AIR) {
				npcInv.setLeggings(new ItemStack(matLegs, 1));
			} else {
				npcInv.setLeggings(null);
			}
			if (matTorso != null && matTorso != Material.AIR) {
				npcInv.setChestplate(new ItemStack(matTorso, 1));
			} else {
				npcInv.setChestplate(null);
			}
			npc.getNPCData().setItems(items);
		}
	}

	// Adds to an npc's text.
	public static void addText(int UID, String text) {
		Deque<String> texts = NPCDataManager.getText(UID);
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

	// Get an npc's text.
	public static Deque<String> getText(int UID) {
		return NPCTexts.get(UID);
	}

	// Sets an npc's text to the given texts.
	public static void setText(int UID, Deque<String> text) {
		text = StringUtils.colourise(text);
		NPCTexts.put(UID, text);
		NPCManager.get(UID).getNPCData().setTexts(text);
	}

	// Resets an NPC's text.
	public static void resetText(int UID) {
		setText(UID, new ArrayDeque<String>());
	}
}