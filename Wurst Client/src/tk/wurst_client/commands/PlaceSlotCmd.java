/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.item.ItemStack;
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
		if(args.length != 1)
			syntaxError();
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		if(args.length == 1) {
			int slotid = -1;
			ItemStack item = null;
			item = mc.thePlayer.inventory.getCurrentItem();
			if(item == null)
				error("There is no item in your hand.");
			switch(args[0].toLowerCase())
			{
				case "head":
					slotid = 3;
					break;
				case "chest":
					slotid = 2;
					break;
				case "legs":
					slotid = 1;
					break;
				case "feet":
					slotid = 0;
					break;
				default:
					syntaxError();
					break;
			}
			if(mc.thePlayer.getInventory()[slotid] != null)	
				error("Item already in slot \"" + args[0] + "\"");
			if (slotid > 3 || slotid < 0) {
				error("Armor slot is invaild.");
			} else {
				mc.thePlayer.getInventory()[slotid] = item;
			}
			wurst.chat.message("Item placed in slot \"" + args[0] + "\"");
		}
		
	} 
		
			
}