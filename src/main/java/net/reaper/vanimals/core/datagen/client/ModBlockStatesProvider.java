package net.reaper.vanimals.core.datagen.client;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.util.ResourceLocationUtils;

public class ModBlockStatesProvider extends BlockStateProvider {
    public ModBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Vanimals.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
    protected void roeBlock(Block block){
        roeBlock(block, true);
    }

    protected void roeBlock(Block block, boolean generateBlockItem){
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        ModelFile model = models().withExistingParent(name.toString(), modLoc("block/roe_prefab")).texture("roe", ResourceLocationUtils.prepend(name, "block/")).renderType(mcLoc("cutout"));
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(dir == Direction.DOWN ? 0 : dir.getAxis().isHorizontal() ? 90 : 180)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot())) % 360)
                            .build();
                });
        if (generateBlockItem)
            simpleBlockItem(block, model);
    }

    @Override
    public void simpleBlock(Block block, ModelFile model) {
        super.simpleBlock(block, model);
        simpleBlockItem(block, model);
    }
}