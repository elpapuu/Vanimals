package net.reaper.vanimals.common.entity.water;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.reaper.vanimals.common.entity.goals.CreeperFishSwellGoal;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;

public class CreeperfishEntity extends WaterAnimal {
    public CreeperfishEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SmoothSwimmingMoveControl(this, 130, 35, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR;
    private static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(CreeperfishEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED;
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED;
    public final AnimationState flopAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int oldSwell;
    private int swell;
    private int maxSwell = 20;
    private int explosionRadius = 3;

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        if (!this.isInWater()) {
            if (this.idleAnimationState.isStarted())
                this.idleAnimationState.stop();
            this.flopAnimationState.startIfStopped(this.tickCount);
            } else {
                this.flopAnimationState.stop();
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


    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 3, 3));
        this.goalSelector.addGoal(2, new CreeperFishSwellGoal(this));
        this.goalSelector.addGoal(2, new CreeperFishSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(this, Dolphin.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(this, Pufferfish.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 15D)
                .add(Attributes.FOLLOW_RANGE, 43D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 2f);
    }


    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        boolean $$3 = super.causeFallDamage(p_149687_, p_149688_, p_149689_);
        this.swell += (int)(p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return $$3;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, false);
        this.entityData.define(DATA_IS_IGNITED, false);
        this.entityData.define(JUMPING, false);
    }

    public void addAdditionalSaveData(CompoundTag p_32304_) {
        super.addAdditionalSaveData(p_32304_);
        if ((Boolean)this.entityData.get(DATA_IS_POWERED)) {
            p_32304_.putBoolean("powered", true);
        }

        p_32304_.putShort("Fuse", (short)this.maxSwell);
        p_32304_.putByte("ExplosionRadius", (byte)this.explosionRadius);
        p_32304_.putBoolean("ignited", this.isIgnited());
    }

    public void readAdditionalSaveData(CompoundTag p_32296_) {
        super.readAdditionalSaveData(p_32296_);
        this.entityData.set(DATA_IS_POWERED, p_32296_.getBoolean("powered"));
        if (p_32296_.contains("Fuse", 99)) {
            this.maxSwell = p_32296_.getShort("Fuse");
        }

        if (p_32296_.contains("ExplosionRadius", 99)) {
            this.explosionRadius = p_32296_.getByte("ExplosionRadius");
        }

        if (p_32296_.getBoolean("ignited")) {
            this.ignite();
        }

    }

    public void tick() {
        {
            super.tick();

            if(this.level().isClientSide()) {
                setupAnimationStates();
            }
        }
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int $$0 = this.getSwellDir();
            if ($$0 > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += $$0;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }

        super.tick();
    }

    public void setTarget(@Nullable LivingEntity p_149691_) {
        if (!(p_149691_ instanceof Goat)) {
            super.setTarget(p_149691_);
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_32309_) {
        return SoundEvents.CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    protected void dropCustomDeathLoot(DamageSource p_32292_, int p_32293_, boolean p_32294_) {
        super.dropCustomDeathLoot(p_32292_, p_32293_, p_32294_);
        Entity $$3 = p_32292_.getEntity();
        if ($$3 != this && $$3 instanceof Creeper $$4) {
            if ($$4.canDropMobsSkull()) {
                $$4.increaseDroppedSkulls();
                this.spawnAtLocation(Items.CREEPER_HEAD);
            }
        }

    }

    public boolean doHurtTarget(Entity p_32281_) {
        return true;
    }

    public float getSwelling(float p_32321_) {
        return Mth.lerp(p_32321_, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return (Integer)this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int p_32284_) {
        this.entityData.set(DATA_SWELL_DIR, p_32284_);
    }

    public void thunderHit(ServerLevel p_32286_, LightningBolt p_32287_) {
        super.thunderHit(p_32286_, p_32287_);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    protected InteractionResult mobInteract(Player p_32301_, InteractionHand p_32302_) {
        ItemStack $$2 = p_32301_.getItemInHand(p_32302_);
        if ($$2.is(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent $$3 = $$2.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.level().playSound(p_32301_, this.getX(), this.getY(), this.getZ(), $$3, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level().isClientSide) {
                this.ignite();
                if (!$$2.isDamageableItem()) {
                    $$2.shrink(1);
                } else {
                    $$2.hurtAndBreak(1, p_32301_, (p_32290_) -> {
                        p_32290_.broadcastBreakEvent(p_32302_);
                    });
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(p_32301_, p_32302_);
        }
    }

    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius, Level.ExplosionInteraction.MOB);
            this.discard();
            this.spawnLingeringCloud();
        }

    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> $$0 = this.getActiveEffects();
        if (!$$0.isEmpty()) {
            AreaEffectCloud $$1 = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            $$1.setRadius(2.5F);
            $$1.setRadiusOnUse(-0.5F);
            $$1.setWaitTime(10);
            $$1.setDuration($$1.getDuration() / 2);
            $$1.setRadiusPerTick(-$$1.getRadius() / (float)$$1.getDuration());
            Iterator var3 = $$0.iterator();

            while(var3.hasNext()) {
                MobEffectInstance $$2 = (MobEffectInstance)var3.next();
                $$1.addEffect(new MobEffectInstance($$2));
            }

            this.level().addFreshEntity($$1);
        }

    }
    public boolean isJumping() {
        return this.entityData.get(JUMPING);
    }

    public void setJumping(boolean jumping) {
        this.entityData.set(JUMPING, jumping);
    }
    public class CreeperFishJumpGoal extends JumpGoal {
        private static final int[] STEPS_TO_CHECK = new int[]{0, 1, 4, 5, 6, 7};
        private final CreeperfishEntity dolphin;
        private final int interval;
        private boolean breached;

        public CreeperFishJumpGoal(CreeperfishEntity p_25168_, int p_25169_) {
            this.dolphin = p_25168_;
            this.interval = reducedTickDelay(p_25169_);
        }

        public boolean canUse() {
            if (this.dolphin.getRandom().nextInt(this.interval) != 0) {
                return false;
            } else {
                Direction $$0 = this.dolphin.getMotionDirection();
                int $$1 = $$0.getStepX();
                int $$2 = $$0.getStepZ();
                BlockPos $$3 = this.dolphin.blockPosition();
                int[] var5 = STEPS_TO_CHECK;
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    int $$4 = var5[var7];
                    if (!this.waterIsClear($$3, $$1, $$2, $$4) || !this.surfaceIsClear($$3, $$1, $$2, $$4)) {
                        return false;
                    }
                }

                return true;
            }
        }

        private boolean waterIsClear(BlockPos p_25173_, int p_25174_, int p_25175_, int p_25176_) {
            BlockPos $$4 = p_25173_.offset(p_25174_ * p_25176_, 0, p_25175_ * p_25176_);
            return this.dolphin.level().getFluidState($$4).is(FluidTags.WATER) && !this.dolphin.level().getBlockState($$4).blocksMotion();
        }

        private boolean surfaceIsClear(BlockPos p_25179_, int p_25180_, int p_25181_, int p_25182_) {
            return this.dolphin.level().getBlockState(p_25179_.offset(p_25180_ * p_25182_, 1, p_25181_ * p_25182_)).isAir() && this.dolphin.level().getBlockState(p_25179_.offset(p_25180_ * p_25182_, 2, p_25181_ * p_25182_)).isAir();
        }

        public boolean canContinueToUse() {
            double $$0 = this.dolphin.getDeltaMovement().y;
            return (!($$0 * $$0 < 0.029999999329447746) || this.dolphin.getXRot() == 0.0F || !(Math.abs(this.dolphin.getXRot()) < 10.0F) || !this.dolphin.isInWater()) && !this.dolphin.onGround();
        }

        public boolean isInterruptable() {
            return false;
        }

        public void start() {
            Direction $$0 = this.dolphin.getMotionDirection();
            this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add((double)$$0.getStepX() * 0.6, 0.7, (double)$$0.getStepZ() * 0.6));
            this.dolphin.getNavigation().stop();
        }

        public void stop() {
            this.dolphin.setXRot(0.0F);
        }

        public void tick() {
            boolean $$0 = this.breached;
            if (!$$0) {
                FluidState $$1 = this.dolphin.level().getFluidState(this.dolphin.blockPosition());
                this.breached = $$1.is(FluidTags.WATER);
            }

            if (this.breached && !$$0) {
                this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
            }

            Vec3 $$2 = this.dolphin.getDeltaMovement();
            if ($$2.y * $$2.y < 0.029999999329447746 && this.dolphin.getXRot() != 0.0F) {
                this.dolphin.setXRot(Mth.rotLerp(0.2F, this.dolphin.getXRot(), 0.0F));
            } else if ($$2.length() > 9.999999747378752E-6) {
                double $$3 = $$2.horizontalDistance();
                double $$4 = Math.atan2(-$$2.y, $$3) * 57.2957763671875;
                this.dolphin.setXRot((float)$$4);
            }

        }
    }

    public boolean isIgnited() {
        return (Boolean)this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    static {
        DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
        DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    }
}
