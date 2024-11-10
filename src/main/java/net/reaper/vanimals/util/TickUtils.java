package net.reaper.vanimals.util;

import net.minecraft.world.entity.Entity;

public class TickUtils {
    public static void doEvery(Entity pEntity, int pIntervals, Runnable pAction) {
        if (pEntity.tickCount % pIntervals == 0) {
            pAction.run();
        }
    }
}
