package net.reaper.vanimals.core.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Vanimals.MODID);
    // BELOW PLEASE

    public static final RegistryObject<SoundEvent> BISON_IDLE = registerSoundEvents("bison_idle");
    public static final RegistryObject<SoundEvent> BISON_IDLE2 = registerSoundEvents("bison_idle2");
    public static final RegistryObject<SoundEvent> BISON_HURT = registerSoundEvents("bison_hurt");
    public static final RegistryObject<SoundEvent> BISON_HURT2 = registerSoundEvents("bison_hurt2");
    public static final RegistryObject<SoundEvent> BISON_ATTACK = registerSoundEvents("bison_attack");
    public static final RegistryObject<SoundEvent> BISON_ATTACK_2 = registerSoundEvents("bison_attack_2");
    public static final RegistryObject<SoundEvent> BISON_ATTACK_3 = registerSoundEvents("bison_attack_3");
    public static final RegistryObject<SoundEvent> BISON_ROAR = registerSoundEvents("bison_roar");
    public static final RegistryObject<SoundEvent> BISON_ROAR_2 = registerSoundEvents("bison_roar_2");
    public static final RegistryObject<SoundEvent> BISON_DEATH = registerSoundEvents("bison_death");
    public static final RegistryObject<SoundEvent> BISON_DEATH2 = registerSoundEvents("bison_death2");

    // ABOVE PLEASE
    private static  RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name,() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Vanimals.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
