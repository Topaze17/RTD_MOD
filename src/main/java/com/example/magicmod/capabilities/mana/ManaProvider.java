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

    private final Mana mana = new Mana(1000, 1000);
    private final LazyOptional<IMana> optional = LazyOptional.of(() -> mana);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.MANA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registryAccess) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Mana", mana.getMana());
        tag.putInt("MaxMana", mana.getMaxMana());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registryAccess, CompoundTag tag) {
        if (tag.getInt("Mana").isPresent()) mana.setMana(tag.getInt("Mana").get());
        if (tag.getInt("MaxMana").isPresent()) mana.setMaxMana(tag.getInt("MaxMana").get());
    }

    public void invalidate() {
        optional.invalidate();
    }
}


