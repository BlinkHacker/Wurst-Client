/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.listeners.UpdateListener;

@Info(help = "Drops all your items on the ground.",
	name = "drop",
	syntax = {"[infinite <on|off>]"})
public class DropCmd extends Cmd implements UpdateListener
{
	private int timer;
	private int counter;
	private boolean infinite;
	
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 2)
			syntaxError();
		if(args.length == 2)
			if(args[0].equalsIgnoreCase("infinite") && mc.thePlayer.capabilities.isCreativeMode && args[1].equalsIgnoreCase("on"))
				infinite = true;
			else if(args[0].equalsIgnoreCase("infinite") && !mc.thePlayer.capabilities.isCreativeMode && args[1].equalsIgnoreCase("on"))
			error("Creative mode only.");
			else if(args[0].equalsIgnoreCase("infinite") && args[1].equalsIgnoreCase("off"))
				infinite = false;
			else
				syntaxError();
		timer = 0;
		counter = 9;
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(infinite)
		{
			if(!mc.thePlayer.capabilities.isCreativeMode) {
				infinite=false;
			}
			Item item = null;
			while(item == null)
				item = Item.getItemById(new Random().nextInt(431));
			mc.thePlayer.sendQueue
				.addToSendQueue(new C10PacketCreativeInventoryAction(-1,
					new ItemStack(item, 64)));
			return;
		}
		if(wurst.mods.yesCheatMod.isActive())
		{
			timer++;
			if(timer >= 5)
			{
				mc.playerController.windowClick(0, counter, 1, 4, mc.thePlayer);
				counter++;
				timer = 0;
				if(counter >= 45)
					wurst.events.remove(UpdateListener.class, this);
			}
		}else
		{
			for(int i = 9; i < 45; i++)
				mc.playerController.windowClick(0, i, 1, 4, mc.thePlayer);
			wurst.events.remove(UpdateListener.class, this);
		}
	}
	@Override
	public String getPrimaryAction()
	{
		return "Drop all items";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".drop", true));
	}
}
