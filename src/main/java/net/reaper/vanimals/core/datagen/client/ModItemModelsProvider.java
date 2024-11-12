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
import net.reaper.vanimals.core.init.VItems;

public class ModItemModelsProvider extends ItemModelProvider {
    protected ModelFile.ExistingModelFile generated = getExistingFile(mcLoc("item/generated"));

    public ModItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Vanimals.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simple(VItems.BISON_FUR.get());
        this.simple(VItems.BISON_HORN.get());
        this.simple(VItems.RAW_BISON.get());
        this.simple(VItems.COOKED_BISON.get());

        this.spawnEgg(VItems.BISON_SPAWN_EGG.get());
        this.spawnEgg(VItems.NAPOLEON_FISH_SPAWN_EGG.get());

        this.handheldRod(VItems.APPLE_ON_A_STICK);
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

    protected void simple(ItemLike... pItems){
        for (ItemLike item : pItems) {
            this.basicItem(item.asItem());
        }
    }

    protected void spawnEgg(ItemLike... pItems){
        for (ItemLike item : pItems){
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item.asItem());
            this.getBuilder(id.toString()).parent(this.getExistingFile(this.mcLoc("item/template_spawn_egg")));
        }
    }
}