package net.reaper.vanimals.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;

public class VCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Vanimals.MODID);

    public static final RegistryObject<CreativeModeTab> VANIMALS_TAB;

    static {
        VANIMALS_TAB = CREATIVE_MODE_TABS.register("vanimals_tab",
                () -> CreativeModeTab.builder().icon(() -> new ItemStack(VItems.BISON_FUR.get()))
                        .title(Component.translatable(createTranslationKey("Vanimals")))
                        .displayItems((itemDisplayParameters, pOutput) -> {
                            pOutput.accept(VItems.BISON_FUR.get());
                            pOutput.accept(VItems.BISON_HORN.get());
                            pOutput.accept(VItems.APPLE_ON_A_STICK.get());
                            pOutput.accept(VItems.RAW_BISON.get());
                            pOutput.accept(VItems.COOKED_BISON.get());
                            pOutput.accept(VItems.BISON_SPAWN_EGG.get());

                        }).build());
    }


    /**
     * this creates the translation key for the creative tabs
     * for example: input: "test_tab" -> "creativetab.(modid).test_tab"
     * this is used so the LanguageProvider can actually use this as translation key
     */
    public static String createTranslationKey(String pName) {
        return "creativetab." + Vanimals.MODID + "." + pName;
    }
}