package com.example.magicmod.capabilities.mana;


import com.example.magicmod.capabilities.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**

 * <p>
 * Class implementing {@link ICapabilitySerializable} in order to provide
 * a {@link #serializeNBT} and {@link #deserializeNBT} for our capability mana and the {@link #getCapability(Capability, Direction)}
 * <p>
 * @author Topaze17
 */
public class ManaProvider implements ICapabilitySerializable<CompoundTag> {

    private static final String MANA_KEY = "Mana";
    private static final String MAX_MANA_KEY = "MaxMana";
    private static final String IS_IN_REGEN_BLOCK_KEY = "IsInRegenBlock";

    private final Mana mana = new Mana(1000, 1000);
    private final LazyOptional<CapabilityMana> optional = LazyOptional.of(() -> mana);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.MANA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registryAccess) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(MANA_KEY, mana.getMana());
        tag.putInt(MAX_MANA_KEY, mana.getMaxMana());
        tag.putBoolean(IS_IN_REGEN_BLOCK_KEY, mana.isInRegenBlock());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registryAccess, CompoundTag tag) {
        if (tag.getInt(MAX_MANA_KEY).isPresent()) mana.setMaxMana(tag.getInt(MAX_MANA_KEY).get());
        if (tag.getInt(MANA_KEY).isPresent()) mana.setMana(tag.getInt(MANA_KEY).get());
        if (tag.getBoolean(IS_IN_REGEN_BLOCK_KEY).isPresent()) mana.setInRegenBlock(tag.getBoolean(IS_IN_REGEN_BLOCK_KEY).get());
    }

    public void invalidate() {
        optional.invalidate();
    }
}


