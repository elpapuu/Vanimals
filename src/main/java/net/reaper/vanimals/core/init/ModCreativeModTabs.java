package net.reaper.vanimals.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.core.init.ModItems;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Vanimals.MODID);

    public static final RegistryObject<CreativeModeTab> VANIMALS_TAB = CREATIVE_MODE_TABS.register("vanimals_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BISON_FUR.get()))
                    .title(Component.translatable(createTranslationKey("Vanimals")))
                    .displayItems((itemDisplayParameters, pOutput) -> {
                        pOutput.accept(ModItems.BISON_FUR.get());
                        pOutput.accept(ModItems.BISON_HORN.get());
                        pOutput.accept(ModItems.APPLE_ON_A_STICK.get());
                        pOutput.accept(ModItems.SHIELDOSTEUS_BUCKET.get());
                        pOutput.accept(ModItems.RAW_BISON.get());
                        pOutput.accept(ModItems.COOKED_BISON.get());
                        pOutput.accept(ModItems.BISON_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SHIELDOSTEUS_SPAWN_EGG.get());

                    })

                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    /**
     * this creates the translation key for the creative tabs
     * for example: input: "test_tab" -> "creativetab.(modid).test_tab"
     * this is used so the LanguageProvider can actually use this as translation key
     */
    public static String createTranslationKey(String name) {
        return "creativetab." + Vanimals.MODID + "." + name;
    }

}