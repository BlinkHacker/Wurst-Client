/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;

@Cmd.Info(help = "Allows you to get the plugins of the server.",
	name = "plugins",
	syntax = {})
public class PluginsCmd extends Cmd implements PacketInputListener
{
	public boolean clearblocks;
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
		{
			mc.getNetHandler().addToSendQueue(new C14PacketTabComplete("/"));
			wurst.events.add(PacketInputListener.class, this);
		}
		else
			syntaxError();
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		if (event.getPacket() instanceof S3APacketTabComplete) {
            S3APacketTabComplete packet = (S3APacketTabComplete) event.getPacket();
            event.cancel();
            List<String> plugins = new ArrayList<>();
            for (String cmd : packet.func_149630_c()) {
                String[] arguments = cmd.split(":");
                if (arguments.length > 1 && !arguments[0].substring(1).equals("") && 
                	!plugins.contains(arguments[0].substring(1)))
                    plugins.add(arguments[0].substring(1));
            }
            plugins = plugins.stream()
                .filter(plugin -> !plugin.equalsIgnoreCase("minecraft"))
                .filter(plugin -> !plugin.equalsIgnoreCase("bukkit")).collect(Collectors.toList());
            StringBuilder builder = new StringBuilder("Plugins (" + plugins.size() + "): ");
            plugins.forEach(plugin -> builder.append(plugin).append(", "));
            if (plugins.size() > 0 && !builder.toString().equals(""))
                wurst.chat.message(builder.toString().substring(0, builder.toString().length() - 2)); 
            else
                wurst.chat.message("Cannot find any plugins!");
            wurst.events.remove(PacketInputListener.class, this);
		}
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Get Plugins";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".plugins", true));
	}
}