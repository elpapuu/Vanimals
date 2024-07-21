package net.reaper.vanimals.common.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.reaper.vanimals.common.entity.ground.BisonEntity;

import java.util.Random;

public class DisablePlayerShieldGoal extends Goal {

    private final BisonEntity entity;
    private final Random random = new Random();

    public DisablePlayerShieldGoal(BisonEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target instanceof Player && this.entity.isAttacking();
    }

    @Override
    public void start() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            // Randomly disable the player's shield
            if (random.nextBoolean()) {
                ((Player) target).disableShield(true);
            }
        }
    }
}