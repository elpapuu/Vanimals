package net.reaper.vanimals.client.model.entity;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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
import net.reaper.vanimals.client.animations.entity.BisonAnimations;
import net.reaper.vanimals.client.animations.entity.CreeperfishAnimations;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.water.CreeperfishEntity;

public class CreeperfishModel extends HierarchicalModel<CreeperfishEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation CREEPERFISH_LAYER = new ModelLayerLocation(new ResourceLocation(
			Vanimals.MODID, "creeperfish"), "main");
	private final ModelPart creeperfish;
	public ModelPart body;

	public CreeperfishModel(ModelPart root) {
		this.creeperfish = root.getChild("creeperfish");
		this.body = this.creeperfish.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition creeperfish = partdefinition.addOrReplaceChild("creeperfish", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition body = creeperfish.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bulb = head.addOrReplaceChild("bulb", CubeListBuilder.create().texOffs(0, 17).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition tentacle = creeperfish.addOrReplaceChild("tentacle", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -0.1F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(-1.0F, 0.0F, -2.0F));

		PartDefinition tentacle2 = creeperfish.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(36, 17).addBox(-1.0F, -0.1F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.0F, 0.0F, -2.0F));

		PartDefinition tentacle3 = creeperfish.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(24, 34).addBox(-1.0F, -0.1F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition tentacle4 = creeperfish.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -0.1F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.0F, 0.0F, 2.0F));

		PartDefinition tentacle5 = creeperfish.addOrReplaceChild("tentacle5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.1F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(-1.0F, 0.0F, 2.0F));

		PartDefinition tentacle6 = creeperfish.addOrReplaceChild("tentacle6", CubeListBuilder.create().texOffs(32, 34).addBox(-1.0F, -0.1F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		creeperfish.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public void setupAnim(CreeperfishEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.creeperfish.getAllParts().forEach(ModelPart::resetPose);
		applyHeadRotation(entity, netHeadYaw, headPitch, ageInTicks);

		if (entity.isSwimming())
			this.animateWalk(CreeperfishAnimations.SWIM, limbSwing, limbSwingAmount, 4f, 4.5f);

		if (! entity.isSwimming())
			this.animate(entity.idleAnimationState, CreeperfishAnimations.IDLE, ageInTicks, 1.0F);

		if (! entity.isInWater() & entity.isSwimming())
			this.animate(entity.flopAnimationState, CreeperfishAnimations.FLOP, ageInTicks, 1.0F);
		if (entity.isSprinting())
			this.animateWalk(CreeperfishAnimations.SPRINT, limbSwing, limbSwingAmount, 4f, 4.5f);
	}

	private void applyHeadRotation(CreeperfishEntity pEntity, float pNetHeadYaw, float pHeadPitch, float ageInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.creeperfish.yRot = pNetHeadYaw * 0.017453292F;
		this.creeperfish.xRot = pHeadPitch * 0.017453292F;
	}
	@Override
	public ModelPart root() {
		return creeperfish;
	}
}