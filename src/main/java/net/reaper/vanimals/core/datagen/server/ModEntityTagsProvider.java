package net.reaper.vanimals.core.datagen.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.core.init.VTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static net.reaper.vanimals.core.init.VEntityTypes.BISON;


public class ModEntityTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, p_256572_, Vanimals.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(VTags.Entities.FISH).add(EntityType.SALMON, EntityType.PUFFERFISH, EntityType.COD);
        tag(VTags.Entities.ANIMAL).add(BISON.get(), EntityType.AXOLOTL, EntityType.COW, EntityType.BAT, EntityType.BEE);
    }
}