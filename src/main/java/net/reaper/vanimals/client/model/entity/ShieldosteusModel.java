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
import net.reaper.vanimals.client.animations.entity.ShieldosteusAnimations;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.water.ShieldosteusEntity;

public class ShieldosteusModel extends HierarchicalModel<ShieldosteusEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation SHIELDOSTEUS_LAYER = new ModelLayerLocation(new ResourceLocation(
			Vanimals.MODID, "shieldosteus"), "main");
	private final ModelPart shieldosteus;
	public ModelPart body;

	public ShieldosteusModel(ModelPart root) {
		this.shieldosteus = root.getChild("shieldosteus");
		this.body = this.shieldosteus.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition shieldosteus = partdefinition.addOrReplaceChild("shieldosteus", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = shieldosteus.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.7494F, -7.5833F, 2.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(0.0F, -11.7494F, 1.4167F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(6, 0).addBox(-1.0F, -6.7494F, -8.5333F, 2.0F, 5.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, -9.2506F, -0.4167F));

		PartDefinition jaw = body.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 30).addBox(-1.0F, -6.0F, -0.1F, 2.0F, 7.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -0.7494F, -7.5833F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, -3.0F, 0.0F, 0.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.7494F, 8.4167F));

		PartDefinition anal_fin = body.addOrReplaceChild("anal_fin", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -4.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.2506F, 6.4167F));

		PartDefinition fin = body.addOrReplaceChild("fin", CubeListBuilder.create(), PartPose.offset(-1.0F, -0.7494F, -3.0833F));

		PartDefinition cube_r1 = fin.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 27).addBox(0.0F, 0.0F, -1.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition fin2 = body.addOrReplaceChild("fin2", CubeListBuilder.create(), PartPose.offset(1.0F, -0.7494F, -3.0833F));

		PartDefinition cube_r2 = fin2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 27).addBox(0.0F, 0.0F, -1.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		shieldosteus.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public void setupAnim(ShieldosteusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.shieldosteus.getAllParts().forEach(ModelPart::resetPose);
		applyHeadRotation(entity, netHeadYaw, headPitch, ageInTicks);

		//WALK NORMAL
		this.animateWalk(ShieldosteusAnimations.SWIM, limbSwing, limbSwingAmount, 4f, 5.5f);

		if (!entity.isInWater())
		animate(entity.flopAnimation, ShieldosteusAnimations.FLOP, ageInTicks);
		//IDLE
		animate(entity.idleAnimationState, ShieldosteusAnimations.IDLE, ageInTicks);
		//ATTACK
		animate(entity.attackAnimationState, ShieldosteusAnimations.ATTACK, ageInTicks);
	}

	private void applyHeadRotation(ShieldosteusEntity pEntity, float pNetHeadYaw, float pHeadPitch, float ageInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.shieldosteus.getChild("body").yRot = pNetHeadYaw * 0.017453292F;
		this.shieldosteus.getChild("body").xRot = pHeadPitch * 0.017453292F;
	}
	@Override
	public ModelPart root() {
		return shieldosteus;
	}
}