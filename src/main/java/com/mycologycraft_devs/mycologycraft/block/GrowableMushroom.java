package com.mycologycraft_devs.mycologycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GrowableMushroom extends Block{


    //0 is base mushroom
    //1 is head of big mushroom
    //2 is stalk of big mushroom
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 2);
    public GrowableMushroom(Properties properties) {
        super(properties
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

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(oldState, world, pos, newState, isMoving);

        if(oldState.getValue(GROWTH_STAGE) == 1) {
            BlockPos belowPos = pos.below();
            BlockState belowState = world.getBlockState(belowPos);
            if(belowState.getBlock() instanceof GrowableMushroom && belowState.getValue(GROWTH_STAGE) == 2) {
                //if head removed, reset stalk to base mushroom
                world.setBlock(belowPos, belowState.setValue(GROWTH_STAGE, 0), UPDATE_ALL);
            }
        } else if(oldState.getValue(GROWTH_STAGE) == 2) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = world.getBlockState(abovePos);
            if(aboveState.getBlock() instanceof GrowableMushroom && aboveState.getValue(GROWTH_STAGE) == 1) {
                //if stalk removed, remove head as well
                world.removeBlock(abovePos, false);  
            }
        }
    }

    //if you place it on a base it creates a big mushroom
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, net.minecraft.world.item.ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);

        BlockPos belowPos = pos.below();
        BlockState belowState = world.getBlockState(belowPos);
        if(belowState.getBlock() instanceof GrowableMushroom) {
            if(belowState.getValue(GROWTH_STAGE) == 0) {
                //convert to big mushroom
                world.setBlock(belowPos, belowState.setValue(GROWTH_STAGE, 2), UPDATE_ALL);
                world.setBlock(pos, state.setValue(GROWTH_STAGE, 1), UPDATE_ALL);
            }
            //dont let it be placed if below is head or stalk
            else {
                world.removeBlock(pos, false);
            }   
        }
    }

    //only placeable on blocks that can support mushrooms
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = world.getBlockState(belowPos);
        
        //Variable that stores the growth stage of THIS block
        int growthStage = state.getValue(GROWTH_STAGE);

        //If the growth stage is 0 (base mushroom) or 2 (stalk) it can be placed on any block that supports mushrooms
        if((growthStage == 0) || (growthStage == 2)) {
            if(belowState.is(Blocks.MYCELIUM)
                || belowState.is(Blocks.PODZOL)
                || belowState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
                return true;
            } else {
                return false;
            }
        }
        //if the growth stage is 1 (head) it can only be placed on top of a stalk (growth stage 2)
        else if(growthStage == 1)
        {
            if(belowState.getBlock() instanceof GrowableMushroom) {
                return belowState.getValue(GROWTH_STAGE) == 2;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, net.minecraft.core.Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        // if(!state.canSurvive(world, pos)) {
        //     return Blocks.AIR.defaultBlockState();
        // }
        if(!this.canSurvive(state, world, pos)) { //not sure about the difference
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
  
}
