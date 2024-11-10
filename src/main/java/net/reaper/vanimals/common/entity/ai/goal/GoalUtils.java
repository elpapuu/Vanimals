package net.reaper.vanimals.common.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class GoalUtils {
    @Contract("null->false")
    public static boolean canAttackPlayer(@Nullable LivingEntity pEntity) {
        if (pEntity == null) {
            return false;
        }
        return pEntity instanceof Player player && (player.isSpectator() || player.isCreative());
    }
}
