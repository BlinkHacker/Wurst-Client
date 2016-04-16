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
	description = "Cancels slowness effects caused by water, soul sand and\n"
		+ "using items. Water and soul sand slowdown cannot bypass NCP.",
	name = "NoSlowdown")
public class NoSlowdownMod extends Mod implements UpdateListener
{
	public CheckboxSetting items = new CheckboxSetting(
		"No Item Slowdown", true);
	public CheckboxSetting blocks = new CheckboxSetting(
		"No Block Slowdown", true);
	
	@Override
	public void initSettings()
	{
		settings.add(blocks);
		settings.add(items);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(mc.thePlayer.onGround && mc.thePlayer.isInWater()
			&& mc.gameSettings.keyBindJump.pressed && blocks.isChecked())
			mc.thePlayer.jump();
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
