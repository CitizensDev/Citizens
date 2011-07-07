package com.citizens.resources.npclib;

import java.util.logging.Logger;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;

import org.bukkit.entity.Player;

import com.citizens.Citizens;

public class CraftNPC extends PathNPC {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");
	@SuppressWarnings("unused")
	private final int lastTargetId;
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
	public void b(EntityHuman entity) {
		if (lastBounceId != entity.id
				|| System.currentTimeMillis() - lastBounceTick > 1000) {
			lastBounceTick = System.currentTimeMillis();
		}
		lastBounceId = entity.id;
		super.b(entity);
	}

	public void applyGravity() {
		if (Citizens.initialized
				&& chunkLoaded()
				&& (!this.onGround || ((Player) this.getBukkitEntity())
						.getEyeLocation().getY() % 1 <= 0.62)) {
			// onGround only checks if they're at least below 0.62 above it ->
			// need to check if they actually are standing on the block.
			float yaw = this.yaw;
			float pitch = this.pitch;
			this.a(0, 0); // cheap hack - move with motion of 0.
			this.yaw = yaw;
			this.pitch = pitch;
		}
	}

	private boolean chunkLoaded() {
		return this.bukkitEntity.getWorld().isChunkLoaded(
				this.bukkitEntity.getLocation().getBlock().getChunk());
	}
}