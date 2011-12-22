package net.citizensnpcs.lib;

import net.citizensnpcs.Settings;
import net.citizensnpcs.lib.NPCAnimator.Animation;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftNPC extends PathNPC {

	public CraftNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
		iteminworldmanager.b(0);
		NetworkManager netMgr = new NPCNetworkManager(new NPCSocket(),
				"npc mgr", new NetHandler() {
					@Override
					public boolean c() {
						return false;
					}
				});
		this.netServerHandler = new NPCNetHandler(minecraftserver, this, netMgr);
		netMgr.a(this.netServerHandler);
		netMgr.a();
	}

	public void applyGravity() {
		return;
		/*
		if (Citizens.initialized
				&& chunkLoaded()
				&& (!this.onGround || ((Player) this.getBukkitEntity())
						.getEyeLocation().getY() % 1 <= 0.62)) {
			// onGround only checks if they're at least below 0.62 above it ->
			// need to check if they actually are standing on the block.
			// TODO: fix this, as it's broken -- need a good way to do it.
		}
		*/
	}

	private boolean canAttack(Entity entity) {
		double distance = entity.getLocation().distance(
				this.bukkitEntity.getLocation());
		ItemStack inhand = ((Player) this.getBukkitEntity()).getItemInHand();
		boolean inRange;
		boolean bow = inhand != null && inhand.getType() == Material.BOW;

		if (bow) {
			inRange = distance > Settings.getDouble("MinArrowRange")
					&& distance < Settings.getDouble("MaxArrowRange");
		} else {
			net.minecraft.server.Entity handle = ((CraftEntity) entity)
					.getHandle();
			inRange = distance < 1.75F
					&& handle.boundingBox.e > this.boundingBox.b
					&& handle.boundingBox.b < this.boundingBox.e;
		}
		return this.attackTicks <= 0 && inRange;
	}

	@SuppressWarnings("unused")
	private boolean chunkLoaded() {
		return this.bukkitEntity.getWorld().isChunkLoaded(
				this.getBukkitEntity().getLocation().getBlockX() >> 4,
				this.getBukkitEntity().getLocation().getBlockZ() >> 4);
	}

	@Override
	public Entity getBukkitEntity() {
		if (this.bukkitEntity == null) {
			NPCSpawner.delayedRemove(this.name);
		}
		return super.getBukkitEntity();
	}

	public float getYawDifference(double diffZ, double diffX) {
		float vectorYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float diffYaw = vectorYaw - this.yaw;

		for (this.aU = this.aY; diffYaw < -180.0F; diffYaw += 360.0F) {
		}
		while (diffYaw >= 180.0F) {
			diffYaw -= 360.0F;
		}
		return Math.max(-30F, Math.min(30, diffYaw));
	}

	public boolean hasTarget() {
		return this.targetEntity != null;
	}

	@Override
	public void jump() {
		jump(false);
	}

	public void jump(boolean force) {
		if (this.onGround || force) {
			this.motY = 0.42D + JUMP_FACTOR;
		}
	}

	@Override
	public void performAction(Animation action) {
		this.animations.performAnimation(action);
	}

	public boolean rayTrace(Entity entity) {
		return this.g(((CraftEntity) entity).getHandle());
	}

	public boolean tryAttack(Entity entity) {
		if (!canAttack(entity))
			return false;
		this.attackTicks = 20; // Possibly causes attack spam (maybe higher?).

		Player local = ((Player) this.bukkitEntity);
		ItemStack inhand = local.getItemInHand();
		if (inhand != null
				&& inhand.getType() == Material.BOW
				&& entity.getLocation().distance(
						this.bukkitEntity.getLocation()) >= Settings
						.getDouble("MinArrowRange")) {
			NPCManager.faceEntity(this.getBukkitEntity(), entity);

			// make inaccuracies.
			boolean up = this.random.nextBoolean();
			this.yaw += this.random.nextInt(5) * (up ? 1 : -1);

			up = this.random.nextBoolean();
			this.pitch += this.random.nextInt(5) * (up ? 1 : -1);

			((Player) this.bukkitEntity).shootArrow();
		} else {
			this.performAction(Animation.SWING_ARM);
			LivingEntity other = (LivingEntity) entity;

			int damage = this.inventory.a(((CraftEntity) entity).getHandle());
			other.damage(damage);
		}
		return true;
	}

	@Override
	public void walk(float x, float z) {
		this.a(x, z);
	}

	@Override
	public void walkOnCurrentHeading() {
		walk(this.aT, this.aU);
	}

	private static final double JUMP_FACTOR = 0.07D;
}