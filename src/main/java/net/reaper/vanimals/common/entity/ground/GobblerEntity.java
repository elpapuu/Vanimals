package net.reaper.vanimals.common.entity.ground;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.reaper.vanimals.common.entity.ai.goal.SmallerEntityTargetGoal;

import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GobblerEntity extends Animal{
    private static final EntityDataAccessor<Boolean> DIGESTING_ID = SynchedEntityData.defineId(GobblerEntity.class, EntityDataSerializers.BOOLEAN);
    public static final String DIGESTING_TAG = "DigestingTime";
    private final int TICKS_TO_DIGEST = 400; // 20 seconds
    private int ticksEnsnaring;
    private int ticksDigesting;
    private int ticksUntilHungry;
    int HUNGER_COOLDOWN = 9600; // 8 minutes
    boolean isHungry;
    // anim
    boolean isHoldingFood;
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(GobblerEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState floppingAnimationState = new AnimationState();
    public final AnimationState eatingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;

    public GobblerEntity(EntityType<? extends Animal> p_28285_, Level p_28286_) {
        super(p_28285_, p_28286_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(new ItemLike[]{Items.BEEF, Items.GLOW_BERRIES, Items.SWEET_BERRIES, Items.RABBIT, Items.TROPICAL_FISH, Items.MELON, Items.CAKE, Items.MUTTON, Items.LILY_PAD, Items.MOSS_BLOCK, Items.MOSS_CARPET}), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new SmallerEntityTargetGoal<>(this, Animal.class, false, p -> !p.isInWater() && !p.isPassenger()));
        this.targetSelector.addGoal(2, new SmallerEntityTargetGoal<>(this, Monster.class, false, p -> !p.isInWater() && !p.isPassenger() && !(p instanceof GobblerEntity)));
        this.targetSelector.addGoal(2, new SmallerEntityTargetGoal<>(this, Player.class, false, p -> !p.isInWater() && !p.isPassenger() && !(p instanceof GobblerEntity)));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.FOLLOW_RANGE, 4.0D);
    }
    @Override
    public void tick() {
        if (this.isAlive() && !this.isNoAi()) {
            if (this.level().isClientSide()) {

                setupAnimationStates();
            } else {
                if (this.getTarget() == null) {
                    if (this.isHoldingFood()) {

                        if (this.hasPassenger(entity -> entity.getBoundingBox().getSize() < this.getBoundingBox().getSize())) this.getFirstPassenger().stopRiding();
                    }
                    this.setAttacking(false);
                } else {

                    this.getLookControl().setLookAt(this.getTarget().getX(), this.getTarget().getEyeY(), this.getTarget().getZ());

                    if (this.isHoldingFood() && getTarget().getMobType() == MobType.WATER) {
                        LivingEntity prey = getTarget();
                        prey.setXRot(this.getXRot());
                        prey.setYRot(this.getYRot());
                        prey.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 5, false, false));

                        if (this.isDigesting()) {

                            --this.ticksDigesting;
                            if (this.ticksDigesting < 0) {

                                this.digest();
                            } else if (this.ticksDigesting % 100 == 0) {

                                if (!this.isSilent()) playAmbientSound();
                                prey.hurt(prey.damageSources().mobAttack(this), 0.0F);
                            }
                        } else {
                            ++this.ticksEnsnaring;
                            if (this.ticksEnsnaring >= 10) {

                                this.startDigesting(TICKS_TO_DIGEST);
                            }
                        }
                    } else {

                        this.ticksEnsnaring = -1;
                        this.ticksDigesting = -1;
                        this.setDigesting(false);
                        --this.ticksUntilHungry;
                    }
                }
                if (!this.isHungry() && this.ticksUntilHungry == 0) {

                    setHungry(true);
                }
            }
        }
        super.tick();
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (!this.isInWater()) {
            if (this.idleAnimationState.isStarted()) this.idleAnimationState.stop();
            if (this.eatingAnimationState.isStarted()) this.eatingAnimationState.stop();
            if (this.attackAnimationState.isStarted()) this.attackAnimationState.stop();
            this.floppingAnimationState.startIfStopped(this.tickCount);
        } else {
            this.floppingAnimationState.stop();
            this.eatingAnimationState.animateWhen(this.isHoldingFood(), this.tickCount);
            this.attackAnimationState.animateWhen(this.isAttacking(), this.tickCount);
        }
        // idle anim
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        // eat anim
        if (this.isHoldingFood()) {
            this.eatingAnimationState.start(this.tickCount);
            this.eatingAnimationState.startIfStopped(this.tickCount);
        } else {
            this.eatingAnimationState.stop();
        }
        if (!this.isHoldingFood()) {
            attackAnimationState.stop();
        }

        // attack anim
        if (attackAnimationTimeout <= 0 && this.isAttacking()) {

            attackAnimationTimeout = 20;
            attackAnimationState.start(this.tickCount);
        } else {

            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            attackAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }


    // thank god for the camel class
    // used for positioning "riders" as "snared" targets.
    protected void positionRider(@Nonnull Entity pPassenger, @Nonnull Entity.MoveFunction pCallback) {
        int i = this.getPassengers().indexOf(pPassenger);
        if (i >= 0) {
            boolean flag = i == 0;
            float f = 0.5F;
            if (this.getPassengers().size() > 1) {
                if (!flag) {
                    f = -0.7F;
                }

                if (pPassenger instanceof Animal) {
                    f += 0.2F;
                }
            }

            Vec3 vec3 = (new Vec3(0.0D, 0.0D, f)).yRot(-this.yBodyRot * ((float) Math.PI / 180F));
            pCallback.accept(pPassenger, this.getX() + vec3.x * 1.3F, this.getY() - 0.2F, this.getZ() + vec3.z * 1.3F);
            //this.clampRotation(pPassenger);
        }
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DIGESTING_ID, false);
        this.getEntityData().define(IS_ATTACKING, false);
    }

    public void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(DIGESTING_TAG, this.isDigesting() ? this.ticksDigesting : -1);
    }

    public void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains(DIGESTING_TAG, 99) && pCompound.getInt(DIGESTING_TAG) > -1) {
            this.startDigesting(pCompound.getInt(DIGESTING_TAG));
        }
    }

    private void startDigesting(int digestionTime) {
        this.ticksDigesting = digestionTime;
        this.setDigesting(true);
    }

    protected void digest() {
        // kills players, but discards entities to prevent item drops
        if (this.getFirstPassenger() != null) if (this.getFirstPassenger() instanceof Player) this.getFirstPassenger().kill(); else this.getFirstPassenger().discard();
        if (!this.isSilent()) {

            playAmbientSound();
        }
        handleEntityEvent((byte) 7);
        this.ticksUntilHungry = HUNGER_COOLDOWN;
        setHungry(false);
        setHoldingFood(false);
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }

    public boolean isHoldingFood() {
        return isHoldingFood;
    }

    public void setHoldingFood(boolean holdingFood) {
        isHoldingFood = holdingFood;
    }
    public boolean isDigesting() {
        return this.getEntityData().get(DIGESTING_ID);
    }

    public void setDigesting(boolean isDigesting) {
        this.entityData.set(DIGESTING_ID, isDigesting);
    }

    public boolean isAttacking() {
        return this.getEntityData().get(IS_ATTACKING);
    }

    public void setAttacking(boolean isAttacking) {
        this.entityData.set(IS_ATTACKING, isAttacking);
    }
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.TROPICAL_FISH);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_28306_) {
        return SoundEvents.COW_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    public Cow getBreedOffspring(ServerLevel p_148890_, AgeableMob p_148891_) {
        return (Cow)EntityType.COW.create(p_148890_);
    }

    protected float getStandingEyeHeight(Pose p_28295_, EntityDimensions p_28296_) {
        return this.isBaby() ? p_28296_.height * 0.95F : 1.3F;
    }
    static class MeleeGoal extends MeleeAttackGoal {
        private final GobblerEntity mob;
        LivingEntity target;

        public MeleeGoal(GobblerEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.mob = pMob;
            target = mob.getTarget();
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            // Only attacks entities its size and larger! Eats smaller ones.
            return super.canUse() && !this.mob.isHoldingFood() && this.mob.getTarget().getBoundingBox().getSize() > this.mob.getTarget().getBoundingBox().getSize();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            this.mob.setAttacking(true);
            super.checkAndPerformAttack(pEnemy, pDistToEnemySqr);
        }
    }
}
