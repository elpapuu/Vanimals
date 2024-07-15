package net.reaper.vanimals.core.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.core.init.ModSounds;
import org.jetbrains.annotations.NotNull;

public class ModSoundProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Vanimals.MODID, helper);
    }

    @Override
    public void registerSounds() {
       simple(ModSounds.BISON_IDLE.get());
        simple(ModSounds.BISON_IDLE2.get());
        simple(ModSounds.BISON_HURT.get());
        simple(ModSounds.BISON_HURT2.get());
        simple(ModSounds.BISON_DEATH.get());
        simple(ModSounds.BISON_DEATH2.get());
    }

    public void simple(@NotNull SoundEvent event){
        add(event, simpleDefinition(event));
    }

    protected SoundDefinition simpleDefinition(SoundEvent event){
        return SoundDefinition.definition().with(SoundDefinition.Sound.sound(event.getLocation(), SoundDefinition.SoundType.SOUND).stream()).subtitle(createSubtitle(event));
    }

    public static String createSubtitle(SoundEvent event){
        return "sound." + event.getLocation().getNamespace() + "." + event.getLocation().getPath();
    }
}