package com.msdoggirl.dglib.api;

import java.util.UUID;

import com.msdoggirl.dglib.SkinSwapper;

public class SkinSwapperAPI {

    public static void enableLeftArm(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbSwap(uuid, "left_arm", altTexturePath);
    }

    public static void disableLeftArm(UUID uuid) {
        SkinSwapper.disableLimbSwap(uuid, "left_arm");
    }

    public static void enableRightArm(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbSwap(uuid, "right_arm", altTexturePath);
    }

    public static void disableRightArm(UUID uuid) {
        SkinSwapper.disableLimbSwap(uuid, "right_arm");
    }

    public static void enableLeftLeg(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbSwap(uuid, "left_leg", altTexturePath);
    }

    public static void disableLeftLeg(UUID uuid) {
        SkinSwapper.disableLimbSwap(uuid, "left_leg");
    }

    public static void enableRightLeg(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbSwap(uuid, "right_leg", altTexturePath);
    }

    public static void disableRightLeg(UUID uuid) {
        SkinSwapper.disableLimbSwap(uuid, "right_leg");
    }

    public static void enableHead(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbSwap(uuid, "head", altTexturePath);
    }

    public static void disableHead(UUID uuid) {
        SkinSwapper.disableLimbSwap(uuid, "head");
    }

    public static void enableBody(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbSwap(uuid, "body", altTexturePath);
    }

    public static void disableBody(UUID uuid) {
        SkinSwapper.disableLimbSwap(uuid, "body");
    }

    public static void enableLeftArmOverlay(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbOverlay(uuid, "left_arm", altTexturePath);
    }

    public static void disableLeftArmOverlay(UUID uuid) {
        SkinSwapper.disableLimbOverlay(uuid, "left_arm");
    }

    public static void enableRightArmOverlay(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbOverlay(uuid, "right_arm", altTexturePath);
    }

    public static void disableRightArmOverlay(UUID uuid) {
        SkinSwapper.disableLimbOverlay(uuid, "right_arm");
    }

    public static void enableLeftLegOverlay(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbOverlay(uuid, "left_leg", altTexturePath);
    }

    public static void disableLeftLegOverlay(UUID uuid) {
        SkinSwapper.disableLimbOverlay(uuid, "left_leg");
    }

    public static void enableRightLegOverlay(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbOverlay(uuid, "right_leg", altTexturePath);
    }

    public static void disableRightLegOverlay(UUID uuid) {
        SkinSwapper.disableLimbOverlay(uuid, "right_leg");
    }

    public static void enableHeadOverlay(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbOverlay(uuid, "head", altTexturePath);
    }

    public static void disableHeadOverlay(UUID uuid) {
        SkinSwapper.disableLimbOverlay(uuid, "head");
    }

    public static void enableBodyOverlay(UUID uuid, String altTexturePath) {
        SkinSwapper.enableLimbOverlay(uuid, "body", altTexturePath);
    }

    public static void disableBodyOverlay(UUID uuid) {
        SkinSwapper.disableLimbOverlay(uuid, "body");
    }

    public static void enableFullSkin(UUID uuid, String altTexturePath) {
        String current = SkinSwapper.playerFullSkinPaths.get(uuid);
        if (current != null && current.equals(altTexturePath)) return;
        SkinSwapper.playerFullSkinPaths.put(uuid, altTexturePath);
        SkinSwapper.regenerateSwappedSkin(uuid);
    }

    public static void disableFullSkin(UUID uuid) {
        if (!SkinSwapper.playerFullSkinPaths.containsKey(uuid)) return;
        SkinSwapper.playerFullSkinPaths.remove(uuid);
        SkinSwapper.regenerateSwappedSkin(uuid);
    }

}
