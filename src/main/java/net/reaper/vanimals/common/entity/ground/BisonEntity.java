package net.reaper.vanimals.common.entity.ground;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.reaper.vanimals.core.init.ModEntities;
import net.reaper.vanimals.core.init.ModSounds;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BisonEntity extends Animal implements ItemSteerable, Saddleable{

    public BisonEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    }
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private final ItemBasedSteering steering;


    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
        if (level().isClientSide()) {
            this.idleAnimationState.animateWhen(!isInWaterOrBubble() && !this.walkAnimation.isMoving(), this.tickCount);
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;

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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(1, new BisonPanicGoal(1.5));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.APPLE), false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));

        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.MOVEMENT_SPEED, 0.12D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 2f);
    }

    protected float getStandingEyeHeight(Pose p_28295_, EntityDimensions p_28296_) {
        return this.isBaby() ? p_28296_.height * 0.95F : 1.3F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.BISON.get().create(pLevel);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.APPLE);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(2) == 0 ? ModSounds.BISON_IDLE.get() : ModSounds.BISON_IDLE2.get();
    }

    public InteractionResult mobInteract(Player p_28298_, InteractionHand p_28299_) {
        ItemStack $$2 = p_28298_.getItemInHand(p_28299_);
        boolean flag = this.isFood(p_28298_.getItemInHand(p_28299_));
        if ($$2.is(Items.BUCKET) && !this.isBaby()) {
            p_28298_.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack $$3 = ItemUtils.createFilledResult($$2, p_28298_, Items.MILK_BUCKET.getDefaultInstance());
            p_28298_.setItemInHand(p_28299_, $$3);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (!flag && this.isSaddled() && !this.isVehicle() && !p_28298_.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                p_28298_.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            InteractionResult interactionresult = super.mobInteract(p_28298_, p_28299_);
            if (!interactionresult.consumesAction()) {
                ItemStack itemstack = p_28298_.getItemInHand(p_28299_);
                return itemstack.is(Items.SADDLE) ? itemstack.interactLivingEntity(p_28298_, this, p_28299_) : InteractionResult.PASS;
            } else {
                return interactionresult;
            }
        }

    }

    @javax.annotation.Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof Player) {
                Player player = (Player)entity;
                if (player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(Items.CARROT_ON_A_STICK)) {
                    return player;
                }
            }
        }

        return null;
    }
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_29480_) {
        if (DATA_BOOST_TIME.equals(p_29480_) && this.level().isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(p_29480_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    public void addAdditionalSaveData(CompoundTag p_29495_) {
        super.addAdditionalSaveData(p_29495_);
        this.steering.addAdditionalSaveData(p_29495_);
    }

    public void readAdditionalSaveData(CompoundTag p_29478_) {
        super.readAdditionalSaveData(p_29478_);
        this.steering.readAdditionalSaveData(p_29478_);
    }
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(@javax.annotation.Nullable SoundSource p_29476_) {
        this.steering.setSaddle(true);
        if (p_29476_ != null) {
            this.level().playSound((Player)null, this, SoundEvents.PIG_SADDLE, p_29476_, 0.5F, 1.0F);
        }

    }
    public Vec3 getDismountLocationForPassenger(LivingEntity p_29487_) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] aint = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            UnmodifiableIterator var6 = p_29487_.getDismountPoses().iterator();

            while (var6.hasNext()) {
                Pose pose = (Pose) var6.next();
                AABB aabb = p_29487_.getLocalBoundsForPose(pose);
                int[][] var9 = aint;
                int var10 = aint.length;

                for (int var11 = 0; var11 < var10; ++var11) {
                    int[] aint1 = var9[var11];
                    blockpos$mutableblockpos.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos$mutableblockpos, d0);
                        if (DismountHelper.canDismountTo(this.level(), p_29487_, aabb.move(vec3))) {
                            p_29487_.setPose(pose);
                            return vec3;
                        }
                    }
                }
            }

        }
        return super.getDismountLocationForPassenger(p_29487_);
    }
    protected void tickRidden(Player p_278330_, Vec3 p_278267_) {
        super.tickRidden(p_278330_, p_278267_);
        this.setRot(p_278330_.getYRot(), p_278330_.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.steering.tickBoost();
    }

    protected Vec3 getRiddenInput(Player p_278309_, Vec3 p_275479_) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    protected float getRiddenSpeed(Player p_278258_) {
        return (float)(this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1 * (double)this.steering.boostFactor());
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource pDamageSource) {
        return this.random.nextInt(2) == 0 ? ModSounds.BISON_HURT.get() : ModSounds.BISON_HURT2.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.random.nextInt(2) == 0 ? ModSounds.BISON_DEATH.get() : ModSounds.BISON_DEATH2.get();
    }

    class BisonPanicGoal extends PanicGoal {
        public BisonPanicGoal(double p_203124_) {
            super(BisonEntity.this, p_203124_);
        }

        protected boolean shouldPanic() {
            return this.mob.isBaby() & this.mob.hurtMarked || this.mob.isOnFire() || this.mob.isFreezing();
        }
    }
    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.INT);
    }
}