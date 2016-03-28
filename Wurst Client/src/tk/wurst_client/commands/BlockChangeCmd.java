/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.events.ChatOutputEvent;

@Cmd.Info(help = "Removes all boxes from BlockChangeESP.",
	name = "blockchange",
	syntax = {})
public class BlockChangeCmd extends Cmd
{
	public boolean clearblocks;
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
			clearblocks = true;
		else
			syntaxError();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Clear BlockChangeESP Boxes";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".blockchange", true));
	}
}
