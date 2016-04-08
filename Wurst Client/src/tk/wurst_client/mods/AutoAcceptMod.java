/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.events.ChatInputEvent;
import tk.wurst_client.events.listeners.ChatInputListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.CHAT,
	description = "Automatically accept friend teleport requests.\n"
		+ "You can choose if you want to accept teleport-to requests.",
	name = "AutoAccept")
public class AutoAcceptMod extends Mod implements ChatInputListener
{	
	public final CheckboxSetting tpto = new CheckboxSetting(
		"AutoAccept TP-To Requests", false);
	
	@Override
	public void initSettings()
	{
		settings.add(tpto);
	}
	@Override
	public void onEnable()
	{
		wurst.events.add(ChatInputListener.class, this);
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		String message = new String(event.getComponent().getUnformattedText());
		if(message.startsWith("§c[§6Wurst§c]§f "))
			return;
		if(message.contains("has requested to teleport to you."))
			for(String friend : wurst.friends)
				if(message.contains(friend))
					mc.thePlayer.sendChatMessage("/tpaccept " + friend);
		if(message.contains("has requested that you teleport to them.") && tpto.isChecked())
			for(String friend : wurst.friends)
				if(message.contains(friend))
					mc.thePlayer.sendChatMessage("/tpaccept " + friend);
	}	
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(ChatInputListener.class, this);
	}
}
