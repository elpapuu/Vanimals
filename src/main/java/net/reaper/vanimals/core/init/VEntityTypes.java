package net.reaper.vanimals.core.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.ground.TestEntity;
import net.reaper.vanimals.common.entity.water.NapoleonFishEntity;

public class VEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Vanimals.MODID);

    public static final RegistryObject<EntityType<BisonEntity>> BISON;
    public static final RegistryObject<EntityType<NapoleonFishEntity>> NAPOLEON_FISH;

    static {
        BISON = registerEntity("bison", EntityType.Builder.of(BisonEntity::new, MobCategory.CREATURE).sized(1.7F, 2.0F));
        NAPOLEON_FISH = registerEntity("napoleon_fish", EntityType.Builder.of(NapoleonFishEntity::new, MobCategory.WATER_AMBIENT).sized(1.2F, 0.5F));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String pName, EntityType.Builder<T> pEntityBuilder) {
        return ENTITY_TYPES.register(pName, () -> pEntityBuilder.build(pName));
    }
}