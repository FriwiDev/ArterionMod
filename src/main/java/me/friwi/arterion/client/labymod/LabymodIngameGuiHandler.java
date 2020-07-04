package me.friwi.arterion.client.labymod;

import me.friwi.arterion.client.data.ModValueEnum;
import me.friwi.arterion.client.gui.OverlayGui;
import me.friwi.arterion.client.gui.util.Mouse;
import me.friwi.arterion.client.keybinds.KeybindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LabymodIngameGuiHandler {
	OverlayGui topGui = new OverlayGui(Minecraft.getMinecraft());
	
    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event)
    {
    	if(ModValueEnum.IS_ARTERION.getInt()!=1)return;
    	Mouse.res = new ScaledResolution(Minecraft.getMinecraft());
    	if(topGui!=null
    			&& !Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isKeyDown()
    			&& !Minecraft.getMinecraft().gameSettings.showDebugInfo)topGui.render();
    }
}
