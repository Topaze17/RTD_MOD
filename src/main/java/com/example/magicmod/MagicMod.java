package com.example.magicmod;

import com.example.magicmod.block.ModBlockRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MagicMod.MODID)
public final class MagicMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "magicmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "magicmod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "magicmod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "magicmod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "magicmod:example_block", combining the namespace and path
    /**
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .setId(BLOCKS.key("example_block"))
            .mapColor(MapColor.STONE)
        )
    );
    // Creates a new BlockItem with the id "magicmod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block",
        () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().setId(ITEMS.key("example_block")))
    );

    // Creates a new food items with the id "magicmod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
        () -> new Item(new Item.Properties()
            .setId(ITEMS.key("example_item"))
            .food(new FoodProperties.Builder()
                .alwaysEdible()
                .nutrition(1)
                .saturationModifier(2f)
                .build()
            )
        )
    );
*/

    public static final RegistryObject<Item> MANA_CRYSTAL_BLOCK_ITEM =
            ITEMS.register("mana_crystal_block",
                    () -> new BlockItem(ModBlockRegister.MANA_CRYSTAL_BLOCK.get(),
                            new Item.Properties().setId(ITEMS.key("mana_crystal_block"))));
    public static final RegistryObject<Item> FIRE_CRYSTAL_BLOCK_ITEM =
            ITEMS.register("fire_crystal_block",
                    () -> new BlockItem(ModBlockRegister.FIRE_CRYSTAL_BLOCK.get(),
                            new Item.Properties().setId(ITEMS.key("fire_crystal_block"))));
    public static final RegistryObject<Item> MANA_CRYSTAL_SHARD =
            ITEMS.register("mana_crystal_shard",
                    () -> new BlockItem(ModBlockRegister.FIRE_CRYSTAL_BLOCK.get(),
                            new Item.Properties().setId(ITEMS.key("mana_crystal_shard"))));
    // Creates a creative tab with the id "magicmod:example_tab" for the example items, that is placed after the combat tab
    /**
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example items to the tab. For your own tabs, this method is preferred over the event
            }).build());
     */
    /**public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB_2 = CREATIVE_MODE_TABS.register("mana_crystal_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CRYSTAL_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CRYSTAL_BLOCK_ITEM.get()); // Add the example items to the tab. For your own tabs, this method is preferred over the event
            }).build());*/

    public MagicMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        // Register the commonSetup method for modloading
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        //BLOCKS.register(modBusGroup);
        ModBlockRegister.BLOCKS.register(modBusGroup);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modBusGroup);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modBusGroup);

        // Register the items to a creative tab

        BuildCreativeModeTabContentsEvent.BUS.addListener(MagicMod::addCreative);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block items to the building blocks tab

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(MANA_CRYSTAL_BLOCK_ITEM);
            event.accept(FIRE_CRYSTAL_BLOCK_ITEM);
            event.accept(MANA_CRYSTAL_SHARD);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
