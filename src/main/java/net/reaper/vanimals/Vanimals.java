package net.reaper.vanimals;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.core.init.ModCreativeModTabs;
import net.reaper.vanimals.core.init.ModEntities;
import net.reaper.vanimals.core.init.ModItems;
import org.slf4j.Logger;

import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Vanimals.MODID)
public class Vanimals {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "vanimals";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public Vanimals() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntities.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    public static ResourceLocation modLoc(String name){
        return new ResourceLocation(MODID, name);
    }

    public static ResourceLocation entityTexture(String name){
        return modLoc("textures/entity/" + name);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Adds everything to Search Tab, since Doot's too lazy to /give during debug.
        if (event.getTabKey() == CreativeModeTabs.SEARCH) {
            List<Item> items = ModItems.ITEMS.getEntries()
                    .stream()
                    .map(RegistryObject::get)
                    .toList();
            for (Item item : items) {
                event.accept(item);
            }
        }
    }
}