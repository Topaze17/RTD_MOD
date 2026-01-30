package com.example.magicmod.block;

import com.example.magicmod.block.crystal.FireCrystalBlock;
import com.example.magicmod.block.crystal.ManaCrystalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.example.magicmod.MagicMod.MODID;

public class ModBlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> MANA_CRYSTAL_BLOCK =
            BLOCKS.register("mana_crystal_block",
                    () -> new ManaCrystalBlock(BlockBehaviour.Properties
                            .of()
                            .strength(10f)
                            .sound(SoundType.AMETHYST)
                            .setId(BLOCKS.key("mana_crystal_block"))
                    ));
    public static final RegistryObject<Block> FIRE_CRYSTAL_BLOCK =
            BLOCKS.register("fire_crystal_block",
                    () -> new FireCrystalBlock(BlockBehaviour.Properties
                            .of()
                            .strength(10f)
                            .sound(SoundType.AMETHYST)
                            .setId(BLOCKS.key("fire_crystal_block"))
                            .lightLevel(state -> 15)
                    ));
}
