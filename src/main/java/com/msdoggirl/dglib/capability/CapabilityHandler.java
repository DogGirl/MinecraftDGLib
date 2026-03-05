package com.msdoggirl.dglib.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilityHandler {

    public static Capability<IGlowData> GLOW_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}