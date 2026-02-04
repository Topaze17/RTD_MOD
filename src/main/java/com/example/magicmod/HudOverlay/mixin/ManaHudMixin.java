


package com.example.magicmod.HudOverlay.mixin;

import com.example.magicmod.capabilities.mana.CapabilityMana;
import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.network.cache.ManaClientCache;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/**
 * Abstract Class intended to inject code in render function of the main minecraft code to
 * render the mana on the player GUI
 * <p>
 * @author Topaze17
 */
@Mixin(Gui.class)
public abstract class ManaHudMixin {
    /**
     * note those param description are made on guess from seeing the code of the function
     * we are injecting to
     * <p>
     * @param graphics the utility to dray
     * @param delta the time between this frame and the previous one
     * @param ci NO IDEA CAN UPDATE DESCIPTIONS BY ANYONE WHO KNOWS
     */
    @Inject(method = "renderHotbarAndDecorations", at = @At("TAIL"))
    private void magicmod$renderManaHud(GuiGraphics graphics, DeltaTracker delta, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.options.hideGui) return;
        LazyOptional<CapabilityMana> mana = mc.player.getCapability(ModCapabilities.MANA);
        if (!mc.player.isCreative() && !mc.player.isSpectator()) {
            if (mana.isPresent()) {
                mc.player.getCapability(ModCapabilities.MANA).ifPresent(m -> {
                    Component text = Component.literal("Mana: " + m.getMana() + " / " + m.getMaxMana());
                    graphics.drawString(mc.font, text, 10, 10, 0xFFFFFFFF, true);
                });
            } else {
                Component text = Component.literal("Mana: " + ManaClientCache.mana + " / " + ManaClientCache.maxMana);
                graphics.drawString(mc.font, text, 10, 10, 0xFFFFFFFF, true);
            }
        }


    }
}
