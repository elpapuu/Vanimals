package net.reaper.vanimals.util;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class TickUtils {
    public static void doEvery(LivingEntity pEntity, int pIntervals, Runnable pAction) {
        if (pEntity.tickCount % pIntervals == 0) {
            pAction.run();
        }
    }
}
