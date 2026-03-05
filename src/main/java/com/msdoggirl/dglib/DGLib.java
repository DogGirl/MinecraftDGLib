package com.msdoggirl.dglib;

import com.msdoggirl.dglib.capability.GlowDataProvider;
import com.msdoggirl.dglib.capability.IGlowData;
import com.msdoggirl.dglib.network.GlowChangePacket;
import com.msdoggirl.dglib.network.GlowSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(DGLib.MODID)
public class DGLib {

    public static final String MODID = "dglib";

    // Networking channel
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public DGLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Register capability
        modBus.addListener(this::registerCapabilities);

        // Register packets
        modBus.addListener(this::commonSetup);

        // Client-side events
        forgeBus.addListener(ModEvents::onClientTick);

        // Attach capability to players
        forgeBus.register(new GlowDataProvider());

        //Deprecated
        forgeBus.addListener(ClientModEvents::onClientTick);


    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IGlowData.class);
    }






    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            int id = 0;

            NETWORK.registerMessage(id++, GlowChangePacket.class,
                    GlowChangePacket::encode,
                    GlowChangePacket::decode,
                    GlowChangePacket::handle);

            NETWORK.registerMessage(id++, GlowSyncPacket.class,
                    GlowSyncPacket::encode,
                    GlowSyncPacket::decode,
                    GlowSyncPacket::handle);
        });
    }
}