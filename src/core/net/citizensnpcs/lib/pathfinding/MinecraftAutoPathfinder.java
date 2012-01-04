package net.citizensnpcs.lib.pathfinding;

import java.util.Random;

import net.citizensnpcs.lib.CraftNPC;
import net.minecraft.server.MathHelper;

import org.bukkit.Location;

public class MinecraftAutoPathfinder implements AutoPathfinder {
	private final Random random = new Random();

	@Override
	public void find(CraftNPC npc) {
		if (random.nextInt(70) != 0 || random.nextInt(70) != 0)
			return;
		boolean flag = false;
		int x = -1, y = -1, z = -1;
		double pathWeight = -99999.0F;
		for (int l = 0; l < 10; ++l) {
			int x2 = MathHelper
					.floor(npc.locX + this.random.nextInt(13) - 6.0D);
			int y2 = MathHelper.floor(npc.locY + this.random.nextInt(7) - 3.0D);
			int z2 = MathHelper
					.floor(npc.locZ + this.random.nextInt(13) - 6.0D);
			double tempPathWeight = 0.5 - npc.world.m(x2, y2, z2);

			if (tempPathWeight > pathWeight) {
				pathWeight = tempPathWeight;
				x = x2;
				y = y2;
				z = z2;
				flag = true;
			}
		}
		if (flag)
			npc.getPathController().pathTo(
					new Location(npc.getBukkitEntity().getWorld(), x, y, z));
	}
}
