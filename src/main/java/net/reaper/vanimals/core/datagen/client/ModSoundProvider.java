package net.reaper.vanimals.core.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.core.init.VSoundEvents;

public class ModSoundProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param pOutput The {@linkplain PackOutput} instance provided by the data generator.
     * @param pHelper The existing file helper provided by the event you are initializing this provider in.
     */
    public ModSoundProvider(PackOutput pOutput, ExistingFileHelper pHelper) {
        super(pOutput, Vanimals.MODID, pHelper);
    }

    @Override
    public void registerSounds() {
        this.simple(VSoundEvents.BISON_IDLE.get());
        this.simple(VSoundEvents.BISON_IDLE2.get());
        this.simple(VSoundEvents.BISON_HURT.get());
        this.simple(VSoundEvents.BISON_HURT2.get());
        this.simple(VSoundEvents.BISON_ATTACK.get());
        this.simple(VSoundEvents.BISON_ATTACK_2.get());
        this.simple(VSoundEvents.BISON_ATTACK_3.get());
        this.simple(VSoundEvents.BISON_ROAR.get());
        this.simple(VSoundEvents.BISON_ROAR_2.get());
        this.simple(VSoundEvents.BISON_DEATH.get());
        this.simple(VSoundEvents.BISON_DEATH2.get());
    }

    public void simple(SoundEvent pSoundEvent){
        this.add(pSoundEvent, this.simpleDefinition(pSoundEvent));
    }

    protected SoundDefinition simpleDefinition(SoundEvent pSoundEvent){
        return SoundDefinition.definition().with(SoundDefinition.Sound.sound(pSoundEvent.getLocation(), SoundDefinition.SoundType.SOUND).stream()).subtitle(createSubtitle(pSoundEvent));
    }

    public static String createSubtitle(SoundEvent pSoundEvent){
        return "sound." + pSoundEvent.getLocation().getNamespace() + "." + pSoundEvent.getLocation().getPath();
    }
}