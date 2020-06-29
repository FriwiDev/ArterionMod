package me.friwi.arterion.client.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.friwi.arterion.client.network.ModPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ModConnection {
	public static final String CHANNEL_NAME = "arterion:mod";
	public static final short PROTOCOL_VERSION = 3;
	
	private static PacketListener listener;

    public ModConnection(PacketListener listener) {
        this.listener = listener;
    }
	
    public static void sendPacket(ModPacket packet) {
    	ByteBuf buff = Unpooled.wrappedBuffer(ModPacketList.toBytes(packet));
    	Minecraft.func_71410_x().field_71439_g.field_71174_a.func_147297_a(new C17PacketCustomPayload(CHANNEL_NAME, new PacketBuffer(buff)));
    }
    
    public static void receivePacket(byte id, byte[] data) {
    	ModPacket packet = ModPacketList.fromBytes(id, data);
    	Minecraft.getMinecraft().addScheduledTask(()->listener.handlePacket(packet));
    }
}
