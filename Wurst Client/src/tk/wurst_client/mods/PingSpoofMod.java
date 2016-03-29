/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import tk.wurst_client.events.PacketOutputEvent;
import tk.wurst_client.events.listeners.PacketOutputListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
	description = "Spoofs ping to 0. Works best when enabled"
		+ "before you join a server and when you keep this on.",
	name = "PingSpoof")
public class PingSpoofMod extends Mod implements PacketOutputListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		 PacketOutputEvent sent = (PacketOutputEvent)event;
         if(sent.getPacket() instanceof C00PacketKeepAlive) {
            C00PacketKeepAlive packet = (C00PacketKeepAlive)sent.getPacket();
            packet.key = Integer.MAX_VALUE;
         }
		
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketOutputListener.class, this);
	}

}
