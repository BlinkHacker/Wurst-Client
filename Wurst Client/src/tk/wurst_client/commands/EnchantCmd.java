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
	syntax = {"clear", "<level>","all <level>"})
public class EnchantCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		EntityPlayerSP player = mc.thePlayer;
		if (args[0].equalsIgnoreCase("clear") && args.length == 1) {
			 			ItemStack currentItem = player.inventory.getCurrentItem();
			 			if(currentItem == null)
			 				error("There is no item in your hand.");
			 			
			 			NBTTagCompound tag = currentItem.getTagCompound();
			 			
			 			if (tag != null && tag.hasKey("ench")) {
			 				tag.removeTag("ench");
			 				
			 				mc.thePlayer.sendQueue.addToSendQueue(
			 						new C10PacketCreativeInventoryAction(
			 								36+player.inventory.currentItem, currentItem));
			 			
			 			}
			 			} else if (args.length == 1 && MiscUtils.isInteger(args[0])) {
			 				
			 				ItemStack currentItem = player.inventory.getCurrentItem();
				 			if(currentItem == null)
				 				error("There is no item in your hand.");
			 				if(MiscUtils.isInteger(args[0])) 
			 				if(Integer.valueOf(args[0]) < -128 || Integer.valueOf(args[0]) > 127)
			 					error("Enchantments cannot be higher than 127 or less than -128.");
			 				for(Enchantment enchantment : Enchantment.enchantmentsList)
			 					try
			 					{
			 						if(enchantment == Enchantment.silkTouch)
			 							continue;
			 						currentItem.addEnchantment(enchantment, Integer.valueOf(args[0]));
			 					}catch(Exception e)
			 					{	
			 						
			 					}
			 				            
			 			} else if (args.length == 2 && args[0].equals("all") && MiscUtils.isInteger(args[1])) {
			 				int items = 0;
			 				for(int i = 5; i < 45; i++)
			 				{
			 					ItemStack currentItem = player.inventoryContainer.getSlot(i).getStack();
			 					if(currentItem == null)
			 						continue;
			 					items++;
			 					if(MiscUtils.isInteger(args[1])) 
			 					if(Integer.valueOf(args[1]) < -128 || Integer.valueOf(args[1]) > 127)
					 				error("Enchantments cannot be higher than 127 or less than -128.");
			 					for(Enchantment enchantment : Enchantment.enchantmentsList)
			 						try
			 						{
			 							if(enchantment == Enchantment.silkTouch)
			 								continue;
			 							currentItem.addEnchantment(enchantment, Integer.valueOf(args[1]));
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
