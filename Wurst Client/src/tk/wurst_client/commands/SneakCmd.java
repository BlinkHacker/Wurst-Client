/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.ChatOutputEvent;

@Info(help = "Toggles Sneak or changes its mode.",
	name = "sneak",
	syntax = {"[mode Packet|Real]", "[on|off|t]"})
public class SneakCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 2)
			syntaxError();
		if(args.length == 0) {
			if(wurst.mods.sneakMod.isEnabled())
				wurst.mods.sneakMod.setEnabled(false);
			else
			 wurst.mods.sneakMod.setEnabled(true);
				
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) 
			{
				wurst.mods.sneakMod.setEnabled(true);
			} else if(args[0].equalsIgnoreCase("off"))
			{
				wurst.mods.sneakMod.setEnabled(false);
			} else if (args[0].equalsIgnoreCase("t")) 
			{
				if(wurst.mods.sneakMod.isEnabled())
					wurst.mods.sneakMod.setEnabled(false);
				else
				 wurst.mods.sneakMod.setEnabled(true);
			}
		} else if(args.length == 2) {
			if (args[0].equalsIgnoreCase("mode")) 
			{
				wurst.mods.sneakMod.setMode(parseMode(args[1]));
				wurst.files.saveOptions();
				wurst.chat
					.message("Sneak mode set to \"" + args[1] + "\".");
			}
		} else
			syntaxError();
	}
	@Override
	public String getPrimaryAction()
	{
		return "Toggle Sneak";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".sneak", true));
	}
	private int parseMode(String input) throws SyntaxError
	{
		// search mode by name
		String[] modeNames = wurst.mods.sneakMod.getModes();
		for(int i = 0; i < modeNames.length; i++)
			if(input.equals(modeNames[i].toLowerCase()))
				return i;
		
		// syntax error if mode does not exist
		syntaxError("Invalid mode: " + input);
		return 0;
	}
}
