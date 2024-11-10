package net.reaper.vanimals.common.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.reaper.vanimals.common.entity.base.AbstractAnimal;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.util.EntityUtils;
import net.reaper.vanimals.util.TickUtils;

import java.util.EnumSet;

public class RamAtTargetGoal extends Goal {
    protected final AbstractAnimal animal;
    private final float speedModifier;
    protected float reachDistance = 0.2F;
    protected Vec3 direction;
    private int runTicks;

    public RamAtTargetGoal(AbstractAnimal pAnimal, float pSpeedModifier) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.animal = pAnimal;
        this.speedModifier = pSpeedModifier;
    }

    public RamAtTargetGoal setReachDistance(float pReachDistance) {
        this.reachDistance = pReachDistance;
        return this;
    }

    private boolean isSafePath(BlockPos pBlockPos) {
        BlockState blockState = this.animal.level().getBlockState(pBlockPos.below());
        return !blockState.isAir() && !blockState.is(Blocks.WATER);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.animal.getTarget();
        if (this.animal.isVehicle() && this.animal.getAttackStrategy() != AbstractAnimal.AttackStrategy.RAM) {
            return false;
        }
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        super.start();
        LivingEntity target = this.animal.getTarget();
        if (target != null) {
            this.direction = target.position().subtract(this.animal.position()).normalize();
            this.runTicks = 65;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.animal.getTarget();
        if (target != null) {
            --this.runTicks;
            this.animal.setSprinting(true);
            this.animal.attackEntitiesInFront(0.35F, 1.3F, entity -> {
                this.runTicks = 0;
                if (this.animal instanceof BisonEntity bison) {
                    bison.knockBack(entity);
                }
            });
            if (this.direction != null) {
                BlockPos nextPos = this.animal.blockPosition().offset((int) Math.round(this.direction.x), 0, (int) Math.round(this.direction.z));
                if (!this.isSafePath(nextPos)) {
                    this.runTicks = 0;
                    return;
                }
                double angle = Math.atan2(this.direction.z, this.direction.x);
                this.animal.setYRot((float) Math.toDegrees(angle) - 90);
                this.animal.setDeltaMovement(this.direction.x * this.speedModifier, this.animal.getDeltaMovement().y, this.direction.z * this.speedModifier);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.animal.getTarget() != null && this.runTicks > 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.animal.setAttackStrategy(AbstractAnimal.AttackStrategy.MELEE);
        this.animal.setSprinting(false);
    }
}
