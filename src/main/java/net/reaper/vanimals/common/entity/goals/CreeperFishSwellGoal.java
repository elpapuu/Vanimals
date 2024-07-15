package net.reaper.vanimals.common.entity.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.reaper.vanimals.common.entity.water.CreeperfishEntity;

public class CreeperFishSwellGoal extends Goal {
    private final CreeperfishEntity creeperfish;
    @Nullable
    private LivingEntity target;

    public CreeperFishSwellGoal(CreeperfishEntity p_25919_) {
        this.creeperfish = p_25919_;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        LivingEntity $$0 = this.creeperfish.getTarget();
        return this.creeperfish.getSwellDir() > 0 || $$0 != null && this.creeperfish.distanceToSqr($$0) < 9.0;
    }

    public void start() {
        this.creeperfish.getNavigation().stop();
        this.target = this.creeperfish.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.target == null) {
            this.creeperfish.setSwellDir(-1);
        } else if (this.creeperfish.distanceToSqr(this.target) > 49.0) {
            this.creeperfish.setSwellDir(-1);
        } else if (!this.creeperfish.getSensing().hasLineOfSight(this.target)) {
            this.creeperfish.setSwellDir(-1);
        } else {
            this.creeperfish.setSwellDir(1);
        }
    }
}