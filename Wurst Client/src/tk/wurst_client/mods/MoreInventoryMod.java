/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.client.C0DPacketCloseWindow;
import tk.wurst_client.events.PacketOutputEvent;
import tk.wurst_client.events.listeners.PacketOutputListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Prevents items from dropping in crafting slots.\n"
		+ "Note: You must click the items in the slots to make them\n"
		+ "appear again after closing your inventory.",
	name = "MoreInventory")
public class MoreInventoryMod extends Mod implements PacketOutputListener
{
	
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		PacketOutputEvent receive = (PacketOutputEvent)event;
		 if(receive.getPacket() instanceof C0DPacketCloseWindow) 
		 {
			 event.cancel();
		 }
	}	
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketOutputListener.class, this);
	}
}
