package net.reaper.vanimals.common.entity.ai.behavior;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class DietBuilder {
    private final List<DietEntry> dietEntries = new ArrayList<>();

    public DietBuilder() {
    }

    public List<DietEntry> getDietEntries() {
        return this.dietEntries;
    }

    public List<ItemLike> getDietItems() {
        List<ItemLike> items = new ArrayList<>();
        for (DietEntry entry : this.dietEntries) {
            if (entry instanceof ItemEntry itemEntry) {
                items.add(itemEntry.item());
            }
        }
        return items;
    }

    public DietBuilder addFood(ItemLike pFoodItem) {
        this.dietEntries.add(new ItemEntry(pFoodItem));
        return this;
    }

    public DietBuilder addFoodTag(TagKey<Item> pFoodTag) {
        this.dietEntries.add(new TagEntry(pFoodTag));
        return this;
    }

    public Ingredient toIngredient() {
        for (ItemLike itemLike : this.getDietItems()) {
            return Ingredient.of(itemLike);
        }
        return Ingredient.EMPTY;
    }

    public static DietBuilder fromItems(ItemLike... pItems) {
        DietBuilder dietBuilder = new DietBuilder();
        for (ItemLike item : pItems) {
            dietBuilder.addFood(item);
        }
        return dietBuilder;
    }

    public boolean isFoodInDiet(ItemLike pFoodItem) {
        for (DietEntry entry : this.dietEntries) {
            if (entry.matches(pFoodItem)) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface DietEntry {
        boolean matches(ItemLike pFoodItem);
    }

    public record ItemEntry(ItemLike item) implements DietEntry {
        @Override
            public boolean matches(ItemLike pFoodItem) {
                return this.item == pFoodItem;
            }
        }

    public record TagEntry(TagKey<Item> itemTag) implements DietEntry {
        @SuppressWarnings("deprecation")
        @Override
            public boolean matches(ItemLike pFoodItem) {
                if (pFoodItem instanceof Item item) {
                    return item.builtInRegistryHolder().is(this.itemTag);
                }
                return false;
            }
        }
}
