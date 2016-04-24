/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.events.BlockBBEvent;
import tk.wurst_client.events.listeners.BlockBBListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
	description = "Phase through blocks.\n"
		+ "Tip: You can climb blocks with this.",
	name = "VPhase",
	tags = "CheckerClimb")
public class VPhaseMod extends Mod implements BlockBBListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(BlockBBListener.class, this);
	}
	
	@Override
	public void onBlockBB(BlockBBEvent event)
	{
		 mc.thePlayer.boundingBox.offsetAndUpdate(0, 0, 0);
		 if (event.getBoundingBox() != null && event.getBoundingBox().maxY > 
		 mc.thePlayer.boundingBox.minY)
		      event.setBoundingBox(null);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(BlockBBListener.class, this);
	}
}
