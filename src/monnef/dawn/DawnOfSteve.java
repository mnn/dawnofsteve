package monnef.dawn;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.CustomLogger;
import monnef.core.utils.IDProvider;
import monnef.dawn.client.DawnCreativeTab;
import monnef.dawn.common.CommonProxy;
import monnef.dawn.item.ItemBlunderbuss;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.logging.Level;

import static monnef.dawn.Reference.ModId;
import static monnef.dawn.Reference.ModName;
import static monnef.dawn.Reference.Version;

@Mod(modid = ModId, name = ModName, version = Version, dependencies = "required-after:" + monnef.core.Reference.ModId)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"jaffas-01-sstone"}, packetHandler = PacketHandler.class)
public class DawnOfSteve {
    public static final String BLUNDERBUSS = "blunderbuss";

    @Mod.Instance(ModId)
    public static DawnOfSteve instance;

    @SidedProxy(clientSide = "monnef.dawn.client.ClientProxy", serverSide = "monnef.dawn.common.CommonProxy")
    public static CommonProxy proxy;
    public static DawnCreativeTab CreativeTab;

    public static CustomLogger Log = new CustomLogger(ModId);

    public static ItemBlunderbuss blunderbuss;

    @Mod.PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        CreativeTab = new DawnCreativeTab(ModId);
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        try {
            IDProvider provider = new IDProvider(2700, 17500);
            config.load();
            provider.linkWithConfig(config);

            blunderbuss = new ItemBlunderbuss(provider.getItemIDFromConfig(BLUNDERBUSS));
            LanguageRegistry.addName(blunderbuss, "Blunderbuss");
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod \"" + Reference.ModName + "\" can't read config file.");
        } finally {
            config.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        LanguageRegistry.instance().addStringLocalization("itemGroup." + ModId, "en_US", ModName);

        PlayerAPI.register(ModId, PlayerHooksClient.class);
        ServerPlayerAPI.register(ModId, PlayerHooksServer.class);
        MinecraftForge.EVENT_BUS.register(new PlayerWorldHandlers());

        printInitializedMessage();
    }

    private void printInitializedMessage() {
        Log.printInfo(String.format("%s %s initialized.", ModName, Version));
    }
}
