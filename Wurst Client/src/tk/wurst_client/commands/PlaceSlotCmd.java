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

@Info(help = "Duplicates items in your hand in a specified armor slot.\n"
	+ "Requires creative mode.",
	name = "placeslot",
	syntax = {"(head|chest|legs|feet)"})
public class PlaceSlotCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		if(args.length == 1 && args[0].equalsIgnoreCase("head") || 
			args[0].equalsIgnoreCase("chest") || args[0].equalsIgnoreCase("legs") ||
			args[0].equalsIgnoreCase("feet")) 
		{
			int slotid = -1;
			int slotinvid = -1;
			ItemStack item = null;
			item = mc.thePlayer.inventory.getCurrentItem();
			if(item == null)
				error("There is no item in your hand.");
			switch(args[0].toLowerCase())
			{
				case "head":
					slotid = 5;
					slotinvid = 3;
					break;
				case "chest":
					slotid = 6;
					slotinvid = 2;
					break;
				case "legs":
					slotid = 7;
					slotinvid = 1;
					break;
				case "feet":
					slotid = 8;
					slotinvid = 0;
					break;
				default:
					syntaxError();
					break;
			}
			if(mc.thePlayer.getInventory()[slotinvid] != null)	
				error("Item already in slot \"" + args[0] + "\"");
			if (slotid > 8 || slotid < 5)
				error("Armor slot is invaild.");
			else 
			{
				mc.thePlayer.inventoryContainer.putStackInSlot(slotid, item);
				mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(slotid, item));
			}
			wurst.chat.message("Item placed in slot \"" + args[0] + "\"");
		} else
			syntaxError();
	}
}