package me.friwi.arterion.client.keybinds;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeybindsGui extends GuiScreen
{
    /** The parent Gui screen */
    protected GuiScreen parentScreen;
    /** The List GuiSlot object reference. */
    private KeybindsGui.List list;
    private GuiButton back;
    private GuiButton edit;
    private GuiButton delete;
    private GuiButton create;

    public KeybindsGui(GuiScreen screen)
    {
        this.parentScreen = screen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void func_73866_w_()
    {
        this.field_146292_n.add(this.back = new GuiButton(1, this.field_146294_l / 2 - 159, this.field_146295_m - 38, 78, 20, "Back"));
        this.field_146292_n.add(this.edit = new GuiButton(2, this.field_146294_l / 2 - 79, this.field_146295_m - 38, 78, 20, "Edit"));
        this.field_146292_n.add(this.delete = new GuiButton(3, this.field_146294_l / 2 + 1, this.field_146295_m - 38, 78, 20, "Delete"));
        this.field_146292_n.add(this.create = new GuiButton(4, this.field_146294_l / 2 + 81, this.field_146295_m - 38, 78, 20, "Create"));
        this.list = new KeybindsGui.List(this.field_146297_k);
        this.list.func_148134_d(7, 8);
        if(KeybindManager.list.size()==0) {
        	edit.field_146124_l = false;
        	delete.field_146124_l = false;
        }
    }

    /**
     * Handles mouse input.
     */
    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.list.func_178039_p();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void func_146284_a(GuiButton button)
    {
        if (button.field_146124_l)
        {
            switch (button.field_146127_k)
            {
                case 1:
                    this.field_146297_k.func_147108_a(this.parentScreen);
                    break;
                case 2:
                	Keybind editb = KeybindManager.list.get(list.selected);
                	if(editb!=null) {
                		this.field_146297_k.func_147108_a(new NewCommandKeybindGui(this, (CommandKeybind) editb));
                	}
                    break;
                case 3:
                	Keybind deleteb = KeybindManager.list.get(list.selected);
                	if(deleteb!=null) {
                		KeybindManager.delKeybind(deleteb);
                		if(KeybindManager.list.size()==0) {
                        	edit.field_146124_l = false;
                        	delete.field_146124_l = false;
                        	list.selected = 0;
                        }else if(KeybindManager.list.size()<=list.selected) {
                			list.selected = KeybindManager.list.size()-1;
                		}
                	}
                	break;
                case 4:
                	this.field_146297_k.func_147108_a(new NewCommandKeybindGui(this, null));
                    break;
                default:
                    this.list.func_148147_a(button);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        this.list.func_148128_a(mouseX, mouseY, partialTicks);
        this.func_73732_a(this.field_146289_q, "Keybinds", this.field_146294_l / 2, 16, 16777215);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    class List extends GuiSlot
    {
        public int selected = 0;
    	
    	public List(Minecraft mcIn)
        {
            super(mcIn, KeybindsGui.this.field_146294_l, KeybindsGui.this.field_146295_m, 32, KeybindsGui.this.field_146295_m - 65 + 4, 18);
            KeybindManager.sortKeybinds();
        }

        protected int func_148127_b()
        {
            return KeybindManager.list.size();
        }

        /**
         * The element in the slot that was clicked, boolean for whether it was double clicked or not
         */
        protected void func_148144_a(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            selected = slotIndex;
            if(isDoubleClick) {
            	KeybindsGui.this.func_146284_a(edit);
            }
        }

        /**
         * Returns true if the element passed in is currently selected
         */
        protected boolean func_148131_a(int slotIndex)
        {
            return selected==slotIndex;
        }

        /**
         * Return the height of the content being scrolled
         */
        protected int func_148138_e()
        {
            return this.func_148127_b() * 18;
        }

        protected void func_148123_a()
        {
            KeybindsGui.this.func_146276_q_();
        }

        protected void func_180791_a(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
        {
        	Keybind bind = KeybindManager.list.get(entryID);
            KeybindsGui.this.func_73731_b(KeybindsGui.this.field_146289_q, KeybindManager.getKeyText(bind.keycode), p_180791_2_ + 3, p_180791_3_ + 3, 16777215);
            KeybindsGui.this.func_73731_b(KeybindsGui.this.field_146289_q, ((CommandKeybind)bind).command, p_180791_2_ + 50, p_180791_3_ + 3, 16777215);
        }
    }
}