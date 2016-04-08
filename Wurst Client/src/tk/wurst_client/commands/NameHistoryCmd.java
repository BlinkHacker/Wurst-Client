/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Shows a player's name history using their name or UUID.",
	name = "namehistory",
	syntax = {"[<name|UUID>]"})
public class NameHistoryCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 1)
		{
			String uuid = "";
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    uuid = line.split("\"")[3];
                }

                br.close();

                url = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");
                br = new BufferedReader(new InputStreamReader(url.openStream()));

                String originalName;
                String oldNames = "";

                while ((line = br.readLine()) != null) {
                    originalName = line.split("\"")[3];

                    for (int i = 0; i < line.split("\"").length; i++) {
                        if ((i != line.split("\"").length - 1) && (line.split("\"")[(i + 1)].equals(","))) {
                            if (oldNames.equals("")) {
                                oldNames = line.split("\"")[i];
                            } else {
                                oldNames = oldNames + ", " + line.split("\"")[i];
                            }
                        }
                    }

                    if (oldNames.equals(""))
                        wurst.chat.message(args[0] + " hasn't changed their name.");
                    else
                        wurst.chat.message(args[0] + "'s name history: " + originalName + ", " + oldNames + ".");
                }
            } catch (IOException e)
            {
                wurst.chat.error("Unable to retrieve UUID of the player.");
            }
		} else
			syntaxError();
	}
}
