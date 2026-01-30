package com.example.magicmod.block.crystal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class CrystalBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(
            3, 0, 3,   // minX, minY, minZ
            13, 14, 13 // maxX, maxY, maxZ
    );
    public CrystalBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE; // shape de sélection (outline)
    }

    @Override
    protected boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        LevelReader levelReader = p_60526_;//for reading purpose
        BlockPos blockPos = p_60527_;//for reading purpose
        BlockState blockUnder = levelReader.getBlockState(blockPos.below());
        return !blockUnder.isAir() && !blockUnder.is(this);
    }

    @Override
    protected void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos p_60511_, Block p_60512_, @Nullable Orientation p_364486_, boolean p_60514_) {
        super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_364486_, p_60514_);
        if(!p_60510_.isClientSide()) {
            if(p_60510_.getBlockState(p_60511_.below()).isAir()) {
                p_60510_.destroyBlock(p_60511_, false);
            }
        }
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE; // shape de collision (physique)
    }
}
