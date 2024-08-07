package net.reaper.vanimals.core.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.ground.GobblerEntity;
import net.reaper.vanimals.common.entity.water.ShieldosteusEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Vanimals.MODID);

    public static final RegistryObject<EntityType<BisonEntity>> BISON =
            ENTITY_TYPES.register("bison", () -> EntityType.Builder.of(BisonEntity::new, MobCategory.CREATURE)
                    .sized(2f, 3f).build("bison"));
    public static final RegistryObject<EntityType<ShieldosteusEntity>> SHIELDOSTEUS =
            ENTITY_TYPES.register("shieldosteus", () -> EntityType.Builder.of(ShieldosteusEntity::new, MobCategory.MONSTER)
                    .sized(1f, 1.6f).build("shieldosteus"));
    public static final RegistryObject<EntityType<GobblerEntity>> GOBBLER =
            ENTITY_TYPES.register("gobbler", () -> EntityType.Builder.of(GobblerEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 1.3f).build("gobbler"));

    public static final <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder){
        return ENTITY_TYPES.register(name, () -> builder.get().build(Vanimals.MODID));

    }
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}