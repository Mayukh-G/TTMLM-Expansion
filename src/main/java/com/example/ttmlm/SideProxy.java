package com.example.ttmlm;

import com.example.ttmlm.client.renders.*;
import com.example.ttmlm.config.CommonConfig;
import com.example.ttmlm.config.Config;
import com.example.ttmlm.entity.SpawnEntities;
import com.example.ttmlm.entity.changed.*;
import com.example.ttmlm.entity.original.*;
import com.example.ttmlm.init.*;
import com.example.ttmlm.item.tools.capabilities.ESLCapability;
import com.example.ttmlm.item.tools.lootmodifiers.BlazingTouchModifier;
import com.example.ttmlm.item.tools.lootmodifiers.EnderTouchModifier;
import com.example.ttmlm.item.tools.lootmodifiers.FreeingTouchModifier;
import com.example.ttmlm.item.tools.lootmodifiers.FrozenGeodeModifier;
import com.example.ttmlm.item.weapons.IngotVariantSwords;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
//@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class SideProxy {
    private static final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    private static final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
    @Nullable
    private static final IModInfo modInfo = (ModList.get().getModContainerById(TTMLM.MOD_ID).isPresent()) ? ModList.get().getModContainerById(TTMLM.MOD_ID).get().getModInfo() : null;

    SideProxy() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);

        //Life-cycle events
        modEventBus.addListener(SideProxy::commonSetup);
        modEventBus.addListener(SideProxy::enqueueIMC);
        modEventBus.addListener(SideProxy::processIMC);
        modEventBus.addListener(SideProxy::loadComplete);

        //Attributes
        modEventBus.addListener(SideProxy::addEntityAttributes);
        // Registering
        modEventBus.addGenericListener(Block.class, ModBlocks::registerAll);
        modEventBus.addGenericListener(Item.class, ModItems::registerALL);
        modEventBus.addGenericListener(EntityType.class, ModEntities::registerALL);
        modEventBus.addGenericListener(Structure.class, SideProxy::onRegisterStructures);

        //Other
        forgeEventBus.register(this);
        forgeEventBus.addListener(SideProxy::serverStarting);
        forgeEventBus.addListener(SpawnEntities::trySpawning);
        forgeEventBus.addListener(SideProxy::onAttackVariantSword);
        forgeEventBus.addListener(SideProxy::checkNetherBossSpawn);
        forgeEventBus.addListener(SideProxy::playerJoinVersionChecker);
        forgeEventBus.addListener(EventPriority.NORMAL, SideProxy::addDimensionalSpacing);
        forgeEventBus.addListener(EventPriority.HIGH, SideProxy::biomeMod);
    }

    // This will be removed in 1.17
    private static void addEntityAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.HARD_BLAZE, HardBlaze.createAttributes().build());
        event.put(ModEntities.HARD_CAVE_SPIDER, HardCaveSpider.createCaveSpiderAttributes().build());
        event.put(ModEntities.HARD_CREEPER, HardCreeper.createAttributes().build());
        event.put(ModEntities.HARD_DROWNED, HardDrowned.createDrownedAttributes().build());
        event.put(ModEntities.HARD_ENDERMAN, HardEnderman.createEnderAttributes().build());
        event.put(ModEntities.HARD_GHAST, HardGhast.createGhastAttributes().build());
        event.put(ModEntities.HARD_HUSK, HardHusk.createHuskAttributes().build());
        event.put(ModEntities.HARD_SKELETON, HardSkeleton.createAttributes().build());
        event.put(ModEntities.HARD_SPIDER, HardSpider.createAttributes().build());
        event.put(ModEntities.HARD_STRAY, HardStray.createStrayAttributes().build());
        event.put(ModEntities.HARD_ZOMBIE, HardZombie.createAttributes().build());
        event.put(ModEntities.HARD_WITHER_SKELETON, HardWitherSkeleton.createWSkeletonAttributes().build());
        event.put(ModEntities.HARD_Z_PIGLIN, HardZPiglin.createZpPiglinAttributes().build());
        // Originals

        event.put(ModEntities.HARD_DROWNED_SC, HardDrownedSwarmCaller.createDrownedSCAttributes().build());
        event.put(ModEntities.HARD_HUSK_SC, HardHuskSwarmCaller.createHuskSCAttributes().build());
        event.put(ModEntities.HARD_ZOMBIE_SC, HardZombieSwarmCaller.createZombieSCAttributes().build());
        event.put(ModEntities.HARD_Z_PIGLIN_SC, HardZPiglinSwarmCaller.createZPiglinSCAttributes().build());
        event.put(ModEntities.NETHER_BOSS, NetherBoss.createNetherBossAttributes().build());
    }

    private static void commonSetup(FMLCommonSetupEvent event){
        // Loot conditions
        ModConditions.registerAll();

        // Old Way of Registering
//        LootConditionManager.registerCondition(new BlazingToolCondition.Serializer());
//        LootConditionManager.registerCondition(new EnderToolCondition.Serializer());
//        LootConditionManager.registerCondition(new FreezingToolCondition.Serializer());
//        LootConditionManager.registerCondition(new FrozenGeodeCondition.Serializer());

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
    }
    //     You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    //     Event bus for receiving Registry Events
    //     Being used for General Registries (Loot functions)
    @Mod.EventBusSubscriber(modid = TTMLM.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onLootFunctionRegistry(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> lootRegisterEvent) {
            lootRegisterEvent.getRegistry().registerAll(
                    new BlazingTouchModifier.Serializer().setRegistryName(TTMLM.getID("blazing_touch_modifier")),
                    new EnderTouchModifier.Serializer().setRegistryName(TTMLM.getID("ender_touch_modifier")),
                    new FreeingTouchModifier.Serializer().setRegistryName(TTMLM.getID("freezing_touch_modifier")),
                    new FrozenGeodeModifier.Serializer().setRegistryName(TTMLM.getID("frozen_geode_modifier"))
            );
        }

    }

    private static void onRegisterStructures(final RegistryEvent.Register<Structure<?>> event){
        StructureInit.TTStructures.registerStructures(event);
        StructureInit.TTStructuresConfig.registerConfiguredStructures();
    }

    private static void playerJoinVersionChecker(final PlayerEvent.PlayerLoggedInEvent event){
        IFormattableTextComponent header = new StringTextComponent("[TTMLM Expansion Version Checker] STATUS : ").withStyle(TextFormatting.GOLD);
        IFormattableTextComponent outdated = header.copy().append(new StringTextComponent("OUTDATED").withStyle(TextFormatting.YELLOW));
        IFormattableTextComponent upToDate = header.copy().append(new StringTextComponent("UP-TO-DATE").withStyle(TextFormatting.GREEN));
        IFormattableTextComponent failed = header.copy().append(new StringTextComponent("FAILED").withStyle(TextFormatting.RED));

        VersionChecker.CheckResult checkResult = VersionChecker.getResult(modInfo);

        if (checkResult.status == VersionChecker.Status.OUTDATED){
            String target_version = checkResult.target.toString();
            ITextComponent log_url = ForgeHooks.newChatWithLinks(checkResult.changes.getOrDefault(checkResult.target, "NOT FOUND"));
            ITextComponent download_url = ForgeHooks.newChatWithLinks(checkResult.url);

            IFormattableTextComponent sendMsg = outdated.copy().append(new StringTextComponent(String.format("\nTarget Version: %1$s\nChangelog: ", target_version)).withStyle(TextFormatting.YELLOW));
            sendMsg.append(log_url);
            sendMsg.append(new StringTextComponent("\nAvailable at: ").withStyle(TextFormatting.YELLOW));
            sendMsg.append(download_url);
            event.getPlayer().sendMessage(sendMsg, UUID.randomUUID());
        }
        else if (checkResult.status == VersionChecker.Status.UP_TO_DATE){
            event.getPlayer().sendMessage(upToDate, UUID.randomUUID());
        }
        else if (checkResult.status == VersionChecker.Status.FAILED || checkResult.status == VersionChecker.Status.PENDING){
            event.getPlayer().sendMessage(failed, UUID.randomUUID());
        }
    }

    private static void biomeMod(final BiomeLoadingEvent event){
        //Generate ores

        WorldGenOres.onInitBiomesGen(event);


        if (!CommonConfig.structSwitch.get()) return;

        if (event.getCategory() == Biome.Category.NETHER) {
            event.getGeneration().getStructures().add(() -> StructureInit.TTStructuresConfig.CONFIG_HARD_FORTRESS);
        }
        else if (event.getCategory() == Biome.Category.ICY || event.getCategory() == Biome.Category.TAIGA){
            event.getGeneration().getStructures().add(() -> StructureInit.TTStructuresConfig.CONFIG_SNOW_DUNGEON);
        }
    }

//    private static Method GETCODEC_METHOD;
    private static void addDimensionalSpacing(final WorldEvent.Load event){
        // Config Disables Structure when off
        if (!CommonConfig.structSwitch.get()) return;

        if(event.getWorld() instanceof ServerWorld){
            TTMLM.LOGGER.info("Start Dimension Spacing");
            ServerWorld serverW = (ServerWorld) event.getWorld();

            if(serverW.getChunkSource().generator instanceof FlatChunkGenerator && serverW.dimension().equals(World.OVERWORLD)){
                return;
            }

            if (serverW.dimension().equals(World.NETHER)){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverW.getChunkSource().generator.getSettings().structureConfig());
                tempMap.putIfAbsent(StructureInit.TTStructures.HARD_FORTRESS, DimensionStructuresSettings.DEFAULTS.get(StructureInit.TTStructures.HARD_FORTRESS));
                serverW.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
            else if (serverW.dimension().equals(World.OVERWORLD)){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverW.getChunkSource().generator.getSettings().structureConfig());
                tempMap.putIfAbsent(StructureInit.TTStructures.SNOW_DUNGEON, DimensionStructuresSettings.DEFAULTS.get(StructureInit.TTStructures.SNOW_DUNGEON));
                serverW.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
            TTMLM.LOGGER.info("End Dimension Spacing");
        }
    }

    public static void onAttackVariantSword(LivingHurtEvent event){
        if (!event.getEntityLiving().level.isClientSide) {
            Object attacker = event.getSource().getDirectEntity(); // Getting whom attacked
            if (attacker instanceof LivingEntity) {
                LivingEntity Entityattacker = (LivingEntity) attacker; //If LivingEntity type is attacker
                ItemStack HeldItemStack = Entityattacker.getMainHandItem();
                Item HeldItem = HeldItemStack.getItem(); //Checking what was used to attack
                if (HeldItem instanceof IngotVariantSwords) {
                    if (Entityattacker instanceof PlayerEntity) { //If attacker is a Player
                        PlayerEntity PlayerAttacker = (PlayerEntity) Entityattacker;
                        ((IngotVariantSwords) HeldItem).onAttack(event.getEntityLiving(), PlayerAttacker, event.getAmount());
                    } else { //If attacker is not a player
                        ((IngotVariantSwords) HeldItem).onAttack(event.getEntityLiving(), null, event.getAmount());
                    }
                }
            }
        }
    }

//    public static void onCapabilitiesAttach(AttachCapabilitiesEvent<ItemStack> event){
//        Item item = event.getObject().getItem();
//        if(item instanceof ToolItem){
//            ToolItem tool = (ToolItem) item;
//            if(tool.getTier() == IngotVariantTiers.ENDER){
//                event.addCapability(ExampleMod.getID("esl_capability"), new EnderStorageLinker(event.getObject()));
//            }
//        }
//    }

    public static void checkNetherBossSpawn(BlockEvent.EntityPlaceEvent event){
        if(!event.getWorld().isClientSide()) {
            ServerWorld world = (ServerWorld) event.getWorld();
            BlockState blockState = event.getPlacedBlock();
            if(blockState.getBlock() == IngotVariants.WEAK_BLAZING_AllOY.getIngotBlock()){
                boolean activated = false;
                boolean eastWest = false;
                BlockPos blockPos = event.getPos();
                BlockPos PosUp = blockPos.above();
                BlockPos PosDown = blockPos.below();
                BlockPos PosDownDown = PosDown.below();
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
                    BlockState airState = Blocks.AIR.defaultBlockState();
                    world.setBlockAndUpdate(blockPos, airState);
                    world.setBlockAndUpdate(PosDown, airState);
                    world.setBlockAndUpdate(PosUp, airState);
                    world.setBlockAndUpdate(PosDownDown, airState);
                    if(eastWest){
                        world.setBlockAndUpdate(PosDownEast, airState);
                        world.setBlockAndUpdate(PosDownWest, airState);
                        world.setBlockAndUpdate(PosEast, airState);
                        world.setBlockAndUpdate(PosWest, airState);
                        world.setBlockAndUpdate(PosUpEast, airState);
                        world.setBlockAndUpdate(PosUpWest, airState);
                    }else {
                        world.setBlockAndUpdate(PosDownNorth, airState);
                        world.setBlockAndUpdate(PosDownSouth, airState);
                        world.setBlockAndUpdate(PosNorth, airState);
                        world.setBlockAndUpdate(PosSouth, airState);
                        world.setBlockAndUpdate(PosUpNorth, airState);
                        world.setBlockAndUpdate(PosUpSouth, airState);
                    }
                    NetherBoss boss = new NetherBoss(ModEntities.NETHER_BOSS, world);
                    boss.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    boss.finalizeSpawn(world, world.getCurrentDifficultyAt(boss.blockPosition()), SpawnReason.EVENT, null, null);
                    world.addFreshEntity(boss);
                    TTMLM.LOGGER.debug("SPAWN ATTEMPT FOR NETHER BOSS");
                }
            }
        }
    }

    static class Client extends  SideProxy {
        Client() {
            modEventBus.addListener(Client::clientSetup);
        }

        private static void clientSetup(FMLClientSetupEvent event) {
            // The Piglin Render has a map for textures, it associates resource locations with Entity Types, we need to override this
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_Z_PIGLIN, (EntityRendererManager p_i232472_1_) -> new PiglinRenderer(p_i232472_1_, new Random().nextBoolean()){
                @NotNull
                @Override
                public ResourceLocation getTextureLocation(MobEntity p_110775_1_) {
                    return new ResourceLocation("textures/entity/piglin/zombified_piglin.png");
                }
            });
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
            // Originals
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.NETHER_BOSS, NetherBossRender::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_ZOMBIE_SC, HardZombieSCRender::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_HUSK_SC, HardHuskSCRender::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_DROWNED_SC, HardDrownedSCRender::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.HARD_Z_PIGLIN_SC, HardZPiglinSCRender::new);
        }
    }

static class Server extends SideProxy {
    Server() {
        modEventBus.addListener(Server::serverSetup);
    }

    private static void serverSetup(FMLDedicatedServerSetupEvent event) {
        TTMLM.LOGGER.debug("----PHYSICAL SERVER DETECTED-----");
    }
}
}
