package net.citizensnpcs.npcdata;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.SettingsManager;
import net.citizensnpcs.api.event.npc.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.npc.NPCRightClickEvent;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.InventoryUtils.Armor;
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

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

public class NPCDataManager {
	// TODO: make editors an interface.
	public static final Map<String, Integer> pathEditors = Maps.newHashMap();
	public static final Map<Player, Integer> equipmentEditors = Maps
			.newHashMap();
	public static final Map<Integer, Deque<String>> NPCTexts = new MapMaker()
			.makeMap();
	public static final Map<String, Integer> selectedNPCs = new MapMaker()
			.makeMap();

	public static void handleEquipmentEditor(NPCRightClickEvent event) {
		Player player = event.getPlayer();
		HumanNPC npc = event.getNPC();
		if (equipmentEditors.get(player) != null
				&& equipmentEditors.get(player) == npc.getUID()) {
			equip(player, npc);
		}
	}

	// equip an NPC based on a player's item-in-hand
	@SuppressWarnings("deprecation")
	private static void equip(Player player, HumanNPC npc) {
		// TODO: cleanup
		ItemStack hand = player.getItemInHand();
		PlayerInventory npcInv = npc.getInventory();
		List<ItemData> items = Lists.newArrayList();
		items.add(new ItemData(npc.getItemInHand().getTypeId(), npc
				.getItemInHand().getDurability()));
		for (Armor armor : Armor.values()) {
			items.add(new ItemData(armor.get(npcInv).getTypeId(), armor.get(
					npcInv).getDurability()));
		}
		List<ItemStack> toAdd = Lists.newArrayList();
		if (player.getItemInHand() == null
				|| player.getItemInHand().getType() == Material.AIR) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getID() != 0) {
					toAdd.add(items.get(i).createStack());
				}
				items.set(i, new ItemData(0, (short) 0));
			}
			player.sendMessage(StringUtils.wrap(npc.getStrippedName())
					+ " is now naked. Here are the items!");
		} else {
			int itemID = hand.getTypeId();
			String error = npc.getStrippedName() + " is already equipped with "
					+ MessageUtils.getMaterialName(itemID) + ".";
			String slot = "";
			if (player.isSneaking()) {
				if (Material.getMaterial(items.get(0).getID()) == Material
						.getMaterial(itemID)) {
					Messaging.sendError(player, error);
					return;
				}
				slot = "item-in-hand";
				if (npc.getItemInHand().getType() != Material.AIR) {
					toAdd.add(items.get(0).createStack());
				}
				items.set(0,
						new ItemData(hand.getTypeId(), hand.getDurability()));
			} else {
				Armor armor = Armor.getArmorSlot(itemID);
				if (armor != null) {
					if (armor.get(npcInv).getType() == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					slot = armor.name().toLowerCase();
					if (armor.get(npcInv).getType() != Material.AIR) {
						toAdd.add(items.get(armor.getSlot() + 1).createStack());
					}
					items.set(
							armor.getSlot() + 1,
							new ItemData(hand.getTypeId(), hand.getDurability()));
				} else {
					if (Material.getMaterial(items.get(0).getID()) == Material
							.getMaterial(itemID)) {
						Messaging.sendError(player, error);
						return;
					}
					slot = "item-in-hand";
					if (npc.getItemInHand().getType() != Material.AIR) {
						toAdd.add(items.get(0).createStack());
					}
					items.set(
							0,
							new ItemData(hand.getTypeId(), hand.getDurability()));
				}
			}
			player.sendMessage(StringUtils.wrap(npc.getStrippedName() + "'s ")
					+ slot + " was set to "
					+ StringUtils.wrap(MessageUtils.getMaterialName(itemID))
					+ ".");
		}
		// remove item that was added to NPC
		InventoryUtils.decreaseItemInHand(player);
		// add all items to the player's inventory AFTER in-hand item was
		// removed
		boolean drop = false;
		for (ItemStack i : toAdd) {
			// drop items that don't fit in a player's inventory
			for (ItemStack unadded : player.getInventory().addItem(i).values()) {
				player.getWorld().dropItemNaturally(player.getLocation(),
						unadded);
				drop = true;
			}
		}
		if (drop) {
			Messaging
					.sendError(player,
							"Some items couldn't fit in your inventory and were dropped at your location.");
		}
		player.updateInventory();

		addItems(npc, items);
		NPCManager.removeForRespawn(npc.getUID());
		NPCManager.register(npc.getUID(), npc.getOwner(),
				NPCCreateReason.RESPAWN);
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
	public static void addItems(HumanNPC npc, List<ItemData> items) {
		if (items != null) {
			npc.setItemInHand(items.get(0).getID() == 0 ? null : items.get(0)
					.createStack());
			for (int i = 0; i < items.size() - 1; i++) {
				Armor.getArmor(i).set(
						npc.getInventory(),
						items.get(i + 1).getID() == 0 ? null : items.get(i)
								.createStack());
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