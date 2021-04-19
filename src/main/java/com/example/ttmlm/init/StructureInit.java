package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.structures.HardFortress;
import com.example.ttmlm.structures.HardFortressPieces;
import com.example.ttmlm.structures.SnowDungeon;
import com.example.ttmlm.structures.SnowDungeonPieces;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;

import java.util.HashMap;
import java.util.Map;

public class StructureInit { //TODO : SOMETHING IS WRONG WITH STRCUTURES, causes the server to hang forever. Mainly HardFortress but sometimes also Snow dungeon

    public static class TTStructures{

        public static Structure<NoFeatureConfig> HARD_FORTRESS = new HardFortress(NoFeatureConfig.CODEC);
        public static Structure<NoFeatureConfig> SNOW_DUNGEON = new SnowDungeon(NoFeatureConfig.CODEC);
        public static IStructurePieceType HFP = HardFortressPieces.Piece::new;
        public static IStructurePieceType SDP = SnowDungeonPieces.Piece::new;

        public static void registerStructures(RegistryEvent.Register<Structure<?>> event){
            TTMLM.register(event.getRegistry(), HARD_FORTRESS, "hard_fortress");
            TTMLM.register(event.getRegistry(), SNOW_DUNGEON, "snow_dungeon");

            registerStructure(
                    HARD_FORTRESS, /* The instance of the structure */
                    new StructureSeparationSettings(30 /* maximum distance apart in chunks between spawn attempts */,
                            23 /* minimum distance apart in chunks between spawn attempts */,
                            67289176 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                    true);

            registerStructure(
                    SNOW_DUNGEON, /* The instance of the structure */
                    new StructureSeparationSettings(15 /* maximum distance apart in chunks between spawn attempts */,
                            8 /* minimum distance apart in chunks between spawn attempts */,
                            17362648 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                    true);

            TTStructures.registerAllPieces();

        }

        public static <F extends Structure<NoFeatureConfig>> void registerStructure(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand)
        {
            Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
            DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(DimensionStructuresSettings.DEFAULTS)
                    .put(structure, structureSeparationSettings)
                    .build();

            WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings ->{

                Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

                if (structureMap instanceof ImmutableMap){
                    Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                    tempMap.put(structure, structureSeparationSettings);
                    settings.getValue().structureSettings().structureConfig = tempMap;
                }else {
                    structureMap.put(structure, structureSeparationSettings);
                }

            });
        }

        public static void registerAllPieces() {
            registerStructurePiece(HFP, TTMLM.getID("hfp"));
            registerStructurePiece(SDP, TTMLM.getID("sdp"));
        }

        static void registerStructurePiece(IStructurePieceType structurePiece, ResourceLocation rl) {
            Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
        }
    }


    public static class TTStructuresConfig{
        public static StructureFeature<?, ?> CONFIG_HARD_FORTRESS = TTStructures.HARD_FORTRESS.configured(IFeatureConfig.NONE);
        public static StructureFeature<?, ?> CONFIG_SNOW_DUNGEON = TTStructures.SNOW_DUNGEON.configured(IFeatureConfig.NONE);

        public static void registerConfiguredStructures() {
            Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;

            Registry.register(registry, TTMLM.getID("config_hard_fortress"), CONFIG_HARD_FORTRESS);
            Registry.register(registry, TTMLM.getID("config_snow_dungeon"), CONFIG_SNOW_DUNGEON);
            // This needs to be here to avoid crashes with custom generators

            FlatGenerationSettings.STRUCTURE_FEATURES.put(TTStructures.SNOW_DUNGEON, CONFIG_SNOW_DUNGEON);
            FlatGenerationSettings.STRUCTURE_FEATURES.put(TTStructures.HARD_FORTRESS, CONFIG_HARD_FORTRESS);
        }
    }
}
