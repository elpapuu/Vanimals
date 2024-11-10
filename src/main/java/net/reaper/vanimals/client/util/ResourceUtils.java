package net.reaper.vanimals.client.util;

import net.minecraft.resources.ResourceLocation;
import net.reaper.vanimals.Vanimals;

public class ResourceUtils {
    public static ResourceLocation modLoc(String pPath){
        return new ResourceLocation(Vanimals.MODID, pPath);
    }

    public static ResourceLocation png(String pPath){
        return modLoc("textures/" + pPath + ".png");
    }

    public static ResourceLocation entityTexture(String pPath){
        return modLoc("textures/entity/" + pPath);
    }
}
