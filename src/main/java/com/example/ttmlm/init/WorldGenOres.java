package com.example.ttmlm.init;

import com.example.ttmlm.config.CommonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;



public class WorldGenOres {

    public static final RuleTest END_REPLACEABLE = new BlockMatchRuleTest(Blocks.END_STONE);

    public static void onInitBiomesGen(final BiomeLoadingEvent event) {
        if (!CommonConfig.oreSwitch.get()) return; // Config

        for (CoalVariants coalVariants : CoalVariants.values()) {
            // If in nether and correct Enum
            if (event.getCategory() == Biome.Category.NETHER && coalVariants == CoalVariants.BLAZING_CARBON) {
                // Add features
                genOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NETHER_ORE_REPLACEABLES,
                            coalVariants.getOreBlocks().defaultBlockState(),
                            8,
                            4,
                            128,
                            8);
                    // If in cold place and correct Enum
            } else if ((event.getCategory() == Biome.Category.ICY || event.getCategory() == Biome.Category.TAIGA) && coalVariants == CoalVariants.FREEZING_CARBON) {
                    //Add features
                genOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            coalVariants.getOreBlocks().defaultBlockState(),
                            9,
                            30,
                            128,
                            12);
            }
        }
            // Mutable Ore Gen in End
        genOre(event.getGeneration(), END_REPLACEABLE,
                    ModBlocks.mutableAlloyOreEnd.defaultBlockState(),
                    15,
                    0,
                    255,
                    15);
            // Mutable Ore Gen in Nether
        genOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NETHER_ORE_REPLACEABLES,
                    ModBlocks.mutableAlloyOreNether.defaultBlockState(),
                    10,
                    4,
                    255,
                    10);
            // Mutable Ore in Overworld
        genOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                    ModBlocks.mutableAlloyOre.defaultBlockState(),
                    5,
                    2,
                    128,
                    10);
    }

    private static void genOre (BiomeGenerationSettingsBuilder biome, RuleTest fillT, BlockState state, int veinSize, int minHeight, int maxHeight, int common){ // 8, 4, 128, 8

        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                        .configured(new OreFeatureConfig(fillT, state, veinSize))
                        .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .squared()
                        .count(common));
    }
}

