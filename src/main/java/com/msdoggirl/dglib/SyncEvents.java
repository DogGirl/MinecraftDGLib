package com.msdoggirl.dglib;

import com.msdoggirl.dglib.capability.CapabilityHandler;
import com.msdoggirl.dglib.network.GlowSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = DGLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SyncEvents {

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof Player target)) return;
        if (event.getEntity().level().isClientSide) return;

        ServerPlayer tracker = (ServerPlayer) event.getEntity();
        target.getCapability(CapabilityHandler.GLOW_CAPABILITY).ifPresent(data -> {
            syncLimbTo(tracker, target.getUUID(), "head", data.isHeadGlowing(), data.getHeadColor(), data.getHeadMask());
            syncLimbTo(tracker, target.getUUID(), "body", data.isBodyGlowing(), data.getBodyColor(), data.getBodyMask());
            syncLimbTo(tracker, target.getUUID(), "right_arm", data.isRightArmGlowing(), data.getRightArmColor(), data.getRightArmMask());
            syncLimbTo(tracker, target.getUUID(), "left_arm", data.isLeftArmGlowing(), data.getLeftArmColor(), data.getLeftArmMask());
            syncLimbTo(tracker, target.getUUID(), "right_leg", data.isRightLegGlowing(), data.getRightLegColor(), data.getRightLegMask());
            syncLimbTo(tracker, target.getUUID(), "left_leg", data.isLeftLegGlowing(), data.getLeftLegColor(), data.getLeftLegMask());
        });
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        ServerPlayer serverPlayer = (ServerPlayer) player;
        player.getCapability(CapabilityHandler.GLOW_CAPABILITY).ifPresent(data -> {
            syncLimbTo(serverPlayer, player.getUUID(), "head", data.isHeadGlowing(), data.getHeadColor(), data.getHeadMask());
            syncLimbTo(serverPlayer, player.getUUID(), "body", data.isBodyGlowing(), data.getBodyColor(), data.getBodyMask());
            syncLimbTo(serverPlayer, player.getUUID(), "right_arm", data.isRightArmGlowing(), data.getRightArmColor(), data.getRightArmMask());
            syncLimbTo(serverPlayer, player.getUUID(), "left_arm", data.isLeftArmGlowing(), data.getLeftArmColor(), data.getLeftArmMask());
            syncLimbTo(serverPlayer, player.getUUID(), "right_leg", data.isRightLegGlowing(), data.getRightLegColor(), data.getRightLegMask());
            syncLimbTo(serverPlayer, player.getUUID(), "left_leg", data.isLeftLegGlowing(), data.getLeftLegColor(), data.getLeftLegMask());
        });
    }

    private static void syncLimbTo(ServerPlayer recipient, UUID uuid, String limb, boolean enabled, int color, net.minecraft.resources.ResourceLocation mask) {
        if (enabled && mask != null) {
            DGLib.NETWORK.send(PacketDistributor.PLAYER.with(() -> recipient), new GlowSyncPacket(uuid, limb, color, true, mask));
        }
    }
}