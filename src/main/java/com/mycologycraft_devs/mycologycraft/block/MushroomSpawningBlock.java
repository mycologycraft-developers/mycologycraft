package com.mycologycraft_devs.mycologycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MushroomSpawningBlock extends Block {

    public MushroomSpawningBlock(BlockBehaviour.Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);
        // Custom logic for mushroom spawning can be added here

        if (random.nextInt(100) < 100) { // 100% chance to spawn a mushroom
            // sspawn a custom mushroom block above if the space is empty
            BlockPos abovePos = pos.above();
            if (world.isEmptyBlock(abovePos)) {
                world.setBlockAndUpdate(abovePos, ModBlocks.FEATURELESS_DOUBLE_MUSHROOM_BLOCK.get().defaultBlockState());
            }
        }
    }
}