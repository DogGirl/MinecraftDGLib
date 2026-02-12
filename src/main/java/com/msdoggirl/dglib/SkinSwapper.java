package com.msdoggirl.dglib;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class SkinSwapper {

    // Per-player data
    private static final Map<UUID, NativeImage> playerOriginalSkinImages = new HashMap<>();
    private static final Map<UUID, ResourceLocation> playerOriginalSkinLocations = new HashMap<>();
    private static final Map<UUID, ResourceLocation> playerSwappedSkinRLs = new HashMap<>();
    private static final Map<UUID, Map<String, String>> playerActiveLimbSwaps = new HashMap<>();
    private static final Map<UUID, Map<String, String>> playerActiveOverlays = new HashMap<>();
    public static Map<UUID, String> playerFullSkinPaths = new HashMap<>();

    // ------------------ Mixin access ------------------

    public static boolean isSkinSwapped(UUID uuid) {
        return playerSwappedSkinRLs.containsKey(uuid);
    }

    public static ResourceLocation getSwappedSkinForPlayer(AbstractClientPlayer player) {
        UUID uuid = player.getUUID();
        return playerSwappedSkinRLs.get(uuid);
    }

    // ------------------ Internal ------------------

    public static void enableLimbSwap(UUID uuid, String limb, String path) {
        getActiveLimbSwaps(uuid).put(limb, path);
        regenerateSwappedSkin(uuid);
    }

    public static void disableLimbSwap(UUID uuid, String limb) {
        getActiveLimbSwaps(uuid).remove(limb);
        regenerateSwappedSkin(uuid);
    }

    public static void enableLimbOverlay(UUID uuid, String limb, String path) {
        getActiveLimbOverlays(uuid).put(limb, path);
        regenerateSwappedSkin(uuid);
    }

    public static void disableLimbOverlay(UUID uuid, String limb) {
        getActiveLimbOverlays(uuid).remove(limb);
        regenerateSwappedSkin(uuid);
    }

    private static Map<String, String> getActiveLimbSwaps(UUID uuid) {
        return playerActiveLimbSwaps.computeIfAbsent(uuid, k -> new HashMap<>());
    }

    private static Map<String, String> getActiveLimbOverlays(UUID uuid) {
        return playerActiveOverlays.computeIfAbsent(uuid, k -> new HashMap<>());
    }

    public static void regenerateSwappedSkin(UUID uuid) {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = (AbstractClientPlayer) mc.level.getPlayerByUUID(uuid);
        if (player == null) return;

        PlayerInfo info = mc.getConnection().getPlayerInfo(uuid);
        if (info == null) return;

        ResourceLocation originalLocation = info.getSkinLocation();
        NativeImage originalImage = playerOriginalSkinImages.get(uuid);

        if (originalImage == null) {
            originalImage = backupOriginalSkin(player, originalLocation);
            if (originalImage == null) return;
            playerOriginalSkinImages.put(uuid, originalImage);
            playerOriginalSkinLocations.put(uuid, originalLocation);
        } else if (!originalLocation.equals(playerOriginalSkinLocations.get(uuid))) {
            // Skin changed externally
            originalImage.close();
            originalImage = backupOriginalSkin(player, originalLocation);
            if (originalImage == null) return;
            playerOriginalSkinImages.put(uuid, originalImage);
            playerOriginalSkinLocations.put(uuid, originalLocation);
        }

        NativeImage modified = new NativeImage(originalImage.format(), originalImage.getWidth(), originalImage.getHeight(), false);
        copyFullImage(originalImage, modified);

        // Apply full skin if set
        String fullPath = playerFullSkinPaths.get(uuid);
        if (fullPath != null) {
            NativeImage fullSrc = loadAltImage(fullPath);
            if (fullSrc != null) {
                applyLimbPixels(fullSrc, modified, "full");
                fullSrc.close();
            }
        }

        // Apply limb swaps
        Map<String, String> limbs = getActiveLimbSwaps(uuid);
        for (Map.Entry<String, String> entry : limbs.entrySet()) {
            NativeImage limbSrc = loadAltImage(entry.getValue());
            if (limbSrc != null) {
                applyLimbPixels(limbSrc, modified, entry.getKey());
                limbSrc.close();
            }
        }

        // Apply overlays
        Map<String, String> overlays = getActiveLimbOverlays(uuid);
        for (Map.Entry<String, String> entry : overlays.entrySet()) {
            NativeImage overlaySrc = loadAltImage(entry.getValue());
            if (overlaySrc != null) {
                applyLimbOverlay(overlaySrc, modified, entry.getKey());
                overlaySrc.close();
            }
        }

        // If no changes, don't register new texture
        if (fullPath == null && limbs.isEmpty() && overlays.isEmpty()) {
            modified.close();
            removeSwappedSkin(uuid);
            return;
        }

        // Register or update dynamic texture
        DynamicTexture texture = new DynamicTexture(modified);
        ResourceLocation newRL = mc.getTextureManager().register("dglib_swapped_" + uuid.toString().replace("-", ""), texture);
        ResourceLocation oldRL = playerSwappedSkinRLs.put(uuid, newRL);
        if (oldRL != null && !oldRL.equals(newRL)) {
            mc.getTextureManager().release(oldRL);
        }
    }

    private static void removeSwappedSkin(UUID uuid) {
        ResourceLocation rl = playerSwappedSkinRLs.remove(uuid);
        if (rl != null) {
            Minecraft.getInstance().getTextureManager().release(rl);
        }
    }

    private static NativeImage backupOriginalSkin(AbstractClientPlayer player, ResourceLocation rl) {
        Minecraft mc = Minecraft.getInstance();
        //String testUUID = "58933e99-9e98-42dd-bbb9-a383b01bd154";
        //String uuidNoDashes = testUUID.replace("-", "");
        String uuidNoDashes = player.getUUID().toString().replace("-", "");

        // Mojang official profile API fetch
        String profileUrlStr = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuidNoDashes + "?unsigned=false";
        try {
            URL profileUrl = new URL(profileUrlStr);
            HttpURLConnection profileConn = (HttpURLConnection) profileUrl.openConnection();
            profileConn.setRequestMethod("GET");
            profileConn.setConnectTimeout(5000);
            profileConn.setReadTimeout(10000);
            if (profileConn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(profileConn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                    String json = sb.toString();
                    JsonObject root = JsonParser.parseString(json).getAsJsonObject();
                    JsonArray properties = root.getAsJsonArray("properties");
                    if (properties != null && properties.size() > 0) {
                        JsonObject texturesProp = null;
                        for (JsonElement propElem : properties) {
                            JsonObject prop = propElem.getAsJsonObject();
                            if ("textures".equals(prop.get("name").getAsString())) {
                                texturesProp = prop;
                                break;
                            }
                        }
                        if (texturesProp != null) {
                            String value = texturesProp.get("value").getAsString();
                            byte[] decodedBytes = Base64.getDecoder().decode(value);
                            String texturesJson = new String(decodedBytes, StandardCharsets.UTF_8);
                            JsonObject texturesRoot = JsonParser.parseString(texturesJson).getAsJsonObject();
                            JsonObject texturesObj = texturesRoot.getAsJsonObject("textures");
                            if (texturesObj.has("SKIN")) {
                                JsonObject skinObj = texturesObj.getAsJsonObject("SKIN");
                                String skinUrlStr = skinObj.get("url").getAsString();
                                URL skinUrl = new URL(skinUrlStr);
                                HttpURLConnection skinConn = (HttpURLConnection) skinUrl.openConnection();
                                skinConn.setRequestMethod("GET");
                                skinConn.setConnectTimeout(5000);
                                skinConn.setReadTimeout(10000);
                                if (skinConn.getResponseCode() == 200) {
                                    try (InputStream stream = skinConn.getInputStream()) {
                                        NativeImage img = NativeImage.read(stream);
                                        if (isValidSkin(img)) {
                                            System.out.println("Skin fetched from Mojang servers.");
                                            return img;
                                        }
                                        img.close();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        // Try to load from resource manager (local files or packs)
        try {
            var resource = mc.getResourceManager().getResource(rl);
            if (resource.isPresent()) {
                try (InputStream stream = resource.get().open()) {
                    NativeImage img = NativeImage.read(stream);
                    if (isValidSkin(img)) {
                        return img;
                    }
                    img.close();
                }
            }
        } catch (Exception ignored) {}

        // Minotar fallback using player's UUID
        String uuidNoDashes = player.getUUID().toString().replace("-", "");
        String minotarUrl = "https://minotar.net/skin/" + uuidNoDashes;
        try {
            URL url = new URL(minotarUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            if (conn.getResponseCode() == 200) {
                try (InputStream stream = url.openStream()) {
                    NativeImage img = NativeImage.read(stream);
                    if (isValidSkin(img)) {
                        return img;
                    }
                    img.close();
                }
            }
        } catch (Exception ignored) {}

        // Default Steve/Alex based on model
        ResourceLocation defaultRL = player.getModelName().equals("slim") ?
                new ResourceLocation("minecraft", "textures/entity/alex.png") : // Adjust if wrong path
                new ResourceLocation("minecraft", "textures/entity/steve.png");

        try {
            var resource = mc.getResourceManager().getResource(defaultRL);
            if (resource.isPresent()) {
                try (InputStream stream = resource.get().open()) {
                    NativeImage img = NativeImage.read(stream);
                    if (isValidSkin(img)) {
                        return img;
                    }
                    img.close();
                }
            }
        } catch (Exception ignored) {}

        return null;
    }

    private static boolean isValidSkin(NativeImage img) {
        return img.getWidth() == 64 && (img.getHeight() == 32 || img.getHeight() == 64);
    }

    private static NativeImage loadAltImage(String path) {
        if (path == null) return null;
        ResourceLocation rl = new ResourceLocation("dglib", path);
        Minecraft mc = Minecraft.getInstance();
        try {
            var resource = mc.getResourceManager().getResource(rl);
            if (resource.isPresent()) {
                try (InputStream stream = resource.get().open()) {
                    NativeImage img = NativeImage.read(stream);
                    if (isValidSkin(img)) {
                        return img;
                    }
                    img.close();
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    // Pixel manipulation methods (unchanged)

    private static void applyLimbPixels(NativeImage src, NativeImage dst, String limb) {
        switch (limb.toLowerCase()) {
            case "left_arm":
                copyRect(src, dst, 32, 48, 32, 48, 16, 16);
                copyRect(src, dst, 48, 48, 48, 48, 16, 16);
                break;
            case "right_arm":
                copyRect(src, dst, 40, 16, 40, 16, 16, 16);
                copyRect(src, dst, 40, 32, 40, 32, 16, 16);
                break;
            case "left_leg":
                copyRect(src, dst, 16, 48, 16, 48, 16, 16);
                copyRect(src, dst, 0, 48, 0, 48, 16, 16);
                break;
            case "right_leg":
                copyRect(src, dst, 0, 16, 0, 16, 16, 16);
                copyRect(src, dst, 0, 32, 0, 32, 16, 16);
                break;
            case "head":
                copyRect(src, dst, 0, 0, 0, 0, 64, 16);
                copyRect(src, dst, 32, 0, 32, 0, 32, 16);
                break;
            case "body":
                copyRect(src, dst, 16, 16, 16, 16, 24, 16);
                copyRect(src, dst, 16, 32, 16, 32, 24, 16);
                break;
            case "full":
                copyFullImage(src, dst);
                break;
        }
    }

        private static void applyLimbOverlay(NativeImage src, NativeImage dst, String limb) {
        switch (limb.toLowerCase()) {
            case "left_arm":
                blendRect(src, dst, 32, 48, 32, 48, 16, 16);
                blendRect(src, dst, 48, 48, 48, 48, 16, 16);
                break;
            case "right_arm":
                blendRect(src, dst, 40, 16, 40, 16, 16, 16);
                blendRect(src, dst, 40, 32, 40, 32, 16, 16);
                break;
            case "left_leg":
                blendRect(src, dst, 16, 48, 16, 48, 16, 16);
                blendRect(src, dst, 0, 48, 0, 48, 16, 16);
                break;
            case "right_leg":
                blendRect(src, dst, 0, 16, 0, 16, 16, 16);
                blendRect(src, dst, 0, 32, 0, 32, 16, 16);
                break;
            case "head":
                blendRect(src, dst, 0, 0, 0, 0, 64, 16);
                blendRect(src, dst, 32, 0, 32, 0, 32, 16);
                break;
            case "body":
                blendRect(src, dst, 16, 16, 16, 16, 24, 16);
                blendRect(src, dst, 16, 32, 16, 32, 24, 16);
                break;
            case "full":
                copyFullBlend(src, dst);
                break;
        }
    }



    private static void blendRect(NativeImage src, NativeImage dst, int sx, int sy, int dx, int dy, int w, int h) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int srcColor = src.getPixelRGBA(sx + x, sy + y);
                int srcA = (srcColor >> 24) & 0xFF;
                if (srcA == 0) continue;

                int dstColor = dst.getPixelRGBA(dx + x, dy + y);

                if (srcA == 255) {
                    dst.setPixelRGBA(dx + x, dy + y, srcColor);
                } else {
                    int srcR = (srcColor >> 16) & 0xFF;
                    int srcG = (srcColor >> 8)  & 0xFF;
                    int srcB =  srcColor        & 0xFF;

                    int dstR = (dstColor >> 16) & 0xFF;
                    int dstG = (dstColor >> 8)  & 0xFF;
                    int dstB =  dstColor        & 0xFF;

                    float alpha = srcA / 255f;
                    int r = (int)(srcR * alpha + dstR * (1 - alpha));
                    int g = (int)(srcG * alpha + dstG * (1 - alpha));
                    int b = (int)(srcB * alpha + dstB * (1 - alpha));

                    int blended = (srcA << 24) | (r << 16) | (g << 8) | b;
                    dst.setPixelRGBA(dx + x, dy + y, blended);
                }
            }
        }
    }

    private static void copyRect(NativeImage src, NativeImage dst, int sx, int sy, int dx, int dy, int w, int h) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                dst.setPixelRGBA(dx + x, dy + y, src.getPixelRGBA(sx + x, sy + y));
            }
        }
    }

    private static void copyFullImage(NativeImage src, NativeImage dst) {
        copyRect(src, dst, 0, 0, 0, 0, src.getWidth(), src.getHeight());
    }

    private static void copyFullBlend(NativeImage src, NativeImage dst) {
        blendRect(src, dst, 0, 0, 0, 0, src.getWidth(), src.getHeight());
    }

    public static void cleanup() {
        for (UUID uuid : new HashMap<>(playerOriginalSkinImages).keySet()) {
            NativeImage img = playerOriginalSkinImages.remove(uuid);
            if (img != null) img.close();
            removeSwappedSkin(uuid);
        }
        playerOriginalSkinLocations.clear();
        playerActiveLimbSwaps.clear();
        playerFullSkinPaths.clear();
        playerActiveOverlays.clear();
    }
}