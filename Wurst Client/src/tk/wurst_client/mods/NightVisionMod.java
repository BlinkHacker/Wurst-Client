/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.RENDER,
	description = "Renders the Night Vision effect. \n"
	+ "Note that only you can see this effect, and to \n"
	+ "others it will seem as you have no effect.",
	name = "NightVision")
public class NightVisionMod extends Mod implements UpdateListener
{	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		mc.thePlayer.addPotionEffect((new PotionEffect(Potion.nightVision.getId(), 100000000, 2)));
	}
	
	@Override
	public void onDisable()
	{
		mc.thePlayer.removePotionEffect(16);
		wurst.events.remove(UpdateListener.class, this);
	}
}
