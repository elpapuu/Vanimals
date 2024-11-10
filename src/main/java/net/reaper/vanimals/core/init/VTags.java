package net.reaper.vanimals.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.util.ResourceUtils;


public class VTags {
    public static class Items{
        public static final TagKey<Item> FOOD;

        static {
            FOOD = bind("food");
        }

        private static TagKey<Item> bind(String pName) {
            return TagKey.create(Registries.ITEM, ResourceUtils.modLoc(pName));
        }
    }

    public static class Entities{
        public static final TagKey<EntityType<?>> FISH;
        public static final TagKey<EntityType<?>> ANIMAL;

        static {
            FISH = bind("fish");
            ANIMAL = bind("animal");
        }

        private static TagKey<EntityType<?>> bind(String pName) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceUtils.modLoc(pName));
        }
    }
}