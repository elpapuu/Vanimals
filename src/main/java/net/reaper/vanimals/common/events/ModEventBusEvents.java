package net.reaper.vanimals.common.events;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.core.init.VEntityTypes;

import net.minecraft.world.entity.SpawnPlacements;

@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(VEntityTypes.BISON.get(), BisonEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                VEntityTypes.BISON.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}