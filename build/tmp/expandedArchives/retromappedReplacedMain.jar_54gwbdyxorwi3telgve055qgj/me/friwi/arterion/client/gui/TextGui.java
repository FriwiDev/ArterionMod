package me.friwi.arterion.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.friwi.arterion.client.ArterionModConfig;
import me.friwi.arterion.client.gui.util.ParagraphUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class TextGui extends GuiScreen {
	/** Text field containing the answer. */
	private GuiTextField inputTextField;
	/** "Done" button for the GUI. */
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private String title;
	private String[] message;

	public TextGui(String title, String message) {
		this.title = title;
		List<String> m = ParagraphUtil.getDescriptionWithLimit(message, 60);
		this.message = new String[m.size()];
		m.toArray(this.message);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void func_73876_c() {
		this.inputTextField.func_146178_a();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
	public void func_73866_w_() {
		if(title.length()==0)this.field_146297_k.func_147108_a((GuiScreen) null);
		
		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = getDesiredScale();
		
		ScaledResolution scaled = new ScaledResolution(field_146297_k);
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
		
		Keyboard.enableRepeatEvents(true);
		this.field_146292_n.clear();
		int y = this.field_146295_m / 4 - 16 + 25 + message.length * field_146297_k.field_71466_p.field_78288_b + 25;
		this.field_146292_n.add(this.doneBtn = new GuiButton(0, this.field_146294_l / 2 - 4 - 150, y+55,
				150, 20,
				I18n.func_135052_a("gui.done", new Object[0])));
		this.field_146292_n.add(this.cancelBtn = new GuiButton(1, this.field_146294_l / 2 + 4, y+55,
				150, 20,
				I18n.func_135052_a("gui.cancel", new Object[0])));
		this.inputTextField = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 150,
				y, 300, 20);
		this.inputTextField.func_146203_f(50);
		this.inputTextField.func_146195_b(true);
		this.inputTextField.func_146180_a("");
		this.doneBtn.field_146124_l = this.inputTextField.func_146179_b().trim().length() > 0;
		
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;
		
		scaled = new ScaledResolution(field_146297_k);
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void func_146281_b() {
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for
	 * buttons)
	 */
	protected void func_146284_a(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == 1) {
				this.mc.displayGuiScreen((GuiScreen) null);
				mc.addScheduledTask(()->this.mc.thePlayer.sendChatMessage("q"));
			} else if (button.id == 0) {
				this.mc.displayGuiScreen((GuiScreen) null);
				mc.addScheduledTask(()->this.mc.thePlayer.sendChatMessage(inputTextField.getText()));
			}
		}
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void func_73869_a(char typedChar, int keyCode) throws IOException {
		this.inputTextField.func_146201_a(typedChar, keyCode);
		this.doneBtn.field_146124_l = this.inputTextField.func_146179_b().trim().length() > 0;

		if (keyCode != 28 && keyCode != 156) {
			if (keyCode == 1) {
				this.func_146284_a(this.cancelBtn);
			}
		} else {
			this.func_146284_a(this.doneBtn);
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = getDesiredScale();
		
		ScaledResolution scaled = new ScaledResolution(field_146297_k);
		mouseX = mouseX*scaled.func_78326_a()/field_146294_l;
		mouseY = mouseY*scaled.func_78328_b()/field_146295_m;
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
		
		super.func_73864_a(mouseX, mouseY, mouseButton);
		this.inputTextField.func_146192_a(mouseX, mouseY, mouseButton);
		
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;
		
		scaled = new ScaledResolution(field_146297_k);
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
		int backupScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = getDesiredScale();
		
		ScaledResolution scaled = new ScaledResolution(field_146297_k);
		mouseX = mouseX*scaled.func_78326_a()/field_146294_l;
		mouseY = mouseY*scaled.func_78328_b()/field_146295_m;
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
		
		this.field_146297_k.field_71460_t.func_78478_c();
		
		this.func_146276_q_();
		int y = this.field_146295_m / 4 -16;
		this.func_73732_a(this.field_146289_q, title, this.field_146294_l / 2, y, 16777215);
		for (int i = 0; i < message.length; i++) {
			this.func_73731_b(this.field_146289_q, message[i], this.field_146294_l / 2 - 150,
					y + 25 + i * field_146297_k.field_71466_p.field_78288_b, 0xFFFFFFFF);
		}
		this.inputTextField.func_146194_f();

		super.func_73863_a(mouseX, mouseY, partialTicks);
		
		Minecraft.func_71410_x().field_71474_y.field_74335_Z = backupScale;
		
		scaled = new ScaledResolution(field_146297_k);
		this.field_146294_l = scaled.func_78326_a();
		this.field_146295_m = scaled.func_78328_b();
		
		this.field_146297_k.field_71460_t.func_78478_c();
	}
	
	private int getDesiredScale() {
		int scale = ArterionModConfig.getGuiScale();
		if(scale==1||scale==2) {
			return scale;
		}else {
			return 2;
		}
	}
}