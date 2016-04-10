/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.listeners.UpdateListener;

@Info(help = "Duplicates items in your hand in a specified armor slot.\n"
	+ "Requires creative mode.",
	name = "placeslot",
	syntax = {"(head|chest|legs|feet)", "headderp (on|off)"})
public class PlaceSlotCmd extends Cmd implements UpdateListener
{
	private boolean headderp;
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
		} else if(args.length == 2 && args[0].equalsIgnoreCase("headderp") && 
			args[1].equalsIgnoreCase("on"))
		{
			headderp = true;
			wurst.events.add(UpdateListener.class, this);
		} else if(args.length == 2 && args[0].equalsIgnoreCase("headderp") && 
			args[1].equalsIgnoreCase("off"))
		{
			headderp = false;
			wurst.events.remove(UpdateListener.class, this);
		} else
			syntaxError();
	}
	
	@Override
	public void onUpdate()
	{
		if(headderp)
		{
			if (!mc.playerController.isInCreativeMode() || mc.theWorld == null)
				return;
			ItemStack block = getRandomBlock();
		      if (Item.getIdFromItem(block.getItem()) != 0 &&
		    	  Item.getIdFromItem(block.getItem()) != 86)
		        {
		          mc.thePlayer.inventoryContainer.putStackInSlot(5, block);
		          mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, block));
		        }
		}
	} 	
	
	private ItemStack getRandomBlock()
	{
		Random random = new Random();
	    ArrayList blockreg = Lists.newArrayList(Block.blockRegistry.iterator());
	    int i = 0;
	    int size = blockreg.size() - 1;
	    int randblock = random.nextInt(size - i) + i;
	    return new ItemStack((Block)blockreg.get(randblock));
	}
}