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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Class implementing {@link ICapabilitySerializable} in order to provide
 * a {@link #serializeNBT} and {@link #deserializeNBT} for our capability mana and the {@link #getCapability(Capability, Direction)}
 * <p>
 * @author Topaze17
 */
public class ManaProvider implements ICapabilitySerializable<CompoundTag> {

    private static final String MANA_KEY = "Mana";
    private static final String MAX_MANA_MODIFIERS_KEY = "MaxManaModifiers";
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
        tag.putBoolean(IS_IN_REGEN_BLOCK_KEY, mana.isInRegenBlock());

        // Serialize the maxMana modifiers HashMap
        CompoundTag modifiersTag = new CompoundTag();
        for (Map.Entry<String, Integer> entry : mana.getMaxManaModifiers().entrySet()) {
            modifiersTag.putInt(entry.getKey(), entry.getValue());
        }
        tag.put(MAX_MANA_MODIFIERS_KEY, modifiersTag);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registryAccess, CompoundTag tag) {
        // Load max mana modifiers HashMap
        if (tag.contains(MAX_MANA_MODIFIERS_KEY)) {
            tag.getCompound(MAX_MANA_MODIFIERS_KEY).ifPresent(modifiersTag -> {
                Map<String, Integer> modifiers = new HashMap<>();

                // Iterate through all keys in the CompoundTag
                for (String key : modifiersTag.keySet()) {
                    modifiersTag.getInt(key).ifPresent(value -> modifiers.put(key, value));
                }

                if (!modifiers.isEmpty()) {
                    mana.setMaxManaModifiers(modifiers);
                }
            });
        }

        // Load current mana (after maxMana to avoid incorrect clamping)
        tag.getInt(MANA_KEY).ifPresent(mana::setMana);

        // Load regen block state
        tag.getBoolean(IS_IN_REGEN_BLOCK_KEY).ifPresent(mana::setInRegenBlock);
    }

    public void invalidate() {
        optional.invalidate();
    }
}


