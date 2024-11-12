package net.reaper.vanimals.common.entity.ground;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.reaper.vanimals.client.util.IDynamicCamera;
import net.reaper.vanimals.client.util.IShakeScreenOnStep;
import net.reaper.vanimals.common.entity.ai.behavior.DietBuilder;
import net.reaper.vanimals.common.entity.ai.behavior.EntityCategory;
import net.reaper.vanimals.common.entity.base.AbstractAnimal;
import net.reaper.vanimals.core.init.VEntityTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestEntity extends AbstractAnimal implements ItemSteerable, Saddleable, IDynamicCamera, IShakeScreenOnStep {
    public TestEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public EntityCategory getCategory() {
        return null;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.9F));
    }

    @Override
    protected DietBuilder createDiet() {
        return null;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f = this.getPose() == Pose.STANDING ? Math.min(pPartialTick * 8.5F, 1.0F) : 0.0F;
        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pPose, @NotNull EntityDimensions pDimensions) {
        return this.isBaby() ? pDimensions.height * 0.95F : 1.3F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return VEntityTypes.BISON.get().create(pLevel);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pItemStack) {
        return pItemStack.is(Items.APPLE) || pItemStack.is(Items.WHEAT);
    }

    @Override
    public boolean boost() {
        return false;
    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    @Override
    public void equipSaddle(@Nullable SoundSource pSource) {

    }

    @Override
    public boolean isSaddled() {
        return false;
    }

    @Override
    public float getMaxCameraTilt() {
        return 0;
    }

    @Override
    public float getCameraTiltSpeed() {
        return 0;
    }

    @Override
    public float getShakePower() {
        return 0;
    }

    @Override
    public float getShakeFrequency() {
        return 0;
    }

    @Override
    public float getShakeDistance() {
        return 0;
    }
}
