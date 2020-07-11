package com.example.examplemod.init;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;


public class WorldGenOres {

    public static void onInitBiomesGen(){
        // Cycle through every biome in registries
        for (Biome biome : ForgeRegistries.BIOMES){
            // Cycle through custom enum
            for (CoalVariants coalVariants : CoalVariants.values()){
                // If in nether and correct Enum
                if(biome == Biomes.NETHER && coalVariants == CoalVariants.BLAZING_CARBON){
                    // Add features
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                            Feature.ORE
                                    .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, coalVariants.getOreBlocks().getDefaultState(), 8))
                                    .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8,2,2,102))));
                // If in cold place and correct Enum
                }else if(biome.getPrecipitation() == Biome.RainType.SNOW && coalVariants == CoalVariants.FREEZING_CARBON){
                    //Add features
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                            Feature.ORE
                                    .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, coalVariants.getOreBlocks().getDefaultState(), 8))
                                    .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(9,30,0,128))));

                }
            }
            // Mutable Ore Gen in End
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                    Feature.ORE
                            .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.create("END_STONE","end_stone", new BlockMatcher(Blocks.END_STONE)),ModBlocks.mutableAlloyOreEnd.getDefaultState(),15))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(15,0,0,255))));
            // Mutable Ore Gen in Nether
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                    Feature.ORE
                            .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.mutableAlloyOreNether.getDefaultState(),10))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10,2,2,255))));
            // Mutable Ore in Overworld
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                    Feature.ORE
                            .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.mutableAlloyOre.getDefaultState(),10))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(5,2,0,128))));

        }
    }
}
