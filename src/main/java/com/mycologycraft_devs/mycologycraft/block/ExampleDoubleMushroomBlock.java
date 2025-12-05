package com.mycologycraft_devs.mycologycraft.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExampleDoubleMushroomBlock extends ExampleMushroomBlock //im doing this because we will disable some vanilla features of the mushroom (MushroomBlock), and theres no reason to duplicate that code
{
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	protected static final VoxelShape UPPER_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
	protected static final VoxelShape LOWER_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			return LOWER_SHAPE;
		} else {
			return UPPER_SHAPE;
		}
	}
	public ExampleDoubleMushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Properties properties)
	{
		super(feature, properties);
		registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
		
		if (facing.getAxis() != Direction.Axis.Y //if update is not vertical, check
		|| (doubleblockhalf == DoubleBlockHalf.LOWER) != (facing == Direction.UP) // read the != like a XOR: half is LOWER and facing is UP, or half is UPPER and facing is DOWN: != is false. basically if UPPER HALF gets from UP or LOWER HALF gets from DOWN, state is valid, proceed check
		|| (facingState.is(this) && (facingState.getValue(HALF) != doubleblockhalf))) { //if you get updateshape from same type, different half, its ok, proceed check (at this point the direction is confirmed to be vertical (first condition) and matching the halves(second condition))
		return (doubleblockhalf == DoubleBlockHalf.LOWER) && (facing == Direction.DOWN) && (!state.canSurvive(level, currentPos)) //if you are LOWER half, getting update from DOWN, and can NOT survive: 
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, facing, facingState, level, currentPos, facingPos);
		} else {
				return Blocks.AIR.defaultBlockState();
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
			BlockPos blockpos = context.getClickedPos();
			Level level = context.getLevel();
			return (blockpos.getY() < (level.getMaxBuildHeight() - 1)) && level.getBlockState(blockpos.above()).canBeReplaced(context) //can place if there is space above (build limit +AND replaceable)
					? super.getStateForPlacement(context) //returns default placement state (which is LOWER half)
					: null;
	}

	public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
		return state.hasProperty(BlockStateProperties.WATERLOGGED)
				? state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(level.isWaterAt(pos)))
				: state;
	}

	/**
	* Called by BlockItem after this block has been placed.
	*/
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		//also place the upper half	(the lower half is placed by the BlockItem)
		BlockPos blockposabove = pos.above();
			level.setBlock(blockposabove, copyWaterloggedFrom(level, blockposabove, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);
	}

	/**
	* Places both halves of the double block.
	* Useful when the block is placed by other means than a player placing it with a BlockItem.
	*/
	public static void placeAt(LevelAccessor level, BlockState state, BlockPos pos, int flags) {
		BlockPos blockposabove = pos.above();
		level.setBlock(pos, copyWaterloggedFrom(level, pos, state.setValue(HALF, DoubleBlockHalf.LOWER)), flags);
		level.setBlock(blockposabove, copyWaterloggedFrom(level, blockposabove, state.setValue(HALF, DoubleBlockHalf.UPPER)), flags);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
			return super.canSurvive(state, level, pos); //lower half uses super canSurvive
		} else {
			BlockState belowblockstate = level.getBlockState(pos.below()); //upper half needs to check block below
			if (state.getBlock() != this) return super.canSurvive(state, level, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return belowblockstate.is(this) && belowblockstate.getValue(HALF) == DoubleBlockHalf.LOWER; //upper half can survive only if below is lower half of same block
		}
	}
	
	@Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventDropFromBottomPart(level, pos, state, player);
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * Called after a player has successfully harvested this block. This method will only be called if the player has used the correct tool and drops should be spawned.
     */
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), te, stack);
    }

    protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockposbelow = pos.below();
            BlockState blockstatebelow = level.getBlockState(blockposbelow);
            if (blockstatebelow.is(state.getBlock()) && blockstatebelow.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState newblockstatebelow = blockstatebelow.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockposbelow, newblockstatebelow, 35); //this sets the block WITHOUT harvesting/dropping
                level.levelEvent(player, 2001, blockposbelow, Block.getId(blockstatebelow)); //but it plays the block break event
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    /**
     * Return a random long to be passed to {@link net.minecraft.client.resources.model.BakedModel#getQuads}, used for random model rotations
     */
    @Override
    protected long getSeed(BlockState state, BlockPos pos) {
		//get the seed of the pos below (.below(1)) for upper half, proper pos (.below(0)) for lower half
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }
}
