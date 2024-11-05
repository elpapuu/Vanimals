package net.reaper.vanimals.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.client.renderer.entity.BisonRenderer;
import net.reaper.vanimals.common.ModCommonProxy;
import net.reaper.vanimals.core.init.ModEntities;

public class ModClientProxy extends ModCommonProxy {
    public static float currentRoll = 0.0F;

    @Override
    public void clientInitialize() {
        this.registerEntityRender(ModEntities.BISON, BisonRenderer::new);
    }

    private <T extends Entity> void registerEntityRender(RegistryObject<EntityType<T>> pEntityType, EntityRendererProvider<T> pRenderer) {
        EntityRenderers.register(pEntityType.get(), pRenderer);
    }
}
