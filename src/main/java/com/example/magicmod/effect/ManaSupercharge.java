package com.example.magicmod.effect;

import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.network.Sync;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mana Supercharge effect:
 * - Total duration: 800 ticks (40s)
 * - First 400 ticks: doubles max mana and blocks mana regeneration
 * - Last 400 ticks: restores max mana and continues to block regeneration
 */
public class ManaSupercharge extends MobEffect {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Store original max mana per player (thread-safe)
    private static final Map<UUID, Integer> ORIGINAL_MAX_MANA = new ConcurrentHashMap<>();
    // Track current phase per player to handle reapplication correctly
    private enum Phase { SUPERCHARGE, REGEN_BLOCK }
    private static final Map<UUID, Phase> PHASE = new ConcurrentHashMap<>();

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
     * Apply effect: store original max mana and double it.
     * Reapplication while effect active (any phase) will not grant extra current mana.
     */
    @Override
    public void onEffectStarted(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof ServerPlayer player) {
            LOGGER.info("ManaSupercharge started for player: {}", player.getName().getString());
            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                UUID uid = player.getUUID();

                // Atomically mark the player as in SUPERCHARGE phase if not already present.
                // If putIfAbsent returns non-null, a phase was already tracked -> reapplication.
                Phase previous = PHASE.putIfAbsent(uid, Phase.SUPERCHARGE);
                if (previous != null) {
                    LOGGER.info("Reapplication detected for {} - resetting duration only", player.getName().getString());
                    return;
                }

                // First application: record original and apply supercharge
                int currentMaxMana = mana.getMaxMana();
                ORIGINAL_MAX_MANA.put(uid, currentMaxMana);

                // Double max mana and grant extra current mana
                mana.addMaxMana(player, currentMaxMana);
                mana.addMana(player, currentMaxMana);

                LOGGER.info("First application: new max mana: {}, current mana: {}", mana.getMaxMana(), mana.getMana());
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
                // Atomically restore & remove the stored original if present (safe if this method runs multiple times)
                ORIGINAL_MAX_MANA.computeIfPresent(player.getUUID(), (uuid, originalMaxMana) -> {
                    // switch phase to regen-block before restoration
                    PHASE.put(player.getUUID(), Phase.REGEN_BLOCK);

                    mana.setMaxMana(originalMaxMana);
                    if (mana.getMana() > originalMaxMana) {
                        mana.setMana(originalMaxMana);
                    }
                    Sync.syncManaTo(player);
                    LOGGER.info("Transition restore: max mana set to {} for {}", originalMaxMana, player.getName().getString());

                    // return null to remove the entry from the map
                    return null;
                });
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
            LOGGER.info("ManaSupercharge removed for player: {}", player.getName().getString());
            // As a safe fallback, atomically restore & remove if still present
            ORIGINAL_MAX_MANA.computeIfPresent(player.getUUID(), (uuid, originalMaxMana) -> {
                player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                    mana.setMaxMana(originalMaxMana);
                    if (mana.getMana() > originalMaxMana) {
                        mana.setMana(originalMaxMana);
                    }
                    // Sync the changes to the client
                    Sync.syncManaTo(player);
                    LOGGER.info("Fallback restore on removal: max mana set to {} for {}", originalMaxMana, player.getName().getString());
                });
                // return null to remove the entry from the map
                return null;
            });

            // Clean up phase tracking
            PHASE.remove(player.getUUID());
        }
    }

    /**
     * Cleanup stored data for a player UUID (called on player logout) to avoid memory leaks.
     */
    public static void cleanupFor(UUID playerUuid) {
        ORIGINAL_MAX_MANA.remove(playerUuid);
        PHASE.remove(playerUuid);
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

    /**
     * Return true while effect active to block mana regeneration.
     */
    public static boolean isInRegenBlockPhase(ServerPlayer player) {
        var effect = player.getEffect(ModEffects.MANA_SUPERCHARGE.getHolder().get());
        return effect != null;
    }
}
