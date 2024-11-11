package net.reaper.vanimals.common.entity.ground;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.reaper.vanimals.client.input.InputKey;
import net.reaper.vanimals.client.input.KeyPressType;
import net.reaper.vanimals.client.util.IDynamicCamera;
import net.reaper.vanimals.client.util.IShakeScreenOnStep;
import net.reaper.vanimals.common.entity.ai.behavior.DietBuilder;
import net.reaper.vanimals.common.entity.ai.goal.RamAtTargetGoal;
import net.reaper.vanimals.common.entity.base.AbstractAnimal;
import net.reaper.vanimals.common.entity.goals.DisablePlayerShieldGoal;
import net.reaper.vanimals.core.init.VEntityTypes;
import net.reaper.vanimals.core.init.VItems;
import net.reaper.vanimals.core.init.VSoundEvents;
import net.reaper.vanimals.util.EntityUtils;
import net.reaper.vanimals.util.TickUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class BisonEntity extends AbstractAnimal implements ItemSteerable, Saddleable, IDynamicCamera, IShakeScreenOnStep {

    public BisonEntity(EntityType<? extends AbstractAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
        this.setMaxUpStep(1.0F);
        this.xpReward = 20;
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0F);
    }

    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState stunnedAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;
    private int stunnedAnimationTimeout = 0;
    private final ItemBasedSteering steering;
    private int attackTick;
    private int stunnedTick;
    private int roarTick;
    private int shieldDamageCounter = 0;


    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!isInWaterOrBubble() && !this.walkAnimation.isMoving(), this.tickCount);
            this.stunnedAnimationState.animateWhen((this.stunnedTick > 0) && !this.isUnderWater(), this.tickCount);
            this.attackAnimationState.animateWhen((this.attackTick > 0) && this.isAttacking(), this.tickCount);
            this.handleScreenShake();
        } else {
            LivingEntity target = this.getTarget();
            if (target != null) {
                if (AbstractAnimal.RAM_COOLDOWN.get(this) <= 0 && this.distanceTo(target) >= 4.5F) {
                    AbstractAnimal.RAM_COOLDOWN.set(this, 150);
                    this.setAttackStrategy(AttackStrategy.RAM);
                }
            }
        }
    }

    @Override
    protected void setupAnimations() {
        //Idle Animation
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        //Stunned animation
        if (this.stunnedTick > 0 && !this.isUnderWater()) {
            if (this.idleAnimationState.isStarted()) {
                this.idleAnimationState.stop();
            }
            if (this.attackAnimationState.isStarted()) {
                this.attackAnimationState.stop();
            }
            this.stunnedAnimationTimeout = 20;
            this.stunnedAnimationState.start(this.tickCount);
        } else {
            --this.stunnedAnimationTimeout;
        }
        if (this.stunnedTick <= 0 || this.isUnderWater()) {
            this.stunnedAnimationState.stop();
        }
        //Attack animation
        if (this.attackTick > 0 && this.isAttacking()) {
            this.attackAnimationTimeout = 20;
            this.attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if (this.attackTick <= 0 && !this.isAttacking()) {
            this.attackAnimationState.stop();
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (damageSource.getDirectEntity() instanceof Projectile && damageSource.getEntity() instanceof Player) {
            Player player = (Player) damageSource.getEntity();
            ItemStack shield = player.getMainHandItem();

            // Check if the target is holding a shield
            if (shield.getItem() instanceof ShieldItem) {
                shieldDamageCounter++; // Increase shield damage counter
                if (shieldDamageCounter >= 2) {
                    // Shield breaks after 3 attacks
                    shield.shrink(1); // Break the shield
                    shieldDamageCounter = 0; // Reset the counter
                }
            }
        }
        return super.hurt(damageSource, amount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity pTarget) {
                double reach = BisonEntity.this.getBbWidth() * 1.8;
                return reach * reach + pTarget.getBbWidth();
            }

            @Override
            public boolean canUse() {
                return super.canUse() && BisonEntity.this.getAttackStrategy() == AttackStrategy.MELEE;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && BisonEntity.this.getAttackStrategy() == AttackStrategy.MELEE;
            }
        });
        this.goalSelector.addGoal(1, new RamAtTargetGoal(this, 0.3F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F) {
            @Override
            protected boolean shouldPanic() {
                return super.shouldPanic() && this.mob.isBaby() && DamageSource.class.desiredAssertionStatus();
            }
        });
        this.goalSelector.addGoal(1, new DisablePlayerShieldGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.APPLE, Items.WHEAT, VItems.APPLE_ON_A_STICK.get()), false));
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
                .add(Attributes.MAX_HEALTH, 45D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ATTACK_SPEED, 0.1D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 3f);
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
    protected DietBuilder createDiet() {
        return null;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pItemStack) {
        return pItemStack.is(Items.APPLE) || pItemStack.is(Items.WHEAT);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(2) == 0 ? VSoundEvents.BISON_IDLE.get() : VSoundEvents.BISON_IDLE2.get();
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

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof Player player) {
                if (EntityUtils.checkItemsInHands(player, item -> item == VItems.APPLE_ON_A_STICK.get())) {
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

    protected float getRiddenSpeed(Player p_278258_) {
        return (float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1 * (double) this.steering.boostFactor());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.getEntityData().define(IS_ATTACKING, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag p_29495_) {
        super.addAdditionalSaveData(p_29495_);
        this.steering.addAdditionalSaveData(p_29495_);
        p_29495_.putInt("AttackTick", this.attackTick);
        p_29495_.putInt("StunTick", this.stunnedTick);
        p_29495_.putInt("RoarTick", this.roarTick);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag p_29478_) {
        super.readAdditionalSaveData(p_29478_);
        this.steering.readAdditionalSaveData(p_29478_);
        this.attackTick = p_29478_.getInt("AttackTick");
        this.stunnedTick = p_29478_.getInt("StunTick");
        this.roarTick = p_29478_.getInt("RoarTick");
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource pSoundSource) {
        this.steering.setSaddle(true);
        if (pSoundSource != null) {
            this.level().playSound(null, this, SoundEvents.HORSE_SADDLE, pSoundSource, 0.5F, 1.0F);
        }
    }

    @Override
    protected float[] getInputSpeed(@NotNull Player pPlayer) {
        return new float[]{0.0F, 0.0F, this.isSprinting() ? 0.8F : 0.4F};
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.3F;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource pDamageSource) {
        return this.random.nextInt(2) == 0 ? VSoundEvents.BISON_HURT.get() : VSoundEvents.BISON_HURT2.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.random.nextInt(2) == 0 ? VSoundEvents.BISON_DEATH.get() : VSoundEvents.BISON_DEATH2.get();
    }

    @Override
    public boolean boost() {
        return true;
    }

    @Override
    public boolean isRideable() {
        return true;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        switch (pId) {
            case 4:
                this.attackTick = 10;
                this.playSound(VSoundEvents.BISON_ATTACK.get(), 1.0F, 1.0F);
                break;
            case 39:
                this.stunnedTick = 40;
                break;
            default:
                super.handleEntityEvent(pId);
        }
    }

    public void aiStep() {
        super.aiStep();

        if (this.isAlive()) {

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
                    this.playSound(VSoundEvents.BISON_ROAR.get(), 1.0F, 1.0F);
                }
            }
        }


        super.aiStep();
    }

    private float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
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

    public boolean doHurtTarget(@NotNull Entity pTarget) {
        this.attackTick = 10;
        this.level().broadcastEntityEvent(this, (byte) 4);
        float $$1 = this.getAttackDamage();
        float $$2 = (int) $$1 > 0 ? $$1 / 2.0F + (float) this.random.nextInt((int) $$1) : $$1;
        boolean $$3 = pTarget.hurt(this.damageSources().mobAttack(this), $$2);
        if ($$3) {
            double var10000;
            if (pTarget instanceof LivingEntity) {
                LivingEntity $$4 = (LivingEntity) pTarget;
                var10000 = $$4.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            } else {
                var10000 = 0.0;
            }

            double $$5 = var10000;
            double $$6 = Math.max(0.0, 1.0 - $$5);
            pTarget.setDeltaMovement(pTarget.getDeltaMovement().add(0.0, 0.4000000059604645 * $$6, 0.0));
            this.doEnchantDamageEffects(this, pTarget);
        }
        if (this.getRandom().nextFloat() < 0.5F) {
            this.cooldownShield(pTarget, 60);
        }
        this.playSound(VSoundEvents.BISON_ATTACK_2.get(), 1.0F, 1.0F);
        return $$3;
    }

    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d0 = this.getX() - (double) this.getBbWidth() * Math.sin((double) (this.yBodyRot * 0.017453292F)) + (this.random.nextDouble() * 0.6 - 0.3);
            double d1 = this.getY() + (double) this.getBbHeight() - 0.3;
            double d2 = this.getZ() + (double) this.getBbWidth() * Math.cos((double) (this.yBodyRot * 0.017453292F)) + (this.random.nextDouble() * 0.6 - 0.3);
            this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, d0, d1, d2, 0.4980392156862745, 0.5137254901960784, 0.5725490196078431);
        }

    }

    @Override
    protected void blockedByShield(@NotNull LivingEntity pEntity) {
        if (this.roarTick == 0) {
            if (this.random.nextDouble() < 0.5) {
                this.stunnedTick = 50;
                this.playSound(VSoundEvents.BISON_ATTACK_2.get(), 1.0F, 1.0F);
                this.level().broadcastEntityEvent(this, (byte) 39);
                pEntity.push(this);
            }
            pEntity.hurtMarked = true;
        }

    }

    public void knockBack(@Nullable Entity pEntity) {
        if (pEntity == null) {
            return;
        }
        double yRot = Math.toRadians(this.getYRot());
        double dirX = -Math.sin(yRot);
        double dirZ = Math.cos(yRot);
        Vec3 vec3 = new Vec3(dirX, 0.0F, dirZ).normalize().scale(1.9F);
        pEntity.setDeltaMovement(vec3.x, 1.0F, vec3.z);
    }

    @Override
    public void onServerInput(InputKey pInputKey, KeyPressType pKeyPressType) {
        if (pKeyPressType == KeyPressType.HOLD && pInputKey == InputKey.CTRL) {
            this.attackEntitiesInFront(0.25F, 0.6F, this::knockBack);
            Vec3 headPosition = this.position().add(0, this.getEyeHeight(), 0);
            Vec3 lookDirection = this.getLookAngle().normalize();
            Vec3 headWithOffset = headPosition.add(lookDirection.scale(3.5F));
            float yaw = this.getYRot();
            float pitch = this.getXRot();
            Vec3 direction = new Vec3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)), -Math.sin(Math.toRadians(pitch)), Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))).normalize();
            if (pitch > 30.0F) {
                Vec3 targetPosition = headWithOffset.add(direction.scale(2.8F));
                BlockHitResult hitResult = this.level().clip(new ClipContext(headWithOffset, targetPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos targetPos = hitResult.getBlockPos();
                    BlockState targetBlockState = this.level().getBlockState(targetPos);
                    if (targetBlockState.is(Blocks.DIRT) || targetBlockState.is(Blocks.GRASS_BLOCK)) {
                        this.level().playSound(this, targetPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 0.7F);
                        this.level().setBlock(targetPos, Blocks.FARMLAND.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 2.0F, 1.0F);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.5F);
    }


    @Override
    public float getMaxCameraTilt() {
        return 10.0F;
    }

    @Override
    public float getCameraTiltSpeed() {
        return 3.0F;
    }

    @Override
    public boolean canEntityShake(@NotNull LivingEntity pEntity) {
        return IShakeScreenOnStep.super.canEntityShake(pEntity) && EntityUtils.isEntityMoving(this, 0.08F);
    }

    @Override
    public float getShakePower() {
        return 0.2F;
    }

    @Override
    public float getShakeFrequency() {
        return 1.0F;
    }

    @Override
    public float getShakeDistance() {
        return 5.0F;
    }

    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.INT);
    }
}