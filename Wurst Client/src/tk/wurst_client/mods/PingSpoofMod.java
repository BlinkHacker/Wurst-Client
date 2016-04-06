/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import tk.wurst_client.events.PacketOutputEvent;
import tk.wurst_client.events.listeners.PacketOutputListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.navigator.settings.SliderSetting;

@Info(category = Category.EXPLOITS,
	description = "Spoofs ping to a low amount. While the key mode\n"
		+ "is excellent if it works, the packet mode works 100%.",
	name = "PingSpoof")
public class PingSpoofMod extends Mod implements PacketOutputListener
{
	private int mode = 0;
	private String[] modes = new String[]{"Key", "Packet"};
	private long packetdelay;
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Packet Delay", packet, 1, 20, 0.5,
			ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				packet = (float)getValue();
				packetdelay = (long)(packet * 1000);
			}
		});
		settings.add(new ModeSetting("Mode", modes, mode)
		{
			@Override
			public void update()
			{
				mode = getSelected();
			}
		});
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if(mode == 0)
		{
		 PacketOutputEvent sent = (PacketOutputEvent)event;
         if(sent.getPacket() instanceof C00PacketKeepAlive) {
            C00PacketKeepAlive packet = (C00PacketKeepAlive)sent.getPacket();
            packet.key = Integer.MAX_VALUE;
         }
		}
		if(mode == 1)
		{
			updateMS();
			if (!(event.getPacket() instanceof C00PacketKeepAlive))
				return;
			if (!hasTimePassedM(packetdelay))
		            event.cancel();
		        else
		            updateLastMS();
		}
		
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketOutputListener.class, this);
	}

}
