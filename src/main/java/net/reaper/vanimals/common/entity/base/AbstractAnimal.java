package net.reaper.vanimals.common.entity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.reaper.vanimals.client.input.InputKey;
import net.reaper.vanimals.client.input.InputStateManager;
import net.reaper.vanimals.client.input.KeyPressType;
import net.reaper.vanimals.common.entity.ai.behavior.DietBuilder;
import net.reaper.vanimals.common.entity.ai.behavior.EntityCategory;
import net.reaper.vanimals.common.entity.ai.control.RealisticMoveControl;
import net.reaper.vanimals.common.entity.ai.navigation.SmartGroundNavigation;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.util.EntityUtils;
import net.reaper.vanimals.util.ParticleEffectBuilder;
import net.reaper.vanimals.util.TickUtils;
import net.reaper.vanimals.util.compound.CompoundType;
import net.reaper.vanimals.util.compound.EntityData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractAnimal extends Animal {
    public static final EntityData<Integer> ATTACK_STRATEGY = new EntityData<>(AbstractAnimal.class, "AttackStrategy", CompoundType.INTEGER, 0);;
    public static final EntityData<Integer> RAM_COOLDOWN = new EntityData<>(AbstractAnimal.class, "RunCooldown", CompoundType.INTEGER, 0);;
    private final DietBuilder diet;
    public final AnimationState idleAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0;
    public EntityCategory category;

    public AbstractAnimal(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.category = this.getCategory();
        this.diet = this.createDiet();
        this.setAttackStrategy(AttackStrategy.MELEE);
        this.moveControl = new RealisticMoveControl(this, this.getBodyRotateSpeed());
        this.navigation = new SmartGroundNavigation(this, this.getNavigationAccuracy());
    }

    public abstract EntityCategory getCategory();

    protected float getBodyRotateSpeed() {
        return 2.0F;
    }

    protected float getNavigationAccuracy() {
        return 1.0F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return null;
    }

    public DietBuilder getDiet() {
        return this.diet;
    }

    protected abstract DietBuilder createDiet();

    protected void setupAnimations() {

    }

    @NotNull
    protected ParticleEffectBuilder getParticleEffectBuilder() {
        return new ParticleEffectBuilder(this.level(), this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        ATTACK_STRATEGY.define(this);
        RAM_COOLDOWN.define(this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        ATTACK_STRATEGY.write(this, pCompound);
        RAM_COOLDOWN.write(this, pCompound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        ATTACK_STRATEGY.read(this, pCompound);
        RAM_COOLDOWN.read(this, pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimations();
            if (this.canSprintByPlayer()) {
                if (this.getFirstPassenger() != null) {
                    if (InputStateManager.getInstance().isKeyPress(InputKey.CTRL, KeyPressType.HOLD)) {
                        if (EntityUtils.isEntityMoving(this, 0.08F)) {
                            if (this.category == EntityCategory.GROUND) {
                                BlockPos blockPos = this.getOnPos();
                                BlockState blockState = this.level().getBlockState(blockPos);
                                TickUtils.doEvery(this, 4, () -> {
                                    for (float angle : new float[]{-0.3F, 0.3F}) {
                                        if (!blockState.addRunningEffects(this.level(), blockPos, this) && blockState.getRenderShape() != RenderShape.INVISIBLE) {
                                            Vec3 delta = this.getDeltaMovement();
                                            Vec3 lookAngle = this.getLookAngle().normalize();
                                            Vec3 sideOffset = new Vec3(-lookAngle.z, 0, lookAngle.x + 0.6F).normalize().scale(angle);
                                            double particleX = this.getX() + sideOffset.x;
                                            double particleZ = this.getZ() + sideOffset.z;
                                            this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, particleX, this.getY() + 0.3F, particleZ, delta.x / 2.5F, 0.01F, delta.z / 2.5F);
                                        }
                                    }
                                });
                            }
                            this.setSprinting(true);
                        } else {
                            this.setSprinting(false);
                        }
                    } else {
                        this.setSprinting(false);
                    }
                }
            }
        } else {
            RAM_COOLDOWN.set(this,  Math.max(RAM_COOLDOWN.get(this) - 1, 0));
        }
    }

    public boolean canBreakBlockNearby() {
        return false;
    }

    public float getBlockBreakRadius() {
        return 0.2F;
    }

    public boolean isValidBlockForBreak(BlockState pBlockState) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.canBreakBlockNearby()) {
            if (this.isAlive()) {
                if (this.horizontalCollision && ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                    boolean destroyedBlock = false;
                    AABB boundingBox = this.getBoundingBox().inflate(this.getBlockBreakRadius());
                    int minX = Mth.floor(boundingBox.minX);
                    int minY = Mth.floor(boundingBox.minY);
                    int minZ = Mth.floor(boundingBox.minZ);
                    int maxX = Mth.floor(boundingBox.maxX);
                    int maxY = Mth.floor(boundingBox.maxY);
                    int maxZ = Mth.floor(boundingBox.maxZ);
                    for (BlockPos pos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
                        BlockState state = this.level().getBlockState(pos);
                        if (this.isValidBlockForBreak(state)) {
                            destroyedBlock = this.level().destroyBlock(pos, true, this) || destroyedBlock;
                        }
                    }
                    if (!destroyedBlock && this.onGround()) {
                        this.jumpFromGround();
                    }
                }
            }
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack pItemStack) {
        return this.getDiet().isFoodInDiet(pItemStack.getItem());
    }

    public void onServerInput(InputKey pInputKey, KeyPressType pKeyPressType) {

    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity pPassenger) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis().isHorizontal()) {
            int[][] offsets = DismountHelper.offsetsForDirection(direction);
            for (Pose pose : pPassenger.getDismountPoses()) {
                for (int[] offset : offsets) {
                    BlockPos targetPos = this.blockPosition().offset(offset[0], 0, offset[1]);
                    double floorHeight = this.level().getBlockFloorHeight(targetPos);
                    if (DismountHelper.isBlockFloorValid(floorHeight)) {
                        Vec3 dismountPos = Vec3.upFromBottomCenterOf(targetPos, floorHeight);
                        if (DismountHelper.canDismountTo(this.level(), pPassenger, pPassenger.getLocalBoundsForPose(pose).move(dismountPos))) {
                            pPassenger.setPose(pose);
                            return dismountPos;
                        }
                    }
                }
            }
        }
        return super.getDismountLocationForPassenger(pPassenger);
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, @NotNull MoveFunction pCallback) {
        if (this.isPassengerOfSameVehicle(pPassenger)) {
            pCallback.accept(pPassenger, this.getX(), this.getY() + this.getPassengersRidingOffset(), this.getZ());
        } else {
            super.positionRider(pPassenger, pCallback);
        }
    }

    @Contract("null->false")
    public boolean startRiding(@Nullable LivingEntity pEntity) {
        if (pEntity != null) {
            pEntity.setYRot(this.getYRot());
            pEntity.setXRot(this.getXRot());
            pEntity.setYHeadRot(this.getYHeadRot());
            pEntity.startRiding(this);
            return true;
        }
        return false;
    }

    @Contract("null->false")
    public boolean canAddPassenger(@Nullable LivingEntity pEntity) {
        if (pEntity == null) {
            return false;
        }
        return !this.isBaby() && this.canAddPassenger(pEntity);
    }

    public boolean isRideable() {
        return false;
    }

    protected float[] getInputSpeed(@NotNull Player pPlayer) {
        return new float[]{0.0F, 0.0F, 0.0F};
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        if (this.isRideable() && this.isVehicle()) {
            float[] speed = this.getInputSpeed(pPlayer);
            return new Vec3(speed[0], speed[1], speed[2]);
        }
        return Vec3.ZERO;
    }

    @Override
    protected void tickRidden(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        if (this.isRideable()) {
            this.setRot(pPlayer.getYRot(), pPlayer.getXRot() * 0.5F);
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            this.yRotO = this.getYRot();
        }
    }

    public boolean canSprintByPlayer() {
        return true;
    }

    public void cooldownShield(@Nullable Entity pEntity, int pCooldown) {
        if (pEntity instanceof Player player) {
            Item item = player.getUseItem().getItem();
            if (item == Items.SHIELD) {
                player.stopUsingItem();
                InteractionHand hand = player.getUsedItemHand();
                player.getUseItem().hurtAndBreak(1, player, (player1) -> {
                    player.broadcastBreakEvent(hand);
                    ForgeEventFactory.onPlayerDestroyItem(player, player.getUseItem(), hand);
                });
                ItemCooldowns cooldowns = player.getCooldowns();
                if (!cooldowns.isOnCooldown(item)) {
                    cooldowns.addCooldown(item, pCooldown);
                }
                player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
            }
        }
    }

    public boolean attackEntitiesInFront(float pDistance, float pRadius, Consumer<LivingEntity> pTarget) {
        Vec3 lookAngle = this.getLookAngle().normalize();
        Vec3 start = this.position().add(0, this.getBbHeight() / 2, 0);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(start.add(-pRadius, -pRadius, -pRadius), start.add(pRadius, pDistance, pRadius)));
        for (LivingEntity entity : entities) {
            if (entity != this && entity.isAlive()) {
                Vec3 directionToEntity = entity.position().add(0, entity.getBbHeight() / 2, 0).subtract(start).normalize();
                double dotProduct = lookAngle.dot(directionToEntity);
                double angle = Math.acos(dotProduct);
                if (angle <= Math.PI / 2) {
                    pTarget.accept(entity);
                    this.doHurtTarget(entity);
                    return true;
                }
            }
        }
        return false;
    }

    public AttackStrategy getAttackStrategy() {
        return AttackStrategy.values()[ATTACK_STRATEGY.get(this)];
    }

    public void setAttackStrategy(AttackStrategy pAttackStrategy) {
        ATTACK_STRATEGY.set(this, pAttackStrategy.ordinal());
    }

    public enum AttackStrategy {
        MELEE,
        RAM
    }
}
