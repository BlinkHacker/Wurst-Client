/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.listeners.UpdateListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;

@Info(help = "Spams /msg to all players on the server.",
	name = "massmessage",
	syntax = {"[<message>]"})
public class MassMessageCmd extends Cmd implements UpdateListener
{
	private float speed = 1F;
	private int i;
	private ArrayList<String> players;
	private Random random = new Random();
	private String message;
	private boolean toggled;
	
	@Override
	public void execute(String[] args) throws Error
	{
		toggled = !toggled;
		if(toggled)
		{
			if(args.length == 1)
			{
				i = 0;
				message = args[0];
				Iterator itr = mc.getNetHandler().getPlayerInfo().iterator();
				players = new ArrayList<String>();
				while(itr.hasNext())
				players.add(StringUtils.stripControlCodes(((NetworkPlayerInfo)itr
				.next()).getPlayerNameForReal()));
				Collections.shuffle(players, random);
				wurst.events.add(UpdateListener.class, this);
			} else
			{
				syntaxError();
				toggled = false;
			}
		} else
		{
			wurst.events.remove(UpdateListener.class, this);
			if(message != null)
			{
				wurst.chat.message("Disabling MassMessage.");
				message = null;
			}
		}
	}

	@Override
	public void onUpdate()
	{
		if(message != null)
		{
			updateMS();
		if(hasTimePassedS(speed))
		{
			String name = players.get(i);
			if(!name.equals(mc.thePlayer.getName()))
				mc.thePlayer.sendChatMessage("/msg " + name + " " + message);
			updateLastMS();
			i++;
			if(i == players.size())
			{
				wurst.events.remove(UpdateListener.class, this);
				wurst.chat.message("MassMessage finished.");
			}
		}
		}
	}
	
}