package net.citizensnpcs.lib.creatures;

import java.util.Random;

import net.citizensnpcs.Settings;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.lib.CraftNPC;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;
import net.minecraft.server.EntityHuman;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EvilCreatureNPC extends CreatureNPC {
	private static final int RANGE = 25;
	private boolean isTame = false;

	public EvilCreatureNPC(CraftNPC handle) {
		super(handle);
	}

	@Override
	public void doTick() {
		super.doTick();
		if (isTame)
			return;
		EntityHuman closest = handle.getClosestPlayer(RANGE);
		if (!handle.getPathController().isPathing() && closest != null) {
			if (!PermissionManager
					.hasPermission((Player) closest.getBukkitEntity(),
							"citizens.evils.immune")) {
				handle.getPathController().target(closest.getBukkitEntity(),
						true);
			}
		}
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}

	@Override
	public void onDeath() {
		ItemStack item = UtilityProperties.getRandomDrop(Settings
				.getString("EvilDrops"));
		if (item == null)
			return;
		this.getPlayer().getWorld().dropItemNaturally(this.getLocation(), item);
	}

	@Override
	public void onLeftClick(Player player) {
	}

	@Override
	public void onRightClick(Player player) {
		if (!PermissionManager.canCreate(player)) {
			Messaging
					.sendError(
							player,
							"You cannot tame this Evil NPC because you have reached the NPC creation limit.");
			return;
		}
		if (player.getItemInHand().getTypeId() != Settings
				.getInt("EvilTameItem"))
			return;
		if (new Random().nextInt(100) <= Settings.getInt("EvilTameChance")) {
			InventoryUtils.decreaseItemInHand(player);
			isTame = true;
			CreatureTask.despawn(this);
			HumanNPC npc = NPCManager.register(handle.getPlayer().getName(),
					player.getLocation(), NPCCreateReason.RESPAWN);
			npc.getNPCData().setOwner(player.getName());
			player.sendMessage(ChatColor.GREEN + "You have tamed "
					+ StringUtils.wrap(handle.getPlayer().getName())
					+ "! You can now toggle it to be any type.");
		} else {
			Messaging.send(
					player,
					StringUtils.colourise(Settings.getString("ChatFormat")
							.replace("%name%", handle.getPlayer().getName()))
							+ ChatColor.WHITE
							+ MessageUtils.getRandomMessage(Settings
									.getString("EvilFailedTameMessages")));
		}
	}

	@Override
	public void onSpawn() {
		super.onSpawn();
		Player player = handle.getPlayer();
		player.getInventory()
				.setItemInHand(
						new ItemStack(weapons[new Random()
								.nextInt(weapons.length)], 1));
		player.setHealth(Math.min(Settings.getInt("EvilHealth"),
				player.getMaxHealth()));
	}
}