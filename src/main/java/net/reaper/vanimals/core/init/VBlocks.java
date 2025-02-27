package net.reaper.vanimals.core.init;

import net.minecraft.world.level.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;

import java.util.function.Function;
import java.util.function.Supplier;

public class VBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Vanimals.MODID);


    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Supplier<Item.Properties> properties) {
        return register(name, blockSupplier, b -> new BlockItem(b, properties.get()));
    }

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Function<Block, Item> blockItemFunction) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        VItems.ITEMS.register(name, () -> blockItemFunction.apply(block.get()));
        return block;
    }

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return VItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}