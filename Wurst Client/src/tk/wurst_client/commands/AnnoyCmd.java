/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.ChatInputEvent;
import tk.wurst_client.events.listeners.ChatInputListener;
import tk.wurst_client.utils.MiscUtils;
import java.util.Timer;
import java.util.TimerTask;

@Info(help = "Annoys a player by repeating everything he says.",
	name = "annoy",
	syntax = {"<player> [delay]"})
public class AnnoyCmd extends Cmd implements ChatInputListener
{
	private boolean toggled;
	private String name;
	private int delay;
	private int start;
	
	@Override
	public void execute(String[] args) throws Error
	{
		toggled = !toggled;
		if(toggled)
		{
			if(args.length == 1)
			{
				name = args[0];
				delay = 0;
				wurst.chat.message("Now annoying " + name + ".");
				if(name.equals(mc.thePlayer.getName()))
					wurst.chat.warning("Annoying yourself is a bad idea!");
				wurst.events.add(ChatInputListener.class, this);
			}else if(args.length == 2 && MiscUtils.isInteger(args[1]))
			{
				name = args[0];
				delay = Integer.parseInt(args[1]) * 1000;
				if(Integer.parseInt(args[1]) != 1)
				wurst.chat.message("Now annoying " + name + " with " + Integer.parseInt(args[1]) + " seconds delay.");
				else
				wurst.chat.message("Now annoying " + name + " with " + Integer.parseInt(args[1]) + " second delay.");
				if(name.equals(mc.thePlayer.getName()))
					wurst.chat.warning("Annoying yourself is a bad idea!");
				wurst.events.add(ChatInputListener.class, this);	
			}else
			{
				toggled = false;
				syntaxError();
			}
		}else
		{
			wurst.events.remove(ChatInputListener.class, this);
			if(name != null)
			{
				wurst.chat.message("No longer annoying " + name + ".");
				name = null;
			}
		}
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		String message = new String(event.getComponent().getUnformattedText());
		if(message.startsWith("§c[§6Wurst§c]§f "))
			return;
		if(message.startsWith("<" + name + ">") || message.contains(name + ">"))
			start = 1;
		else if(message.contains("] " + name + ":")
			|| message.contains("]" + name + ":") || message.contains(name + ":"))
			start = 2;
		else if(message.contains(name + " §f"))
			start = 3;				
		else if (message.contains(name + "»"))
			start = 4;
		else 
			start = 0;
		if(start != 0)
		{
			String repeatMessage;
			if(start == 1)
				repeatMessage = message.substring(message.indexOf(">") + 1);
			else if(start == 2)
				repeatMessage = message.substring(message.indexOf(":") + 1);
			else if(start == 3)
				repeatMessage = message.substring(message.indexOf("§f"));
			else if(start == 4)
				repeatMessage = message.substring(message.indexOf("»") + 1);
			else
				repeatMessage = "";
			if(repeatMessage.startsWith("/"))
				return;
			if(repeatMessage.startsWith("."))
				repeatMessage = ".say " + repeatMessage;
			String repeatMessage2 = repeatMessage;
			if (delay > 0)
			{
				Timer timer = new Timer();
				timer.schedule(new TimerTask() 
				{
					@Override
					public void run()
					{
						mc.thePlayer.sendChatMessage(repeatMessage2);
					}
				  
				}, delay);
			} else
				mc.thePlayer.sendChatMessage(repeatMessage2);
		}
	}
}