/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Removes all Wurst related elements for recording..",
	name = "RecordingMode")
public class RecordingModeMod extends Mod implements UpdateListener
{	
	public boolean WmsWasOff;
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	@Override
	public void onUpdate()
	{
		if(!wurst.chat.isChatEnabled())
			WmsWasOff=true;
		wurst.commands.onSentMessage(new ChatOutputEvent(".wms off", true));
		wurst.commands.onSentMessage(new ChatOutputEvent(".clear", true));
		
		
	}
	@Override
	public void onDisable()
	{
		if(!WmsWasOff)
			wurst.commands.onSentMessage(new ChatOutputEvent(".wms on", true));
		wurst.events.remove(UpdateListener.class, this);
	}
}
