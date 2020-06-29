package me.friwi.arterion.client;

import me.friwi.arterion.client.gui.ChatModifier;
import me.friwi.arterion.client.gui.ChestMenuModifier;
import me.friwi.arterion.client.gui.EscapeMenuModifier;
import me.friwi.arterion.client.gui.RenderIngameGuiHandler;
import me.friwi.arterion.client.gui.util.HeadCacheUtil;
import me.friwi.arterion.client.network.client.ConnectionHandler;
import me.friwi.arterion.client.network.client.ModConnection;
import me.friwi.arterion.client.network.client.PacketListenerClient;
import me.friwi.arterion.client.network.client.PacketReceiver;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ArterionClientMod.MODID, version = ArterionClientMod.VERSION)
public class ArterionClientMod
{
    public static final String MODID = "arterionclientmod";
    public static final String TEXMODID = "minecraft";
    public static final String VERSION = "0.2";
    public static boolean IS_LABY = false;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	new PacketReceiver().init();
    	MinecraftForge.EVENT_BUS.register(new RenderIngameGuiHandler());
    	MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
    	MinecraftForge.EVENT_BUS.register(new EscapeMenuModifier());
    	MinecraftForge.EVENT_BUS.register(new ChestMenuModifier());
    	MinecraftForge.EVENT_BUS.register(new ChatModifier());
    	new ModConnection(new PacketListenerClient());
    	HeadCacheUtil.startWorkerThread();
    	ArterionModConfig.load();
    }
}
