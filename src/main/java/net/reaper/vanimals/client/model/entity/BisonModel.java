package net.reaper.vanimals.client.model.entity;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.reaper.vanimals.client.animations.entity.BisonAnimations;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import org.jetbrains.annotations.NotNull;

public class BisonModel extends HierarchicalModel<BisonEntity> {
    private final ModelPart root;
    private final ModelPart bison;
    private final ModelPart body;

    public BisonModel(ModelPart pRoot) {
        this.root = pRoot.getChild("root");
        this.bison = this.root.getChild("bison");
        this.body = this.bison.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bison = root.addOrReplaceChild("bison", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 13.0F));
        PartDefinition body = bison.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 80).addBox(0.0F, -28.0F, -35.0F, 0.0F, 12.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition backside = body.addOrReplaceChild("backside", CubeListBuilder.create().texOffs(58, 80).addBox(-8.0F, -17.0F, -6.0F, 16.0F, 17.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(82, 32).addBox(-8.0F, -17.0F, -6.0F, 16.0F, 10.0F, 12.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, -3.0F));
        PartDefinition udder = backside.addOrReplaceChild("udder", CubeListBuilder.create().texOffs(0, 136).addBox(-2.0F, 0.0F, -3.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 97).addBox(-2.0F, 0.0F, -3.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.5F));
        PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create().texOffs(82, 0).addBox(0.0F, -1.25F, -11.5F, 0.0F, 7.0F, 25.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-9.0F, -24.25F, -12.5F, 18.0F, 24.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.25F, -19.5F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(82, 54).addBox(-6.0F, -8.0F, -8.0F, 12.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(132, 70).addBox(-8.0F, -6.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(18, 136).addBox(-10.0F, -9.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(40, 137).addBox(6.0F, -6.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(26, 137).addBox(8.0F, -9.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(58, 111).addBox(-6.0F, -8.0F, -8.0F, 12.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -8.0F, -32.0F));
        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(132, 17).addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -9.5F, -4.0F, 0.0F, -1.0472F, 0.0F));
        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(132, 17).addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.5F, -4.0F, 0.0F, 1.0472F, 0.0F));
        PartDefinition beard = head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(98, 132).addBox(-1.0F, -0.75F, -4.75F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(98, 111).addBox(0.0F, 1.25F, -10.75F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.75F, -1.25F));
        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(82, 76).addBox(-6.0F, -0.5F, -1.5F, 12.0F, 1.0F, 3.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -1.5F, -6.5F));
        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(118, 76).addBox(-4.0F, -3.75F, -3.25F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(80, 125).addBox(0.0F, -7.75F, -6.25F, 0.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.75F, -7.75F));
        PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create(), PartPose.offset(6.0F, -3.0F, -1.0F));
        PartDefinition cube_r3 = ear2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(122, 70).addBox(0.0F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));
        PartDefinition ear = head.addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.offset(-6.0F, -3.0F, -1.0F));
        PartDefinition cube_r4 = ear.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(118, 107).addBox(-3.0F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));
        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(34, 137).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.5F, 5.5F));
        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(118, 132).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));
        PartDefinition leg1 = bison.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(122, 54).addBox(-3.0F, -1.0F, -3.75F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 121).addBox(-3.0F, -1.0F, -3.75F, 6.0F, 7.0F, 8.0F, new CubeDeformation(0.2F)).texOffs(132, 0).addBox(0.0F, -1.0F, -3.75F, 0.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -6.0F, -26.25F));
        PartDefinition leg2 = bison.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 121).addBox(-0.5F, -0.75F, -4.375F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(118, 92).addBox(-0.5F, -0.75F, -4.375F, 6.0F, 7.0F, 8.0F, new CubeDeformation(0.2F)).texOffs(128, 123).addBox(2.5F, -0.75F, -4.375F, 0.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -6.25F, -25.625F));
        PartDefinition leg3 = bison.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(128, 107).addBox(-3.0F, -0.5F, -3.5F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -6.5F, 0.5F));
        PartDefinition leg4 = bison.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(54, 125).addBox(-3.0F, -0.5F, -3.5F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -6.5F, 0.5F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);
        this.body.getChild("head").yRot = pNetHeadYaw * 0.017453292F;
        this.body.getChild("head").xRot = pHeadPitch * 0.017453292F;
    }

    @Override
    public void setupAnim(@NotNull BisonEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(pNetHeadYaw, pHeadPitch);

        //SPRINT
        if (pEntity.isSprinting()) {
            this.animateWalk(BisonAnimations.BISON_SPRINT, pLimbSwing, pLimbSwingAmount, 3f, 4.5f);
        }

        //WALK NORMAL
        this.animateWalk(BisonAnimations.BISON_WALK, pLimbSwing, pLimbSwingAmount, 4f, 5.5f);
        this.animate(pEntity.idleAnimationState, BisonAnimations.BISON_IDLE, pAgeInTicks);
        this.animate(pEntity.stunnedAnimationState, BisonAnimations.BISON_STUNNED, pAgeInTicks);
        this.animate(pEntity.attackAnimationState, BisonAnimations.BISON_ATTACK, pAgeInTicks);
    }

    public void setMatrixStack(@NotNull PoseStack pMatrixStack) {
        this.root.translateAndRotate(pMatrixStack);
        this.bison.translateAndRotate(pMatrixStack);
        this.body.translateAndRotate(pMatrixStack);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}