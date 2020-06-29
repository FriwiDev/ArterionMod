package me.friwi.arterion.client.network.client;

import me.friwi.arterion.client.data.FriendlyPlayerList;
import me.friwi.arterion.client.data.ModValueEnum;
import me.friwi.arterion.client.keybinds.KeybindManager;
import me.friwi.arterion.client.network.packet.Packet01ModVersion;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ConnectionHandler
{
	Object previous = null;
	
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
		if(previous!=Minecraft.getMinecraft().thePlayer) {
			previous = Minecraft.getMinecraft().thePlayer;
			ModValueEnum.IS_ARTERION.setValue(Integer.valueOf(0));
			if(previous!=null) {
				FriendlyPlayerList.clear();
				ModConnection.sendPacket(new Packet01ModVersion(ModConnection.PROTOCOL_VERSION));
				KeybindManager.load();
			}
		}
    }
}