/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.MISC,
	description = "Allows you to eat food much faster.\n" + "OM! NOM! NOM!",
	name = "FastEat",
	noCheatCompatible = false)
public class FastEatMod extends Mod implements UpdateListener
{
	public final CheckboxSetting useanythingfast  = new CheckboxSetting(
		"Eat/Drink Faster with Any Item", false);
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(mc.thePlayer.getHealth() > 0
			&& mc.thePlayer.onGround
			&& mc.thePlayer.inventory.getCurrentItem() != null
			&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFood
			&& mc.thePlayer.getFoodStats().needFood()
			&& mc.gameSettings.keyBindUseItem.pressed)
			for(int i = 0; i < 100; i++)
				mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer(false)); 
		if(useanythingfast.isChecked())
		{
			if(mc.thePlayer.getHealth() > 0
				&& mc.thePlayer.onGround
				&& mc.thePlayer.inventory.getCurrentItem() != null)
			{
				ItemStack stack = mc.thePlayer.inventory.getItemStack();
				if(stack != null && stack.getItem() == Items.golden_apple ||
					stack.getItem() == Items.potionitem)
					if(mc.gameSettings.keyBindUseItem.isPressed())
						for(int i = 0; i < 100; i++)
							mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer(false)); 
						
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
