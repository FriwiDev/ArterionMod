package me.friwi.arterion.client.gui;

import java.util.LinkedList;
import java.util.List;

import me.friwi.arterion.client.ArterionModConfig;
import me.friwi.arterion.client.data.ModValueEnum;
import me.friwi.arterion.client.data.SkillDataList;
import me.friwi.arterion.client.gui.util.TooltipPainter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatModifier {

	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (ModValueEnum.IS_ARTERION.getInt() != 1)
			return;
		
		if(Minecraft.func_71410_x().field_71456_v==null||Minecraft.func_71410_x().field_71456_v.func_146158_b()==null||!Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146241_e()) {
			return;
		}

		int width = event.gui.field_146294_l;
		int height = event.gui.field_146295_m;
		int mouseX = event.mouseX;
		int mouseY = event.mouseY;

		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = getDesiredScale();

		ScaledResolution scaled = new ScaledResolution(Minecraft.func_71410_x());
		mouseX = mouseX * scaled.func_78326_a() / width;
		mouseY = mouseY * scaled.func_78328_b() / height;
		width = scaled.func_78326_a();
		height = scaled.func_78328_b();

		Minecraft.func_71410_x().field_71460_t.func_78478_c();

		int slotCount = SkillDataList.getSkills().length;
		for (int i = 0; i < SkillDataList.getSkills().length; i++) {
			if (SkillDataList.getSkills()[i] != null && SkillDataList.getSkills()[i].getSkill() != -1) {
				int x = width / 2 - slotCount * OverlayGui.slotWidth / 2 + i * OverlayGui.slotWidth;
				int y = 25;
				if (x <= mouseX && y <= mouseY && x + OverlayGui.slotWidth > mouseX
						&& y + OverlayGui.slotHeight > mouseY) {
					//Draw skill description
					List<String> desc = formatWithLimit(SkillDataList.getSkills()[i].getSkillDescription(), "\2477", 40);
					desc.add(0, "\2478"+SkillDataList.getSkills()[i].getSkillName());
					TooltipPainter.drawHoveringText(desc, mouseX, mouseY, Minecraft.func_71410_x().field_71466_p);
				}
			}
		}

		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;

		Minecraft.func_71410_x().field_71460_t.func_78478_c();
	}

	private int getDesiredScale() {
		int scale = ArterionModConfig.getGuiScale();
		if (scale == 1 || scale == 2) {
			return scale;
		} else {
			return 2;
		}
	}
	
	List<String> formatWithLimit(String d, String prefix, int limit) {
        List<String> ret = new LinkedList<>();
        for(String e : d.split("\n")) {
	        String[] desc = e.split(" ");
	        String s = "";
	        for (String x : desc) {
	            if (s.length() + x.length() < limit) s += x + " ";
	            else {
	                ret.add(prefix + s);
	                s = "";
	                s += x + " ";
	            }
	        }
	        if (s.length() > 0) ret.add(prefix + s);
        }
        return ret;
    }
}