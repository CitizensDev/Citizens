package com.fullwall.resources.redecouverte.NPClib;

import java.util.logging.Logger;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Events.NPCTargetEvent;

public class CraftNPC extends PathNPC {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");
	@SuppressWarnings("unused")
	private int lastTargetId;
	private long lastBounceTick;
	private int lastBounceId;
	boolean said = false;

	public CraftNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);

		NetworkManager netMgr = new NPCNetworkManager(new NPCSocket(),
				"npc mgr", new NetHandler() {
					@Override
					public boolean c() {
						return false;
					}
				});
		this.netServerHandler = new NPCNetHandler(minecraftserver, this, netMgr);
		// netMgr.a(this.netServerHandler);
		this.lastTargetId = -1;
		this.lastBounceId = -1;
		this.lastBounceTick = 0;
	}

	@Override
	public boolean a(EntityHuman entity) {
		EntityTargetEvent rightClickEvent = new NPCTargetEvent(
				getBukkitEntity(), entity.getBukkitEntity());
		CraftServer server = ((WorldServer) this.world).getServer();
		server.getPluginManager().callEvent(rightClickEvent);
		return super.a(entity);
	}

	@Override
	public void b(EntityHuman entity) {
		lastTargetId = entity.id;
		super.b(entity);
	}

	@Override
	public void c(Entity entity) {
		if (lastBounceId != entity.id
				|| System.currentTimeMillis() - lastBounceTick > 1000) {
			lastBounceTick = System.currentTimeMillis();
		}
		lastBounceId = entity.id;
		super.c(entity);
	}

	@Override
	public void a(Entity entity) {
		// System.out.println(entity);
		super.a(entity);
	}

	@Override
	public void a(EntityLiving entityliving) {
		// System.out.println(entityliving);
		super.a(entityliving);
	}

	public void applyGravity() {
		if (Citizens.initialized
				&& chunkLoaded()
				&& (!this.onGround || ((Player) this.getBukkitEntity())
						.getEyeLocation().getY() % 1 <= 0.62)) {
			// onGround doesn't check if the player is actually standing on a
			// block, just that they're at least below above 0.62 -> need to
			// check 0.62.
			float yaw = this.yaw;
			float pitch = this.pitch;
			this.a(0, 0);
			this.yaw = yaw;
			this.pitch = pitch;
		}
	}

	private boolean chunkLoaded() {
		return this.bukkitEntity.getWorld().isChunkLoaded(
				this.bukkitEntity.getLocation().getBlock().getChunk());
	}
}