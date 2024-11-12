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

    public static boolean isEntityStepping(LivingEntity pEntity, float pAnimationSpeedFactor, float pScale) {
        float stepOffset = (float) Math.tan(pEntity.walkAnimation.position() * pAnimationSpeedFactor - 0.2F);
        return stepOffset * stepOffset < pScale * pScale;
    }

    public static boolean isEntityMoving(@Nullable Entity pEntity, float pMinChange) {
        if (pEntity == null) {
            return false;
        }
        Vec3 delta = pEntity.getDeltaMovement();
        return !(delta.length() > -pMinChange && delta.length() < pMinChange);
    }
}
