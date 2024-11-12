package net.reaper.vanimals.core.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
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
        this.auto(VItems.BISON_FUR.get());
        this.auto(VItems.BISON_HORN.get());
        this.auto(VItems.RAW_BISON.get());
        this.auto(VItems.COOKED_BISON.get());
        this.auto(VItems.BISON_SPAWN_EGG.get());
        this.auto(VItems.NAPOLEON_FISH_SPAWN_EGG.get());
        this.auto(VItems.APPLE_ON_A_STICK.get());

        //subtitles
        this.add(VSoundEvents.BISON_IDLE.get(), "Bison ambient");
        this.add(VSoundEvents.BISON_IDLE2.get(), "Bison ambient");
        this.add(VSoundEvents.BISON_HURT.get(), "Bison hurt");
        this.add(VSoundEvents.BISON_HURT2.get(), "Bison hurt");
        this.add(VSoundEvents.BISON_ATTACK.get(), "Bison attack");
        this.add(VSoundEvents.BISON_ATTACK_2.get(), "Bison attack");
        this.add(VSoundEvents.BISON_ATTACK_3.get(), "Bison attack");
        this.add(VSoundEvents.BISON_ROAR.get(), "Bison roar");
        this.add(VSoundEvents.BISON_ROAR_2.get(), "Bison roar");
        this.add(VSoundEvents.BISON_DEATH.get(), "Bison dying");
        this.add(VSoundEvents.BISON_DEATH2.get(), "Bison dying");

        //creative tab
        this.addTab("Vanimals", "Vanimals");
    }
    public void add(SoundEvent soundEvent, String translation){
        add(ModSoundProvider.createSubtitle(soundEvent), translation);
        add("item." + ModSoundProvider.createSubtitle(soundEvent) + ".desc", translation);
    }
    public void auto(ItemLike item) {
        add(item.asItem(), toTitleCase(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath()));
    }

    public void auto(EntityType<?> type) {
        add(type, toTitleCase(ForgeRegistries.ENTITY_TYPES.getKey(type).getPath()));
    }

    /**
     * important pass in here the same name that u passed in in {@link VCreativeModTabs#createTranslationKey(String)}
     */
    public void addTab(String tab, String translation) {
        this.add(VCreativeModTabs.createTranslationKey(tab), translation);
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