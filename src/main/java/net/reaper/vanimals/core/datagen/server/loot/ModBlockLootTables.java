package net.reaper.vanimals.core.datagen.server.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.core.init.VBlocks;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {



    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return VBlocks.BLOCKS.getEntries().stream(). map(RegistryObject::get)::iterator;
    }
}