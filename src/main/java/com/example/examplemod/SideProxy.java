package com.example.examplemod;

import com.example.examplemod.init.*;
import com.example.examplemod.entity.SpawnEntities;
import com.example.examplemod.item.weapons.IngotVariantSwords;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
//@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class SideProxy {
    SideProxy() {
        //Life-cycle events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::loadComplete);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerALL);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModEntities::registerALL);


        //Other
        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(SpawnEntities::trySpawning);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::onAttackVariantSword);
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

    public static void loadComplete(FMLLoadCompleteEvent event){
        //Generate ores
        WorldGenOres.onInitBiomesGen();
    }

    public static void onAttackVariantSword(LivingHurtEvent event){
        Object attacker = event.getSource().getTrueSource(); // Getting whom attacked
        if(attacker instanceof LivingEntity){
            LivingEntity Entityattacker = (LivingEntity)attacker; //If LivingEntity type is attacker
            ItemStack HeldItemStack = Entityattacker.getHeldItemMainhand();
            Item HeldItem = HeldItemStack.getItem(); //Checking what was used to attack
            if(HeldItem instanceof IngotVariantSwords){
                if(Entityattacker instanceof PlayerEntity) { //If attacker is a Player
                    PlayerEntity PlayerAttacker = (PlayerEntity) Entityattacker;
                    ((IngotVariantSwords) HeldItem).onAttack(event.getEntityLiving(), PlayerAttacker, event.getAmount());

                }else { //If attacker is not a player
                    ((IngotVariantSwords) HeldItem).onAttack(event.getEntityLiving(), null, event.getAmount());
                }
            }
        }
    }

    static class Client extends  SideProxy {
        Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::clientSetup);
        }

        private static void clientSetup(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_Z_PIGMAN, PigZombieRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_DROWNED, DrownedRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_HUSK, HuskRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_ZOMBIE, ZombieRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_SKELETON, SkeletonRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_CREEPER, CreeperRenderer::new);

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
