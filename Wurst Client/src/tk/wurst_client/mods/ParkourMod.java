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
	description = "Allows you to make the perfect jump for parkour.",
	name = "Parkour")
public class ParkourMod extends Mod implements UpdateListener
{
	public final CheckboxSetting Boost = new CheckboxSetting(
		"Jump Further (NCP Incompatible)", false);   
	@Override
	public void initSettings()
	{
		settings.add(Boost);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if (!mc.thePlayer.isSneaking() && !wurst.mods.flightMod.isActive() &&
			!wurst.mods.glideMod.isActive()) {
			if (mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(
				mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(
					mc.thePlayer.motionX, -0.1f, mc.thePlayer.motionZ)).isEmpty()) 
			{
				if (Boost.isChecked()) 
				{
					double ox = mc.thePlayer.motionX*3;
					double oy = 0;
					double oz = mc.thePlayer.motionZ*3;
					mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX+ox, 
						mc.thePlayer.posY+oy, mc.thePlayer.posZ+oz);
				}
				mc.thePlayer.jump();
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
