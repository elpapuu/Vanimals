package net.reaper.vanimals.client.animations.entity;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class NapoleonFishAnimations {
    public static final AnimationDefinition NAPOLEON_FISH_SWIM = AnimationDefinition.Builder.withLength(1.4925373134328357f).looping().addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.373134328358209f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.746268656716418f, KeyframeAnimations.degreeVec(0f, 0f, -5f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.119402985074627f, KeyframeAnimations.degreeVec(0f, 5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.492537313432836f, KeyframeAnimations.degreeVec(0f, 0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 10f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.746268656716418f, KeyframeAnimations.degreeVec(0f, -10f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.492537313432836f, KeyframeAnimations.degreeVec(0f, 10f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin_tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.373134328358209f, KeyframeAnimations.degreeVec(0f, 10f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.119402985074627f, KeyframeAnimations.degreeVec(0f, -10f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.492537313432836f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0.44f, -4.98f, -5.02f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.746268656716418f, KeyframeAnimations.degreeVec(0.65f, 4.96f, 7.53f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.492537313432836f, KeyframeAnimations.degreeVec(0.44f, -4.98f, -5.02f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.746268656716418f, KeyframeAnimations.degreeVec(0f, -15f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.492537313432836f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin2", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.746268656716418f, KeyframeAnimations.degreeVec(0f, 15f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.492537313432836f, KeyframeAnimations.degreeVec(0f, 5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition NAPOLEON_FISH_SWIM_FAST = AnimationDefinition.Builder.withLength(0.7389162561576356f).looping().addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1847290640394089f, KeyframeAnimations.posVec(0f, 1f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3694581280788178f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5541871921182266f, KeyframeAnimations.posVec(0f, -1.12f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(-5f, -10f, 2.5f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1847290640394089f, KeyframeAnimations.degreeVec(0f, 5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3694581280788178f, KeyframeAnimations.degreeVec(5f, 10f, -5f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5541871921182266f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.degreeVec(-5f, -10f, 2.5f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1847290640394089f, KeyframeAnimations.degreeVec(-7.5f, -20f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5541871921182266f, KeyframeAnimations.degreeVec(7.5f, 20f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin_tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 15f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3694581280788178f, KeyframeAnimations.degreeVec(0f, -15f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.degreeVec(0f, 15f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.1847290640394089f, KeyframeAnimations.degreeVec(8.38f, 9.95f, 7.6f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5541871921182266f, KeyframeAnimations.degreeVec(-7.06f, -9.98f, -5.06f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3694581280788178f, KeyframeAnimations.degreeVec(0f, 2.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin2", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, -2.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3694581280788178f, KeyframeAnimations.degreeVec(0f, 5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7389162561576356f, KeyframeAnimations.degreeVec(0f, -2.5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition NAPOLEON_FISH_FLOP = AnimationDefinition.Builder.withLength(0.5118755118755118f).looping().addAnimation("napoleon", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, -90f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2559181062060141f, KeyframeAnimations.degreeVec(0f, 0f, -90f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 0f, -90f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0f, KeyframeAnimations.posVec(-3f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2559181062060141f, KeyframeAnimations.posVec(-3f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.posVec(-3f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12795905310300704f, KeyframeAnimations.degreeVec(0f, -5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3838771593090211f, KeyframeAnimations.degreeVec(0f, 10f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 7.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12795905310300704f, KeyframeAnimations.degreeVec(0f, -7.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3838771593090211f, KeyframeAnimations.degreeVec(0f, 12.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 7.5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin_tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 12.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12795905310300704f, KeyframeAnimations.degreeVec(0f, -20f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3838771593090211f, KeyframeAnimations.degreeVec(0f, 12.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 12.5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12795905310300704f, KeyframeAnimations.degreeVec(0f, 10f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3838771593090211f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 7.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2559181062060141f, KeyframeAnimations.degreeVec(0f, -42.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 7.5f, 0f), AnimationChannel.Interpolations.CATMULLROM))).addAnimation("fin2", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.12795905310300704f, KeyframeAnimations.degreeVec(0f, -6.87f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2559181062060141f, KeyframeAnimations.degreeVec(0f, 7.5f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4052036681595223f, KeyframeAnimations.degreeVec(0f, -6.87f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.5118362124120281f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).build();
}