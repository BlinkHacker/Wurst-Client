/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.ChatOutputEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Enchants items with everything, or removes enchantments.",
	name = "enchant",
	syntax = {"all","clear", "<level>","all <level>"})
public class EnchantCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		EntityPlayerSP player = mc.thePlayer;
		if(args.length == 0)
		{
			ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
			if(currentItem == null)
				error("There is no item in your hand.");
			for(Enchantment enchantment : Enchantment.enchantmentsList)
				try
				{
					if(enchantment == Enchantment.silkTouch)
						continue;
					currentItem.addEnchantment(enchantment, 127);
				}catch(Exception e)
				{	
					
				}
			            mc.thePlayer.sendQueue.addToSendQueue(
				 					new C10PacketCreativeInventoryAction(
				 							36+player.inventory.currentItem, currentItem));
		}else if(args[0].equals("all"))
		{
			int items = 0;
			for(int i = 0; i < 40; i++)
			{
				ItemStack currentItem =
					mc.thePlayer.inventory.getStackInSlot(i);
				if(currentItem == null)
					continue;
				items++;
				for(Enchantment enchantment : Enchantment.enchantmentsList)
					try
					{
						if(enchantment == Enchantment.silkTouch)
							continue;
						currentItem.addEnchantment(enchantment, 127);
					}catch(Exception e)
					{	
						
					}
				mc.thePlayer.sendQueue.addToSendQueue(
					 		new C10PacketCreativeInventoryAction(
					 						i, currentItem));
			}
			if(items == 1)
				wurst.chat.message("Enchanted 1 item.");
			else
				wurst.chat.message("Enchanted " + items + " items.");
		}else if (args[0].equalsIgnoreCase("clear")) {
			 			ItemStack currentItem = player.inventory.getCurrentItem();
			 			if(currentItem == null)
			 				error("There is no item in your hand.");
			 			
			 			NBTTagCompound tag = currentItem.getTagCompound();
			 			
			 			if (tag != null && tag.hasKey("ench")) {
			 				tag.removeTag("ench");
			 				
			 				mc.thePlayer.sendQueue.addToSendQueue(
			 						new C10PacketCreativeInventoryAction(
			 								36+player.inventory.currentItem, currentItem));
			 				
			 				wurst.chat.message("Disenchanted 1 item.");
			 			} else if (args.length == 1) {
			 				if(args[0] == null || MiscUtils.isInteger(args[0])) 
			 					error("Level must be a number.");
			 				for(Enchantment enchantment : Enchantment.enchantmentsList)
			 					try
			 					{
			 						if(enchantment == Enchantment.silkTouch)
			 							continue;
			 						currentItem.addEnchantment(enchantment, Integer.valueOf(args[0]));
			 					}catch(Exception e)
			 					{	
			 						
			 					}
			 				            mc.thePlayer.sendQueue.addToSendQueue(
			 					 					new C10PacketCreativeInventoryAction(
			 					 							36+player.inventory.currentItem, currentItem));
			 			}
		}else
			syntaxError();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Enchant Current Item";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".enchant", true));
	}
}
