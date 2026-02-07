package com.example.magicmod.capabilities;


import com.example.magicmod.capabilities.mana.CapabilityMana;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
/**
 * Class to generate Capabilities token
 * and store them as to be able to manage custom capabilities
 * <p>
 * @author Topaze17
 */
public class ModCapabilities {
    public static final Capability<CapabilityMana> MANA =
            CapabilityManager.get(new CapabilityToken<>() {});
}


