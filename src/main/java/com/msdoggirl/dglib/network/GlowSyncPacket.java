package com.msdoggirl.dglib.network;

import com.msdoggirl.dglib.api.ColoredSkinGlowLayerAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class GlowSyncPacket {

    private final UUID playerUUID;
    private final String limb;
    private final int colorARGB;
    private final boolean enabled;
    private final ResourceLocation mask;

    public GlowSyncPacket(UUID playerUUID, String limb, int colorARGB, boolean enabled, ResourceLocation mask) {
        this.playerUUID = playerUUID;
        this.limb = limb;
        this.colorARGB = colorARGB;
        this.enabled = enabled;
        this.mask = mask;
    }

    public static void encode(GlowSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.limb);
        buf.writeInt(msg.colorARGB);
        buf.writeBoolean(msg.enabled);
        buf.writeResourceLocation(msg.mask);
    }

    public static GlowSyncPacket decode(FriendlyByteBuf buf) {
        return new GlowSyncPacket(
                buf.readUUID(),
                buf.readUtf(),
                buf.readInt(),
                buf.readBoolean(),
                buf.readResourceLocation()
        );
    }

    public static void handle(GlowSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Only run on client
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // Apply on client
                switch (msg.limb.toLowerCase()) {
                    case "head"       -> ColoredSkinGlowLayerAPI.setHeadGlowColored(msg.playerUUID, msg.mask, msg.colorARGB, msg.enabled);
                    case "body"       -> ColoredSkinGlowLayerAPI.setBodyGlowColored(msg.playerUUID, msg.mask, msg.colorARGB, msg.enabled);
                    case "right_arm"  -> ColoredSkinGlowLayerAPI.setRightArmGlowColored(msg.playerUUID, msg.mask, msg.colorARGB, msg.enabled);
                    case "left_arm"   -> ColoredSkinGlowLayerAPI.setLeftArmGlowColored(msg.playerUUID, msg.mask, msg.colorARGB, msg.enabled);
                    case "right_leg"  -> ColoredSkinGlowLayerAPI.setRightLegGlowColored(msg.playerUUID, msg.mask, msg.colorARGB, msg.enabled);
                    case "left_leg"   -> ColoredSkinGlowLayerAPI.setLeftLegGlowColored(msg.playerUUID, msg.mask, msg.colorARGB, msg.enabled);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}