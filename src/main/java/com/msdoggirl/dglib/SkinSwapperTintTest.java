//package com.msdoggirl.dglib;
//
//import java.util.UUID;
//import net.minecraft.client.Minecraft;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import com.msdoggirl.dglib.api.SkinSwapperAPI;
//
//@Mod.EventBusSubscriber(modid = "dglib", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//public class SkinSwapperTintTest {
//
//    private static boolean tested = false;
//    private static int tickCounter = 0;
//
//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        if (event.phase != TickEvent.Phase.START) return;
//        tickCounter++;
//
//        if (tested) return;
//        if (tickCounter < 100) return;
//
//        Minecraft mc = Minecraft.getInstance();
//        System.out.println("[SkinSwapperTintTest] Tick " + tickCounter + ", Player: " + (mc.player != null));
//
//        if (mc.player == null) return;
//        if (mc.level == null) return;
//
//        System.out.println("[SkinSwapperTintTest] Running test...");
//        testTintedOverlays(mc.player.getUUID());
//        tested = true;
//    }
//
//    public static void testTintedOverlays(UUID playerUUID) {
//        String texturePath = "textures/skins/test_skin.png";
//
//        // Red tinted left arm
//        SkinSwapperAPI.enableLeftArmTintedOverlay(playerUUID, texturePath, 0xFFFF0000);
//
//        // Green tinted right arm
//        SkinSwapperAPI.enableRightArmTintedOverlay(playerUUID, texturePath, 0xFF00FF00);
//
//        // Blue tinted head
//        SkinSwapperAPI.enableHeadTintedOverlay(playerUUID, texturePath, 0xFF0000FF);
//
//        // Yellow tinted body
//        SkinSwapperAPI.enableBodyTintedOverlay(playerUUID, texturePath, 0xFFFFFF00);
//
//        // Cyan tinted left leg
//        SkinSwapperAPI.enableLeftLegTintedOverlay(playerUUID, texturePath, 0xFF00FFFF);
//
//        // Magenta tinted right leg
//        SkinSwapperAPI.enableRightLegTintedOverlay(playerUUID, texturePath, 0xFFFF00FF);
//
//        System.out.println("[SkinSwapperTintTest] All tinted overlays applied to " + playerUUID);
//    }
//}
