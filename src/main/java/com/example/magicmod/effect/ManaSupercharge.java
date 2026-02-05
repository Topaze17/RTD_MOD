package com.example.magicmod.effect;

import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.capabilities.mana.NetworkMana;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Mana Supercharge effect:
 * - Total duration: 800 ticks (40s)
 * - First 400 ticks: doubles max mana and blocks mana regeneration
 * - Last 400 ticks: restores max mana and continues to block regeneration
 */
public class ManaSupercharge extends MobEffect {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Store original max mana per player
    private static final Map<UUID, Integer> ORIGINAL_MAX_MANA = new HashMap<>();

    // Transition point (400 ticks remaining)
    private static final int REGEN_BLOCK_DURATION = 400;

    public ManaSupercharge(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    /**
     * Apply effect: store original max mana, double it, and block regeneration.
     */
    @Override
    public void onEffectStarted(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof ServerPlayer player) {
            // TODO: Remove logger before production
            LOGGER.info("ManaSupercharge started for player: {}", player.getName().getString());

            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                UUID uid = player.getUUID();

                // Check if already applied (reapplication case)
                if (ORIGINAL_MAX_MANA.containsKey(uid)) {
                    return;
                }

                // First application: record original max mana
                int originalMaxMana = mana.getMaxMana();
                ORIGINAL_MAX_MANA.put(uid, originalMaxMana);

                // Double max mana and grant extra current mana (first so isn't block)
                mana.addMaxMana(player, originalMaxMana);
                mana.addMana(player, originalMaxMana);

                // Block mana regeneration for the entire duration
                if (mana instanceof NetworkMana netMana) {
                    netMana.setInRegenBlock(true);
                }
            });
        }
    }

    /**
     * Transition behavior: at 400 ticks remaining restore original max and clamp current mana.
     */
    @Override
    public boolean applyEffectTick(ServerLevel pServerLevel, LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof ServerPlayer player) {
            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                Integer originalMaxMana = ORIGINAL_MAX_MANA.get(player.getUUID());
                if (originalMaxMana == null) return;

                // Calculate and remove the bonus
                int currentMaxMana = mana.getMaxMana();
                int bonus = currentMaxMana - originalMaxMana;
                mana.addMaxMana(player, -bonus);

                // Clamp current mana if it exceeds the restored max
                if (mana.getMana() > originalMaxMana) {
                    int excessMana = mana.getMana() - originalMaxMana;
                    mana.addMana(player, -excessMana);
                }

                // Note: isInRegenBlock stays true - we still block regen in phase 2
            });
        }
        return true;
    }

    /**
     * Called when the effect is removed from an entity.
     * Restore fallback if needed and cleanup state.
     */
    public static void onEffectRemoved(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof ServerPlayer player) {
            // TODO: Remove logger before production
            LOGGER.info("ManaSupercharge removed for player: {}", player.getName().getString());

            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                Integer originalMaxMana = ORIGINAL_MAX_MANA.remove(player.getUUID());
                if (originalMaxMana == null) return;

                // Re-enable mana regeneration
                if (mana instanceof NetworkMana netMana) {
                    netMana.setInRegenBlock(false);
                } else {
                    LOGGER.warn("Mana capability for player {} is not NetworkMana; cannot clear regen block.", player.getName().getString());
                }

                // Restore original max mana if still doubled
                int currentMaxMana = mana.getMaxMana();
                int bonus = currentMaxMana - originalMaxMana;
                if (bonus > 0) {
                    mana.addMaxMana(player, -bonus);

                    // Clamp current mana if needed
                    if (mana.getMana() > originalMaxMana) {
                        int excessMana = mana.getMana() - originalMaxMana;
                        mana.addMana(player, -excessMana);
                    }
                }
            });
        }
    }

    /**
     * Cleanup stored data for a player UUID (called on player logout) to avoid memory leaks.
     */
    public static void cleanupFor(UUID playerUuid) {
        ORIGINAL_MAX_MANA.remove(playerUuid);
    }

    /**
     * Determines whether the effect should be applied on this tick.
     * Returns true only at the transition point (400 ticks remaining)
     * to restore max mana before entering the regen block phase.
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration == REGEN_BLOCK_DURATION;
    }
}
