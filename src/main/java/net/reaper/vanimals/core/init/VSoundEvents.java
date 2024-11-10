package net.reaper.vanimals.core.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reaper.vanimals.Vanimals;

public class VSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Vanimals.MODID);
    // BELOW PLEASE

    public static final RegistryObject<SoundEvent> BISON_IDLE;
    public static final RegistryObject<SoundEvent> BISON_IDLE2;
    public static final RegistryObject<SoundEvent> BISON_HURT;
    public static final RegistryObject<SoundEvent> BISON_HURT2;
    public static final RegistryObject<SoundEvent> BISON_ATTACK;
    public static final RegistryObject<SoundEvent> BISON_ATTACK_2;
    public static final RegistryObject<SoundEvent> BISON_ATTACK_3;
    public static final RegistryObject<SoundEvent> BISON_ROAR;
    public static final RegistryObject<SoundEvent> BISON_ROAR_2;
    public static final RegistryObject<SoundEvent> BISON_DEATH;
    public static final RegistryObject<SoundEvent> BISON_DEATH2;

    static {
        BISON_IDLE = registerSoundEvents("bison_idle");
        BISON_IDLE2 = registerSoundEvents("bison_idle2");
        BISON_HURT = registerSoundEvents("bison_hurt");
        BISON_HURT2 = registerSoundEvents("bison_hurt2");
        BISON_ATTACK = registerSoundEvents("bison_attack");
        BISON_ATTACK_2 = registerSoundEvents("bison_attack_2");
        BISON_ATTACK_3 = registerSoundEvents("bison_attack_3");
        BISON_ROAR = registerSoundEvents("bison_roar");
        BISON_ROAR_2 = registerSoundEvents("bison_roar_2");
        BISON_DEATH = registerSoundEvents("bison_death");
        BISON_DEATH2 = registerSoundEvents("bison_death2");
    }

    // ABOVE PLEASE
    private static  RegistryObject<SoundEvent> registerSoundEvents(String pName) {
        return SOUND_EVENTS.register(pName,() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Vanimals.MODID, pName)));
    }
}
