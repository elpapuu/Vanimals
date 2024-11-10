package net.reaper.vanimals.core.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class VFoods {
    public static final FoodProperties RAW_BISON;
    public static final FoodProperties COOKED_BISON;

    static {
        RAW_BISON = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).meat().effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200), 0.1f).build();
        COOKED_BISON = new FoodProperties.Builder().nutrition(5).saturationMod(0.7f).build();
    }
}