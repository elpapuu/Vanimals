package net.reaper.vanimals.common.entity.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class DisableShieldGoal extends Goal {
    private final Entity entity;

    public DisableShieldGoal(Entity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public void tick() {
        List<Player> nearbyPlayers = this.entity.level().getEntitiesOfClass(Player.class, new AABB(this.entity.blockPosition()).inflate(1.0D));

        for (Player player : nearbyPlayers) {
            if (player.isBlocking()) {
                player.disableShield(true);
            }
        }
    }
}