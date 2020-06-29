package me.friwi.arterion.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import me.friwi.arterion.client.ArterionModConfig;
import me.friwi.arterion.client.gui.util.TooltipPainter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.ResourceLocation;

public class ChestMenuGui extends GuiScreen {
	private String title;
	private ContainerChest container;

	private int rows;
	private int totalWidth;
	private int totalHeight;
	private int elementWidth;
	private int elementHeight;
	private int spacer = 5;

	public ChestMenuGui(ContainerChest container) {
		this.title = container.func_85151_d().func_70005_c_().replace("\2478", "\2477");
		this.container = container;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
	public void func_73866_w_() {
		this.field_146297_k.field_71439_g.field_71070_bA = this.container;

		IInventory chest = container.func_85151_d();

		this.rows = chest.func_70302_i_() / 9;

		if (this.field_146294_l < 50 || this.field_146295_m < 50)
			return; // Do not crash client by division by 0

		// Calc outer dimensions and box sizes
		this.totalWidth = (int) ((this.field_146294_l / 4 * 3.8) - 10 * this.spacer);
		this.elementWidth = this.totalWidth / 9;
		this.elementHeight = (int) (this.elementWidth * 1.5f);
		this.totalHeight = this.elementWidth * this.rows + this.spacer * (this.rows + 1);
		if (this.totalHeight > this.field_146295_m / 4 * 3 - this.spacer * (this.rows + 1)) {
			this.totalHeight = this.field_146295_m / 4 * 3 - this.spacer * (this.rows + 1);
			this.elementHeight = this.totalHeight / this.rows;
			this.elementWidth = (int) (this.elementHeight / 1.5f);
			this.totalWidth = this.elementWidth * 9;
		}
		this.totalWidth += this.spacer * 10;
		this.totalHeight += this.spacer * (this.rows + 1);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void func_146281_b() {

	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.func_73864_a(mouseX, mouseY, mouseButton);

		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = getDesiredScale();

		ScaledResolution scaled = new ScaledResolution(field_146297_k);
		mouseX = mouseX * scaled.func_78326_a() / field_146294_l;
		mouseY = mouseY * scaled.func_78328_b() / field_146295_m;
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();

		// Calc outer dimensions and box sizes
		this.totalWidth = (int) ((this.field_146294_l / 4 * 3.8) - 10 * this.spacer);
		this.elementWidth = this.totalWidth / 9;
		this.elementHeight = (int) (this.elementWidth * 1.5f);
		this.totalHeight = this.elementWidth * this.rows + this.spacer * (this.rows + 1);
		if (this.totalHeight > this.field_146295_m / 4 * 3 - this.spacer * (this.rows + 1)) {
			this.totalHeight = this.field_146295_m / 4 * 3 - this.spacer * (this.rows + 1);
			this.elementHeight = this.totalHeight / this.rows;
			this.elementWidth = (int) (this.elementHeight / 1.5f);
			this.totalWidth = this.elementWidth * 9;
		}
		this.totalWidth += this.spacer * 10;
		this.totalHeight += this.spacer * (this.rows + 1);

		int yoff = this.field_146295_m / 8 + 25;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < rows; y++) {
				int xoffs = this.field_146294_l / 2 - this.totalWidth / 2 + this.spacer + x * (this.spacer + this.elementWidth);
				int yoffs = yoff + this.spacer + y * (this.spacer + this.elementHeight);
				boolean m = isMouseOver(mouseX, mouseY, xoffs, yoffs, this.elementWidth, this.elementWidth);
				if (m) {
					ItemStack stack = container.func_75139_a(y * 9 + x).func_75211_c();
					if (stack != null) {
						Minecraft.func_71410_x().field_71439_g.field_71174_a.func_147297_a(
								new C0EPacketClickWindow(this.container.field_75152_c, y * 9 + x, 0, 0, stack, (short) 0));
						this.playPressSound(field_146297_k.func_147118_V());
					}
				}
			}
		}

		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;

		scaled = new ScaledResolution(field_146297_k);
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
	}

	private int getDesiredScale() {
		int scale = ArterionModConfig.getGuiScale();
		if (scale == 1 || scale == 2) {
			return scale;
		} else {
			return 2;
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = getDesiredScale();

		ScaledResolution scaled = new ScaledResolution(field_146297_k);
		mouseX = mouseX * scaled.func_78326_a() / field_146294_l;
		mouseY = mouseY * scaled.func_78328_b() / field_146295_m;
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();

		this.field_146297_k.field_71460_t.func_78478_c();

		// Calc outer dimensions and box sizes
		this.totalWidth = (int) ((this.field_146294_l / 4 * 3.8) - 10 * this.spacer);
		this.elementWidth = this.totalWidth / 9;
		this.elementHeight = (int) (this.elementWidth * 1.5f);
		this.totalHeight = this.elementWidth * this.rows + this.spacer * (this.rows + 1);
		if (this.totalHeight > this.field_146295_m / 4 * 3 - this.spacer * (this.rows + 1)) {
			this.totalHeight = this.field_146295_m / 4 * 3 - this.spacer * (this.rows + 1);
			this.elementHeight = this.totalHeight / this.rows;
			this.elementWidth = (int) (this.elementHeight / 1.5f);
			this.totalWidth = this.elementWidth * 9;
		}
		this.totalWidth += this.spacer * 10;
		this.totalHeight += this.spacer * (this.rows + 1);

		this.func_146276_q_();
		int yoff = this.field_146295_m / 8 - 16;
		this.func_73732_a(this.field_146289_q, title, this.field_146294_l / 2, yoff, 16777215);

		yoff += 41;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < rows; y++) {
				int xoffs = this.field_146294_l / 2 - this.totalWidth / 2 + this.spacer + x * (this.spacer + this.elementWidth);
				int yoffs = yoff + this.spacer + y * (this.spacer + this.elementHeight);
				boolean m = isMouseOver(mouseX, mouseY, xoffs, yoffs, this.elementWidth, this.elementWidth);
				ItemStack stack = container.func_75139_a(y * 9 + x).func_75211_c();
				if (stack != null) {
					this.drawBox(stack, xoffs, yoffs, this.elementWidth, this.elementHeight, m);
				}
			}
		}
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < rows; y++) {
				int xoffs = this.field_146294_l / 2 - this.totalWidth / 2 + this.spacer + x * (this.spacer + this.elementWidth);
				int yoffs = yoff + this.spacer + y * (this.spacer + this.elementHeight);
				boolean m = isMouseOver(mouseX, mouseY, xoffs, yoffs, this.elementWidth, this.elementWidth);
				ItemStack stack = container.func_75139_a(y * 9 + x).func_75211_c();
				if (stack != null) {
					this.drawBoxTooltip(stack, xoffs, yoffs, this.elementWidth, this.elementHeight, m);
				}
			}
		}

		super.func_73863_a(mouseX, mouseY, partialTicks);

		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;

		scaled = new ScaledResolution(field_146297_k);
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();

		this.field_146297_k.field_71460_t.func_78478_c();
	}

	private void drawBox(ItemStack stack, int xoffs, int yoffs, int width, int height, boolean mouseOver) {
		if (mouseOver) {
			this.func_73734_a(xoffs, yoffs, xoffs + width, yoffs + width, 0xFFAAAAAA);
		}
		GlStateManager.func_179094_E();
		GlStateManager.func_179109_b(xoffs, yoffs, 0);
		GlStateManager.func_179152_a((width + 0f) / 16f, (width + 0f) / 16f, 1f);
		RenderHelper.func_74518_a();
		RenderHelper.func_74520_c();
		field_146297_k.func_175599_af().func_175042_a(stack, 0, 0);
		GlStateManager.func_179121_F();
	}

	private void drawBoxTooltip(ItemStack stack, int xoffs, int yoffs, int width, int height, boolean mouseOver) {
		TooltipPainter.drawXCenteredHoveringText(stack.func_82840_a(Minecraft.func_71410_x().field_71439_g, false),
				xoffs + this.elementWidth / 2 + 3, yoffs + this.elementWidth, field_146297_k.field_71466_p);
	}

	private boolean isMouseOver(int mouseX, int mouseY, int xoffs, int yoffs, int width, int height) {
		return mouseX >= xoffs && mouseX < xoffs + width && mouseY >= yoffs && mouseY < yoffs + height;
	}

	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.func_147682_a(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
	}
}