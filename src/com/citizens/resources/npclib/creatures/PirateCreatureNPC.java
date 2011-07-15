package com.citizens.resources.npclib.creatures;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.ItemStack;

public class PirateCreatureNPC extends CreatureNPC {
	// Another Wall of Text Explaining Pirates -
	// spawn in water on boats come ashore to steal:
	// 1. player inventories
	// 2. chests/dispensers/furnaces (they place a sign that marks that they
	// stole from there)
	// 3. player's economy money
	// once their inventory is full (or a certain amount of on-shore ticks have
	// passed), they return
	// to their boat to escape; if they die, they drop all of the items that
	// they stole and return all economy money .... anything else?
	public PirateCreatureNPC(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	@Override
	public void onSpawn() {
		if (this.npc != null) {
			this.npc.getPlayer().setPassenger(
					this.npc.getWorld().spawn(this.npc.getLocation(),
							Boat.class));
		}
		npc.getInventory().setItemInHand(
				new ItemStack(weapons[this.random.nextInt(weapons.length)], 1));
	}

	@Override
	public void doTick() {
		this.randomPather = false;
		if (isBoating()) {
			// findShore();
		} else {
			// loot();
		}
	}

	private boolean isBoating() {
		Vehicle vehicle = this.npc.getPlayer().getVehicle();
		if (vehicle == null) {
			return false;
		}
		return vehicle instanceof Boat;
	}

	@Override
	public void onDeath() {
		if (isBoating()) {
			removeBoat();
		}
		for (ItemStack item : this.npc.getInventory().getContents()) {
			this.getEntity().getWorld()
					.dropItemNaturally(this.getLocation(), item);
		}
	}

	private void removeBoat() {
		this.npc.getPlayer().eject();
		this.npc.getPlayer().getVehicle().remove();
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.PIRATE;
	}

	@Override
	public void onRightClick(Player player) {
	}

	@Override
	public void onLeftClick(Player player) {
	}
}