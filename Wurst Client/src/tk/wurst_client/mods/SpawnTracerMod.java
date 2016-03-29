/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.awt.Color;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Draws a tracer back to the spawn."
		+ "Note: The tracer might not be exact.",
	name = "SpawnTracer")
public class SpawnTracerMod extends Mod implements RenderListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		BlockPos spawn = mc.theWorld.getSpawnPoint();
	    double d1 = spawn.getX() - mc.getRenderManager().renderPosX;
	    double d2 = spawn.getY() - mc.getRenderManager().renderPosY;
	    double d3 = spawn.getZ() - mc.getRenderManager().renderPosZ;
	    RenderUtils.tracerLine(spawn.getX(), spawn.getY(), spawn.getZ(), new Color(0.1F, 0.1F, 0.1F, 0.5F));
	    RenderUtils.box(spawn.getX(), spawn.getY(), spawn.getZ(), 
	    	spawn.getX() + 1.0D, spawn.getY() + 1.0D, spawn.getZ() + 1.0D, new Color(0.1F, 0.1F, 0.1F, 0.15F));

	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
	}
}