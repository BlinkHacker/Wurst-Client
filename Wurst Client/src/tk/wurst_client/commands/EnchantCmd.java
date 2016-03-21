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
	syntax = {"clear", "(hand|allitems) <enchantment_name_or_id|all> <level>"})
public class EnchantCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		EntityPlayerSP player = mc.thePlayer;
		if (args[0].equalsIgnoreCase("clear") && args.length == 1) {
			//enchant clear
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
			 			} else if (args.length == 3 && MiscUtils.isInteger(args[2]) 
			 				&& args[0].equalsIgnoreCase("hand") && args[1].equalsIgnoreCase("all")) {
			 				//enchant hand <all> <level>
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
			 				mc.thePlayer.sendQueue.addToSendQueue(
								new C10PacketCreativeInventoryAction(
										36+player.inventory.currentItem, currentItem));
			 			} else if (args.length == 3 && !args[1].equalsIgnoreCase("all") && MiscUtils.isInteger(args[2])
			 				&& args[0].equalsIgnoreCase("hand")) {
			 				//enchant hand <enchantment> <level>
			 				ItemStack currentItem = player.inventory.getCurrentItem();
			 			if(currentItem == null)
			 				error("There is no item in your hand.");
			 			if(MiscUtils.isInteger(args[2])) 
			 				if(Integer.valueOf(args[2]) < -128 || Integer.valueOf(args[2]) > 127)
			 					error("Enchantments cannot be higher than 127 or less than -128."); 
			 				//TODO: Better way to do this!
			 				if(args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("protection"))
			 					currentItem.addEnchantment(Enchantment.field_180310_c, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("fire_protection"))
			 					currentItem.addEnchantment(Enchantment.fireProtection, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("2") || args[1].equalsIgnoreCase("feather_falling"))
			 					currentItem.addEnchantment(Enchantment.field_180309_e, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("3") || args[1].equalsIgnoreCase("blast_protection"))
			 					currentItem.addEnchantment(Enchantment.blastProtection, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("4") || args[1].equalsIgnoreCase("projectile_protection"))
			 					currentItem.addEnchantment(Enchantment.field_180308_g, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("5") || args[1].equalsIgnoreCase("respiration"))
			 					currentItem.addEnchantment(Enchantment.field_180317_h, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("6") || args[1].equalsIgnoreCase("aqua_affinity"))
			 					currentItem.addEnchantment(Enchantment.aquaAffinity, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("7") || args[1].equalsIgnoreCase("thorns"))
			 					currentItem.addEnchantment(Enchantment.thorns, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("8") || args[1].equalsIgnoreCase("depth_strider"))
			 					currentItem.addEnchantment(Enchantment.field_180316_k, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("16") || args[1].equalsIgnoreCase("sharpness"))
			 					currentItem.addEnchantment(Enchantment.field_180314_l, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("17") || args[1].equalsIgnoreCase("smite"))
			 					currentItem.addEnchantment(Enchantment.field_180315_m, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("18") || args[1].equalsIgnoreCase("bane_of_arthropods"))
			 					currentItem.addEnchantment(Enchantment.field_180312_n, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("19") || args[1].equalsIgnoreCase("knockback"))
			 					currentItem.addEnchantment(Enchantment.field_180313_o, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("20") || args[1].equalsIgnoreCase("fire_aspect"))
			 					currentItem.addEnchantment(Enchantment.fireAspect, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("21") || args[1].equalsIgnoreCase("looting"))
			 					currentItem.addEnchantment(Enchantment.looting, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("32") || args[1].equalsIgnoreCase("efficiency"))
			 					currentItem.addEnchantment(Enchantment.efficiency, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("33") || args[1].equalsIgnoreCase("silk_touch"))
			 					currentItem.addEnchantment(Enchantment.silkTouch, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("34") || args[1].equalsIgnoreCase("unbreaking"))
			 					currentItem.addEnchantment(Enchantment.unbreaking, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("35") || args[1].equalsIgnoreCase("fortune"))
			 					currentItem.addEnchantment(Enchantment.fortune, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("48") || args[1].equalsIgnoreCase("power"))
			 					currentItem.addEnchantment(Enchantment.power, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("49") || args[1].equalsIgnoreCase("punch"))
			 					currentItem.addEnchantment(Enchantment.punch, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("50") || args[1].equalsIgnoreCase("flame"))
			 					currentItem.addEnchantment(Enchantment.flame, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("51") || args[1].equalsIgnoreCase("infinity"))
			 					currentItem.addEnchantment(Enchantment.infinity, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("61") || args[1].equalsIgnoreCase("luck_of_the_sea"))
			 					currentItem.addEnchantment(Enchantment.luckOfTheSea, Integer.valueOf(args[2]));
			 				if(args[1].equalsIgnoreCase("62") || args[1].equalsIgnoreCase("lure"))
			 					currentItem.addEnchantment(Enchantment.lure, Integer.valueOf(args[2]));
			 				mc.thePlayer.sendQueue.addToSendQueue(
								new C10PacketCreativeInventoryAction(
										36+player.inventory.currentItem, currentItem));
			 			
			 			} else if (args.length == 3 && args[1].equalsIgnoreCase("all") && MiscUtils.isInteger(args[2])
			 				&& args[0].equalsIgnoreCase("allitems")) {
			 				//enchant allitems <all> <level>
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
			 			}else if(args.length == 3 && !args[1].equalsIgnoreCase("all") && MiscUtils.isInteger(args[2]) 
			 				&& args[0].equalsIgnoreCase("allitems")) {
			 				//enchant allitems <enchant> <level>
			 				//enchant allitems <all> <level>
			 				int items2 = 0;
			 				for(int i = 5; i < 45; i++)
			 				{
			 					ItemStack currentItem = player.inventoryContainer.getSlot(i).getStack();
			 					if(currentItem == null)
			 						continue;
			 					items2++;
			 					if(MiscUtils.isInteger(args[1])) 
			 					if(Integer.valueOf(args[1]) < -128 || Integer.valueOf(args[1]) > 127)
					 				error("Enchantments cannot be higher than 127 or less than -128.");
			 					//This mess again...
				 				if(args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("protection"))
				 					currentItem.addEnchantment(Enchantment.field_180310_c, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("fire_protection"))
				 					currentItem.addEnchantment(Enchantment.fireProtection, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("2") || args[1].equalsIgnoreCase("feather_falling"))
				 					currentItem.addEnchantment(Enchantment.field_180309_e, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("3") || args[1].equalsIgnoreCase("blast_protection"))
				 					currentItem.addEnchantment(Enchantment.blastProtection, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("4") || args[1].equalsIgnoreCase("projectile_protection"))
				 					currentItem.addEnchantment(Enchantment.field_180308_g, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("5") || args[1].equalsIgnoreCase("respiration"))
				 					currentItem.addEnchantment(Enchantment.field_180317_h, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("6") || args[1].equalsIgnoreCase("aqua_affinity"))
				 					currentItem.addEnchantment(Enchantment.aquaAffinity, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("7") || args[1].equalsIgnoreCase("thorns"))
				 					currentItem.addEnchantment(Enchantment.thorns, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("8") || args[1].equalsIgnoreCase("depth_strider"))
				 					currentItem.addEnchantment(Enchantment.field_180316_k, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("16") || args[1].equalsIgnoreCase("sharpness"))
				 					currentItem.addEnchantment(Enchantment.field_180314_l, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("17") || args[1].equalsIgnoreCase("smite"))
				 					currentItem.addEnchantment(Enchantment.field_180315_m, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("18") || args[1].equalsIgnoreCase("bane_of_arthropods"))
				 					currentItem.addEnchantment(Enchantment.field_180312_n, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("19") || args[1].equalsIgnoreCase("knockback"))
				 					currentItem.addEnchantment(Enchantment.field_180313_o, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("20") || args[1].equalsIgnoreCase("fire_aspect"))
				 					currentItem.addEnchantment(Enchantment.fireAspect, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("21") || args[1].equalsIgnoreCase("looting"))
				 					currentItem.addEnchantment(Enchantment.looting, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("32") || args[1].equalsIgnoreCase("efficiency"))
				 					currentItem.addEnchantment(Enchantment.efficiency, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("33") || args[1].equalsIgnoreCase("silk_touch"))
				 					currentItem.addEnchantment(Enchantment.silkTouch, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("34") || args[1].equalsIgnoreCase("unbreaking"))
				 					currentItem.addEnchantment(Enchantment.unbreaking, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("35") || args[1].equalsIgnoreCase("fortune"))
				 					currentItem.addEnchantment(Enchantment.fortune, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("48") || args[1].equalsIgnoreCase("power"))
				 					currentItem.addEnchantment(Enchantment.power, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("49") || args[1].equalsIgnoreCase("punch"))
				 					currentItem.addEnchantment(Enchantment.punch, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("50") || args[1].equalsIgnoreCase("flame"))
				 					currentItem.addEnchantment(Enchantment.flame, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("51") || args[1].equalsIgnoreCase("infinity"))
				 					currentItem.addEnchantment(Enchantment.infinity, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("61") || args[1].equalsIgnoreCase("luck_of_the_sea"))
				 					currentItem.addEnchantment(Enchantment.luckOfTheSea, Integer.valueOf(args[2]));
				 				if(args[1].equalsIgnoreCase("62") || args[1].equalsIgnoreCase("lure"))
				 					currentItem.addEnchantment(Enchantment.lure, Integer.valueOf(args[2]));
				 				mc.thePlayer.sendQueue.addToSendQueue(
				 						new C10PacketCreativeInventoryAction(
				 								i, currentItem));
			 				}
			 				if(items2 == 1)
			 					wurst.chat.message("Enchanted 1 item.");
			 				else
			 					wurst.chat.message("Enchanted " + items2 + " items.");
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
