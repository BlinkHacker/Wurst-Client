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
	description = "Changes the scale of the nametags so you can always\n"
		+ "read them, and allows you to see sneaking player's\n"
		+ "nametags clearly.",
	name = "NameTags")
public class NameTagsMod extends Mod
{	
	public final CheckboxSetting AlwaysSeeNameTags = new CheckboxSetting(
		"Always see NameTags", false);
	@Override
	public void initSettings()
	{
		settings.add(AlwaysSeeNameTags);
	}
	public boolean getCheckbox()
	{
		return AlwaysSeeNameTags.isChecked();
	}
}
