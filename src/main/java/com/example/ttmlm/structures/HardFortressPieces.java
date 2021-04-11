package com.example.ttmlm.structures;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.StructureInit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class HardFortressPieces {

    private static final ResourceLocation SMALL_ENTRANCE = TTMLM.getID("fortress/fortress_small_entrance");
    private static final ResourceLocation ENTRANCE = TTMLM.getID("fortress/fortress_big_entrance");
    private static final ResourceLocation TRANSITION = TTMLM.getID("fortress/fortress_transition");
    private static final ResourceLocation SMALL_TREASURE = TTMLM.getID("fortress/fortress_small_treasure_deadend");
    private static final ResourceLocation SMALL_HALLWAY = TTMLM.getID("fortress/fortress_small_hallway_01");
    private static final ResourceLocation OUTSIDE_TURN = TTMLM.getID("fortress/fortress_outside_turn");
    private static final ResourceLocation INSIDE_TURN = TTMLM.getID("fortress/fortress_inside_turn");
    private static final ResourceLocation HALLWAY_03 = TTMLM.getID("fortress/fortress_hallway_03");
    private static final ResourceLocation HALLWAY_02 = TTMLM.getID("fortress/fortress_hallway_02");
    private static final ResourceLocation HALLWAY_01 = TTMLM.getID("fortress/fortress_hallway_01");
    private static final ResourceLocation DEADEND_SPAWNER = TTMLM.getID("fortress/fortress_deadend_spawner");
    private static final ResourceLocation DEADEND_CHEST = TTMLM.getID("fortress/fortress_deadend_chest");
    private static final ResourceLocation BOSS_ROOM = TTMLM.getID("fortress/fortress_boss_room");
    private static final IndividualPiece SmallEntrance = new IndividualPiece(SMALL_ENTRANCE, 5, 1,5, true);
    private static final IndividualPiece Entrance = new IndividualPiece(ENTRANCE, 6, 1,9, false);
    private static final IndividualPiece Transition = new IndividualPiece(TRANSITION, 6, 7,9, false);
    private static final IndividualPiece SmallTreasure = new IndividualPiece(SMALL_TREASURE, 8, 6,5, true);
    private static final IndividualPiece SmallHallway = new IndividualPiece(SMALL_HALLWAY, 14, 5,5, true);
    private static final IndividualPiece OutsideTurn = new IndividualPiece(OUTSIDE_TURN, 8, 5,8, true);
    private static final IndividualPiece InsideTurn = new IndividualPiece(INSIDE_TURN, 8, 5,8, true);
    private static final IndividualPiece Hallway03 = new IndividualPiece(HALLWAY_03, 16, 7,9, false);
    private static final IndividualPiece Hallway02 = new IndividualPiece(HALLWAY_02, 9, 7,9, false);
    private static final IndividualPiece Hallway01 = new IndividualPiece(HALLWAY_01, 9, 8,9, false);
    private static final IndividualPiece DeadendSpawner = new IndividualPiece(DEADEND_SPAWNER, 11, 7,9, false);
    private static final IndividualPiece DeadendChest = new IndividualPiece(DEADEND_CHEST, 11, 7,9, false);
    private static final IndividualPiece BossRoom = new IndividualPiece(BOSS_ROOM, 28, 11,17, false);

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random){
        final int totalPieces = 25;
        final int x = pos.getX();
        final int z = pos.getZ();
        int chunkCounter = 0;
        int[] totalDisplacement = {0, 0}; //X , Z,
        BlockPos rotationOffset;
        BlockPos blockPos;
        IndividualPiece prevPiece;

        // Do math to make sure it doest extend for more than 9 chunks ie. 8th chunk has to be a turn.
        // Might want to change dead ends to make it more obvious they have stiff behind them

        if(random.nextBoolean()){
            rotationOffset = new BlockPos(0,0,2).rotate(rotation);
            blockPos = rotationOffset.offset(x, pos.getY(), z);
            pieceList.add(new HardFortressPieces.Piece(templateManager, SMALL_ENTRANCE, blockPos, rotation));
            prevPiece = SmallEntrance;
        }else {
            rotationOffset = new BlockPos(0,0,0).rotate(rotation);
            blockPos = rotationOffset.offset(x, pos.getY(), z);
            pieceList.add(new HardFortressPieces.Piece(templateManager, ENTRANCE, blockPos, rotation));
            prevPiece = Entrance;
        }
        IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
        chunkCounter += prevPiece.x;

        for(int i = 0; i < totalPieces; i++){
            int select = random.nextInt(100);
            //Force a turn piece so structure stays with in 18x18 area where start pos is the center
            if((float)chunkCounter/16 >= 6.5){
                chunkCounter = 0;
                if(!prevPiece.small) {
                    prevPiece = IndividualPiece.addTranstion(totalDisplacement[1], totalDisplacement[0], rotation, x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                if(random.nextBoolean()){
                    prevPiece = IndividualPiece.addInsideTurn(totalDisplacement[1], totalDisplacement[0], rotation, x, z, pos, pieceList, templateManager);
                }else {
                    prevPiece = IndividualPiece.addOutsideTurn(totalDisplacement[1], totalDisplacement[0], rotation, x, z, pos, pieceList, templateManager);
                }
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            //Small Treasure
            else if(select <= 4){ // 5%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(!prevPiece.small) {
                    prevPiece = IndividualPiece.addTranstion(totalDisplacement[1], totalDisplacement[0], rotation, x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1] + 2).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, SMALL_TREASURE, blockPos, rotation));
                prevPiece = SmallTreasure;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            //Small Hallways
            else if(select <= 29){ //25%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(!prevPiece.small) {
                    prevPiece = IndividualPiece.addTranstion(totalDisplacement[1], totalDisplacement[0], rotation, x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1] + 2).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, SMALL_HALLWAY, blockPos, rotation));
                prevPiece = SmallHallway;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            //Hallway 03 common large
            else if(select <= 54){ //25%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(prevPiece.small){
                    // flipped 180 to fit better, displacement values changed accordingly
                    prevPiece = IndividualPiece.addTranstion(-totalDisplacement[1] - 8, -totalDisplacement[0] - 5, rotation.getRotated(Rotation.CLOCKWISE_180), x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1]).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, HALLWAY_03, blockPos, rotation));
                prevPiece = Hallway03;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            //Hallway 02 large
            else if(select <= 69){ //15%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(prevPiece.small){
                    // flipped 180 to fit better, displacement values changed accordingly
                    prevPiece = IndividualPiece.addTranstion(-totalDisplacement[1] - 8, -totalDisplacement[0] - 5, rotation.getRotated(Rotation.CLOCKWISE_180), x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1]).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, HALLWAY_02, blockPos, rotation));
                prevPiece = Hallway02;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            //Hallway 01 large
            else if(select <= 74){ //5%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(prevPiece.small){
                    // flipped 180 to fit better, displacement values changed accordingly
                    prevPiece = IndividualPiece.addTranstion(-totalDisplacement[1] - 8, -totalDisplacement[0] - 5, rotation.getRotated(Rotation.CLOCKWISE_180), x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1]).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY()-1, z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, HALLWAY_01, blockPos, rotation));
                prevPiece = Hallway01;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            // Dead end spawner Large
            else if(select <= 89){ //15%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(prevPiece.small){
                    // flipped 180 to fit better, displacement values changed accordingly
                    prevPiece = IndividualPiece.addTranstion(-totalDisplacement[1] - 8, -totalDisplacement[0] - 5, rotation.getRotated(Rotation.CLOCKWISE_180), x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1]).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, DEADEND_SPAWNER, blockPos, rotation));
                prevPiece = DeadendSpawner;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            // Dead end Chest Large
            else if(select <= 99){ //10%
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(prevPiece.small){
                    // flipped 180 to fit better, displacement values changed accordingly
                    prevPiece = IndividualPiece.addTranstion(-totalDisplacement[1] - 8, -totalDisplacement[0] - 5, rotation.getRotated(Rotation.CLOCKWISE_180), x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1]).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, DEADEND_CHEST, blockPos, rotation));
                prevPiece = DeadendChest;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
            /*default just in case nothing is selected
            small hallway*/
            else{
                if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
                    rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
                    IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
                }
                if(!prevPiece.small) {
                    prevPiece = IndividualPiece.addTranstion(totalDisplacement[1], totalDisplacement[0], rotation, x, z, pos, pieceList, templateManager);
                    IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                    chunkCounter += prevPiece.x;
                }
                rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1] + 2).rotate(rotation);
                blockPos = rotationOffset.offset(x, pos.getY(), z);
                pieceList.add(new HardFortressPieces.Piece(templateManager, SMALL_HALLWAY, blockPos, rotation));
                prevPiece = SmallHallway;
                IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
                chunkCounter += prevPiece.x;
            }
        }
        //Boss room
        if(prevPiece == OutsideTurn || prevPiece == InsideTurn){
            rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
            IndividualPiece.initialUpdateDisplacement(totalDisplacement, prevPiece);
        }
        if(prevPiece.small){
            // flipped 180 to fit better, displacement values changed accordingly
            prevPiece = IndividualPiece.addTranstion(-totalDisplacement[1] - 8, -totalDisplacement[0] - 5, rotation.getRotated(Rotation.CLOCKWISE_180), x, z, pos, pieceList, templateManager);
            IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
        }
        rotationOffset = new BlockPos(totalDisplacement[0], 0, totalDisplacement[1] - 4).rotate(rotation);
        blockPos = rotationOffset.offset(x, pos.getY(), z);
        pieceList.add(new HardFortressPieces.Piece(templateManager, BOSS_ROOM, blockPos, rotation));
        IndividualPiece.updateDisplacement(prevPiece, totalDisplacement);
    }

    static class IndividualPiece{
        public int x;
        public int z;
        public int y;
        public ResourceLocation location;
        public boolean small;
        IndividualPiece(ResourceLocation locationIn, int xIn, int yIn, int zIn, boolean smallIn){
            location = locationIn;
            x = xIn;
            y = yIn;
            z = zIn;
            small = smallIn;
        }

        public static IndividualPiece addTranstion(int totalDisplacementZ, int totalDisplacementX, Rotation rotation, int x, int z, BlockPos pos, List<StructurePiece> pieceList, TemplateManager templateManager){
            BlockPos rotationOffset = new BlockPos(totalDisplacementX, 0, totalDisplacementZ).rotate(rotation);
            BlockPos blockPos = rotationOffset.offset(x, pos.getY(), z);
            pieceList.add(new HardFortressPieces.Piece(templateManager, TRANSITION, blockPos, rotation));
            return Transition;
        }

        public static IndividualPiece addOutsideTurn(int totalDisplacementZ, int totalDisplacementX, Rotation rotation, int x, int z, BlockPos pos, List<StructurePiece> pieceList, TemplateManager templateManager){
            BlockPos rotationOffset = new BlockPos(totalDisplacementX, 0, totalDisplacementZ + 2).rotate(rotation);
            BlockPos blockPos = rotationOffset.offset(x, pos.getY(), z);
            pieceList.add(new HardFortressPieces.Piece(templateManager, OUTSIDE_TURN, blockPos, rotation));
            return OutsideTurn;
        }

        public static IndividualPiece addInsideTurn(int totalDisplacementZ, int totalDisplacementX, Rotation rotation, int x, int z, BlockPos pos, List<StructurePiece> pieceList, TemplateManager templateManager){
            BlockPos rotationOffset = new BlockPos(totalDisplacementX, 0, totalDisplacementZ + 2).rotate(rotation);
            BlockPos blockPos = rotationOffset.offset(x, pos.getY(), z);
            pieceList.add(new HardFortressPieces.Piece(templateManager, INSIDE_TURN, blockPos, rotation));
            return InsideTurn;
        }

        public static void updateDisplacement(IndividualPiece prev, int[] displacements){
            displacements[0] += prev.x;
        }

        public static void initialUpdateDisplacement(int[] displacements, IndividualPiece prev){
            int temp;
            temp = displacements[0];
            displacements[0] = displacements[1] + prev.z;
            displacements[1] = -temp - 1;
        }
    }

    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation resourceLocation;
        private Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(StructureInit.TTStructures.HFP, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = new BlockPos(0, 1, 0);
            this.templatePosition = pos.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound)
        {
            super(StructureInit.TTStructures.HFP, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        private void setupPiece(TemplateManager templateManager)
        {
            Template template = templateManager.getOrCreate(this.resourceLocation);
            PlacementSettings placementsettings = new PlacementSettings().setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void addAdditionalSaveData(CompoundNBT p_143011_1_) {
            super.addAdditionalSaveData(p_143011_1_);
            p_143011_1_.putString("Template", this.resourceLocation.toString());
            p_143011_1_.putString("Rot", this.rotation.name());
        }

        //        @Override
//        protected void readAdditional(CompoundNBT tagCompound)
//        {
//            super.readAdditional(tagCompound);
//            tagCompound.putString("Template", this.resourceLocation.toString());
//            tagCompound.putString("Rot", this.rotation.name());
//        }

        @Override
        protected void handleDataMarker(@NotNull String p_186175_1_, @NotNull BlockPos p_186175_2_, @NotNull IServerWorld p_186175_3_, @NotNull Random p_186175_4_, MutableBoundingBox p_186175_5_) {

        }

//        @Override
//        public boolean create(@NotNull IWorld worldIn, ChunkGenerator<?> p_225577_2_, @NotNull Random randomIn, @NotNull MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPos)
//        {
//            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
//            BlockPos blockpos = new BlockPos(0,1,0);
//            this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(-blockpos.getX(), 0, -blockpos.getZ())));
//
//            return super.create(worldIn, p_225577_2_, randomIn, structureBoundingBoxIn, chunkPos);
//        }
    }
}
