package net.reaper.vanimals.core.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.core.init.ModItems;

public class ModItemModelsProvider extends ItemModelProvider {

    protected ModelFile.ExistingModelFile generated = getExistingFile(mcLoc("item/generated"));
    public ModItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Vanimals.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simple(ModItems.BISON_FUR.get());
        simple(ModItems.RAW_BISON.get());
        simple(ModItems.COOKED_BISON.get());

        simple(ModItems.SHIELDOSTEUS_BUCKET.get());

        spawnEgg(ModItems.BISON_SPAWN_EGG.get());
        spawnEgg(ModItems.CREEPERFISH_SPAWN_EGG.get());

        handheldRod(ModItems.APPLE_ON_A_STICK);
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item>item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Vanimals.MODID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder handheldRod(RegistryObject<Item>item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld_rod")).texture("layer0",
                new ResourceLocation(Vanimals.MODID,"item/" + item.getId().getPath()));
    }

    protected void simple(ItemLike... items){
        for (ItemLike item : items) {
            this.basicItem(item.asItem());
        }
    }

    protected void spawnEgg(ItemLike... items){
        for (ItemLike item : items){
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item.asItem());
            getBuilder(id.toString()).parent(getExistingFile(mcLoc("item/template_spawn_egg")));
        }
    }
}