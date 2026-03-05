package com.msdoggirl.dglib;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.InputStream;

public class GlowTextureManager {

    private static final Minecraft mc = Minecraft.getInstance();

    public static void setGlow(boolean enabled, ResourceLocation sourceTexture, Integer colorARGB,
                               ResourceLocation targetDynamicRL,
                               Runnable onEnable, Runnable onDisable) {
        if (!enabled) {
            releaseIfDynamic(targetDynamicRL);
            onDisable.run();
            return;
        }

        releaseIfDynamic(targetDynamicRL);

        boolean success = false;
        if (colorARGB != null) {
            success = updateTintedGlowTexture(sourceTexture, colorARGB, targetDynamicRL);
        }

        if (success) {
            onEnable.run();
        } else {
            onDisable.run(); // fallback
        }
    }

    private static boolean updateTintedGlowTexture(ResourceLocation maskRL, int colorARGB, ResourceLocation targetRL) {
        if (maskRL == null) {
            System.err.println("[GlowTextureManager] maskRL is null for " + targetRL);
            return false;
        }

        ResourceManager rm = mc.getResourceManager();
        NativeImage mask = null;

        try {
            var opt = rm.getResource(maskRL);
            if (opt.isPresent()) {
                try (InputStream is = opt.get().open()) {
                    mask = NativeImage.read(is);
                }
            } else {
                System.err.println("[GlowTextureManager] Mask resource not found: " + maskRL);
                return false;
            }
        } catch (Exception e) {
            System.err.println("[GlowTextureManager] Failed to load mask " + maskRL + ": " + e.getMessage());
            return false;
        }

        if (mask == null || mask.getWidth() <= 0 || mask.getHeight() <= 0) {
            System.err.println("[GlowTextureManager] Invalid mask (null or zero size): " + maskRL);
            return false;
        }

        int w = mask.getWidth();
        int h = mask.getHeight();
        NativeImage tinted = new NativeImage(w, h, false);

        int aTint = (colorARGB >>> 24) & 0xFF;
        int rTint = (colorARGB >>> 16) & 0xFF;
        int gTint = (colorARGB >>> 8) & 0xFF;
        int bTint = colorARGB & 0xFF;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int p = mask.getPixelRGBA(x, y);
                int ma = (p >>> 24) & 0xFF;
                if (ma == 0) {
                    tinted.setPixelRGBA(x, y, 0);
                    continue;
                }

                int mr = (p >>> 16) & 0xFF;
                int mg = (p >>> 8) & 0xFF;
                int mb = p & 0xFF;

                int tr = (mr * bTint + 127) / 255;   // ← swapped
                int tg = (mg * gTint + 127) / 255;
                int tb = (mb * rTint + 127) / 255;   // ← swapped
                int ta = (ma * aTint + 127) / 255;

                tinted.setPixelRGBA(x, y, (ta << 24) | (tr << 16) | (tg << 8) | tb);
            }
        }

        mask.close();

        DynamicTexture tex = new DynamicTexture(tinted);
        mc.getTextureManager().register(targetRL, tex);
        return true;
    }

    private static void releaseIfDynamic(ResourceLocation rl) {
        if (rl == null) return;
        if (rl.getNamespace().equals("dglib") && rl.getPath().startsWith("glows/dynamic/")) {
            mc.getTextureManager().release(rl);
        }
    }

    public static void cleanup() {}
}