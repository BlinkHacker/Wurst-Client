/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.util.Vec3;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Leaves a trail of breadcrumbs behind you.\n"
		+  "Type .breadcrumbs clear to remove breadcrumbs.",
	name = "Breadcrumbs")
public class BreadcrumbsMod extends Mod implements RenderListener, UpdateListener
{	
	private List<double[]> points = new CopyOnWriteArrayList<double[]>();
	public CheckboxSetting opacity = new CheckboxSetting(
		"See Lines Through Walls", false);
	
	@Override
	public void initSettings()
	{
		settings.add(opacity);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!wurst.mods.freecamMod.isActive())
		{
		if (points.size() > 0) 
		{
		double xd = Math.abs(points.get(points.size()-1)[0]-mc.thePlayer.posX);
		double yd = Math.abs(points.get(points.size()-1)[1]-mc.thePlayer.posY);
		double zd = Math.abs(points.get(points.size()-1)[2]-mc.thePlayer.posZ);
		if (xd > 0.25d || yd > 0.25d || zd > 0.25d) {
			points.add(new double[] {mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ});
		}
		} else 
			points.add(new double[] {mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ});
		}
		if(wurst.commands.breadcrumbsCmd.clearcrumbs)
		{
			points.clear();
		wurst.commands.breadcrumbsCmd.clearcrumbs = false;
		}
	}
	@Override
	public void onRender()
	{
		for(int i = 1; i < points.size(); i++) 
		{
			double[] f = points.get(i-1);
			double[] t = points.get(i);
			Vec3 from = new Vec3(f[0], f[1], f[2]);
			Vec3 to = new Vec3(t[0], t[1], t[2]);
			RenderUtils.line(from, to, new Color(0, 0, 1, 0.75F), 2.0F, opacity.isChecked());
		}
	}

	@Override
	public void onDisable()
	{
		points.clear();
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(RenderListener.class, this);
	}
}
