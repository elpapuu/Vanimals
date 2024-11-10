package net.reaper.vanimals.util.compound;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityData<T> {
    private final String name;
    private final CompoundType compoundType;
    private final EntityDataAccessor<T> dataAccessor;
    private final T defaultValue;

    @SuppressWarnings("unchecked")
    public EntityData(Class<? extends Entity> pEntityClass, String pName, CompoundType pCompoundType, T pDefaultValue) {
        this.name = pName;
        this.compoundType = pCompoundType;
        this.dataAccessor = (EntityDataAccessor<T>) compoundType.createAccessor(pEntityClass);
        this.defaultValue = pDefaultValue;
    }

    public String getName() {
        return this.name;
    }

    public CompoundType getCompoundType() {
        return this.compoundType;
    }

    public EntityDataAccessor<T> getDataAccessor() {
        return this.dataAccessor;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public void define(@NotNull Entity pEntity) {
        pEntity.getEntityData().define(this.getDataAccessor(), this.getDefaultValue());
    }

    public T get(@NotNull Entity pEntity) {
        return pEntity.getEntityData().get(this.getDataAccessor());
    }

    public void set(@NotNull Entity pEntity, T pValue) {
        pEntity.getEntityData().set(this.getDataAccessor(), pValue);
    }

    public void write(@NotNull Entity pEntity, CompoundTag pCompound) {
        this.getCompoundType().set(pCompound, this.getName(), this.get(pEntity));
    }

    @SuppressWarnings("unchecked")
    public void read(@NotNull Entity pEntity, CompoundTag pCompound) {
        T value = (T) this.getCompoundType().get(pCompound, this.getName());
        if (value != null) {
            this.set(pEntity, value);
        }
    }
}
