package net.reaper.vanimals.common.entity.water;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.reaper.vanimals.common.entity.ai.behavior.DietBuilder;
import net.reaper.vanimals.common.entity.base.AbstractWaterAnimal;
import net.reaper.vanimals.util.compound.CompoundType;
import net.reaper.vanimals.util.compound.EntityData;
import org.jetbrains.annotations.NotNull;

public class NapoleonFishEntity extends AbstractWaterAnimal {
    public static final EntityData<Integer> EMOTION = new EntityData<>(NapoleonFishEntity.class, "EmotionId", CompoundType.INTEGER, 0);;

    public NapoleonFishEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setEmotion(Emotion.DEFAULT);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractWaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0F)
                .add(Attributes.FOLLOW_RANGE, 48.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 01F);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0F, 15));
    }

    @Override
    protected void setupAnimations() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 30;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f = this.getPose() == Pose.STANDING ? Math.min(pPartialTick * 6.0F, 1.0F) : 0.0F;
        this.walkAnimation.update(f, 0.2F);
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    protected DietBuilder createDiet() {
        return new DietBuilder();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        EMOTION.define(this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        EMOTION.write(this, pCompound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        EMOTION.read(this, pCompound);
    }

    public Emotion getEmotion() {
        return Emotion.values()[EMOTION.get(this)];
    }

    public void setEmotion(Emotion pEmotion) {
        EMOTION.set(this, pEmotion.ordinal());
    }

    public enum Emotion {
        DEFAULT,
        EAT,
        SCARY
    }
}
