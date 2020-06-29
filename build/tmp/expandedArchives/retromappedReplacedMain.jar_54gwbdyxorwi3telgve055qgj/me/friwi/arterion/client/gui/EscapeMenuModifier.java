package me.friwi.arterion.client.gui;

import me.friwi.arterion.client.ArterionModConfig;
import me.friwi.arterion.client.data.ModValueEnum;
import me.friwi.arterion.client.keybinds.KeybindsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EscapeMenuModifier {

	@SubscribeEvent
	public void onGuiInit(InitGuiEvent.Post event) {
		if (ModValueEnum.IS_ARTERION.getInt() != 1)
			return;
		if (event.gui instanceof GuiIngameMenu) { // Make sure GUI is Escape menu
			GuiButton button = new GuiButton(1042, event.gui.field_146294_l / 2 - 100, event.gui.field_146295_m / 4 + 120 - 16 + 30,
					"Arterion Keybinds");
			event.buttonList.add(button);
			GuiButton button2 = new GuiButton(1043, event.gui.field_146294_l / 2 - 100, event.gui.field_146295_m / 4 + 120 - 16 + 30 + 25,
					"Arterion Gui Scale: "+ArterionModConfig.GUISCALES[ArterionModConfig.getGuiScale()]);
			event.buttonList.add(button2);
		}
	}

	@SubscribeEvent
	public void onGuiActionPerformed(ActionPerformedEvent.Pre event) {
		if (event.gui instanceof GuiIngameMenu && event.button.field_146127_k == 1042) { // Confirm my button was pressed
			Minecraft.func_71410_x().func_147108_a(new KeybindsGui(event.gui));
		}else if (event.gui instanceof GuiIngameMenu && event.button.field_146127_k == 1043) { // Confirm my button was pressed
			int scale = ArterionModConfig.getGuiScale();
			scale++;
			scale %= ArterionModConfig.GUISCALES.length;
			event.button.field_146126_j = "Arterion Gui Scale: "+ArterionModConfig.GUISCALES[scale];
			ArterionModConfig.setGuiScale(scale);
		}
	}

}