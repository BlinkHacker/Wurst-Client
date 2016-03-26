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
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.RENDER,
	description = "Allows you to see better when under liquids.",
	name = "LiquidVision")
public class LiquidVisionMod extends Mod
{	
	public final CheckboxSetting ClearLava = new CheckboxSetting(
		"Better Lava Vision", false);
	@Override
	public void initSettings()
	{
		settings.add(ClearLava);
	}
}
