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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Enchants items with everything, or removes enchantments.",
	name = "enchant",
	syntax = {"clear", "(hand|allitems) <enchantment_name|enchantment_id|all> <level>"
		,"(hand|allitems) all maxpossible"})
public class EnchantCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		EntityPlayerSP player = mc.thePlayer;
		if (args.length == 0 || args.length == 2 || args.length > 3)
			syntaxError();
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
			 				if(MiscUtils.isInteger(args[2])) 
			 				if(Integer.valueOf(args[2]) < -128 || Integer.valueOf(args[2]) > 127)
			 					error("Enchantments cannot be higher than 127 or less than -128.");
			 				for(Enchantment enchantment : Enchantment.enchantmentsList)
			 					try
			 					{
			 						if(enchantment == Enchantment.silkTouch)
			 							continue;
			 						currentItem.addEnchantment(enchantment, Integer.valueOf(args[2]));
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
			 				if(MiscUtils.isInteger(args[1])) 
			 					if(Integer.valueOf(args[1]) != 62 && Integer.valueOf(args[1]) != 61 &&
			 						Integer.valueOf(args[1]) != 51 && Integer.valueOf(args[1]) != 50 &&
			 						Integer.valueOf(args[1]) != 49 && Integer.valueOf(args[1]) != 48 &&
			 						Integer.valueOf(args[1]) != 35 && Integer.valueOf(args[1]) != 34 &&
			 						Integer.valueOf(args[1]) != 33 && Integer.valueOf(args[1]) != 32 &&
			 						Integer.valueOf(args[1]) != 21 && Integer.valueOf(args[1]) != 20 &&
			 						Integer.valueOf(args[1]) != 19 && Integer.valueOf(args[1]) != 18 &&
			 						Integer.valueOf(args[1]) != 17 && Integer.valueOf(args[1]) != 16 &&
			 						Integer.valueOf(args[1]) != 8 && Integer.valueOf(args[1]) != 7 &&
			 						Integer.valueOf(args[1]) != 6 && Integer.valueOf(args[1]) != 5 &&
			 						Integer.valueOf(args[1]) != 4 && Integer.valueOf(args[1]) != 3 &&
			 						Integer.valueOf(args[1]) != 2 && Integer.valueOf(args[1]) != 1 &&
			 						Integer.valueOf(args[1]) != 0)
			 						error("Enchantment ID is invaild.");
			 				if(!MiscUtils.isInteger(args[1])) 
			 					if(!args[1].equalsIgnoreCase("protection") &&
			 						!args[1].equalsIgnoreCase("fire_protection") &&
			 						!args[1].equalsIgnoreCase("feather_falling") &&
			 						!args[1].equalsIgnoreCase("blast_protection") &&
			 						!args[1].equalsIgnoreCase("projectile_protection") &&
			 						!args[1].equalsIgnoreCase("respiration") &&
			 						!args[1].equalsIgnoreCase("aqua_affinity") &&
			 						!args[1].equalsIgnoreCase("thorns") &&
			 						!args[1].equalsIgnoreCase("depth_strider") &&
			 						!args[1].equalsIgnoreCase("sharpness") &&
			 						!args[1].equalsIgnoreCase("smite") &&
			 						!args[1].equalsIgnoreCase("bane_of_arthropods") &&
			 						!args[1].equalsIgnoreCase("knockback") &&
			 						!args[1].equalsIgnoreCase("fire_aspect") &&
			 						!args[1].equalsIgnoreCase("looting") &&
			 						!args[1].equalsIgnoreCase("efficiency") &&
			 						!args[1].equalsIgnoreCase("silk_touch") &&
			 						!args[1].equalsIgnoreCase("unbreaking") &&
			 						!args[1].equalsIgnoreCase("fortune") &&
			 						!args[1].equalsIgnoreCase("power") &&
			 						!args[1].equalsIgnoreCase("punch") &&
			 						!args[1].equalsIgnoreCase("flame") &&
			 						!args[1].equalsIgnoreCase("infinity") &&
			 						!args[1].equalsIgnoreCase("luck_of_the_sea") &&
			 						!args[1].equalsIgnoreCase("lure"))
			 						error("Enchantment name is invaild.");
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
			 					if(MiscUtils.isInteger(args[2])) 
			 					if(Integer.valueOf(args[2]) < -128 || Integer.valueOf(args[2]) > 127)
					 				error("Enchantments cannot be higher than 127 or less than -128.");
			 					for(Enchantment enchantment : Enchantment.enchantmentsList)
			 						try
			 						{
			 							if(enchantment == Enchantment.silkTouch)
			 								continue;
			 							currentItem.addEnchantment(enchantment, Integer.valueOf(args[2]));
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
			 				int items2 = 0;
			 				for(int i = 5; i < 45; i++)
			 				{
			 					ItemStack currentItem = player.inventoryContainer.getSlot(i).getStack();
			 					if(currentItem == null)
			 						continue;
			 					items2++;
			 					if(MiscUtils.isInteger(args[2])) 
			 					if(Integer.valueOf(args[2]) < -128 || Integer.valueOf(args[2]) > 127)
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
				 				if(MiscUtils.isInteger(args[1])) 
				 					if(Integer.valueOf(args[1]) != 62 && Integer.valueOf(args[1]) != 61 &&
				 						Integer.valueOf(args[1]) != 51 && Integer.valueOf(args[1]) != 50 &&
				 						Integer.valueOf(args[1]) != 49 && Integer.valueOf(args[1]) != 48 &&
				 						Integer.valueOf(args[1]) != 35 && Integer.valueOf(args[1]) != 34 &&
				 						Integer.valueOf(args[1]) != 33 && Integer.valueOf(args[1]) != 32 &&
				 						Integer.valueOf(args[1]) != 21 && Integer.valueOf(args[1]) != 20 &&
				 						Integer.valueOf(args[1]) != 19 && Integer.valueOf(args[1]) != 18 &&
				 						Integer.valueOf(args[1]) != 17 && Integer.valueOf(args[1]) != 16 &&
				 						Integer.valueOf(args[1]) != 8 && Integer.valueOf(args[1]) != 7 &&
				 						Integer.valueOf(args[1]) != 6 && Integer.valueOf(args[1]) != 5 &&
				 						Integer.valueOf(args[1]) != 4 && Integer.valueOf(args[1]) != 3 &&
				 						Integer.valueOf(args[1]) != 2 && Integer.valueOf(args[1]) != 1 &&
				 						Integer.valueOf(args[1]) != 0)
				 						error("Enchantment ID is invaild.");
				 				if(!MiscUtils.isInteger(args[1])) 
				 					if(!args[1].equalsIgnoreCase("protection") &&
				 						!args[1].equalsIgnoreCase("fire_protection") &&
				 						!args[1].equalsIgnoreCase("feather_falling") &&
				 						!args[1].equalsIgnoreCase("blast_protection") &&
				 						!args[1].equalsIgnoreCase("projectile_protection") &&
				 						!args[1].equalsIgnoreCase("respiration") &&
				 						!args[1].equalsIgnoreCase("aqua_affinity") &&
				 						!args[1].equalsIgnoreCase("thorns") &&
				 						!args[1].equalsIgnoreCase("depth_strider") &&
				 						!args[1].equalsIgnoreCase("sharpness") &&
				 						!args[1].equalsIgnoreCase("smite") &&
				 						!args[1].equalsIgnoreCase("bane_of_arthropods") &&
				 						!args[1].equalsIgnoreCase("knockback") &&
				 						!args[1].equalsIgnoreCase("fire_aspect") &&
				 						!args[1].equalsIgnoreCase("looting") &&
				 						!args[1].equalsIgnoreCase("efficiency") &&
				 						!args[1].equalsIgnoreCase("silk_touch") &&
				 						!args[1].equalsIgnoreCase("unbreaking") &&
				 						!args[1].equalsIgnoreCase("fortune") &&
				 						!args[1].equalsIgnoreCase("power") &&
				 						!args[1].equalsIgnoreCase("punch") &&
				 						!args[1].equalsIgnoreCase("flame") &&
				 						!args[1].equalsIgnoreCase("infinity") &&
				 						!args[1].equalsIgnoreCase("luck_of_the_sea") &&
				 						!args[1].equalsIgnoreCase("lure"))
				 						error("Enchantment name is invaild.");
				 				mc.thePlayer.sendQueue.addToSendQueue(
				 						new C10PacketCreativeInventoryAction(
				 								i, currentItem));
			 				}
			 				if(items2 == 1)
			 					wurst.chat.message("Enchanted 1 item.");
			 				else
			 					wurst.chat.message("Enchanted " + items2 + " items.");
						}else if(args.length == 3 && args[1].equalsIgnoreCase("all") && args[2].equalsIgnoreCase("maxpossible") 
			 				&& args[0].equalsIgnoreCase("hand")) {
							//enchant hand all maxpossible
							ItemStack currentItem = player.inventory.getCurrentItem();
				 			if(currentItem == null)
				 				error("There is no item in your hand.");
				 			currentItem.addEnchantment(Enchantment.field_180310_c, 4);
				 			currentItem.addEnchantment(Enchantment.fireProtection, 4);
				 			currentItem.addEnchantment(Enchantment.field_180309_e, 4);
				 			currentItem.addEnchantment(Enchantment.blastProtection, 4);
				 			currentItem.addEnchantment(Enchantment.field_180308_g, 4);
				 			currentItem.addEnchantment(Enchantment.field_180317_h, 2);
				 			currentItem.addEnchantment(Enchantment.aquaAffinity, 1);
				 			currentItem.addEnchantment(Enchantment.thorns, 3);
				 			currentItem.addEnchantment(Enchantment.field_180316_k, 3);
				 			currentItem.addEnchantment(Enchantment.field_180314_l, 5);
				 			currentItem.addEnchantment(Enchantment.field_180315_m, 5);
				 			currentItem.addEnchantment(Enchantment.field_180312_n, 5);
				 			currentItem.addEnchantment(Enchantment.field_180313_o, 2);
				 			currentItem.addEnchantment(Enchantment.fireAspect, 2);
				 			currentItem.addEnchantment(Enchantment.looting, 3);
				 			currentItem.addEnchantment(Enchantment.efficiency, 5);
				 			//silk touch skipped
				 			currentItem.addEnchantment(Enchantment.unbreaking, 3);
				 			currentItem.addEnchantment(Enchantment.fortune, 3);
				 			currentItem.addEnchantment(Enchantment.power, 5);
				 			currentItem.addEnchantment(Enchantment.punch, 2);
				 			currentItem.addEnchantment(Enchantment.flame, 1);
				 			currentItem.addEnchantment(Enchantment.infinity, 1);
				 			currentItem.addEnchantment(Enchantment.luckOfTheSea, 3);
				 			currentItem.addEnchantment(Enchantment.lure, 3);
				 			mc.thePlayer.sendQueue.addToSendQueue(
								new C10PacketCreativeInventoryAction(
										36+player.inventory.currentItem, currentItem));
						}else if(args.length == 3 && args[1].equalsIgnoreCase("all") && args[2].equalsIgnoreCase("maxpossible") 
			 				&& args[0].equalsIgnoreCase("allitems")) {
							//enchant allitems all maxpossible
							int items3 = 0;
			 				for(int i = 5; i < 45; i++)
			 				{
			 					ItemStack currentItem = player.inventoryContainer.getSlot(i).getStack();
			 					if(currentItem == null)
			 						continue;
			 					items3++;
			 					currentItem.addEnchantment(Enchantment.field_180310_c, 4);
					 			currentItem.addEnchantment(Enchantment.fireProtection, 4);
					 			currentItem.addEnchantment(Enchantment.field_180309_e, 4);
					 			currentItem.addEnchantment(Enchantment.blastProtection, 4);
					 			currentItem.addEnchantment(Enchantment.field_180308_g, 4);
					 			currentItem.addEnchantment(Enchantment.field_180317_h, 2);
					 			currentItem.addEnchantment(Enchantment.aquaAffinity, 1);
					 			currentItem.addEnchantment(Enchantment.thorns, 3);
					 			currentItem.addEnchantment(Enchantment.field_180316_k, 3);
					 			currentItem.addEnchantment(Enchantment.field_180314_l, 5);
					 			currentItem.addEnchantment(Enchantment.field_180315_m, 5);
					 			currentItem.addEnchantment(Enchantment.field_180312_n, 5);
					 			currentItem.addEnchantment(Enchantment.field_180313_o, 2);
					 			currentItem.addEnchantment(Enchantment.fireAspect, 2);
					 			currentItem.addEnchantment(Enchantment.looting, 3);
					 			currentItem.addEnchantment(Enchantment.efficiency, 5);
					 			//silk touch skipped
					 			currentItem.addEnchantment(Enchantment.unbreaking, 3);
					 			currentItem.addEnchantment(Enchantment.fortune, 3);
					 			currentItem.addEnchantment(Enchantment.power, 5);
					 			currentItem.addEnchantment(Enchantment.punch, 2);
					 			currentItem.addEnchantment(Enchantment.flame, 1);
					 			currentItem.addEnchantment(Enchantment.infinity, 1);
					 			currentItem.addEnchantment(Enchantment.luckOfTheSea, 3);
					 			currentItem.addEnchantment(Enchantment.lure, 3);
					 			mc.thePlayer.sendQueue.addToSendQueue(
				 						new C10PacketCreativeInventoryAction(
				 								i, currentItem));
			 				}
					 			if(items3 == 1)
				 					wurst.chat.message("Enchanted 1 item.");
				 				else
				 					wurst.chat.message("Enchanted " + items3 + " items.");
						}else
			syntaxError();
	}
}
