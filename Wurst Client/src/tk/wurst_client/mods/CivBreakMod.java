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
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.ModeSetting;

@Info(category = Category.BLOCKS,
	description = "Allows you to mine the nexus faster in Annihilation.\n"
	+ "If you get kicked, use normal civbreak.\n"
	+ "Tip: Instant FastBreak is another form of CivBreak.",
	name = "CivBreak")
public class CivBreakMod extends Mod
{
	private int mode = 0;
	private String[] modes = new String[]{"Normal", "Insane"};
	@Override
	public void initSettings()
	{
		settings.add(new ModeSetting("Mode", modes, mode)
		{
			@Override
			public void update()
			{
				mode = getSelected();
			}
		});
	}
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.fastBreakMod};
	}
	@Override
	public void onEnable()
	{	
		if(wurst.mods.fastBreakMod.isEnabled())
			wurst.mods.fastBreakMod.setEnabled(false);
	}
	public int getMode()
	{
		return mode;
	}
	
	public void setMode(int mode)
	{
		((ModeSetting)settings.get(1)).setSelected(mode);
	}
	
	public String[] getModes()
	{
		return modes;
	}
}