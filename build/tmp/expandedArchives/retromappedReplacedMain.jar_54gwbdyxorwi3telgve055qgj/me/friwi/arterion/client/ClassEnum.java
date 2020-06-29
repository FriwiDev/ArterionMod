package me.friwi.arterion.client;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum ClassEnum {
    NONE(null),
    PALADIN(new ItemStack(Items.field_151037_a)),
    BARBAR(new ItemStack(Items.field_151036_c)),
    SHADOWRUNNER(new ItemStack(Items.field_151040_l)),
    FORESTRUNNER(new ItemStack(Items.field_151031_f)),
    MAGE(new ItemStack(Items.field_151019_K)),
    CLERIC(new ItemStack(Items.field_151055_y));

    private ItemStack stack;

    private ClassEnum(ItemStack stack) {
        this.stack = stack;
    }

	public ItemStack getStack() {
		return stack;
	}    
}
