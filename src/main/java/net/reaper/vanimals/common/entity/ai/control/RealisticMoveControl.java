package net.reaper.vanimals.common.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class RealisticMoveControl extends MoveControl {
    private final Mob mob;
    private final double turnSpeed;

    public RealisticMoveControl(Mob pMob, double pTurnSpeed) {
        super(pMob);
        this.mob = pMob;
        this.turnSpeed = pTurnSpeed;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            double dx = this.wantedX - this.mob.getX();
            double dz = this.wantedZ - this.mob.getZ();
            double distanceSquared = dx * dx + dz * dz;
            if (distanceSquared > 1e-7) {
                float targetYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
                float currentYaw = this.mob.getYRot();
                float yawDiff = Mth.wrapDegrees(targetYaw - currentYaw);
                float newYaw = (float) (currentYaw + Mth.clamp(yawDiff, -this.turnSpeed, this.turnSpeed));
                this.mob.setYRot(Mth.wrapDegrees(newYaw));
                float moveSpeed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.mob.setSpeed(moveSpeed);
                if (this.mob.getNavigation().isDone() || distanceSquared < 1e-3) {
                    this.operation = Operation.WAIT;
                }
            } else {
                this.mob.setSpeed(0.0F);
                this.operation = Operation.WAIT;
            }
        }
    }
}