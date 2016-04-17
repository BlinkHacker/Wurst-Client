/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.SliderSetting;

@Info(category = Category.MOVEMENT,
	description = "Allows you to step up full blocks.\n"
		+ "Bypasses NoCheat if YesCheat+ is enabled.",
	name = "Step")
public class StepMod extends Mod implements UpdateListener
{
	public float height = 1F;
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Height", height, 1, 100, 1,
			ValueDisplay.INTEGER)
		{
			@Override
			public void update()
			{
				height = (float)getValue();
			}
		});
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!wurst.mods.yesCheatMod.isActive())
			mc.thePlayer.stepHeight = isEnabled() ? height : 0.5F;
		else if(wurst.mods.yesCheatMod.isActive())
			mc.thePlayer.stepHeight = 1;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		mc.thePlayer.stepHeight = 0.5F;
	}
}
