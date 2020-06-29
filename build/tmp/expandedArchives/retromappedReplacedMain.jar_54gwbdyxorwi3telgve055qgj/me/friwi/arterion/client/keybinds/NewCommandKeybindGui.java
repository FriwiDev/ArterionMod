package me.friwi.arterion.client.keybinds;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.friwi.arterion.client.gui.util.ParagraphUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class NewCommandKeybindGui extends GuiScreen {
	/** Text field containing the answer. */
	private GuiTextField inputTextField;
	/** "Done" button for the GUI. */
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton keyBtn;
	/** The parent Gui screen */
    protected GuiScreen parentScreen;
    private CommandKeybind bind;
    private boolean captureKey = false;
    private int key;


	public NewCommandKeybindGui(GuiScreen parent, CommandKeybind bind) {
		this.parentScreen = parent;
		this.bind = bind;
		this.key = bind==null?-1:bind.keycode;
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
		Keyboard.enableRepeatEvents(true);
		this.field_146292_n.clear();
		int y = this.field_146295_m / 4 - 16 + 25 + 25;
		this.field_146292_n.add(this.doneBtn = new GuiButton(0, this.field_146294_l / 2 - 4 - 150, y+55,
				150, 20,
				I18n.func_135052_a("gui.done", new Object[0])));
		this.field_146292_n.add(this.cancelBtn = new GuiButton(1, this.field_146294_l / 2 + 4, y+55,
				150, 20,
				I18n.func_135052_a("gui.cancel", new Object[0])));
		this.field_146292_n.add(this.keyBtn = new GuiButton(2, this.field_146294_l / 2 - 152, y,
				304, 20,
				""));
		this.inputTextField = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 150,
				y+22, 300, 20);
		this.inputTextField.func_146203_f(50);
		this.inputTextField.func_146195_b(true);
		this.inputTextField.func_146180_a(bind==null?"/":bind.command);
		this.doneBtn.field_146124_l = this.inputTextField.func_146179_b().trim().length() > 0 && key!=-1;
		updateKeyButtonText();
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
		if (button.field_146124_l) {
			if (button.field_146127_k == 1) {
				this.field_146297_k.func_147108_a(parentScreen);
			} else if (button.field_146127_k == 0) {
				if(bind==null) {
					KeybindManager.addKeybind(new CommandKeybind(key, this.inputTextField.func_146179_b()));
				}else {
					bind.keycode = key;
					bind.command = this.inputTextField.func_146179_b();
					KeybindManager.save();
				}
				this.field_146297_k.func_147108_a(parentScreen);
			} else if (button.field_146127_k == 2) {
				captureKey=!captureKey;
				updateKeyButtonText();
			}
		}
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void func_73869_a(char typedChar, int keyCode) throws IOException {
		if(captureKey) {
			if(keyCode!=Keyboard.KEY_ESCAPE) {
				key = keyCode;
			}
			captureKey=false;
			updateKeyButtonText();
			this.doneBtn.field_146124_l = this.inputTextField.func_146179_b().trim().length() > 0 && key!=-1;
			return;
		}
		
		this.inputTextField.func_146201_a(typedChar, keyCode);
		this.doneBtn.field_146124_l = this.inputTextField.func_146179_b().trim().length() > 0 && key!=-1;

		if (keyCode != 28 && keyCode != 156) {
			if (keyCode == 1) {
				this.func_146284_a(this.cancelBtn);
			}
		} else {
			this.func_146284_a(this.doneBtn);
		}
	}

	private void updateKeyButtonText() {
		if(captureKey) {
			this.keyBtn.field_146126_j = "\2477> Press Key <";
		}else {
			if(key==-1) {
				this.keyBtn.field_146126_j = "\247cNone";
			}else {
				this.keyBtn.field_146126_j = KeybindManager.getKeyText(key);
			}
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.func_73864_a(mouseX, mouseY, mouseButton);
		this.inputTextField.func_146192_a(mouseX, mouseY, mouseButton);
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
		this.func_146276_q_();
		int y = this.field_146295_m / 4 -16;
		this.func_73732_a(this.field_146289_q, bind==null?"Create Keybind":"Edit Keybind", this.field_146294_l / 2, y, 16777215);
		y+=25 + 25 - 10;
		this.func_73731_b(this.field_146289_q, "Please select a key and enter a command for your keybind", this.field_146294_l / 2 - 148, y, 0xFFFFFFFF);
		this.inputTextField.func_146194_f();

		super.func_73863_a(mouseX, mouseY, partialTicks);
	}
}