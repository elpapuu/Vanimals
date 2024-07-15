package net.reaper.vanimals.core.datagen.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.core.init.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static net.reaper.vanimals.core.init.ModEntities.BISON;


public class ModEntityTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, p_256572_, Vanimals.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.Entities.FISH).add(EntityType.SALMON, EntityType.PUFFERFISH, EntityType.COD);
        tag(ModTags.Entities.MOBS).add(BISON.get(), EntityType.AXOLOTL, EntityType.COW, EntityType.BAT, EntityType.BEE);
    }
}