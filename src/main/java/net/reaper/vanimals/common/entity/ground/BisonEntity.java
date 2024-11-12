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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.reaper.vanimals.client.input.InputKey;
import net.reaper.vanimals.client.input.KeyPressType;
import net.reaper.vanimals.client.util.IDynamicCamera;
import net.reaper.vanimals.client.util.IShakeScreenOnStep;
import net.reaper.vanimals.common.entity.ai.behavior.DietBuilder;
import net.reaper.vanimals.common.entity.ai.behavior.EntityCategory;
import net.reaper.vanimals.common.entity.ai.control.RealisticMoveControl;
import net.reaper.vanimals.common.entity.ai.goal.RamAtTargetGoal;
import net.reaper.vanimals.common.entity.base.AbstractAnimal;
import net.reaper.vanimals.core.init.VEntityTypes;
import net.reaper.vanimals.core.init.VItems;
import net.reaper.vanimals.core.init.VSoundEvents;
import net.reaper.vanimals.util.EntityUtils;
import net.reaper.vanimals.util.compound.CompoundType;
import net.reaper.vanimals.util.compound.EntityData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BisonEntity extends AbstractAnimal implements ItemSteerable, Saddleable, IDynamicCamera, IShakeScreenOnStep {
    public static final EntityData<Integer> STUNNED_TICKS = new EntityData<>(BisonEntity.class, "StunnedTicks", CompoundType.INTEGER, 0);
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState stunnedAnimationState = new AnimationState();
    private int attackAnimationTimeout = 0;
    private int stunnedAnimationTimeout = 0;
    private final ItemBasedSteering steering;
    private int shieldDamageCounter = 0;

    public BisonEntity(EntityType<? extends AbstractAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
        this.setMaxUpStep(1.0F);
        this.xpReward = 20;
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0F);
        this.moveControl = new RealisticMoveControl(this, 2.0F) {
            @Override
            public void tick() {
                if (STUNNED_TICKS.get(BisonEntity.this) <= 0) {
                    super.tick();
                }
            }
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                if (AbstractAnimal.RAM_COOLDOWN.get(this) <= 0 && this.distanceTo(target) >= 4.5F) {
                    AbstractAnimal.RAM_COOLDOWN.set(this, 200);
                    this.setAttackStrategy(AttackStrategy.RAM);
                }
            }
        }
    }

    @Override
    protected void setupAnimations() {
        //Idle Animation
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 54;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        //Stunned animation
        if (STUNNED_TICKS.get(this) > 0 &&  this.stunnedAnimationTimeout <= 0 && !this.isUnderWater()) {
            this.stunnedAnimationTimeout = 20;
            this.stunnedAnimationState.start(this.tickCount);
        } else {
            --this.stunnedAnimationTimeout;
        }
        if (STUNNED_TICKS.get(this) <= 0 || this.isUnderWater()) {
            this.stunnedAnimationState.stop();
        }

        //Attack animation
        if (this.attackAnimationTimeout == 8) {
            this.attackAnimationState.start(this.tickCount);
        }
        if (this.attackAnimationTimeout > 0) {
            --this.attackAnimationTimeout;
        } else {
            this.attackAnimationState.stop();
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (damageSource.getDirectEntity() instanceof Projectile && damageSource.getEntity() instanceof Player player) {
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
        this.goalSelector.addGoal(1, new BreedGoal(this, 0.9F));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.1F) {
            @Override
            protected boolean shouldPanic() {
                return super.shouldPanic() && this.mob.isBaby() && DamageSource.class.desiredAssertionStatus();
            }
        });
        this.goalSelector.addGoal(2, new TemptGoal(this, 0.9F, Ingredient.of(Items.APPLE, Items.WHEAT, VItems.APPLE_ON_A_STICK.get()), false));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 0.9F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.9F));
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

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.GROUND;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return VEntityTypes.BISON.get().create(pLevel);
    }

    @Override
    protected DietBuilder createDiet() {
        return new DietBuilder().addFood(Items.APPLE).addFood(Items.WHEAT);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(2) == 0 ? VSoundEvents.BISON_IDLE.get() : VSoundEvents.BISON_IDLE2.get();
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        if (itemInHand.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(itemInHand, pPlayer, Items.MILK_BUCKET.getDefaultInstance()));
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (!this.isFood(itemInHand) && this.isSaddled() && !this.isVehicle() && !pPlayer.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                this.startRiding(pPlayer);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        InteractionResult result = super.mobInteract(pPlayer, pHand);
        if (!result.consumesAction() && itemInHand.is(Items.SADDLE)) {
            return itemInHand.interactLivingEntity(pPlayer, this, pHand);
        }
        return result.consumesAction() ? result : InteractionResult.PASS;
    }

    @Nullable
    @Override
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

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> pDataAccessor) {
        if (DATA_SADDLE_ID.equals(pDataAccessor) && this.level().isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(pDataAccessor);
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        STUNNED_TICKS.define(this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.steering.addAdditionalSaveData(pCompound);
        STUNNED_TICKS.write(this, pCompound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.steering.readAdditionalSaveData(pCompound);
        STUNNED_TICKS.read(this, pCompound);
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
        return new float[]{0.0F, 0.0F, this.isSprinting() ? 0.8F : 0.3F};
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
    protected void updateWalkAnimation(float pPartialTick) {
        float speed = this.getControllingPassenger() != null ? 2.2F : (!this.isBaby() ? 8.5F : 4.5F);
        float f = this.getPose() == Pose.STANDING ? Math.min(pPartialTick * speed, 1.0F) : 0.0F;
        this.walkAnimation.update(f, 0.2F);
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
                if (this.attackAnimationTimeout <= 0) {
                    this.attackAnimationTimeout = 8;
                }
                this.playSound(VSoundEvents.BISON_ATTACK.get(), 1.0F, 1.0F);
                break;
            case 39:
                STUNNED_TICKS.set(this, 50);
                break;
            default:
                super.handleEntityEvent(pId);
        }
    }

    @Override
    public boolean canBreakBlockNearby() {
        return true;
    }

    @Override
    public boolean isValidBlockForBreak(BlockState pBlockState) {
        return pBlockState.getBlock() instanceof LeavesBlock;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (STUNNED_TICKS.get(this) > 0) {
                STUNNED_TICKS.set(this, STUNNED_TICKS.get(this) - 1);
                this.stunEffect();
                if (STUNNED_TICKS.get(this) == 1) {
                    this.playSound(VSoundEvents.BISON_ROAR.get(), 1.0F, 1.0F);
                }
            }
        }
    }

    private float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || STUNNED_TICKS.get(this) > 0;
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity pEntity) {
        return STUNNED_TICKS.get(this) <= 0 && super.hasLineOfSight(pEntity);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pTarget) {
        this.level().broadcastEntityEvent(this, (byte) 4);
        float attackDamage = this.getAttackDamage();
        float damage;
        if (attackDamage > 0) {
            damage = attackDamage / 2.0F + this.random.nextInt((int) attackDamage);
        } else {
            damage = attackDamage;
        }
        boolean hurt = pTarget.hurt(this.damageSources().mobAttack(this), damage);
        if (hurt) {
            double knockbackResistance = 0.0;
            if (pTarget instanceof LivingEntity living) {
                knockbackResistance = living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            }
            double knockbackFactor = Math.max(0.0, 1.0 - knockbackResistance);
            pTarget.setDeltaMovement(pTarget.getDeltaMovement().add(0.0, 0.1 * knockbackFactor, 0.0));
            this.doEnchantDamageEffects(this, pTarget);
        }
        if (this.getAttackStrategy() == AttackStrategy.RAM) {
            this.knockBack(pTarget);
        }
        if (this.getRandom().nextFloat() < 0.5F) {
            this.cooldownShield(pTarget, 60);
        }
        this.playSound(VSoundEvents.BISON_ATTACK_2.get(), 1.0F, 1.0F);
        return hurt;
    }

    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d0 = this.getX() - (double) this.getBbWidth() * Math.sin(this.yBodyRot * 0.017453292F) + (this.random.nextDouble() * 0.6 - 0.3);
            double d1 = this.getY() + (double) this.getBbHeight() - 0.3;
            double d2 = this.getZ() + (double) this.getBbWidth() * Math.cos(this.yBodyRot * 0.017453292F) + (this.random.nextDouble() * 0.6 - 0.3);
            this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, d0, d1, d2, 0.4980392156862745, 0.5137254901960784, 0.5725490196078431);
        }
    }

    @Override
    protected void blockedByShield(@NotNull LivingEntity pEntity) {
        if (this.random.nextDouble() < 0.5) {
            this.playSound(VSoundEvents.BISON_ATTACK_2.get(), 1.0F, 1.0F);
            this.level().broadcastEntityEvent(this, (byte) 39);
            pEntity.push(this);
        }
        pEntity.hurtMarked = true;
    }

    public void knockBack(@Nullable Entity pEntity) {
        if (pEntity == null) {
            return;
        }
        double yRot = Math.toRadians(this.getYRot());
        double dirX = -Math.sin(yRot);
        double dirZ = Math.cos(yRot);
        Vec3 vec3 = new Vec3(dirX, 0.0F, dirZ).normalize().scale(2.1F);
        pEntity.setDeltaMovement(vec3.x, 0.9F, vec3.z);
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
    public float getMaxCameraTilt() {
        return 10.0F;
    }

    @Override
    public float getCameraTiltSpeed() {
        return 3.0F;
    }

    @Override
    public boolean canEntityShake(@NotNull LivingEntity pEntity) {
        boolean stepping = EntityUtils.isEntityStepping(this, this.getShakeFrequency(), !this.isSprinting() ? 0.2F : 0.3F);
        return IShakeScreenOnStep.super.canEntityShake(pEntity) && EntityUtils.isEntityMoving(this, 0.08F) && stepping;
    }

    @Override
    public float getShakePower() {
        return !this.isSprinting() ? 0.07F : 0.16F;
    }

    @Override
    public float getShakeFrequency() {
        return !this.isSprinting() ? 0.6F : 1.3F;
    }

    @Override
    public float getShakeDistance() {
        return !this.isSprinting() ? 10.0F : 15.0F;
    }

    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.INT);
    }
}