/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.CustomLogger;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.dawn.client.ClientTicker;
import monnef.dawn.client.DawnCreativeTab;
import monnef.dawn.common.CommonProxy;
import monnef.dawn.common.PlayerWorldHandlers;
import monnef.dawn.common.Reference;
import monnef.dawn.item.ArmorModelEnum;
import monnef.dawn.item.ItemArmorDawn;
import monnef.dawn.item.ItemDawnSword;
import monnef.dawn.item.ItemGun;
import monnef.dawn.network.DawnPacketHandler;
import monnef.dawn.server.PlayerHooksServer;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.logging.Level;

import static monnef.dawn.common.Reference.CHANNEL;
import static monnef.dawn.common.Reference.ModId;
import static monnef.dawn.common.Reference.ModName;
import static monnef.dawn.common.Reference.Version;
import static monnef.dawn.item.ItemArmorDawn.ArmorType;
import static monnef.dawn.item.ItemArmorDawn.EnumArmorMaterialDawn01;
import static monnef.dawn.item.ItemGun.AmmoRequirement;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:" + monnef.core.Reference.ModId)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = CHANNEL, packetHandler = DawnPacketHandler.class)
public class DawnOfSteve {
    public static final String BLUNDERBUSS = "blunderbuss";
    public static final String BLUE_CHEST = "blueChest";
    public static final String BLUE_LEGS = "blueLegs";
    public static final String BLUE_BOOTS = "blueBoots";
    public static final String HATBLACK = "hatBlack";
    public static final String SABRE = "sabre";
    public static final String KNIFE = "knife";
    public static final String RIFLE = "rifle";
    public static final String BAYONET = "bayonet";

    @Mod.Instance(ModId)
    public static DawnOfSteve instance;

    @SidedProxy(clientSide = "monnef.dawn.client.ClientProxy", serverSide = "monnef.dawn.common.CommonProxy")
    public static CommonProxy proxy;
    public static DawnCreativeTab CreativeTab;

    public static CustomLogger Log = new CustomLogger(ModId);

    public static ItemGun blunderbuss;
    public static ItemGun rifle;
    public static ItemGun bayonet;

    public static ItemArmorDawn hatBlack;
    public static ItemArmorDawn blueChest;
    public static ItemArmorDawn blueBoots;
    public static ItemArmorDawn blueLegs;
    public static ItemDawnSword sabre;
    public static ItemDawnSword knife;

    private int renderIndexArmor01;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        ServerPlayerAPI.register(ModId, PlayerHooksServer.class);
        proxy.onPreLoad();

        CreativeTab = new DawnCreativeTab(ModId);
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        try {
            IDProvider provider = new IDProvider(2700, 17500);
            config.load();
            provider.linkWithConfig(config);

            createItems(provider);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod \"" + Reference.ModName + "\" can't read config file.");
        } finally {
            config.save();
        }
    }

    private void createItems(IDProvider provider) {
        int clipSize = MonnefCorePlugin.debugEnv ? 20 : 1;

        blunderbuss = new ItemGun(provider.getItemIDFromConfig(BLUNDERBUSS));
        blunderbuss.initBasic(AmmoRequirement.BULLETS_SMALL, clipSize, 10, 10);
        RegistryUtils.registerItem(blunderbuss, BLUNDERBUSS, "Blunderbuss");

        rifle = new ItemGun(provider.getItemIDFromConfig(RIFLE));
        rifle.initBasic(AmmoRequirement.BULLETS_SMALL, clipSize, 30, 10);
        RegistryUtils.registerItem(rifle, RIFLE, "Rifle");

        bayonet = new ItemGun(provider.getItemIDFromConfig(BAYONET));
        bayonet.initBasic(AmmoRequirement.BULLETS_SMALL, clipSize, 20, 10).setHitCollDown(30).setMeleeDamage(8);
        RegistryUtils.registerItem(bayonet, BAYONET, "Rifle with Bayonet");

        renderIndexArmor01 = proxy.addArmor("armorBlue");

        hatBlack = new ItemArmorDawn(provider.getItemIDFromConfig(HATBLACK), EnumArmorMaterialDawn01, renderIndexArmor01, ArmorType.helm, "/armor_blank.png", ArmorModelEnum.HAT);
        RegistryUtils.registerItem(hatBlack, HATBLACK, "Black Hat");

        blueChest = new ItemArmorDawn(provider.getItemIDFromConfig(BLUE_CHEST), EnumArmorMaterialDawn01, renderIndexArmor01, ArmorType.chest, "/armor01a.png", ArmorModelEnum.NONE);
        RegistryUtils.registerItem(blueChest, BLUE_CHEST, BLUE_CHEST);
        blueBoots = new ItemArmorDawn(provider.getItemIDFromConfig(BLUE_BOOTS), EnumArmorMaterialDawn01, renderIndexArmor01, ArmorType.boots, "/armor01a.png", ArmorModelEnum.NONE);
        RegistryUtils.registerItem(blueBoots, BLUE_BOOTS, BLUE_BOOTS);
        blueLegs = new ItemArmorDawn(provider.getItemIDFromConfig(BLUE_LEGS), EnumArmorMaterialDawn01, renderIndexArmor01, ArmorType.leggings, "/armor01b.png", ArmorModelEnum.NONE);
        RegistryUtils.registerItem(blueLegs, BLUE_LEGS, BLUE_LEGS);

        sabre = new ItemDawnSword(provider.getItemIDFromConfig(SABRE), 6);
        RegistryUtils.registerItem(sabre, SABRE, "Sabre");
        knife = new ItemDawnSword(provider.getItemIDFromConfig(KNIFE), 6);
        RegistryUtils.registerItem(knife, KNIFE, "Warknife");
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        LanguageRegistry.instance().addStringLocalization("itemGroup." + ModId, "en_US", ModName);

        MinecraftForge.EVENT_BUS.register(new PlayerWorldHandlers());
        proxy.onLoad();

        TickRegistry.registerTickHandler(new ClientTicker(), Side.CLIENT);

        printInitializedMessage();
    }

    private void printInitializedMessage() {
        Log.printInfo(String.format("%s %s initialized.", ModName, Version));
    }
}
