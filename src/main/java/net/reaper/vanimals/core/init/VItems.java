package net.reaper.vanimals.core.init;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.reaper.vanimals.Vanimals;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;


public class VItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Vanimals.MODID);

    public static final RegistryObject<Item> BISON_FUR;
    public static final RegistryObject<Item> BISON_HORN;
    public static final RegistryObject<Item> APPLE_ON_A_STICK;
    public static final RegistryObject<Item> RAW_BISON;
    public static final RegistryObject<Item> COOKED_BISON;
    public static final RegistryObject<Item> BISON_SPAWN_EGG;
    public static final RegistryObject<Item> NAPOLEON_FISH_SPAWN_EGG;

    static {
        BISON_FUR = ITEMS.register("bison_fur", () ->new Item(new Item.Properties()));
        BISON_HORN = ITEMS.register("bison_horn", () ->new Item(new Item.Properties()));
        APPLE_ON_A_STICK = ITEMS.register("apple_on_a_stick", () ->new Item(new Item.Properties().stacksTo(1)));
        RAW_BISON = ITEMS.register("raw_bison", () -> new Item(new Item.Properties().food(VFoods.RAW_BISON)));
        COOKED_BISON = ITEMS.register("cooked_bison", () -> new Item(new Item.Properties().food(VFoods.COOKED_BISON)));
        BISON_SPAWN_EGG = registerSpawnEgg("bison", VEntityTypes.BISON, new Color(82, 58, 46), new Color(152, 121, 62));
        NAPOLEON_FISH_SPAWN_EGG = registerSpawnEgg("napoleon_fish", VEntityTypes.NAPOLEON_FISH, new Color(168,54,79), new Color(64,57,84));
    }

    public static <T extends Mob> RegistryObject<Item> registerSpawnEgg(String pMobName, RegistryObject<EntityType<T>> pEntity, Color pBackgroundColor, Color pHighlightColor) {
        return ITEMS.register(pMobName + "_spawn_egg", () -> new ForgeSpawnEggItem(pEntity, pBackgroundColor.hashCode(), pHighlightColor.hashCode(), new Item.Properties()));
    }
}