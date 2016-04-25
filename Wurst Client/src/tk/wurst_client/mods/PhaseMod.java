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
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.EXPLOITS,
	description = "Exploits a bug in NoCheat+ that allows you to glitch\n"
		+ "through blocks.",
	name = "Phase")
public class PhaseMod extends Mod implements UpdateListener, BlockBBListener
{
	private int mode = 0;
	private String[] modes = new String[]{"Wurst", "InstaDoors"};
	private int resetNext;
	public boolean shouldmove = true;
	
	@Override
	public void initSettings()
	{
		settings.add(new ModeSetting("Mode", modes, mode)
		{
			@Override
			public void update()
			{
				mode = getSelected();
			}
		});
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(BlockBBListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		switch(mode)
		{
			case 0:
				mc.thePlayer.noClip = true;
				mc.thePlayer.fallDistance = 0;
				mc.thePlayer.onGround = true;
				break;
			case 1:
				resetNext -= 1;
			    double xOff = 0.0D;
			    double zOff = 0.0D;
			    double multiplier = 2.6D;
			    double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			    double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
		        xOff = mc.thePlayer.movementInput.moveForward * multiplier * mx + 
		        	mc.thePlayer.movementInput.moveStrafe * multiplier * mz;	
		        zOff = mc.thePlayer.movementInput.moveForward * multiplier * mz - 
		        	mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
			    if (BlockUtils.isInsideBlock(mc.thePlayer) && mc.thePlayer.isSneaking()) 
			      resetNext = 1;
			    if (resetNext > 0)
			      mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0.0D, zOff);
			    break;
			default:
				return;
		}
	}
	
	@Override
	public void onBlockBB(BlockBBEvent event)
	{
		if(mc.thePlayer == null)
			return;
		if(mode == 1)
		if(!BlockUtils.isInsideBlock(mc.thePlayer) && event.getBoundingBox() != null &&
			event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && mc.thePlayer.isSneaking())
			event.setBoundingBox(null);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(BlockBBListener.class, this);
		mc.thePlayer.noClip = false;
	}
	
	public int getMode()
	{
		return mode;
	}
	
	public void setMode(int mode)
	{
		((ModeSetting)settings.get(0)).setSelected(mode);
	}
	
	public String[] getModes()
	{
		return modes;
	}
}
