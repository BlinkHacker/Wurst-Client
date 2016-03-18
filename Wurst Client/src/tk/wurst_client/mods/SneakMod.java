/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.ModeSetting;

@Info(category = Category.MOVEMENT,
	description = "Automatically sneaks all the time.",
	name = "Sneak")
public class SneakMod extends Mod implements UpdateListener
{
	boolean hassneaked = false;
	private int mode = 0;
	private String[] modes = new String[]{"Packet", "Real"};
	@Override
	public void initSettings()
	{
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
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(mode == 0) {
		if(wurst.mods.yesCheatMod.isActive())
		{
			NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;
			sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft
				.getMinecraft().thePlayer, Action.START_SNEAKING));
			sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft
				.getMinecraft().thePlayer, Action.STOP_SNEAKING));
		}else 
			if(hassneaked == false) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(
				Minecraft.getMinecraft().thePlayer, Action.START_SNEAKING));
			hassneaked=true;
			}
		} else if (mode == 1) 
		{
			mc.gameSettings.keyBindSneak.pressed = true;
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		mc.gameSettings.keyBindSneak.pressed = false;
		mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(
			mc.thePlayer, Action.STOP_SNEAKING));
		hassneaked=false;
	}
	public int getMode()
	{
		return mode;
	}
	
	public void setMode(int mode)
	{
		((ModeSetting)settings.get(1)).setSelected(mode);
	}
	
	public String[] getModes()
	{
		return modes;
	}
}
