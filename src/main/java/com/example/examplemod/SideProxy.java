package com.example.examplemod;

import com.example.examplemod.client.renders.NetherBossRender;
import com.example.examplemod.entity.original.NetherBoss;
import com.example.examplemod.init.*;
import com.example.examplemod.entity.SpawnEntities;
import com.example.examplemod.item.tools.capabilities.ESLCapability;
import com.example.examplemod.item.tools.lootmodifiers.*;
import com.example.examplemod.item.weapons.IngotVariantSwords;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureInit::registerFeatures);

        //Other
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(SpawnEntities::trySpawning);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::onAttackVariantSword);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::checkNetherBossSpawn);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::onCapabilitiesAttach);
    }

    private static void commonSetup(FMLCommonSetupEvent event){
        //Adding structures
        for (Biome biome : ForgeRegistries.BIOMES){
            if(biome == Biomes.NETHER){
                biome.addStructure(StructureInit.HARD_FORTRESS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, StructureInit.HARD_FORTRESS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            }
            else if(biome.getPrecipitation() == Biome.RainType.SNOW){
                biome.addStructure(StructureInit.Snow_Dungeon.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, StructureInit.Snow_Dungeon.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            }
        }
        // Loot conditions
        LootConditionManager.registerCondition(new BlazingToolCondition.Serializer());
        LootConditionManager.registerCondition(new EnderToolCondition.Serializer());
        LootConditionManager.registerCondition(new FreezingToolCondition.Serializer());
        LootConditionManager.registerCondition(new FrozenGeodeCondition.Serializer());
        //Capabilities
        ESLCapability.register();
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
    //     You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    //     Event bus for receiving Registry Events
    //     Being used for General Registries (Loot functions)
    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onLootFunctionRegistry(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> lootRegisterEvent) {
            lootRegisterEvent.getRegistry().registerAll(
                    new BlazingTouchModifier.Serializer().setRegistryName(ExampleMod.getID("blazing_touch_modifier")),
                    new EnderTouchModifier.Serializer().setRegistryName(ExampleMod.getID("ender_touch_modifier")),
                    new FreeingTouchModifier.Serializer().setRegistryName(ExampleMod.getID("freezing_touch_modifier")),
                    new FrozenGeodeModifier.Serializer().setRegistryName(ExampleMod.getID("frozen_geode_modifier"))
            );
        }
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

    public static void onCapabilitiesAttach(AttachCapabilitiesEvent<ItemStack> event){
//        Item item = event.getObject().getItem();
//        if(item instanceof ToolItem){
//            ToolItem tool = (ToolItem) item;
//            if(tool.getTier() == IngotVariantTiers.ENDER){
//                event.addCapability(ExampleMod.getID("esl_capability"), new EnderStorageLinker(event.getObject()));
//            }
//        }
    }

    public static void checkNetherBossSpawn(BlockEvent.EntityPlaceEvent event){
        if(!event.getWorld().isRemote()) {
            World world = (World) event.getWorld();
            BlockState blockState = event.getPlacedBlock();
            if(blockState.getBlock() == IngotVariants.WEAK_BLAZING_AllOY.getIngotBlock()){
                boolean activated = false;
                boolean eastWest = false;
                BlockPos blockPos = event.getPos();
                BlockPos PosUp = blockPos.up();
                BlockPos PosDown = blockPos.down();
                BlockPos PosDownDown = PosDown.down();
                BlockPos PosUpEast = PosUp.east();
                BlockPos PosUpNorth = PosUp.north();
                BlockPos PosUpWest = PosUp.west();
                BlockPos PosUpSouth = PosUp.south();
                BlockPos PosEast = blockPos.east();
                BlockPos PosNorth = blockPos.north();
                BlockPos PosWest = blockPos.west();
                BlockPos PosSouth = blockPos.south();
                BlockPos PosDownEast = PosDown.east();
                BlockPos PosDownNorth = PosDown.north();
                BlockPos PosDownWest = PosDown.west();
                BlockPos PosDownSouth = PosDown.south();

                if(world.getBlockState(PosDownDown).getBlock() == Blocks.GOLD_BLOCK){
                    if(world.getBlockState(PosDown).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosUp).getBlock() == Blocks.GOLD_BLOCK){
                        if(world.getBlockState(PosDownEast).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosDownWest).getBlock() == Blocks.GOLD_BLOCK){
                            eastWest = true;
                            if(world.getBlockState(PosEast).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosWest).getBlock() == Blocks.GOLD_BLOCK){
                                if(world.getBlockState(PosUpEast).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosUpWest).getBlock() == Blocks.GOLD_BLOCK){
                                    activated = true;
                                }
                            }
                        }
                        else if (world.getBlockState(PosDownNorth).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosDownSouth).getBlock() == Blocks.GOLD_BLOCK){
                            if(world.getBlockState(PosNorth).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosSouth).getBlock() == Blocks.GOLD_BLOCK){
                                if(world.getBlockState(PosUpNorth).getBlock() == Blocks.GOLD_BLOCK && world.getBlockState(PosUpSouth).getBlock() == Blocks.GOLD_BLOCK){
                                    activated = true;
                                }
                            }
                        }
                    }
                }
                if(activated){
                    //set everything to air and spawn
                    BlockState airState = Blocks.AIR.getDefaultState();
                    world.setBlockState(blockPos, airState);
                    world.setBlockState(PosDown, airState);
                    world.setBlockState(PosUp, airState);
                    world.setBlockState(PosDownDown, airState);
                    if(eastWest){
                        world.setBlockState(PosDownEast, airState);
                        world.setBlockState(PosDownWest, airState);
                        world.setBlockState(PosEast, airState);
                        world.setBlockState(PosWest, airState);
                        world.setBlockState(PosUpEast, airState);
                        world.setBlockState(PosUpWest, airState);
                    }else {
                        world.setBlockState(PosDownNorth, airState);
                        world.setBlockState(PosDownSouth, airState);
                        world.setBlockState(PosNorth, airState);
                        world.setBlockState(PosSouth, airState);
                        world.setBlockState(PosUpNorth, airState);
                        world.setBlockState(PosUpSouth, airState);
                    }
                    NetherBoss boss = new NetherBoss(ModEntities.NETHER_BOSS, world);
                    boss.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    boss.onInitialSpawn(world, world.getDifficultyForLocation(boss.getPosition()), SpawnReason.EVENT, null, null);
                    world.addEntity(boss);
                    ExampleMod.LOGGER.debug("SPAWN ATTEMPT FOR NETHER BOSS");
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
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_STRAY, StrayRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_CREEPER, CreeperRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_SPIDER, SpiderRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_CAVE_SPIDER, CaveSpiderRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_ENDERMAN, EndermanRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_GHAST, GhastRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_WITHER_SKELETON, WitherSkeletonRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_BLAZE, BlazeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.NETHER_BOSS, NetherBossRender::new);

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
