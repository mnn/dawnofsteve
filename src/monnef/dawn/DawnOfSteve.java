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
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.CustomLogger;
import monnef.core.utils.IDProvider;
import monnef.core.utils.RegistryUtils;
import monnef.dawn.client.ClientTicker;
import monnef.dawn.client.DawnCreativeTab;
import monnef.dawn.client.SoundsHandler;
import monnef.dawn.common.CommonProxy;
import monnef.dawn.common.PlayerWorldHandlers;
import monnef.dawn.common.Reference;
import monnef.dawn.entity.EntityKit;
import monnef.dawn.entity.EntitySplashBomb;
import monnef.dawn.item.ArmorModelEnum;
import monnef.dawn.item.ItemArmorDawn;
import monnef.dawn.item.ItemDawnSword;
import monnef.dawn.item.ItemGun;
import monnef.dawn.item.ItemKitBag;
import monnef.dawn.item.ItemSplashBomb;
import monnef.dawn.network.DawnPacketHandler;
import monnef.dawn.server.PlayerHooksServer;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

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
    public static final String AMMO_BAG = "ammoBag";
    public static final String MEDIC_BAG = "medicBag";
    public static final String RATION_BAG = "rationBag";
    public static final String SPLASH_BOMB = "splashBomb";

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

    public static ItemKitBag ammoBag;
    public static ItemKitBag medicBag;
    public static ItemKitBag rationBag;
    private static final String ENTITY_NAME_KIT = "dawnKit";

    public static ItemSplashBomb splashBomb;
    private static final String ENTITY_NAME_SPLASH_BOMB = "dawnSplashBomb";

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
            createEntities(provider);

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod \"" + Reference.ModName + "\" can't read config file.");
        } finally {
            config.save();
        }

        MinecraftForge.EVENT_BUS.register(new SoundsHandler());
    }

    private void createItems(IDProvider provider) {
        int clipSize = MonnefCorePlugin.debugEnv ? 20 : 1;

        blunderbuss = new ItemGun(provider.getItemIDFromConfig(BLUNDERBUSS));
        blunderbuss.initBasic(AmmoRequirement.BULLETS_SMALL, clipSize, 10, 10);
        RegistryUtils.registerItem(blunderbuss, BLUNDERBUSS, "French Flintlock Blunderbuss");

        rifle = new ItemGun(provider.getItemIDFromConfig(RIFLE));
        rifle.initBasic(AmmoRequirement.BULLETS_SMALL, clipSize, 30, 10);
        RegistryUtils.registerItem(rifle, RIFLE, "Ferguson Rifle");

        bayonet = new ItemGun(provider.getItemIDFromConfig(BAYONET));
        bayonet.initBasic(AmmoRequirement.BULLETS_SMALL, clipSize, 20, 10).setHitCollDown(30).setMeleeDamage(8);
        RegistryUtils.registerItem(bayonet, BAYONET, "Charleville Rifle with Bayonet");

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

        ammoBag = new ItemKitBag(provider.getItemIDFromConfig(AMMO_BAG), EntityKit.KitType.AMMO);
        RegistryUtils.registerItem(ammoBag, AMMO_BAG, "Bag with Ammo");
        medicBag = new ItemKitBag(provider.getItemIDFromConfig(MEDIC_BAG), EntityKit.KitType.MEDPACK);
        RegistryUtils.registerItem(medicBag, MEDIC_BAG, "Medic's Bag");
        rationBag = new ItemKitBag(provider.getItemIDFromConfig(RATION_BAG), EntityKit.KitType.RATION);
        RegistryUtils.registerItem(rationBag, RATION_BAG, "Bag with Rations");

        splashBomb = new ItemSplashBomb(provider.getItemIDFromConfig(SPLASH_BOMB));
        RegistryUtils.registerItem(splashBomb, SPLASH_BOMB, "Bomb");
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        LanguageRegistry.instance().addStringLocalization("itemGroup." + ModId, "en_US", ModName);

        MinecraftForge.EVENT_BUS.register(new PlayerWorldHandlers());
        proxy.onLoad();

        TickRegistry.registerTickHandler(new ClientTicker(), Side.CLIENT);

        printInitializedMessage();

    }

    private void createEntities(IDProvider provider) {
        EntityRegistry.registerModEntity(EntityKit.class, ENTITY_NAME_KIT, provider.getModEntityIDFromConfig(ENTITY_NAME_KIT), this, 160, 20, true);
        EntityRegistry.registerModEntity(EntitySplashBomb.class, ENTITY_NAME_SPLASH_BOMB, provider.getModEntityIDFromConfig(ENTITY_NAME_SPLASH_BOMB), this, 160, 2, true);
    }

    private void printInitializedMessage() {
        Log.printInfo(String.format("%s %s initialized.", ModName, Version));
    }
}
