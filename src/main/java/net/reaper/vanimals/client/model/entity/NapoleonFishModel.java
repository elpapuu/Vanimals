package net.reaper.vanimals.client.model.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.reaper.vanimals.client.animations.entity.BisonAnimations;
import net.reaper.vanimals.client.animations.entity.NapoleonFishAnimations;
import net.reaper.vanimals.common.entity.water.NapoleonFishEntity;
import net.reaper.vanimals.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

public class NapoleonFishModel extends HierarchicalModel<NapoleonFishEntity> {
    private final ModelPart root;
    private final ModelPart napoleon;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart fin_tail;

    public NapoleonFishModel(ModelPart pRoot) {
        this.root = pRoot.getChild("root");
        this.napoleon = this.root.getChild("napoleon");
        this.body = this.napoleon.getChild("body");
        this.tail = this.body.getChild("tail");
        this.fin_tail = this.tail.getChild("fin_tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition napoleon = root.addOrReplaceChild("napoleon", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition body = napoleon.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.7F, -9.0F, 8.0F, 13.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(16, 54).addBox(0.0F, 6.3F, 4.0F, 0.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 33).addBox(0.0F, -7.7F, -4.0F, 0.0F, 3.0F, 18.0F, new CubeDeformation(0.0F)).texOffs(58, 53).addBox(1.0F, 7.3F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(58, 61).addBox(-1.0F, 7.3F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.3F, 0.0F));
        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(36, 49).addBox(-2.0F, -3.5F, 0.0F, 4.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.2F, 11.0F));
        tail.addOrReplaceChild("fin_tail", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, -5.5F, 0.0F, 0.0F, 11.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));
        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 33).addBox(-3.0F, -1.625F, -9.0F, 6.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(56, 0).addBox(-3.0F, -5.625F, -5.0F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(58, 49).addBox(-3.0F, -5.625F, -6.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 63).addBox(0.0F, 5.375F, -5.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.925F, -9.0F));
        PartDefinition fin = body.addOrReplaceChild("fin", CubeListBuilder.create(), PartPose.offset(-4.3916F, 3.8F, -2.0257F));
        fin.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(56, 9).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3916F, -1.0F, -0.9743F, 0.0F, -0.1309F, 0.0F));
        PartDefinition fin2 = body.addOrReplaceChild("fin2", CubeListBuilder.create(), PartPose.offset(4.0F, 3.8F, -2.0F));
        fin2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(56, 20).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.0F, 0.1309F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private void dynamicTail(@NotNull NapoleonFishEntity pEntity) {
        float targetYaw = pEntity.prevTailRot + (pEntity.tailRot - pEntity.prevTailRot) * Minecraft.getInstance().getPartialTick();
        this.tail.yRot = Mth.lerp(0.05F, this.tail.yRot, targetYaw);
        this.fin_tail.yRot = Mth.lerp(0.04F, this.fin_tail.yRot, targetYaw);
    }

    protected void applyBodyAndHeadRotation(float pNetHeadYaw, float pHeadPitch) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -360.0F, 360.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -360.0F, 360.0F);
        this.root.xRot = pHeadPitch * (Mth.PI / 180F);
        this.root.yRot = pNetHeadYaw * (Mth.PI / 180F);
    }

    @Override
    public void setupAnim(@NotNull NapoleonFishEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.dynamicTail(pEntity);
        this.applyBodyAndHeadRotation(pNetHeadYaw, pHeadPitch);
        if (pEntity.isSprinting()) {
            this.animateWalk(NapoleonFishAnimations.NAPOLEON_FISH_SWIM_FAST, pLimbSwing, pLimbSwingAmount, 1.0F, 1.0F);
        } else {
            this.animate(pEntity.idleAnimationState, NapoleonFishAnimations.NAPOLEON_FISH_SWIM, pAgeInTicks);
        }
        this.animate(pEntity.flipAnimationState, NapoleonFishAnimations.NAPOLEON_FISH_FLOP, pAgeInTicks);
        float targetRoll = Math.max(-0.45F, Math.min(0.45F, (pEntity.getYRot() - pEntity.yRotO) * 0.1F));
        pEntity.currentRoll = pEntity.currentRoll + (targetRoll - pEntity.currentRoll) * 0.05F;
        this.root.zRot = pEntity.currentRoll;
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}