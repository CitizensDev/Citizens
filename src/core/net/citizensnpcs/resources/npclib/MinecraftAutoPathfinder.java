package net.citizensnpcs.resources.npclib;

import java.util.Random;

import net.minecraft.server.v1_5_R3.MathHelper;
import net.minecraft.server.v1_5_R3.PathEntity;

public class MinecraftAutoPathfinder implements AutoPathfinder {
    private final Random random = new Random();

    @Override
    public PathEntity find(PathNPC npc) {
        if (random.nextInt(70) != 0 || random.nextInt(70) != 0)
            return null;
        boolean flag = false;
        int x = -1, y = -1, z = -1;
        double pathWeight = -99999.0F;
        for (int l = 0; l < 10; ++l) {
            int x2 = MathHelper.floor(npc.locX + this.random.nextInt(13) - 6.0D);
            int y2 = MathHelper.floor(npc.locY + this.random.nextInt(7) - 3.0D);
            int z2 = MathHelper.floor(npc.locZ + this.random.nextInt(13) - 6.0D);
            double tempPathWeight = 0.5 - npc.world.q(x2, y2, z2);

            if (tempPathWeight > pathWeight) {
                pathWeight = tempPathWeight;
                x = x2;
                y = y2;
                z = z2;
                flag = true;
            }
        }
        return flag ? npc.createPathEntity(x, y, z) : null;
    }

    public static final AutoPathfinder INSTANCE = new MinecraftAutoPathfinder();
}
