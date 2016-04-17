/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S07PacketRespawn;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.PacketOutputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.events.listeners.PacketOutputListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Allows you walk around after dying.",
	name = "Ghost")
public class GhostMod extends Mod implements UpdateListener, PacketOutputListener, PacketInputListener
{
	boolean isdead;
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(PacketOutputListener.class, this);
		wurst.events.add(PacketInputListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if (mc.theWorld == null)
			return;
		if(mc.thePlayer.getHealth() <= 0)
			mc.thePlayer.setHealth(20.0F);
		{
			mc.thePlayer.isDead = false;
			isdead = true;
			mc.displayGuiScreen(null);
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			wurst.chat.message("You are now dead and in Ghost mode.");
		}
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		PacketOutputEvent receive = (PacketOutputEvent)event;
		if(receive.getPacket() instanceof C03PacketPlayer && isdead) 
			 event.cancel();
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		PacketInputEvent receive = (PacketInputEvent)event;
		if(receive.getPacket() instanceof S07PacketRespawn && isdead) 
			event.cancel();
	}
	
	@Override
	public void onDisable()
	{
		if(isdead)
		{
		mc.thePlayer.respawnPlayer();
		wurst.chat.message("You are no longer dead and have exited Ghost mode.");
		}
		isdead = false;
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(PacketOutputListener.class, this);
		wurst.events.remove(PacketInputListener.class, this);
	}
}
