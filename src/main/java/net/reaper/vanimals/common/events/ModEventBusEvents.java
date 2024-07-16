package net.reaper.vanimals.common.events;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.ground.GobblerEntity;
import net.reaper.vanimals.common.entity.water.CreeperfishEntity;
import net.reaper.vanimals.core.init.ModEntities;

import net.minecraft.world.entity.SpawnPlacements;

@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BISON.get(), BisonEntity.createAttributes().build());
        event.put(ModEntities.CREEPERFISH.get(), CreeperfishEntity.createAttributes().build());
        event.put(ModEntities.GOBBLER.get(), GobblerEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntities.BISON.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                BisonEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}