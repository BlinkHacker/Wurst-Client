/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Allows you to replicate items from your hand or from an armor slot.\n"
	+ "Requires creative mode.",
	name = "duplicate",
	syntax = {"(hand|chest|legs|feet)"})
public class DuplicateCmd extends Cmd {
	
	@Override
	public void execute(String[] args) throws Error {
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		if(args.length == 0)
		{
			ItemStack item = null;
			item = mc.thePlayer.inventory.getCurrentItem();
			if(item == null)
				error("There is no item in your hand.");
			//copy the item
			for(int i = 0; i < 9; i++)
				if(mc.thePlayer.inventory.getStackInSlot(i) == null)
				{
					mc.thePlayer.sendQueue
						.addToSendQueue(new C10PacketCreativeInventoryAction(
							36 + i, item));
					wurst.chat.message("Item copied.");
					return;
				}
			error("Please clear a slot in your hotbar.");
		
	} else if(args.length == 1)
	{
		ItemStack item = null;
		switch(args[0].toLowerCase())
		{
			case "head":
				item = mc.thePlayer.inventory.armorItemInSlot(3);
				break;
			case "chest":
				item = mc.thePlayer.inventory.armorItemInSlot(2);
				break;
			case "legs":
				item = mc.thePlayer.inventory.armorItemInSlot(1);
				break;
			case "feet":
				item = mc.thePlayer.inventory.armorItemInSlot(0);
				break;
			default:
				syntaxError();
				break;
		}
		if(item == null)
			error("Armor could not be found in your " + args[0] + ".");
		//copy the item
		for(int i = 0; i < 9; i++)
			if(mc.thePlayer.inventory.getStackInSlot(i) == null)
			{
				mc.thePlayer.sendQueue
					.addToSendQueue(new C10PacketCreativeInventoryAction(
						36 + i, item));
				wurst.chat.message("Item copied.");
				return;
			}
		error("Please clear a slot in your hotbar.");
		
		
		
	} else
		syntaxError();
}}