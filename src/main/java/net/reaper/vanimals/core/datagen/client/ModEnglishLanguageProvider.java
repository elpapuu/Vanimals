package net.reaper.vanimals.core.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.core.init.*;

public class ModEnglishLanguageProvider extends LanguageProvider {
    public ModEnglishLanguageProvider(PackOutput output) {
        super(output, Vanimals.MODID, "en_us.json");
    }

    @Override
    protected void addTranslations() {
        auto(ModItems.BISON_FUR.get());
        auto(ModItems.RAW_BISON.get());
        auto(ModItems.COOKED_BISON.get());
        auto(ModItems.BISON_SPAWN_EGG.get());
        auto(ModItems.CREEPERFISH_SPAWN_EGG.get());

        //creative tab
        addTab("vanimals_tab", "Vanimals");
    }

    public void auto(ItemLike item) {
        add(item.asItem(), toTitleCase(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath()));
    }

    public void auto(EntityType<?> type) {
        add(type, toTitleCase(ForgeRegistries.ENTITY_TYPES.getKey(type).getPath()));
    }

    /**
     * important pass in here the same name that u passed in in {@link ModCreativeModTabs#createTranslationKey(String)}
     */
    public void addTab(String tab, String translation) {
        this.add(ModCreativeModTabs.createTranslationKey(tab), translation);
    }

    /**
     * this translates registry names to actual names
     * for example "fossilized_grave becomes "Fossilized Gravel" or "fosslilized_suspicious_gravel" becomes "Fosslilized Suspicious Gravel"
     *
     * @param input
     * @return
     */
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextTitleCase = true;
                titleCase.append(" ");
                continue;
            }

            if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}