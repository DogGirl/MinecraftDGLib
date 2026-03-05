package com.msdoggirl.dglib.api;

import com.msdoggirl.dglib.GlowTextureManager;
import com.msdoggirl.dglib.PlayerGlowState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ColoredSkinGlowLayerAPI {

    private static final Map<UUID, PlayerGlowState> playerGlows = new HashMap<>();

    private static PlayerGlowState getOrCreate(UUID uuid) {
        return playerGlows.computeIfAbsent(uuid, k -> new PlayerGlowState());
    }


    public static void setHeadGlowColored(UUID uuid, ResourceLocation mask, int colorARGB, boolean enabled) {
        if (mask == null) return;
        PlayerGlowState state = getOrCreate(uuid);
        ResourceLocation dynamicLoc = new ResourceLocation("dglib", "glows/dynamic/head/" + uuid.toString().replace("-", ""));

        GlowTextureManager.setGlow(enabled, mask, colorARGB, dynamicLoc,
                () -> {
                    state.headGlowing = true;
                    state.headGlowTexture = dynamicLoc;
                },
                () -> {
                    state.headGlowing = false;
                    state.headGlowTexture = null;
                });
    }

    public static void setBodyGlowColored(UUID uuid, ResourceLocation mask, int colorARGB, boolean enabled) {
        if (mask == null) return;
        PlayerGlowState state = getOrCreate(uuid);
        ResourceLocation dynamicLoc = new ResourceLocation("dglib", "glows/dynamic/body/" + uuid.toString().replace("-", ""));

        GlowTextureManager.setGlow(enabled, mask, colorARGB, dynamicLoc,
                () -> {
                    state.bodyGlowing = true;
                    state.bodyGlowTexture = dynamicLoc;
                },
                () -> {
                    state.bodyGlowing = false;
                    state.bodyGlowTexture = null;
                });
    }

    public static void setRightArmGlowColored(UUID uuid, ResourceLocation mask, int colorARGB, boolean enabled) {
        if (mask == null) return;
        PlayerGlowState state = getOrCreate(uuid);
        ResourceLocation dynamicLoc = new ResourceLocation("dglib", "glows/dynamic/right_arm/" + uuid.toString().replace("-", ""));

        GlowTextureManager.setGlow(enabled, mask, colorARGB, dynamicLoc,
                () -> {
                    state.rightArmGlowing = true;
                    state.rightArmGlowTexture = dynamicLoc;
                },
                () -> {
                    state.rightArmGlowing = false;
                    state.rightArmGlowTexture = null;
                });
    }

    public static void setLeftArmGlowColored(UUID uuid, ResourceLocation mask, int colorARGB, boolean enabled) {
        if (mask == null) return;
        PlayerGlowState state = getOrCreate(uuid);
        ResourceLocation dynamicLoc = new ResourceLocation("dglib", "glows/dynamic/left_arm/" + uuid.toString().replace("-", ""));

        GlowTextureManager.setGlow(enabled, mask, colorARGB, dynamicLoc,
                () -> {
                    state.leftArmGlowing = true;
                    state.leftArmGlowTexture = dynamicLoc;
                },
                () -> {
                    state.leftArmGlowing = false;
                    state.leftArmGlowTexture = null;
                });
    }

    public static void setRightLegGlowColored(UUID uuid, ResourceLocation mask, int colorARGB, boolean enabled) {
        if (mask == null) return;
        PlayerGlowState state = getOrCreate(uuid);
        ResourceLocation dynamicLoc = new ResourceLocation("dglib", "glows/dynamic/right_leg/" + uuid.toString().replace("-", ""));

        GlowTextureManager.setGlow(enabled, mask, colorARGB, dynamicLoc,
                () -> {
                    state.rightLegGlowing = true;
                    state.rightLegGlowTexture = dynamicLoc;
                },
                () -> {
                    state.rightLegGlowing = false;
                    state.rightLegGlowTexture = null;
                });
    }

    public static void setLeftLegGlowColored(UUID uuid, ResourceLocation mask, int colorARGB, boolean enabled) {
        if (mask == null) return;
        PlayerGlowState state = getOrCreate(uuid);
        ResourceLocation dynamicLoc = new ResourceLocation("dglib", "glows/dynamic/left_leg/" + uuid.toString().replace("-", ""));

        GlowTextureManager.setGlow(enabled, mask, colorARGB, dynamicLoc,
                () -> {
                    state.leftLegGlowing = true;
                    state.leftLegGlowTexture = dynamicLoc;
                },
                () -> {
                    state.leftLegGlowing = false;
                    state.leftLegGlowTexture = null;
                });
    }

    public static boolean isHeadGlowing(UUID uuid) {
        return getOrCreate(uuid).headGlowing;
    }

    public static ResourceLocation getHeadGlowTexture(UUID uuid) {
        return getOrCreate(uuid).headGlowTexture;
    }

    public static boolean isBodyGlowing(UUID uuid) {
        return getOrCreate(uuid).bodyGlowing;
    }

    public static ResourceLocation getBodyGlowTexture(UUID uuid) {
        return getOrCreate(uuid).bodyGlowTexture;
    }

    public static boolean isRightArmGlowing(UUID uuid) {
        return getOrCreate(uuid).rightArmGlowing;
    }

    public static ResourceLocation getRightArmGlowTexture(UUID uuid) {
        return getOrCreate(uuid).rightArmGlowTexture;
    }

    public static boolean isLeftArmGlowing(UUID uuid) {
        return getOrCreate(uuid).leftArmGlowing;
    }

    public static ResourceLocation getLeftArmGlowTexture(UUID uuid) {
        return getOrCreate(uuid).leftArmGlowTexture;
    }

    public static boolean isRightLegGlowing(UUID uuid) {
        return getOrCreate(uuid).rightLegGlowing;
    }

    public static ResourceLocation getRightLegGlowTexture(UUID uuid) {
        return getOrCreate(uuid).rightLegGlowTexture;
    }

    public static boolean isLeftLegGlowing(UUID uuid) {
        return getOrCreate(uuid).leftLegGlowing;
    }

    public static ResourceLocation getLeftLegGlowTexture(UUID uuid) {
        return getOrCreate(uuid).leftLegGlowTexture;
    }

    // Convenience for local player
    public static void setHeadGlowColored(ResourceLocation mask, int colorARGB, boolean enabled) {
        UUID local = Minecraft.getInstance().player.getUUID();
        setHeadGlowColored(local, mask, colorARGB, enabled);
    }

    public static void setBodyGlowColored(ResourceLocation mask, int colorARGB, boolean enabled) {
        UUID local = Minecraft.getInstance().player.getUUID();
        setBodyGlowColored(local, mask, colorARGB, enabled);
    }

    public static void setRightArmGlowColored(ResourceLocation mask, int colorARGB, boolean enabled) {
        UUID local = Minecraft.getInstance().player.getUUID();
        setRightArmGlowColored(local, mask, colorARGB, enabled);
    }

    public static void setLeftArmGlowColored(ResourceLocation mask, int colorARGB, boolean enabled) {
        UUID local = Minecraft.getInstance().player.getUUID();
        setLeftArmGlowColored(local, mask, colorARGB, enabled);
    }

    public static void setRightLegGlowColored(ResourceLocation mask, int colorARGB, boolean enabled) {
        UUID local = Minecraft.getInstance().player.getUUID();
        setRightLegGlowColored(local, mask, colorARGB, enabled);
    }

    public static void setLeftLegGlowColored(ResourceLocation mask, int colorARGB, boolean enabled) {
        UUID local = Minecraft.getInstance().player.getUUID();
        setLeftLegGlowColored(local, mask, colorARGB, enabled);
    }
}