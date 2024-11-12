package net.reaper.vanimals.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.client.renderer.entity.BisonRenderer;
import net.reaper.vanimals.client.renderer.entity.NapoleonFishRenderer;
import net.reaper.vanimals.common.VCommonProxy;
import net.reaper.vanimals.core.init.VEntityTypes;

public class VClientProxy extends VCommonProxy {
    public static float currentRoll = 0.0F;

    @Override
    public void clientInitialize() {
        this.registerEntityRender(VEntityTypes.BISON, BisonRenderer::new);
        this.registerEntityRender(VEntityTypes.NAPOLEON_FISH, NapoleonFishRenderer::new);
    }

    private <T extends Entity> void registerEntityRender(RegistryObject<EntityType<T>> pEntityType, EntityRendererProvider<T> pRenderer) {
        EntityRenderers.register(pEntityType.get(), pRenderer);
    }
}
