/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.RENDER,
	description = "Prevents rain and snow client-side. This will block PlayerFinder.",
	name = "NoWeather")
public class NoWeatherMod extends Mod
{	
	@Override
	public void onEnable()
	{
		if(wurst.mods.playerFinderMod.isEnabled())
			wurst.mods.playerFinderMod.setEnabled(false);
	}
}