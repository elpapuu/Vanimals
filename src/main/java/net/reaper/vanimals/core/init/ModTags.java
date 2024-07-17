package net.reaper.vanimals.core.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.reaper.vanimals.Vanimals;


public class ModTags {

    public static class Items{

        public static final TagKey<Item> FOOD = bind("food");

        private static TagKey<Item> bind(String pName) {
            return TagKey.create(Registries.ITEM, Vanimals.modLoc(pName));
        }
    }
    public static class Entities{

        public static final TagKey<EntityType<?>> FISH = bind("fish");
        public static final TagKey<EntityType<?>> ANIMAL = bind("animal");

        private static TagKey<EntityType<?>> bind(String pName) {
            return TagKey.create(Registries.ENTITY_TYPE, Vanimals.modLoc(pName));
        }
    }
}