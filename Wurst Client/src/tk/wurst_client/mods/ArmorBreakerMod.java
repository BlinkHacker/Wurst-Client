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
		+ "This might also throw NCP checks.", 
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
		SwapItem();
	}
	
	public void SwapItem()
	{
		updateMS();
		if(wurst.mods.armorBreakerMod.isActive() && hasTimePassedM(200))
		{
			ItemStack current = mc.thePlayer.getCurrentEquippedItem();
			for(int i = 0; i < 45; i++)
			{
				ItemStack toSwitch = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
		        if (current != null && toSwitch != null)
		        	if (current.getItem() instanceof ItemSword || current.getItem() instanceof ItemAxe ||
		            	current.getItem() instanceof ItemPickaxe || current.getItem() instanceof ItemSpade)
		                if (toSwitch.getItem() instanceof ItemSword || toSwitch.getItem() instanceof ItemAxe || 
		                	toSwitch.getItem() instanceof ItemPickaxe || toSwitch.getItem() instanceof ItemSpade)
		                    mc.playerController.windowClick(0, i, 0, 2, mc.thePlayer);
			}
			 updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(AttackEntityListener.class, this);
	}
}
