package com.mycologycraft_devs.mycologycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FeaturelessDoubleMushroomBlock extends MushroomBlock{
    //0 is base mushroom
    //1 is head of big mushroom
    //2 is stalk of big mushroom
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 2);
    public FeaturelessDoubleMushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Properties properties) {
        super(
            feature, 
            properties //the vanilla minecraft adds these properties in the Blocks.java file, is that better?
            .randomTicks()
            .noOcclusion()
            .noCollission()
            .sound(SoundType.GRASS)
            .instabreak() 
        );
        this.registerDefaultState(this.defaultBlockState().setValue(GROWTH_STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GROWTH_STAGE);
    }

    //move these to the top (rn here for debugging)
    protected static final VoxelShape CAP_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
    protected static final VoxelShape STALK_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int growthStage = state.getValue(GROWTH_STAGE);
       if(growthStage == 2) {
            return STALK_SHAPE;
        } else {
            return CAP_SHAPE;
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);

        if(state.getValue(GROWTH_STAGE) != 0) return;

        if(random.nextInt(100) < 50) { // 50% chance to attempt growth
            BlockPos abovePos = pos.above();
            if(world.isEmptyBlock(abovePos)) {
              world.setBlock(abovePos, state.setValue(GROWTH_STAGE, 1), UPDATE_ALL);
              world.setBlock(pos, state.setValue(GROWTH_STAGE, 2), UPDATE_ALL);
            }
        }
    }

    //removal of double mushrooms
    //stalk makes cap break
    //cap makes stalk revert to base mushroom
    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        
        if(oldState.getBlock() != newState.getBlock()){
            if(oldState.getValue(GROWTH_STAGE) == 1) {
                BlockPos belowPos = pos.below();
                BlockState belowState = world.getBlockState(belowPos);
                if(belowState.getBlock() instanceof FeaturelessDoubleMushroomBlock && belowState.getValue(GROWTH_STAGE) == 2) {
                    //if head removed, reset stalk to base mushroom
                    world.setBlock(belowPos, belowState.setValue(GROWTH_STAGE, 0), UPDATE_ALL);
                }
            } else if(oldState.getValue(GROWTH_STAGE) == 2) {
                BlockPos abovePos = pos.above();
                BlockState aboveState = world.getBlockState(abovePos);
                if(aboveState.getBlock() instanceof FeaturelessDoubleMushroomBlock && aboveState.getValue(GROWTH_STAGE) == 1) {
                    //if stalk removed, remove head as well
                    world.destroyBlock(abovePos, true);
                }
            }
        }

        super.onRemove(oldState, world, pos, newState, isMoving);
    }

    //if you place it on a base it creates a big mushroom
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, net.minecraft.world.item.ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);

        BlockPos belowPos = pos.below();
        BlockState belowState = world.getBlockState(belowPos);
        if(belowState.getBlock() instanceof FeaturelessDoubleMushroomBlock) {
            if(belowState.getValue(GROWTH_STAGE) == 0) {
                //convert to big mushroom
                world.setBlock(belowPos, belowState.setValue(GROWTH_STAGE, 2), UPDATE_ALL);
                world.setBlock(pos, state.setValue(GROWTH_STAGE, 1), UPDATE_ALL); //now handled by state for placement
            }
            //dont let it be placed if below is head or stalk
            else {
                world.removeBlock(pos, false);
            }   
        }
    }

    //override may place on to allow placing on base (mushroom only allows solid block)
    @Override
    protected boolean mayPlaceOn(BlockState groundState, BlockGetter world, BlockPos pos) {
        // can place on other double mushrooms of growth stage 0
        // or super
        if(groundState.getBlock() instanceof FeaturelessDoubleMushroomBlock) {
            return groundState.getValue(GROWTH_STAGE) == 0;
        } else {
            return super.mayPlaceOn(groundState, world, pos);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = world.getBlockState(belowPos);
        
        //Variable that stores the growth stage of THIS block
        int growthStage = state.getValue(GROWTH_STAGE);

        //if trying to place on mushroom
        if(belowState.getBlock() instanceof FeaturelessDoubleMushroomBlock) {
            int belowGrowthStage = belowState.getValue(GROWTH_STAGE);
            //cap (1) can sit on top of stalk (2)
            if(growthStage == 1) {
                return (belowGrowthStage == 2);
            }
            //allow placement from invetory
            else if(growthStage == 0) {
                return (belowGrowthStage == 0);
            }
        }
        else
        {
            //for base and stalk, use normal mushroom placement rules (BUT still not using super() bc of light level we dont need)
            if((growthStage == 0) || (growthStage == 2)) {
                return belowState.is(Blocks.MYCELIUM)
                    || belowState.is(Blocks.PODZOL)
                    || belowState.is(BlockTags.MUSHROOM_GROW_BLOCK);
            }
        }
        return false;
    }

    //performBonemeal
    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if(state.getValue(GROWTH_STAGE) == 0){ //base grows into double mushroom
            BlockPos abovePos = pos.above();
            if(world.isEmptyBlock(abovePos)) {
            world.setBlock(abovePos, state.setValue(GROWTH_STAGE, 1), UPDATE_ALL);
            world.setBlock(pos, state.setValue(GROWTH_STAGE, 2), UPDATE_ALL);
            }
        } else if(state.getValue(GROWTH_STAGE) == 2){ //stalk 
            BlockPos abovePos = pos.above();
            BlockState aboveState = world.getBlockState(abovePos);
            if(aboveState.getBlock() instanceof FeaturelessDoubleMushroomBlock && aboveState.getValue(GROWTH_STAGE) == 1) {
                //if stalk bonemealed, grow big mushroom 
                this.growMushroom(world, pos, state, random);
            }
        } else if (state.getValue(GROWTH_STAGE) == 1){ //cap
            BlockPos belowPos = pos.below();
            BlockState belowState = world.getBlockState(belowPos);
            if(belowState.getBlock() instanceof FeaturelessDoubleMushroomBlock && belowState.getValue(GROWTH_STAGE) == 2) {
                //if cap bonemealed, grow big mushroom 
                this.growMushroom(world, belowPos, belowState, random);
            }
        }
        
    }



    //
    @Override public boolean growMushroom(ServerLevel world, BlockPos pos, BlockState state, RandomSource random) {


        //remove the double mushroom blocks. then call the super method to generate the big mushroom. if it returns false, restore the double mushroom blocks
        BlockPos abovePos = pos.above();
        BlockState aboveState = world.getBlockState(abovePos);

        if(state.getBlock() instanceof FeaturelessDoubleMushroomBlock && state.getValue(GROWTH_STAGE) == 2 &&
           aboveState.getBlock() instanceof FeaturelessDoubleMushroomBlock && aboveState.getValue(GROWTH_STAGE) == 1) {
            
            //remove double mushroom blocks
            world.removeBlock(abovePos, false);
            world.removeBlock(pos, false);

            boolean success = super.growMushroom(world, pos, Blocks.BROWN_MUSHROOM.defaultBlockState(), random);
            if(!success) {
                //restore double mushroom blocks
                world.setBlock(pos, state, UPDATE_ALL);
                world.setBlock(abovePos, aboveState, UPDATE_ALL);
            }
            return success;
        }
        return false;
    }

}
