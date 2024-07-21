package net.reaper.vanimals.core.init;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.reaper.vanimals.Vanimals;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Vanimals.MODID);

    public static final RegistryObject<Item> BISON_FUR = ITEMS.register("bison_fur", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> BISON_HORN = ITEMS.register("bison_horn", () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> APPLE_ON_A_STICK = ITEMS.register("apple_on_a_stick", () ->new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RAW_BISON = ITEMS.register("raw_bison", () -> new Item(new Item.Properties().food(ModFoods.RAW_BISON)));
    public static final RegistryObject<Item> COOKED_BISON = ITEMS.register("cooked_bison", () -> new Item(new Item.Properties().food(ModFoods.COOKED_BISON)));
    public static final RegistryObject<MobBucketItem> SHIELDOSTEUS_BUCKET = ITEMS.register("shieldosteus_bucket", () -> new MobBucketItem(ModEntities.SHIELDOSTEUS, () -> Fluids.WATER, () -> SoundEvents.BUCKET_FILL_FISH, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ForgeSpawnEggItem> BISON_SPAWN_EGG = ITEMS.register("bison_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BISON, 0x523a2e, 0x98793e, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> SHIELDOSTEUS_SPAWN_EGG = ITEMS.register("shieldosteus_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SHIELDOSTEUS, 0x5c9aaa, 0xbdc3d2, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}