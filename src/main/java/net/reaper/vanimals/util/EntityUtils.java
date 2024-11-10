package net.reaper.vanimals.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {
    public static boolean checkItemsInHands(@Nullable LivingEntity pEntity, Predicate<Item> pCondition) {
        if (pEntity == null) {
            return false;
        }
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = pEntity.getItemInHand(hand);
            if (pCondition.test(itemInHand.getItem())) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Entity> @NotNull List<T> getEntitiesAroundPos(Class<T> pEntityClass, @Nullable Entity pSelf, Vec3 pPos, float pRadius, boolean pMustSee) {
        if (pSelf != null) {
            AABB aabb = pSelf.getBoundingBox().inflate(pRadius);
            List<T> entityList = new ArrayList<>(pSelf.level().getEntitiesOfClass(pEntityClass, aabb, EntitySelector.ENTITY_STILL_ALIVE));
            if (!entityList.isEmpty()) {
                if (pSelf instanceof LivingEntity living) {
                    if (pMustSee) {
                        List<T> copyList = new ArrayList<>(entityList);
                        for (T entity : copyList) {
                            if (!living.hasLineOfSight(entity)) {
                                entityList.remove(entity);
                            }
                        }
                    }
                    return entityList;
                }
            }
        }
        return Collections.emptyList();
    }

    public static <T extends Entity> @NotNull List<T> getEntitiesAroundBlock(Class<T> pEntityClass, @Nullable Entity pSelf, Vec3 pPos, float pRadius, boolean pMustSee) {
        return getEntitiesAroundPos(pEntityClass, pSelf, new BlockPos((int) pPos.x, (int) pPos.y, (int) pPos.z).getCenter(), pRadius, pMustSee);
    }

    public static boolean isEntityStepping(LivingEntity pEntity, float pAnimationSpeedFactor, float pScale) {
        float stepOffset = (float) Math.tan(pEntity.walkAnimation.position() * pAnimationSpeedFactor - 0.2F);
        return stepOffset * stepOffset < pScale * pScale;
    }

    public static boolean isEntityMoving(@javax.annotation.Nullable Entity pEntity, float pMinChange) {
        if (pEntity == null) {
            return false;
        }
        Vec3 delta = pEntity.getDeltaMovement();
        return !(delta.length() > -pMinChange && delta.length() < pMinChange);
    }
}
