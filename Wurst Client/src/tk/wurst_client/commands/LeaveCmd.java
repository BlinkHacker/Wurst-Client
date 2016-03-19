/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.mods.AutoLeaveMod;

@Cmd.Info(help = "Leaves the current server or changes the mode of AutoLeave.",
	name = "leave",
	syntax = {"[chars|tp|selfhurt|quit]", "mode chars|tp|selfhurt|quit|taco"})
public class LeaveCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		AutoLeaveMod leave = wurst.mods.autoLeaveMod;
		if(args.length > 2)
			syntaxError();
		if(mc.isIntegratedServerRunning()
			&& mc.thePlayer.sendQueue.getPlayerInfo().size() == 1)
			error("Cannot leave server when in singleplayer.");
		switch(args.length)
		{
			case 0:
				disconnectWithMode(wurst.mods.autoLeaveMod.getMode());
				break;
			case 1:
				if(args[0].equalsIgnoreCase("taco"))
					for(int i = 0; i < 128; i++)
						mc.thePlayer.sendAutomaticChatMessage("Taco!");
				else if(args[0].equalsIgnoreCase("quit")) {
					disconnectWithMode(0);
				} else if(args[0].equalsIgnoreCase("chars")) {
					disconnectWithMode(1);
				} else if(args[0].equalsIgnoreCase("tp")) {
					disconnectWithMode(2);
				} else if(args[0].equalsIgnoreCase("selfhurt")) {
					disconnectWithMode(3);
				} else 
					syntaxError();
				break;
			case 2:
				// search mode by name
				String[] modeNames = leave.getModes();
				String newModeName = args[1];
				int newMode = -1;
				for(int i = 0; i < modeNames.length; i++)
					if(newModeName.equals(modeNames[i].toLowerCase()))
						newMode = i;
				
				// syntax error if mode does not exist
				if(newMode == -1)
					syntaxError("Invalid mode");
				
				if(newMode != leave.getMode())
				{
					leave.setMode(newMode);
				}
				
				wurst.chat.message("Leave mode set to \"" + args[1] + "\".");
				break;
			default:
				break;
		}
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Leave";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".leave", true));
	}
	
	private void disconnectWithMode(int mode)
	{
		switch(mode)
		{
			case 0:
				mc.theWorld.sendQuittingDisconnectingPacket();
				break;
			case 1:
				mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(
					"§"));
				break;
			case 2:
				mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						3.1e7d, 100, 3.1e7d, false));
			case 3:
				mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
					mc.thePlayer, Action.ATTACK));
				break;
			default:
				break;
		}
	}
	

}
