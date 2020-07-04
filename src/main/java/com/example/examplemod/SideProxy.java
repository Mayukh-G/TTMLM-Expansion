package com.example.examplemod;

import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


public class SideProxy {
    SideProxy() {
        //Life-cycle events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerALL);

        //Other
        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
    }

    private static void commonSetup(FMLCommonSetupEvent event){
        ExampleMod.LOGGER.debug("commonSetup Test log");
    }
    private static void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private static void processIMC(final InterModProcessEvent event) {

    }
    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event){
        
    }

    static class Client extends  SideProxy {
        Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::clientSetup);
        }

        private static void clientSetup(FMLClientSetupEvent event) {

        }
    }

    static class Server extends SideProxy {
        Server() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Server::serverSetup);
        }

        private static void serverSetup(FMLDedicatedServerSetupEvent event) {

        }
    }
}
