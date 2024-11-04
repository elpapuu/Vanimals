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
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import org.jetbrains.annotations.NotNull;

public class BisonModel<T extends BisonEntity> extends HierarchicalModel<BisonEntity> {
	public static final ModelLayerLocation BISON_LAYER = new ModelLayerLocation(new ResourceLocation(
			Vanimals.MODID, "bison"), "main");
	public static final ModelLayerLocation BISON_SADDLED_LAYER = new ModelLayerLocation(new ResourceLocation(
			Vanimals.MODID, "bison_saddled"), "second");
	private final ModelPart bison;
	public ModelPart body;
	public ModelPart beard;
	public ModelPart head;

	public BisonModel(ModelPart root) {
		this.bison = root.getChild("bison");
		this.body = this.bison.getChild("body");
		this.head = this.body.getChild("head");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bison = partdefinition.addOrReplaceChild("bison", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 4.0F));

		PartDefinition body = bison.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 176).addBox(-1.25F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 27.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(140, 29).addBox(-1.75F, 0.0F, -3.0F, 3.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 113).addBox(-8.0F, -5.0F, -13.0F, 16.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(84, 86).addBox(-6.0F, -3.0F, -17.0F, 12.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(112, 35).addBox(0.0F, -6.0F, -21.0F, 0.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(61, 66).addBox(-8.0F, -11.0F, -13.0F, 16.0F, 6.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(113, 86).addBox(-8.0F, -14.0F, -6.0F, 16.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-5.0F, 4.0F, -19.0F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(79, 0).addBox(-7.0F, -5.0F, -12.0F, 14.0F, 13.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(131, 0).addBox(-7.0F, 8.0F, -12.0F, 14.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -30.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(113, 86).addBox(-8.0F, -3.0F, 0.0F, 16.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, -6.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -3.5F, -1.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -0.5F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -3.5F, -1.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition horn1 = head.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(100, 102).addBox(-17.0F, -35.0F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(84, 102).addBox(-17.0F, -41.0F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(79, 25).addBox(-17.0F, -29.0F, -12.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 5.0F));

		PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(100, 102).mirror().addBox(-11.0F, -35.0F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 102).mirror().addBox(-11.0F, -41.0F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(79, 25).mirror().addBox(-17.0F, -29.0F, -12.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(24.0F, 22.0F, 5.0F));

		PartDefinition beard = head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(100, 99).addBox(0.0F, 0.0F, -7.0F, 0.0F, 9.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(0, 66).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, -4.0F));

		PartDefinition backside = body.addOrReplaceChild("backside", CubeListBuilder.create().texOffs(172, 73).addBox(-9.5F, -24.0F, -11.0F, 19.0F, 24.0F, 23.0F, new CubeDeformation(0.6F))
				.texOffs(0, 66).addBox(-9.5F, -24.0F, -11.0F, 19.0F, 24.0F, 23.0F, new CubeDeformation(0.0F))
				.texOffs(0, 160).addBox(-9.5F, 0.0F, -11.0F, 19.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 14.0F));

		PartDefinition udder = backside.addOrReplaceChild("udder", CubeListBuilder.create().texOffs(86, 253).addBox(-2.5F, 3.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(86, 253).addBox(0.5F, 3.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(86, 253).addBox(0.5F, 3.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(86, 253).addBox(-2.5F, 3.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(100, 246).addBox(-3.5F, 0.0F, -4.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-11.5F, -41.0F, -3.0F, 23.0F, 33.0F, 33.0F, new CubeDeformation(0.0F))
				.texOffs(190, -33).addBox(0.0F, -46.0F, -3.0F, 0.0F, 5.0F, 33.0F, new CubeDeformation(0.0F))
				.texOffs(0, 98).addBox(0.0F, -8.0F, -3.0F, 0.0F, 6.0F, 33.0F, new CubeDeformation(0.0F))
				.texOffs(144, 214).addBox(-11.5F, -41.0F, -3.0F, 23.0F, 9.0F, 33.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 10.0F, -27.0F));

		PartDefinition leg1 = bison.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(84, 108).addBox(-3.75F, 0.0F, 3.75F, 0.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.75F, 0.0F, -4.25F, 7.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.25F, -13.0F, -16.75F));

		PartDefinition leg2 = bison.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(84, 108).addBox(3.25F, 0.0F, 3.75F, 0.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.75F, 0.0F, -4.25F, 7.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(6.75F, -13.0F, -16.75F));

		PartDefinition leg3 = bison.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 139).addBox(-3.75F, 0.0F, -4.25F, 7.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.75F, -13.0F, 19.25F));

		PartDefinition leg4 = bison.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 139).mirror().addBox(-3.75F, 0.0F, -4.25F, 7.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.25F, -13.0F, 19.25F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bison.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(BisonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.bison.getAllParts().forEach(ModelPart::resetPose);
		applyHeadRotation(entity, netHeadYaw, headPitch, ageInTicks);

		//SPRINT
		if (entity.isSprinting())
			this.animateWalk(BisonAnimations.SPRINT, limbSwing, limbSwingAmount, 3f, 4.5f);

		//WALK NORMAL
		this.animateWalk(BisonAnimations.WALK, limbSwing, limbSwingAmount, 4f, 5.5f);
		animate(entity.idleAnimationState, BisonAnimations.IDLE, ageInTicks);
		animate(entity.stunnedAnimationState, BisonAnimations.STUNNED, ageInTicks);
		animate(entity.attackAnimationState, BisonAnimations.ATTACK, ageInTicks);
	}

	private void applyHeadRotation(BisonEntity pEntity, float pNetHeadYaw, float pHeadPitch, float ageInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.body.getChild("head").yRot = pNetHeadYaw * 0.017453292F;
		this.body.getChild("head").xRot = pHeadPitch * 0.017453292F;
	}
	public void setMatrixStack(@NotNull PoseStack pMatrixStack) {
		this.bison.translateAndRotate(pMatrixStack);
		this.body.translateAndRotate(pMatrixStack);
	}

	@Override
	public ModelPart root() {
		return bison;
	}
}