/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.item.*;
import tk.wurst_client.events.AttackEntityEvent;
import tk.wurst_client.events.listeners.AttackEntityListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Breaks your enemies armor faster in PVP.\n"
		+ "Note: you must have a second weapon available in your hotbar.\n"
		+ "Note 2: This will make you use a weak weapon if you\n"
		+ "have one in your hotbar.", 
	name = "ArmorBreaker")
public class ArmorBreakerMod extends Mod implements AttackEntityListener
{
	@Override
	public void onEnable()
	{
		if(wurst.mods.autoSwordMod.isActive())
			wurst.mods.autoSwordMod.setEnabled(false);
		wurst.events.add(AttackEntityListener.class, this);
	}
	
	@Override
	public void onEntityAttacked(AttackEntityEvent event)
	{
		 ItemStack current = mc.thePlayer.getCurrentEquippedItem();
	        ItemStack toSwitch = mc.thePlayer.inventoryContainer.getSlot(27).getStack();
	        if (current != null && toSwitch != null)
	            if (current.getItem() instanceof ItemSword || current.getItem() instanceof ItemAxe ||
	            	current.getItem() instanceof ItemPickaxe || current.getItem() instanceof ItemSpade)
	                if (toSwitch.getItem() instanceof ItemSword || toSwitch.getItem() instanceof ItemAxe || 
	                	toSwitch.getItem() instanceof ItemPickaxe || toSwitch.getItem() instanceof ItemSpade)
	                    mc.playerController.windowClick(0, 27, mc.thePlayer.inventory.currentItem, 
	                    	2, mc.thePlayer);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(AttackEntityListener.class, this);
	}
	
}
