package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.structures.HardFortress;
import com.example.ttmlm.structures.HardFortressPieces;
import com.example.ttmlm.structures.SnowDungeon;
import com.example.ttmlm.structures.SnowDungeonPieces;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;

public class StructureInit {
    public static Structure<NoFeatureConfig> HARD_FORTRESS = new HardFortress(NoFeatureConfig::deserialize);
    public static IStructurePieceType HFP = HardFortressPieces.Piece::new;
    public static Structure<NoFeatureConfig> Snow_Dungeon = new SnowDungeon(NoFeatureConfig::deserialize);
    public static IStructurePieceType SDP = SnowDungeonPieces.Piece::new;

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event){
        if (!event.getName().equals(ForgeRegistries.FEATURES.getRegistryName())) return;
        IForgeRegistry<Feature<?>> registry = event.getRegistry();

        TTMLM.register(registry, HARD_FORTRESS, "hard_fortress");
        register(HFP, "HFP");
        TTMLM.register(registry, Snow_Dungeon, "snow_dungeon");
        register(SDP, "SDP");
    }

    static IStructurePieceType register(IStructurePieceType structurePiece, String key){
       return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
    }
}
