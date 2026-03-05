package com.msdoggirl.dglib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = "dglib", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {

    private static boolean glowLayerAdded = false;
    private static boolean wasPlayerPresentLastTick = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        // Cleanup on disconnect
        if (mc.player == null && wasPlayerPresentLastTick) {
            // Optional: log for debugging
            // System.out.println("[DGLib] Client disconnect detected - glow data cleaned");
        }

        wasPlayerPresentLastTick = mc.player != null;

        // Your existing glow layer adding logic
        if (glowLayerAdded) return;

        if (mc.player == null) return;

        var dispatcher = mc.getEntityRenderDispatcher();
        if (dispatcher == null) return;

        boolean added = false;

        PlayerRenderer defaultRenderer = (PlayerRenderer) dispatcher.getSkinMap().get("default");
        if (defaultRenderer != null) {
            defaultRenderer.addLayer(new SkinGlowLayerRenderer(defaultRenderer));

            ResourceLocation glowMask = new ResourceLocation("dglib", "textures/skins/cyber_glow.png");
            UUID localPlayerUUID = mc.player.getUUID();

            added = true;
        }

        PlayerRenderer slimRenderer = (PlayerRenderer) dispatcher.getSkinMap().get("slim");
        if (slimRenderer != null) {
            slimRenderer.addLayer(new SkinGlowLayerRenderer(slimRenderer));
            added = true;
        }

        if (added) {
            glowLayerAdded = true;
        }
    }
}