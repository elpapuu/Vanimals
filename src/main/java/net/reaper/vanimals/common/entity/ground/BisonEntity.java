package net.reaper.vanimals.common.entity.ground;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.reaper.vanimals.common.entity.goals.DisableShieldGoal;
import net.reaper.vanimals.core.init.ModEntities;
import net.reaper.vanimals.core.init.ModItems;
import net.reaper.vanimals.core.init.ModSounds;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class BisonEntity extends Animal implements ItemSteerable, Saddleable {


    public BisonEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
        this.setMaxUpStep(1.0F);
        this.xpReward = 20;
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0F);
    }
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    public static final int STUN_DURATION = 40;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState stunnedAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;
    private int stunnedAnimationTimeout = 0;
    private final ItemBasedSteering steering;
    private int attackTick;
    private int stunnedTick;
    private byte p29814;
    private int roarTick;


    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
        if (level().isClientSide()) {
            this.idleAnimationState.animateWhen(!isInWaterOrBubble() && !this.walkAnimation.isMoving(), this.tickCount);
        }
        if (level().isClientSide()) {
            this.stunnedAnimationState.animateWhen((this.stunnedTick > 0) && !this.isUnderWater() , this.tickCount);
        }
        if (level().isClientSide()) {
            this.attackAnimationState.animateWhen((this.attackTick > 0) && this.isAttacking(), this.tickCount);
        }
    }

    private void setupAnimationStates() {
        //Idle Animation
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        //Stunned animation
        if (this.stunnedTick > 0 && !this.isUnderWater()) {
            if (this.idleAnimationState.isStarted()) this.idleAnimationState.stop();
            if (this.attackAnimationState.isStarted()) this.attackAnimationState.stop();
            stunnedAnimationTimeout = 20;
            stunnedAnimationState.start(this.tickCount);
        } else {
            --this.stunnedAnimationTimeout;
        }
        if (this.stunnedTick <= 0 || this.isUnderWater()) {
            stunnedAnimationState.stop();
        }

        //Attack animation
        if (this.attackTick > 0 && this.isAttacking()) {
            attackAnimationTimeout = 20;
            attackAnimationState.start(this.tickCount);
        } else {

            --this.attackAnimationTimeout;
        }
        if (this.attackTick <= 0 && !this.isAttacking()) {
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(1, new BisonPanicGoal(1.5));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(1, new DisableShieldGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.APPLE, Items.WHEAT, ModItems.APPLE_ON_A_STICK.get()), false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));

        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new BisonMeleeAttackGoal());
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 45D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ATTACK_SPEED, 0.1D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 3f);
    }
    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof Player);
        boolean flag1 = !(this.getVehicle() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, flag);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, flag);
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
        return pStack.is(Items.APPLE) || pStack.is(Items.WHEAT);
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
            }


            else {
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
                if (player.getMainHandItem().is(ModItems.APPLE_ON_A_STICK.get()) || player.getOffhandItem().is(ModItems.APPLE_ON_A_STICK.get())) {
                    return player;
                }
            }
        }

        return null;
    }
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_29480_) {
        if (DATA_SADDLE_ID.equals(p_29480_) && this.level().isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(p_29480_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.getEntityData().define(IS_ATTACKING, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    public void addAdditionalSaveData(CompoundTag p_29495_) {
        super.addAdditionalSaveData(p_29495_);
        this.steering.addAdditionalSaveData(p_29495_);
        p_29495_.putInt("AttackTick", this.attackTick);
        p_29495_.putInt("StunTick", this.stunnedTick);
        p_29495_.putInt("RoarTick", this.roarTick);
    }

    public void readAdditionalSaveData(CompoundTag p_29478_) {
        super.readAdditionalSaveData(p_29478_);
        this.steering.readAdditionalSaveData(p_29478_);
        this.attackTick = p_29478_.getInt("AttackTick");
        this.stunnedTick = p_29478_.getInt("StunTick");
        this.roarTick = p_29478_.getInt("RoarTick");
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
            this.level().playSound((Player)null, this, SoundEvents.HORSE_SADDLE, p_29476_, 0.5F, 1.0F);
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
    protected void positionRider(@Nonnull Entity pPassenger, @Nonnull Entity.MoveFunction pCallback) {
        int i = this.getPassengers().indexOf(pPassenger);
        if (i >= 0) {
            boolean flag = i == 0;
            float f = -1.1F;
            if (this.getPassengers().size() > 1) {
                if (!flag) {
                    f = 4F;
                }

                if (pPassenger instanceof Animal) {
                    f += 4F;
                }
            }

            Vec3 vec3 = (new Vec3(0.0D, 0.0D, f)).yRot(-this.yBodyRot * ((float) Math.PI / 180F));
            pCallback.accept(pPassenger, this.getX() + vec3.x * 1.1F, this.getY() + 1.62F, this.getZ() + vec3.z * 1.1F);
            //this.clampRotation(pPassenger);
        }
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

    public void handleEntityEvent(byte p_29814_) {
        p29814 = p_29814_;
        if (p_29814_ == 4) {
            this.attackTick = 10;
            this.playSound(ModSounds.BISON_ATTACK.get(), 1.0F, 1.0F);
        } else if (p_29814_ == 39) {
            this.stunnedTick = 40;
        }

        super.handleEntityEvent(p_29814_);
    }

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
                if (this.isImmobile()) {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
                } else {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
                } if (this.isAggressive()) {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
                    this.setSprinting(true);
                } else if (!this.isAggressive()) {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
                    this.setSprinting(false);

            }
                if (this.horizontalCollision && ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                    boolean flag = false;
                    AABB aabb = this.getBoundingBox().inflate(0.2);
                    Iterator var8 = BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ)).iterator();

                    label62:
                    while (true) {
                        BlockPos blockpos;
                        Block block;
                        do {
                            if (!var8.hasNext()) {
                                if (!flag && this.onGround()) {
                                    this.jumpFromGround();
                                }
                                break label62;
                            }

                            blockpos = (BlockPos) var8.next();
                            BlockState blockstate = this.level().getBlockState(blockpos);
                            block = blockstate.getBlock();
                        } while (!(block instanceof LeavesBlock));

                        flag = this.level().destroyBlock(blockpos, true, this) || flag;
                    }
                }
                if (this.attackTick > 0) {
                    --this.attackTick;
                }

                if (this.stunnedTick > 0) {
                    --this.stunnedTick;
                    this.stunEffect();
                    if (this.stunnedTick == 0) {
                        this.playSound(ModSounds.BISON_ROAR.get(), 1.0F, 1.0F);
                    }
                }
            }
        super.aiStep();
    }
    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    protected boolean isImmobile() {
        return super.isImmobile() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0;
    }

    public boolean hasLineOfSight(Entity p_149755_) {
        return this.stunnedTick <= 0 && this.roarTick <= 0 ? super.hasLineOfSight(p_149755_) : false;
    }

    public boolean isAttacking() {
        return this.getEntityData().get(IS_ATTACKING);
}

    public void setAttacking(boolean isAttacking) {
        this.entityData.set(IS_ATTACKING, isAttacking);
}
    public boolean doHurtTarget(Entity p_28837_) {
        this.attackTick = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        float $$1 = this.getAttackDamage();
        float $$2 = (int)$$1 > 0 ? $$1 / 2.0F + (float)this.random.nextInt((int)$$1) : $$1;
        boolean $$3 = p_28837_.hurt(this.damageSources().mobAttack(this), $$2);
        if ($$3) {
            double var10000;
            if (p_28837_ instanceof LivingEntity) {
                LivingEntity $$4 = (LivingEntity)p_28837_;
                var10000 = $$4.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            } else {
                var10000 = 0.0;
            }

            double $$5 = var10000;
            double $$6 = Math.max(0.0, 1.0 - $$5);
            p_28837_.setDeltaMovement(p_28837_.getDeltaMovement().add(0.0, 0.4000000059604645 * $$6, 0.0));
            this.doEnchantDamageEffects(this, p_28837_);
        }

        this.playSound(ModSounds.BISON_ATTACK_2.get(), 1.0F, 1.0F);
        return $$3;
    }
    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d0 = this.getX() - (double)this.getBbWidth() * Math.sin((double)(this.yBodyRot * 0.017453292F)) + (this.random.nextDouble() * 0.6 - 0.3);
            double d1 = this.getY() + (double)this.getBbHeight() - 0.3;
            double d2 = this.getZ() + (double)this.getBbWidth() * Math.cos((double)(this.yBodyRot * 0.017453292F)) + (this.random.nextDouble() * 0.6 - 0.3);
            this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, d0, d1, d2, 0.4980392156862745, 0.5137254901960784, 0.5725490196078431);
        }

    }
    protected void blockedByShield(LivingEntity p_33361_) {
        if (this.roarTick == 0) {
            if (this.random.nextDouble() < 0.5) {
                this.stunnedTick = 50;
                this.playSound(ModSounds.BISON_ATTACK_2.get(), 1.0F, 1.0F);
                this.level().broadcastEntityEvent(this, (byte) 39);
                p_33361_.push(this);
            } else {
                this.strongKnockback(p_33361_);
            }

            p_33361_.hurtMarked = true;
        }

    }
    private void strongKnockback(Entity p_33340_) {
        double d0 = p_33340_.getX() - this.getX();
        double d1 = p_33340_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        p_33340_.push(d0 / d2 * 4.0, 0.2, d1 / d2 * 4.0);
    }

    class BisonMeleeAttackGoal extends MeleeAttackGoal {
        public BisonMeleeAttackGoal() {
            super(BisonEntity.this, 1.0, true);
        }

        protected double getAttackReachSqr(LivingEntity p_33377_) {
            float f = BisonEntity.this.getBbWidth() - 0.1F;
            return (double) (f * 2.0F * f * 2.0F + p_33377_.getBbWidth());
        }
    }
    class BisonPanicGoal extends PanicGoal {
        public BisonPanicGoal(double p_203124_) {
            super(BisonEntity.this, p_203124_);
        }

        protected boolean shouldPanic() {
            return this.mob.isBaby() & DamageSource.class.desiredAssertionStatus() || this.mob.isOnFire() || this.mob.isFreezing();
        }

    }       public static boolean canSpawn(EntityType<BisonEntity> tEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        return Animal.checkAnimalSpawnRules(tEntityType, serverLevelAccessor, spawnType, blockPos, randomSource) && !serverLevelAccessor.getLevelData().isRaining();
    }
    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.INT);
    }
}