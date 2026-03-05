package com.msdoggirl.dglib.api;

import net.minecraft.resources.ResourceLocation;

@Deprecated
public class SkinGlowLayerAPI {

    public static Boolean isHeadGlowing = false;
    public static Boolean isBodyGlowing = false;
    public static Boolean isRightArmGlowing = false;
    public static Boolean isLeftArmGlowing = false;
    public static Boolean isRightLegGlowing = false;
    public static Boolean isLeftLegGlowing = false;

    public static ResourceLocation HEAD_GLOW_TEXTURE;
    public static ResourceLocation BODY_GLOW_TEXTURE;
    public static ResourceLocation RIGHT_ARM_GLOW_TEXTURE;
    public static ResourceLocation LEFT_ARM_GLOW_TEXTURE;
    public static ResourceLocation RIGHT_LEG_GLOW_TEXTURE;
    public static ResourceLocation LEFT_LEG_GLOW_TEXTURE;

    public static void setHeadGlow(ResourceLocation texture, Boolean condition) {
        if (condition) {
            isHeadGlowing = true;
            HEAD_GLOW_TEXTURE = texture;
        } else {
            isHeadGlowing = false;
            HEAD_GLOW_TEXTURE = null;
        }
    }

    public static void setBodyGlow(ResourceLocation texture, Boolean condition) {
        if (condition) {
            isBodyGlowing = true;
            BODY_GLOW_TEXTURE = texture;
        } else {
            isBodyGlowing = false;
            BODY_GLOW_TEXTURE = null;
        }
    }

    public static void setRightArmGlow(ResourceLocation texture, Boolean condition) {
        if (condition) {
            isRightArmGlowing = true;
            RIGHT_ARM_GLOW_TEXTURE = texture;
        } else {
            isRightArmGlowing = false;
            RIGHT_ARM_GLOW_TEXTURE = null;
        }
    }

    public static void setLeftArmGlow(ResourceLocation texture, Boolean condition) {
        if (condition) {
            isLeftArmGlowing = true;
            LEFT_ARM_GLOW_TEXTURE = texture;
        } else {
            isLeftArmGlowing = false;
            LEFT_ARM_GLOW_TEXTURE = null;
        }
    }

    public static void setRightLegGlow(ResourceLocation texture, Boolean condition) {
        if (condition) {
            isRightLegGlowing = true;
            RIGHT_LEG_GLOW_TEXTURE = texture;
        } else {
            isRightLegGlowing = false;
            RIGHT_LEG_GLOW_TEXTURE = null;
        }
    }

    public static void setLeftLegGlow(ResourceLocation texture, Boolean condition) {
        if (condition) {
            isLeftLegGlowing = true;
            LEFT_LEG_GLOW_TEXTURE = texture;
        } else {
            isLeftLegGlowing = false;
            LEFT_LEG_GLOW_TEXTURE = null;
        }
    }
}