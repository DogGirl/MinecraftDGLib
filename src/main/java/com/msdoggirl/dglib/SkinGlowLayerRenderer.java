package com.msdoggirl.dglib;

import java.lang.reflect.Method;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.msdoggirl.dglib.api.ColoredSkinGlowLayerAPI;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

@OnlyIn(Dist.CLIENT)
public class SkinGlowLayerRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public SkinGlowLayerRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer != null ? renderer : null); 
    }



    public boolean isEpicFightModelActive(AbstractClientPlayer player) {
        if (!ModList.get().isLoaded("epicfight")) {
            return false;
        }

        try {
            Class<?> capsClass = Class.forName("yesman.epicfight.world.capabilities.EpicFightCapabilities");
            Method getPatchMethod = capsClass.getMethod("getEntityPatch",
                    net.minecraft.world.entity.Entity.class, Class.class);

            Object patchObj = getPatchMethod.invoke(null, player,
                    Class.forName("yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch"));

            if (patchObj == null) {
                return false;
            }

      

            // Variant 1: Try getLivingMotion()
            try {
                Method getLivingMotion = patchObj.getClass().getMethod("getLivingMotion");
                Enum<?> motion = (Enum<?>) getLivingMotion.invoke(patchObj);
                String motionName = motion.name();
                //System.out.println("EpicFight current living motion: " + motionName);
                // Customize: true when in combat-related motion
                return motionName.contains("BATTLE") || motionName.contains("COMBAT") ||
                    motionName.contains("BLOCK") || motionName.contains("ATTACK") ||
                    motionName.contains("KATANA") || !motionName.equals("IDLE") && !motionName.equals("WALK");
            } catch (NoSuchMethodException ignored) { }

            // Variant 2: ClientEngine.isBattleMode() 
            try {
                Class<?> engineClass = Class.forName("yesman.epicfight.client.ClientEngine");
                Method getInstance = engineClass.getMethod("getInstance");
                Object engine = getInstance.invoke(null);
                if (engine != null) {
                    Method isBattle = engine.getClass().getMethod("isBattleMode");
                    boolean inBattle = (boolean) isBattle.invoke(engine);
                    //System.out.println("EpicFight ClientEngine.isBattleMode(): " + inBattle);
                    return inBattle;
                }
            } catch (Exception ignored) { }

            // Variant 3: Animator check (fallback - if any animation is playing beyond vanilla)
            try {
                Method getAnimator = patchObj.getClass().getMethod("getAnimator");  
                Object animator = getAnimator.invoke(patchObj);
                if (animator != null) {
                    Method isOnGoing = animator.getClass().getMethod("isOnGoing");
                    boolean animActive = (boolean) isOnGoing.invoke(animator);
                    //System.out.println("EpicFight animator isOnGoing: " + animActive);
                    return animActive;  
                }
            } catch (Exception ignored) { }

            //System.out.println("EpicFight: No recognized mode/motion method found");
            return false;

        } catch (Exception e) {
            e.printStackTrace();  
            return false;
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (isEpicFightModelActive(player)) {
            return;
        }

        UUID uuid = player.getUUID();
        PlayerModel<AbstractClientPlayer> model = getParentModel();

        // Head
        if (ColoredSkinGlowLayerAPI.isHeadGlowing(uuid)) {
            ResourceLocation tex = ColoredSkinGlowLayerAPI.getHeadGlowTexture(uuid);
            if (tex != null) {
                VertexConsumer vc = bufferSource.getBuffer(RenderType.eyes(tex));
                model.head.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        // Body
        if (ColoredSkinGlowLayerAPI.isBodyGlowing(uuid)) {
            ResourceLocation tex = ColoredSkinGlowLayerAPI.getBodyGlowTexture(uuid);
            if (tex != null) {
                VertexConsumer vc = bufferSource.getBuffer(RenderType.eyes(tex));
                model.body.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        // Right arm
        if (ColoredSkinGlowLayerAPI.isRightArmGlowing(uuid)) {
            ResourceLocation tex = ColoredSkinGlowLayerAPI.getRightArmGlowTexture(uuid);
            if (tex != null) {
                VertexConsumer vc = bufferSource.getBuffer(RenderType.eyes(tex));
                model.rightArm.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        // Left arm
        if (ColoredSkinGlowLayerAPI.isLeftArmGlowing(uuid)) {
            ResourceLocation tex = ColoredSkinGlowLayerAPI.getLeftArmGlowTexture(uuid);
            if (tex != null) {
                VertexConsumer vc = bufferSource.getBuffer(RenderType.eyes(tex));
                model.leftArm.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        // Right leg
        if (ColoredSkinGlowLayerAPI.isRightLegGlowing(uuid)) {
            ResourceLocation tex = ColoredSkinGlowLayerAPI.getRightLegGlowTexture(uuid);
            if (tex != null) {
                VertexConsumer vc = bufferSource.getBuffer(RenderType.eyes(tex));
                model.rightLeg.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        // Left leg
        if (ColoredSkinGlowLayerAPI.isLeftLegGlowing(uuid)) {
            ResourceLocation tex = ColoredSkinGlowLayerAPI.getLeftLegGlowTexture(uuid);
            if (tex != null) {
                VertexConsumer vc = bufferSource.getBuffer(RenderType.eyes(tex));
                model.leftLeg.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }
}