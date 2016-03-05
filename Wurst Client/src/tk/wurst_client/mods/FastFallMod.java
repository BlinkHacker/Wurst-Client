/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
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
	description = "Makes you fall very fast.",
	name = "FastFall")
public class FastFallMod extends Mod implements UpdateListener
{
	public final CheckboxSetting fastFallOnWater = new CheckboxSetting(
		"Fast fall on water", false);
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void initSettings()
	{
		settings.add(fastFallOnWater);
	}
	
	@Override
	public void onUpdate()
	{
		if(mc.thePlayer.isInWater() && !fastFallOnWater.isChecked())
		{
			return;
		}
		
		if(!mc.thePlayer.onGround && !mc.thePlayer.isOnLadder()
			&& !mc.thePlayer.capabilities.isFlying
			&& !wurst.mods.flightMod.isEnabled()
			&& !wurst.mods.safeWalkMod.isEnabled())
		{
			mc.thePlayer.motionY = -10;
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
