package net.reaper.vanimals.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.gui.CreativeTabsScreenPage;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.util.ResourceUtils;
import net.reaper.vanimals.core.init.VCreativeModTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
    @Unique
    private final ResourceLocation TIGER_ICON_LOCATION = ResourceUtils.png("icon/tiger_icon");


    @Shadow private CreativeTabsScreenPage currentPage;


    @Shadow protected abstract int getTabX(CreativeModeTab pTab);

    @SuppressWarnings("all")
    @Unique
    private AbstractContainerScreen getAbstractContainerScreen() {
        return ((AbstractContainerScreen) (Object) this);
    }

    @Inject(method = "renderTabButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.BEFORE), cancellable = true)
    public void onRenderTabButton(GuiGraphics pGuiGraphics, CreativeModeTab pCreativeModeTab, CallbackInfo pCallbackInfo) {
        AbstractContainerScreen<?> containerScreen = this.getAbstractContainerScreen();
        boolean isTopPosition  = this.currentPage.isTop(pCreativeModeTab);
        int tabX = containerScreen.getGuiLeft() + this.getTabX(pCreativeModeTab);
        int tabY = containerScreen.getGuiTop();
        if (isTopPosition) {
            tabY -= 28;
        } else {
            tabY += containerScreen.getYSize() - 4;
        }
        tabX += 5;
        tabY += 8 + (isTopPosition  ? 1 : -1);
        if (pCreativeModeTab == VCreativeModTabs.VANIMALS_TAB.get()) {
            pCallbackInfo.cancel();
            PoseStack matrixStack = new PoseStack();
            matrixStack.pushPose();
            pGuiGraphics.blit(TIGER_ICON_LOCATION, tabX, tabY, 0,0, 16, 16, 16, 16);
            matrixStack.popPose();
        }
    }
}
