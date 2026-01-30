package com.example.magicmod.block.crystal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ManaCrystalBlock extends CrystalBlock {



    public ManaCrystalBlock(Properties props) {
        super(props);
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide()) return;

        if (random.nextFloat() < 0.1f) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            double y = pos.getY() + 0.9;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            level.addParticle(ParticleTypes.GLOW, x, y, z, 0.0, 0.02, 0.0);
        }
    }
}
