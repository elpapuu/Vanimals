package net.reaper.vanimals.common.entity.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.reaper.vanimals.common.entity.ai.behavior.DietBuilder;
import net.reaper.vanimals.common.entity.ai.behavior.EntityCategory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class AbstractWaterAnimal extends AbstractAnimal implements Bucketable {
    public AnimationState flipAnimationState = new AnimationState();
    public int flipAnimationTimeout;
    public float currentRoll = 0.0F;
    public float tailRot;
    public float prevTailRot;

    public AbstractWaterAnimal(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SmoothSwimmingMoveControl(this,  90, 50, 0.2F, 1.0F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.navigation = new WaterBoundPathNavigation(this, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.AQUATIC;
    }

    @Override
    protected DietBuilder createDiet() {
        return null;
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.isInWater() && (this.isEffectiveAi() || this.isVehicle())) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9F));
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.calculateEntityAnimation(false);
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return type != ForgeMod.WATER_TYPE.get();
    }

    @Nonnull
    public MobType getMobType() {
        return MobType.WATER;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    protected void handleAirSupply(LivingEntity pEntity, int pAirSupply) {
        if (pEntity.isAlive() && !pEntity.isInWaterOrBubble()) {
            pEntity.setAirSupply(pAirSupply - 1);
            if (pEntity.getAirSupply() == -20) {
                pEntity.setAirSupply(0);
                pEntity.hurt(pEntity.damageSources().drown(), 2.0F);
            }
        } else {
            pEntity.setAirSupply(300);
        }
    }

    protected boolean canFlip() {
        return true;
    }

    @NotNull
    protected SoundEvent getFlopSound() {
        return SoundEvents.EMPTY;
    }

    protected float getFlipYPower() {
        return 0.2F;
    }

    protected float getMaxTailRotate() {
        return 45.0F;
    }

    @Override
    public void tick() {
        super.tick();
        this.handleAirSupply(this, this.getAirSupply());
        this.prevTailRot = this.tailRot;
        this.tailRot += (-(this.yBodyRot - this.yBodyRotO) - this.tailRot) * 0.15F;
        this.tailRot = Mth.clamp(this.tailRot, -this.getMaxTailRotate(), this.getMaxTailRotate());
    }

    @Override
    public void aiStep() {
        if (this.canFlip()) {
            if (!this.isInWater()) {
                if (this.onGround() || this.verticalCollision) {
                    this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, this.getFlipYPower(), (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
                    this.hasImpulse = true;
                    this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
                }
            }
        }
        super.aiStep();
    }

    protected boolean isBucketAble() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void saveToBucketTag(@NotNull ItemStack pItemStack) {
        if (this.isBucketAble()) {
            Bucketable.saveDefaultDataToBucketTag(this, pItemStack);
            CompoundTag compound = pItemStack.getOrCreateTag();
            compound.putInt("Age", this.getAge());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void loadFromBucketTag(@NotNull CompoundTag pCompound) {
        if (this.isBucketAble()) {
            Bucketable.loadDefaultDataFromBucketTag(this, pCompound);
            if (pCompound.contains("Age")) {
                this.setAge(pCompound.getInt("Age"));
            }
        }
    }

    @NotNull
    public ItemStack getBucketItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean fromBucket() {
        return false;
    }

    @Override
    public void setFromBucket(boolean pFromBucket) {

    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    protected InteractionResult getBucketResult(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }
}
