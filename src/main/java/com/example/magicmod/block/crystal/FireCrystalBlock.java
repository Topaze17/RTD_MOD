package com.example.magicmod.block.crystal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

/**
 * @author Angel.
 * */
public class FireCrystalBlock extends CrystalBlock {
    public FireCrystalBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide()) return;

        if (random.nextFloat() < 1f) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            double y = pos.getY() + 0.9;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0, 0.02, 0.0);
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        return super.getDrops(p_287732_, p_287596_);
    }

    @Override
    protected void entityInside(BlockState p_394834_, Level p_393726_, BlockPos p_391291_, Entity p_394807_, InsideBlockEffectApplier p_396230_, boolean p_432053_) {
        super.entityInside(p_394834_, p_393726_, p_391291_, p_394807_, p_396230_, p_432053_);
        Level level = p_393726_; //for reading purpose
        Entity entity = p_394807_; //for reading purpose
        if (level.isClientSide()) return;
        MinecraftServer minecraftServer = level.getServer();
        if(minecraftServer == null) return;
        DamageSource damageSource = level.damageSources().inFire();
        entity.hurtServer(level.getServer().overworld(),damageSource, 1f);
    }
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level.isClientSide()) return;
        MinecraftServer minecraftServer = level.getServer();
        if(minecraftServer == null) return;
        DamageSource damageSource = level.damageSources().inFire();
        entity.hurtServer(level.getServer().overworld(),damageSource, 1f);
    }
}
