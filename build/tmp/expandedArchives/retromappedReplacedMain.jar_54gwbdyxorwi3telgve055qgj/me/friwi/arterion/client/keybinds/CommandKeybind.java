package me.friwi.arterion.client.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class CommandKeybind extends Keybind{
	public String command = null;
	public CommandKeybind(int keycode, String command) {
		super(keycode);
		this.command = command;
	}

	public CommandKeybind(String str) {
		this(Integer.parseInt(str.split(";")[0]), str.split(";")[1]);
	}

	@Override
	public void onStartPressing() {
		if(command.startsWith("/"))Minecraft.func_71410_x().field_71439_g.func_71165_d(command);
		else{
			Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146227_a(new ChatComponentText("\247cCommands only!"));
		}
	}

	@Override
	public void onStopPressing() {
		
	}

	@Override
	public void onDoubleClick() {
		
	}

	@Override
	public String toString() {
		return "0"+keycode+";"+command;
	}

}
