
package com.msdoggirl.dglib;

import com.msdoggirl.dglib.ClientModEvents;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("dglib")
public class DGLib {

    public DGLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();


        if (FMLLoader.getDist() == Dist.CLIENT) {

            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientTick);
  
        }
    }
    
}