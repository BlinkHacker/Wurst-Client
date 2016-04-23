/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.init.Blocks;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.MOVEMENT,
	description = "Moves faster on ice.",
	name = "TerrainSpeed")
public class TerrainSpeedMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(BlockUtils.isOnIce(mc.thePlayer))
		{
			Blocks.ice.slipperiness = 0.6F;
			Blocks.packed_ice.slipperiness = 0.6F;
	        mc.thePlayer.motionX *= 2.5F;
	        mc.thePlayer.motionZ *= 2.5F;
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		Blocks.ice.slipperiness = 0.98F;
		Blocks.packed_ice.slipperiness = 0.98F;
	}
}
