package net.reaper.vanimals.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.animations.entity.GobblerAnimations;
import net.reaper.vanimals.common.entity.ground.GobblerEntity;

public class GobblerModel extends HierarchicalModel<GobblerEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation GOBBLER_LAYER = new ModelLayerLocation(new ResourceLocation(
			Vanimals.MODID, "gobbler"), "main");
	private final ModelPart gobbler;
	public ModelPart body;
	public ModelPart eyes;
	public ModelPart upper_jaw;

	public GobblerModel(ModelPart root) {
		this.gobbler = root.getChild("gobbler");
		this.body = this.gobbler.getChild("body");
		this.upper_jaw = this.body.getChild("upper_jaw");
		this.eyes = this.upper_jaw.getChild("eyes");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition gobbler = partdefinition.addOrReplaceChild("gobbler", CubeListBuilder.create(), PartPose.offset(0.0F, 20.75F, 3.0F));

		PartDefinition body = gobbler.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 38).addBox(-16.0F, -5.5F, -15.0F, 32.0F, 8.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 76).addBox(-16.0F, -0.5F, -15.0F, 32.0F, 3.0F, 30.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, -4.25F, -3.0F));

		PartDefinition pharynx = body.addOrReplaceChild("pharynx", CubeListBuilder.create().texOffs(94, 41).addBox(-15.0F, -5.5F, -1.0F, 30.0F, 9.0F, 2.0F, new CubeDeformation(-0.02F)), PartPose.offset(0.0F, -2.0F, 14.0F));

		PartDefinition upper_jaw = body.addOrReplaceChild("upper_jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-17.0F, -6.0F, -30.0F, 32.0F, 8.0F, 30.0F, new CubeDeformation(0.01F))
		.texOffs(94, 8).addBox(-17.0F, -6.0F, -30.0F, 32.0F, 3.0F, 30.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.0F, -2.5F, 15.0F));

		PartDefinition eyes = upper_jaw.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(0, 9).addBox(1.0F, -0.25F, -3.25F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(0, 0).addBox(-7.0F, -0.25F, -3.25F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(0, 38).addBox(1.0F, -2.25F, -2.25F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.01F))
		.texOffs(0, 18).addBox(-6.0F, -2.25F, -2.25F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(-1.0F, -8.75F, -6.75F));

		PartDefinition leg = gobbler.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(94, 92).addBox(-4.0F, -0.5F, -3.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -1.25F, -13.0F));

		PartDefinition leg2 = gobbler.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 109).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -1.25F, -12.0F));

		PartDefinition leg3 = gobbler.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(94, 52).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -2.75F, 12.0F));

		PartDefinition leg4 = gobbler.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(94, 76).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -2.75F, 12.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		gobbler.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public void setupAnim(GobblerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.gobbler.getAllParts().forEach(ModelPart::resetPose);
		applyHeadRotation(entity, netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(GobblerAnimations.WALK, limbSwing, limbSwingAmount, 4f, 4.5f);
	}

	private void applyHeadRotation(GobblerEntity pEntity, float pNetHeadYaw, float pHeadPitch, float ageInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -5.0F, 10.0F);

		this.gobbler.getChild("body").yRot = pNetHeadYaw * 0.017453292F;
		this.upper_jaw.getChild("eyes").xRot = pHeadPitch * 0.017453292F;
	}
	@Override
	public ModelPart root() {
		return gobbler;
	}
}