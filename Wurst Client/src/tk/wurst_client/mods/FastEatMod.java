/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.MISC,
	description = "Allows you to eat food much faster.\n"
		+ "Bypasses NoCheat if YesCheat+ is enabled.\n"
		+ "OM! NOM! NOM!",
	name = "FastEat")
public class FastEatMod extends Mod implements UpdateListener
{
	public final CheckboxSetting useanythingfast  = new CheckboxSetting(
		"Eat/Drink Faster with Any Item", false);
	
	@Override
	public void initSettings()
	{		
		settings.add(useanythingfast);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!wurst.mods.yesCheatMod.isActive())
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
			if(mc.thePlayer.getHealth() > 0
				&& !mc.thePlayer.capabilities.isCreativeMode
				&& mc.thePlayer.onGround
				&& mc.thePlayer.inventory.getCurrentItem() != null
				&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAppleGold
				&& mc.gameSettings.keyBindUseItem.pressed)
						for(int i = 0; i < 100; i++)
							mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer(false)); 
		if(useanythingfast.isChecked())
			if(mc.thePlayer.getHealth() > 0
				&& mc.thePlayer.onGround
				&& mc.thePlayer.inventory.getCurrentItem() != null
				&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemPotion
				&& mc.gameSettings.keyBindUseItem.pressed)
						for(int i = 0; i < 100; i++)
							mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer(false)); 
		if(useanythingfast.isChecked())
			if(mc.thePlayer.getHealth() > 0
				&& mc.thePlayer.onGround
				&& mc.thePlayer.inventory.getCurrentItem() != null
				&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBucketMilk
				&& mc.gameSettings.keyBindUseItem.pressed)
						for(int i = 0; i < 100; i++)
							mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer(false)); 
		} else
		{
			  if (mc.thePlayer.getHealth() > 0
					&& mc.thePlayer.onGround 
					&& mc.thePlayer.getItemInUseDuration() == 16 && 
				  mc.thePlayer.inventory.getCurrentItem() != null &&
				  !(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow))
		      {
				  boolean isIncorrectItem = mc.thePlayer.inventory.getCurrentItem().getItem() 
					  instanceof ItemBucketMilk ||
					  mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAppleGold ||
					  mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemPotion;
				  if(!isIncorrectItem || useanythingfast.isChecked())
				  {
					  for (int i = 0; i < 17; i++)
						  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
					  mc.thePlayer.sendQueue.addToSendQueue(
						  new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, 
							  BlockPos.ORIGIN, EnumFacing.DOWN));
				  }
		      }
		}
			
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
