package com.msdoggirl.dglib.network;

import com.msdoggirl.dglib.DGLib;
import com.msdoggirl.dglib.capability.CapabilityHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class GlowChangePacket {

    private final UUID playerUUID;
    private final String limb;          // "head", "body", "right_arm", etc.
    private final int colorARGB;
    private final boolean enabled;
    private final ResourceLocation mask;

    public GlowChangePacket(UUID playerUUID, String limb, int colorARGB, boolean enabled, ResourceLocation mask) {
        this.playerUUID = playerUUID;
        this.limb = limb;
        this.colorARGB = colorARGB;
        this.enabled = enabled;
        this.mask = mask;
    }

    public static void encode(GlowChangePacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.limb);
        buf.writeInt(msg.colorARGB);
        buf.writeBoolean(msg.enabled);
        buf.writeResourceLocation(msg.mask);
    }

    public static GlowChangePacket decode(FriendlyByteBuf buf) {
        return new GlowChangePacket(
                buf.readUUID(),
                buf.readUtf(),
                buf.readInt(),
                buf.readBoolean(),
                buf.readResourceLocation()
        );
    }

    public static void handle(GlowChangePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null || !sender.getUUID().equals(msg.playerUUID)) {
                return;
            }

            sender.getCapability(CapabilityHandler.GLOW_CAPABILITY).ifPresent(data -> {
                String limb = msg.limb.toLowerCase();
                switch (limb) {
                    case "head":
                        data.setHeadGlowing(msg.enabled);
                        data.setHeadColor(msg.colorARGB);
                        data.setHeadMask(msg.mask);
                        break;
                    case "body":
                        data.setBodyGlowing(msg.enabled);
                        data.setBodyColor(msg.colorARGB);
                        data.setBodyMask(msg.mask);
                        break;
                    case "right_arm":
                        data.setRightArmGlowing(msg.enabled);
                        data.setRightArmColor(msg.colorARGB);
                        data.setRightArmMask(msg.mask);
                        break;
                    case "left_arm":
                        data.setLeftArmGlowing(msg.enabled);
                        data.setLeftArmColor(msg.colorARGB);
                        data.setLeftArmMask(msg.mask);
                        break;
                    case "right_leg":
                        data.setRightLegGlowing(msg.enabled);
                        data.setRightLegColor(msg.colorARGB);
                        data.setRightLegMask(msg.mask);
                        break;
                    case "left_leg":
                        data.setLeftLegGlowing(msg.enabled);
                        data.setLeftLegColor(msg.colorARGB);
                        data.setLeftLegMask(msg.mask);
                        break;
                }
            });

            // Broadcast to trackers including self
            DGLib.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sender),
                    new GlowSyncPacket(msg.playerUUID, msg.limb, msg.colorARGB, msg.enabled, msg.mask));
        });
        ctx.get().setPacketHandled(true);
    }
}