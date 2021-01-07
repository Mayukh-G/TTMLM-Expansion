package com.example.ttmlm.structures;

import com.example.ttmlm.TTMLM;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Function;

public class SnowDungeon extends Structure<NoFeatureConfig> {
    public SnowDungeon(Function<Dynamic<?>, ? extends NoFeatureConfig> config){
        super(config);
    }

    @NotNull
    @Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int maxDistance = 15;
        int minDistance = 10;

        int xTemp = x + maxDistance * spacingOffsetsX;
        int ztemp = z + maxDistance * spacingOffsetsZ;
        int xTemp2 = xTemp < 0 ? xTemp - maxDistance + 1 : xTemp;
        int zTemp2 = ztemp < 0 ? ztemp - maxDistance + 1 : ztemp;
        int validChunkX = xTemp2 / maxDistance;
        int validChunkZ = zTemp2 / maxDistance;

        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), validChunkX, validChunkZ, this.getSeedModifier());
        validChunkX = validChunkX * maxDistance;
        validChunkZ = validChunkZ * maxDistance;
        validChunkX = validChunkX + random.nextInt(maxDistance - minDistance);
        validChunkZ = validChunkZ + random.nextInt(maxDistance - minDistance);

        return new ChunkPos(validChunkX, validChunkZ);
    }

    private int getSeedModifier() {
        return 17362648;
    }

    @Override
    public boolean canBeGenerated(@NotNull BiomeManager biomeManagerIn, @NotNull ChunkGenerator<?> generatorIn, @NotNull Random randIn, int chunkX, int chunkZ, @NotNull Biome biomeIn) {
        ChunkPos chunkpos = this.getStartPositionForPosition(generatorIn, randIn, chunkX, chunkZ, 0, 0);
        if(chunkpos.x == chunkX && chunkpos.z == chunkZ){
            if(randIn.nextInt(10) <= 3){
                return generatorIn.hasStructure(biomeIn, this);
            }
        }
        return false;
    }

    @NotNull
    @Override
    public IStartFactory getStartFactory() {
        return SnowDungeon.Start::new;
    }

    @NotNull
    @Override
    public String getStructureName() {
        return TTMLM.MOD_ID + "snow_dungeon";
    }

    @Override
    public int getSize() {
        return 8;
    }

    public static class Start extends StructureStart {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn)
        {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void init(@NotNull ChunkGenerator<?> generator, @NotNull TemplateManager templateManagerIn, int chunkX, int chunkZ, @NotNull Biome biomeIn) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);

            BlockPos blockpos = new BlockPos(x, surfaceY, z);
            SnowDungeonPieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

            this.recalculateStructureSize();

            if (TTMLM.isDevBuild()) TTMLM.LOGGER.log(Level.DEBUG, "SnowDungeon at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }

    }
}
