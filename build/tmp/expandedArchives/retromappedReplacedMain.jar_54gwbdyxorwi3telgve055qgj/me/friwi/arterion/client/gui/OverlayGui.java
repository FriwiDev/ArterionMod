package me.friwi.arterion.client.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.lwjgl.opengl.GL11;

import me.friwi.arterion.client.ArterionClientMod;
import me.friwi.arterion.client.ArterionModConfig;
import me.friwi.arterion.client.ClassEnum;
import me.friwi.arterion.client.data.FriendlyPlayerEntry;
import me.friwi.arterion.client.data.FriendlyPlayerList;
import me.friwi.arterion.client.data.KillFeedEntry;
import me.friwi.arterion.client.data.KillFeedList;
import me.friwi.arterion.client.data.ModValueEnum;
import me.friwi.arterion.client.data.Objective;
import me.friwi.arterion.client.data.SkillDataEntry;
import me.friwi.arterion.client.data.SkillDataList;
import me.friwi.arterion.client.gui.util.ChatColor;
import me.friwi.arterion.client.gui.util.HeadCacheUtil;
import me.friwi.arterion.client.gui.util.TimeFormatUtil;
import me.friwi.arterion.client.keybinds.CommandKeybind;
import me.friwi.arterion.client.keybinds.Keybind;
import me.friwi.arterion.client.keybinds.KeybindManager;
import me.friwi.arterion.client.labymod.LabyKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class OverlayGui extends Gui {
	private Minecraft mc;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private ItemStack clock = null;
	private ItemStack gold = null;
	private ItemStack map = null;
	private ItemStack compass = null;
	private static final ResourceLocation xp_bar = new ResourceLocation(ArterionClientMod.TEXMODID,
			"arterionclientmod/textures/gui/xp_bar.bmp");
	private static final ResourceLocation xp_bar_bg = new ResourceLocation(ArterionClientMod.TEXMODID,
			"arterionclientmod/textures/gui/xp_bar_bg.bmp");
	private static final ResourceLocation hp_bar = new ResourceLocation(ArterionClientMod.TEXMODID,
			"arterionclientmod/textures/gui/hp_bar.bmp");
	private static final ResourceLocation mana_bar = new ResourceLocation(ArterionClientMod.TEXMODID,
			"arterionclientmod/textures/gui/mana_bar.bmp");
	private static final ResourceLocation kill_arrow = new ResourceLocation(ArterionClientMod.TEXMODID,
			"arterionclientmod/textures/gui/kill_arrow.png");

	public static final int slotWidth = 66-16;
	public static final int slotHeight = 66-16;
	private final int slotDisabledColor = 0xFF555555;

	private final int friendlyPlayerHeight = 20;
	private final int friendlyPlayerSpacer = 5;

	private final int killSpacer = 3;
	private final int killHeight = 16;

	public OverlayGui(Minecraft mc) {
		this.mc = mc;
	}

	public void render() {
		GlStateManager.func_179094_E();
		GlStateManager.func_179109_b(0, 0, -25f);
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = ArterionModConfig.getGuiScale();
		this.mc.field_71460_t.func_78478_c();

		this.field_73735_i = -2000;

		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.func_78326_a();
		int height = scaled.func_78328_b();

		// Top BG
		this.func_73734_a(0, 0, width, 18, 0xFF000000);
		this.func_73734_a(0, 18, width, 19, 0xFFFFFFFF);

		// Server status or objective
		String status = ModValueEnum.SERVER_STATUS.getString();
		if (Objective.isActive()) {
			status = Objective.buildMessage();
		}
		this.func_73732_a(mc.field_71466_p, status, width / 2, 6, 0xFFFFFFFF);

		// Class & lvl
		int x = 0;
		ClassEnum selectedClass = ModValueEnum.SELECTED_CLASS.getClassEnum();
		if (selectedClass == ClassEnum.NONE) {
			String str = "\2477" + ModValueEnum.SELECTED_CLASS_NAME.getString();
			this.func_73731_b(mc.field_71466_p, str, 3, 6, 0xFFFFFFFF);
			x = 3 + mc.field_71466_p.func_78256_a(str);
		} else {
			String str = "\2472" + ModValueEnum.LEVEL.getInt();
			if(ModValueEnum.PRESTIGE_LEVEL.getInt()>0) {
				str += " \2477(\247c"+ModValueEnum.PRESTIGE_LEVEL.getInt()+"\2477)";
			}
			this.drawItemStack(selectedClass.getStack(), 3, 1);
			this.func_73731_b(mc.field_71466_p, str, 3 + 16 + 3, 6, 0xFFFFFFFF);
			x = 3 + 16 + 3 + mc.field_71466_p.func_78256_a(str);
		}

		x += 20;

		// Gold
		if (gold == null)
			gold = new ItemStack(Items.field_151043_k);
		String num = "\247f" + String.format(Locale.ENGLISH, "%.2f", (ModValueEnum.GOLD.getInt() + 0f) / 100f);
		int point = num.indexOf('.');
		if (point != -1) {
			if (num.endsWith("00")) {
				num = num.substring(0, point);
			} else if (num.endsWith("0")) {
				num = num.substring(0, point + 2);
			}
		}
		this.drawItemStack(gold, x, 1);
		this.func_73731_b(mc.field_71466_p, num, x + 16 + 3, 6, 0xFFFFFFFF);

		// Time
		if (clock == null)
			clock = new ItemStack(Items.field_151113_aN);
		String time = timeFormat.format(new Date());
		int clockWidth = mc.field_71466_p.func_78256_a(time);
		this.func_73731_b(mc.field_71466_p, time, width - 3 - clockWidth, 6, 0xFFFFFFFF);
		this.drawItemStack(clock, width - 3 - clockWidth - 3 - 16, 1);

		x = width - 3 - clockWidth - 3 - 16 - 20;

		// Region
		if (map == null)
			map = new ItemStack(Items.field_151148_bJ);
		String region = ModValueEnum.REGION.getString();
		int regionWidth = mc.field_71466_p.func_78256_a(region);
		this.func_73731_b(mc.field_71466_p, region, x - regionWidth, 6, 0xFFFFFFFF);
		this.drawItemStack(map, x - regionWidth - 3 - 16, 1);

		x = x - regionWidth - 3 - 16 - 20;

		// Compass
		if (compass == null)
			compass = new ItemStack(Items.field_151111_aL);
		String dir = "\2477?";
		Entity entity = this.mc.func_175606_aa();
		EnumFacing enumfacing = entity.func_174811_aO();
		switch (enumfacing) {
		case NORTH:
			dir = "\247fN";
			break;
		case SOUTH:
			dir = "\247fS";
			break;
		case WEST:
			dir = "\247fW";
			break;
		case EAST:
			dir = "\247fE";
		}
		int dirWidth = mc.field_71466_p.func_78256_a(dir);
		this.func_73731_b(mc.field_71466_p, dir, x - dirWidth, 6, 0xFFFFFFFF);
		this.drawItemStack(compass, x - dirWidth - 3 - 16, 1);

		// XP Bar
		this.mc.func_110434_K().func_110577_a(xp_bar_bg);
		this.func_146110_a(0, 19, 0, 0, width, 5, 1, 5);
		this.mc.func_110434_K().func_110577_a(xp_bar);
		this.func_146110_a(0, 19, 0, 0, width * ModValueEnum.XP_PER_MILLE.getInt() / 1000, 5, 1,
				5);
		this.func_73734_a(0, 24, width, 25, 0xFFFFFFFF);

		// Skill slots
		int slotCount = SkillDataList.getSkills().length;
		for (int i = 0; i < SkillDataList.getSkills().length; i++) {
			if (SkillDataList.getSkills()[i] != null) {
				this.drawSkillSlot(i, SkillDataList.getSkills()[i], width / 2 - slotCount * slotWidth / 2 + i * slotWidth,
						25);
			}
		}

		// Render own hp
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		int ownWidth = 150;
		int ownX = 0;
		int ownHeight = 38;
		int ownY = 30;
		int headXOffset = -8;
		int barX = ownX + headXOffset + (int) ((ownHeight * 3f / 2f) / 2);
		int barWidth = ownX + ownWidth - 2 - barX;
		int headEndX = ownX + headXOffset + ownHeight;
		this.func_73734_a(ownX, ownY, ownX + ownWidth + 1, ownY + ownHeight + 1, 0xFF000000);
		GlStateManager.func_179094_E();
		GlStateManager.func_179109_b(ownX + headXOffset, ownY - ownHeight / 6f - 2.5f, 0);
		GlStateManager.func_179152_a((ownHeight + 0f) / 16f * 3f / 2f, (ownHeight + 0f) / 16f * 3f / 2f, 1);
		this.drawItemStack(HeadCacheUtil.supplyItemStack(Minecraft.func_71410_x().func_110432_I().func_148256_e().getId())[0],
				0, 0);
		GlStateManager.func_179121_F();
		this.mc.func_110434_K().func_110577_a(hp_bar);
		float healthperc = Minecraft.func_71410_x().field_71439_g.func_110143_aJ()
				/ Minecraft.func_71410_x().field_71439_g.func_110138_aP();
		int health = (int) (healthperc * ModValueEnum.MAXHEALTH.getInt());
		this.func_146110_a(barX, ownY + 2, 0, 0, (int) ((barWidth + 1) * healthperc), 16, 1, 16);
		this.func_73732_a(mc.field_71466_p, health + " / " + ModValueEnum.MAXHEALTH.getInt(),
				(headEndX + ownX + ownWidth) / 2, ownY + 2 + 4, 0xFFFFFFFF);
		this.mc.func_110434_K().func_110577_a(mana_bar);
		float manaperc = (ModValueEnum.MANA.getInt() + 0f) / (ModValueEnum.MAXMANA.getInt() + 0f);
		this.func_146110_a(barX, ownY + 2 + 16 + 3, 0, 0, (int) ((barWidth + 1) * manaperc), 16,
				1, 16);
		this.func_73732_a(mc.field_71466_p, ModValueEnum.MANA.getInt() + " / " + ModValueEnum.MAXMANA.getInt(),
				(headEndX + ownX + ownWidth) / 2, ownY + 2 + 4 + 16 + 3, 0xFFFFFFFF);

		// Render friendlies
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		int size = FriendlyPlayerList.getFriendlyPlayers().size();
		int i = 0;
		int friendlyX = 0;
		int friendlyY = ownY + ownHeight + 10; // 10 is initial spacer
		for (FriendlyPlayerEntry other : FriendlyPlayerList.getFriendlyPlayers()) {
			drawFriendlyPlayerEntry(other, friendlyX, friendlyY + i * (friendlyPlayerHeight + friendlyPlayerSpacer));
			i++;
		}

		// Killfeed
		int killBeginX = width - 10;
		int killBeginY = 25 + 10;
		i = 0;
		for (KillFeedEntry entry : KillFeedList.getKillFeedEntries()) {
			drawKillFeedEntry(entry, killBeginX, killBeginY + i * (killHeight + killSpacer));
			i++;
		}

		// Fix occasional render bugs
		this.mc.func_110434_K().func_110577_a(SkillDataList.getSkillIcon(-1, false));
		this.func_146110_a(-10000, -10000, 0, 0, -10000, -10000, slotWidth - 2, slotHeight - 2);

		GL11.glPopAttrib();
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);

		this.field_73735_i = 0f;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;
		GlStateManager.func_179121_F();
		this.mc.field_71460_t.func_78478_c();
	}

	private void drawKillFeedEntry(KillFeedEntry entry, int x, int y) {
		int killWidth = 4 + // Left of killer
				(entry.getKillerName().isEmpty() ? 0 : mc.field_71466_p.func_78256_a(entry.getKillerName()) + 4) // Killer,
																														// including
																														// spacer
																														// to
																														// icon
				+ 8 + 3 + 4 // Killicon, arrow, spacer to killed name
				+ mc.field_71466_p.func_78256_a(entry.getKilledName()) // Killed
				+ 4 + 2; // Right of killer

		x -= killWidth;

		int black = 0xAA000000;

		// Background
		this.func_73734_a(x + 1, y, x + killWidth - 1, y + 1, black);
		this.func_73734_a(x + 1, y + killHeight - 1, x + killWidth - 1, y + killHeight, black);
		this.func_73734_a(x, y + 1, x + 1, y + killHeight - 1, black);
		this.func_73734_a(x + killWidth - 1, y + 1, x + killWidth, y + killHeight - 1, black);
		this.func_73734_a(x + 2, y + 2, x + killWidth - 2, y + killHeight - 2, black);

		// Border
		int border = entry.getBorderColor();
		this.func_73734_a(x + 1, y + 1, x + killWidth - 1, y + 2, border);
		this.func_73734_a(x + 1, y + killHeight - 2, x + killWidth - 1, y + killHeight - 1, border);
		this.func_73734_a(x + 1, y + 1, x + 2, y + killHeight - 1, border);
		this.func_73734_a(x + killWidth - 2, y + 1, x + killWidth - 1, y + killHeight - 1, border);

		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);

		int currX = x + 4;
		int yOffs = 4;

		// Killer
		if (!entry.getKillerName().isEmpty()) {
			this.func_73731_b(mc.field_71466_p, entry.getKillerName(), currX, y + yOffs, 0xFFFFFFFF);
			currX += mc.field_71466_p.func_78256_a(entry.getKillerName()) + 4;
		}

		// Icon
		this.func_73734_a(currX, y + yOffs, currX + 8, y + yOffs + 8, black);
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		GlStateManager.func_179147_l();
		this.mc.func_110434_K().func_110577_a(kill_arrow);
		this.func_146110_a(currX + 8, y + yOffs, 0, 0, 3, 8, 3, 8);
		if (entry.getIcon() == null) {
			this.mc.func_110434_K().func_110577_a(entry.getKillfeedIcon());
			this.func_146110_a(currX, y + yOffs, 0, 0, 8, 8, 8, 8);
		} else {
			GlStateManager.func_179094_E();
			GlStateManager.func_179109_b(currX, y + yOffs, 0);
			GlStateManager.func_179152_a(8f / 16f, 8f / 16f, 1);
			this.drawItemStack(entry.getIcon(), 0, 0);
			GlStateManager.func_179121_F();
		}
		currX += 8 + 5 + 4;

		// Killed
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		this.func_73731_b(mc.field_71466_p, entry.getKilledName(), currX, y + yOffs, 0xFFFFFFFF);

		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
	}

	private void drawFriendlyPlayerEntry(FriendlyPlayerEntry other, int x, int y) {
		int friendlyWidth = 150;
		int headXOffset = -2;
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		this.func_73734_a(x, y, x + friendlyWidth + 1, y + friendlyPlayerHeight, 0xFF000000);
		GlStateManager.func_179094_E();
		GlStateManager.func_179109_b(x + headXOffset, y - friendlyPlayerHeight / 6f - 0.5f, 0);
		GlStateManager.func_179152_a((friendlyPlayerHeight - 2f + 0f) / 16f * 3f / 2f,
				(friendlyPlayerHeight - 2f) / 16f * 3f / 2f, 1);
		this.drawItemStack(HeadCacheUtil.supplyItemStack(other.getPlayer())[0], 0, 0);
		GlStateManager.func_179121_F();
		// Hp bar
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		int barX = x + headXOffset + (int) (((friendlyPlayerHeight - 2) * 3f / 2f) / 2);
		int barWidth = x + friendlyWidth - 2 - barX;
		int headEndX = x + headXOffset + friendlyPlayerHeight;
		this.mc.func_110434_K().func_110577_a(hp_bar);
		float healthperc = (other.getHealth() + 0f) / (other.getMaxhealth() + 0f);
		this.func_146110_a(barX, y + 2, 0, 0, (int) ((barWidth + 1) * healthperc),
				friendlyPlayerHeight - 4, 1, friendlyPlayerHeight - 4);
		// Name
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		String name = other.getName();
		if (name.length() > 14)
			name = name.substring(0, 14);
		this.func_73731_b(mc.field_71466_p, name, headEndX + 5, y + 2 + 4, 0xFFFFFFFF);
		// Class icon
		this.drawItemStack(other.getSelectedClass().getStack(), barX + barWidth - 16, y + 2);
		// Level
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		String level = "\247f" + other.getLevel();
		this.func_73731_b(mc.field_71466_p, level, barX + barWidth - 16 - 2 - mc.field_71466_p.func_78256_a(level),
				y + 2 + 4, 0xFFFFFFFF);
	}

	private void drawSkillSlot(int slot, SkillDataEntry skillDataEntry, int x, int y) {
		// Border
		int outerColor = skillDataEntry.hasCooldown() || skillDataEntry.getSkill() == -1 || !skillDataEntry.isEnabled() || skillDataEntry.getMana()>ModValueEnum.MANA.getInt() ? slotDisabledColor
				: skillDataEntry.getDelayColor();
		this.func_73734_a(x, y, x + 1, y + slotHeight, outerColor);
		this.func_73734_a(x + slotWidth - 1, y, x + slotWidth, y + slotHeight, outerColor);
		this.func_73734_a(x, y + slotHeight - 2, x + slotWidth, y + slotHeight, outerColor);

		// Skill icon
		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
		this.mc.func_110434_K().func_110577_a(SkillDataList.getSkillIcon(skillDataEntry.getSkill(), !skillDataEntry.isEnabled()));
		this.func_146110_a(x + 1, y, 0, 0, slotWidth - 2, slotHeight - 2, slotWidth - 2,
				slotHeight - 2);

		// Cooldown bg
		if (skillDataEntry.hasCooldown()) {
			float percentage = (skillDataEntry.getCooldown() + 0f) / (skillDataEntry.getMaxCooldown() + 0f);
			this.func_73734_a(x + 1, y, x + slotWidth - 1, y + slotHeight - 2, 0x7F555555);
			this.func_73734_a(x + 1, y + (int) (percentage * (slotHeight - 2)), x + slotWidth - 1, y + slotHeight - 2,
					0x7F555555);
		} else
		
		// Mana bg
		if (skillDataEntry.isEnabled() && skillDataEntry.getMana()>ModValueEnum.MANA.getInt()) {
			this.func_73734_a(x + 1, y, x + slotWidth - 1, y + slotHeight - 2, 0x7F11117F);
			this.func_73734_a(x + 1, y, x + slotWidth - 1, y + slotHeight - 2, 0x7F11117F);
		}

		// Delay
		if (skillDataEntry.hasDelay()) {
			GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
			String delay = TimeFormatUtil.formatMillis(skillDataEntry.getDelay());
			this.func_73732_a(mc.field_71466_p, delay, x + slotWidth / 2, y + (slotHeight - 2) / 2 - 10,
					skillDataEntry.getDelayColor());
			GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
			float percentage = (skillDataEntry.getDelay() + 0f) / (skillDataEntry.getTotalDelay() + 0f);
			int delayx = x + 5;
			int delayy = y + (slotHeight - 2) / 2 + 2;
			int delayWidth = slotWidth - 10;
			int delayHeight = 8;
			this.func_73734_a(delayx, delayy, delayx + 1, delayy + delayHeight, 0xFFFFFFFF);
			this.func_73734_a(delayx + delayWidth, delayy, delayx + delayWidth + 1, delayy + delayHeight, 0xFFFFFFFF);
			this.func_73734_a(delayx, delayy, delayx + delayWidth, delayy + 1, 0xFFFFFFFF);
			this.func_73734_a(delayx, delayy + delayHeight, delayx + delayWidth, delayy + delayHeight + 1, 0xFFFFFFFF);
			delayx += 2;
			delayWidth -= 4;
			this.func_73734_a((int) (delayx + (delayWidth / 2f) - (percentage * delayWidth / 2f)), delayy + 2,
					(int) (delayx + (delayWidth / 2f) + (percentage * delayWidth / 2f)) + 1, delayy + delayHeight - 1,
					skillDataEntry.getDelayColor());
		} else if (skillDataEntry.hasCooldown()) {
			// Cooldown text
			GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
			String cooldown = TimeFormatUtil.formatMillis(skillDataEntry.getCooldown());
			this.func_73732_a(mc.field_71466_p, cooldown, x + slotWidth / 2, y + (slotHeight - 2) / 2 - 4,
					0xFFAAAAAA);
		} else if (skillDataEntry.isEnabled() && skillDataEntry.getMana()>ModValueEnum.MANA.getInt()) {
			//Mana missing
			int missing = skillDataEntry.getMana()-ModValueEnum.MANA.getInt();
			String mana = "\2477"+missing+" \2479Mana";
			this.func_73732_a(mc.field_71466_p, mana, x + slotWidth / 2, y + (slotHeight - 2) / 2 - 4,
					0xFFFFFFFF);
		}

		// Skill keybind
		String key = null;
		if(ArterionClientMod.IS_LABY) {
			key = LabyKeybinds.getLabyKeybind(slot+1, skillDataEntry.getSkillName());
		}else {
			int code = -10000;
			for(Keybind keybind : KeybindManager.list) {
				if(keybind instanceof CommandKeybind) {
					String command = ((CommandKeybind)keybind).command;
					if(command.toLowerCase().startsWith("/skillslot ")) {
						String slotStr = command.substring("/skillslot ".length());
						if(slotStr.equals((slot+1)+"")) {
							code = keybind.keycode;
							key = ChatColor.stripColor(KeybindManager.getKeyText(code));
							break;
						}
					}
					if(command.toLowerCase().startsWith("/skill ")) {
						String nameStr = command.substring("/skill ".length());
						if(nameStr.replace("_", " ").equalsIgnoreCase(ChatColor.stripColor(skillDataEntry.getSkillName()))) {
							code = keybind.keycode;
							key = ChatColor.stripColor(KeybindManager.getKeyText(code));
							break;
						}
					}
				}
			}
		}
		if (key!=null) {
			int keyWidth = 4 + mc.field_71466_p.func_78256_a(key) + 3;
			int keyHeight = 15;

			x = x + slotWidth/2 - keyWidth/2;
			y = y + slotHeight - keyHeight/2;

			int black = 0xFF000000;
			int color = outerColor;

			// Background
			this.func_73734_a(x + 1, y, x + keyWidth - 1, y + 1, black);
			this.func_73734_a(x + 1, y + keyHeight - 1, x + keyWidth - 1, y + keyHeight, black);
			this.func_73734_a(x, y + 1, x + 1, y + keyHeight - 1, black);
			this.func_73734_a(x + keyWidth - 1, y + 1, x + keyWidth, y + keyHeight - 1, black);
			this.func_73734_a(x + 2, y + 2, x + keyWidth - 2, y + keyHeight - 2, black);

			// Border
			this.func_73734_a(x + 1, y + 1, x + keyWidth - 1, y + 2, color);
			this.func_73734_a(x + 1, y + keyHeight - 2, x + keyWidth - 1, y + keyHeight - 1, color);
			this.func_73734_a(x + 1, y + 1, x + 2, y + keyHeight - 1, color);
			this.func_73734_a(x + keyWidth - 2, y + 1, x + keyWidth - 1, y + keyHeight - 1, color);
			
			// Key itself
			this.func_73731_b(mc.field_71466_p, key, x+4, y + 4, color);
		}

		GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
	}

	/**
	 * Render an ItemStack. Args : stack, x, y
	 */
	private void drawItemStack(ItemStack stack, int x, int y) {
		RenderHelper.func_74518_a();
		RenderHelper.func_74520_c();

		RenderItem itemRender = mc.func_175599_af();
		GlStateManager.func_179094_E();
		GlStateManager.func_179109_b(0.0F, 0.0F, 32.0F);
		this.field_73735_i = -170.0F;
		itemRender.field_77023_b = -170.0F;
		itemRender.func_180450_b(stack, x, y);
		this.field_73735_i = -2000.0F;
		itemRender.field_77023_b = 0.0F;
		GlStateManager.func_179121_F();
	}
}
