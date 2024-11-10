package net.reaper.vanimals.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ParticleEffectBuilder {
    private final Level level;
    private final Entity entity;
    private final List<ParticleEffect> effects = new ArrayList<>();

    public ParticleEffectBuilder(Level pLevel, Entity pEntity) {
        this.level = pLevel;
        this.entity = pEntity;
    }

    public ParticleEffectBuilder addEffect(ParticleOptions pParticleType, int pInterval, Supplier<Vec3> pPositionOffset) {
        this.effects.add(new ParticleEffect(pParticleType, pInterval, pPositionOffset));
        return this;
    }

    public void spawnParticles() {
        for (ParticleEffect effect : this.effects) {
            if (EntityUtils.isEntityMoving(this.entity, 0.08F)) {
                TickUtils.doEvery(this.entity, effect.interval, () -> spawnParticle(effect));
            }
        }
    }

    private void spawnParticle(ParticleEffect pEffect) {
        Vec3 position = this.entity.position().add(pEffect.positionOffset.get());
        Vec3 delta = this.entity.getDeltaMovement();
        this.level.addParticle(pEffect.particleType, position.x, position.y, position.z, delta.x / 2.5F, 0.02F, delta.z / 2.5F);
    }

    private record ParticleEffect(ParticleOptions particleType, int interval, Supplier<Vec3> positionOffset) {
    }
}