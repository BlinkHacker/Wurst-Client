/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.MOVEMENT,
	description = "Makes you sprint whenever you walk.",
	name = "AutoSprint")
public class AutoSprintMod extends Mod implements UpdateListener
{
	public final CheckboxSetting OmnidirectionalSprint = new CheckboxSetting(
		"Sprint all directions", false);
	@Override
	public void initSettings()
	{
		settings.add(OmnidirectionalSprint);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(OmnidirectionalSprint.isChecked())
		{
			if(!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking())
				if(mc.thePlayer.motionX != 0 || mc.thePlayer.motionZ != 0)
					mc.thePlayer.setSprinting(true);
		} else {
		if(!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0
			&& !mc.thePlayer.isSneaking())
			mc.thePlayer.setSprinting(true);
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
