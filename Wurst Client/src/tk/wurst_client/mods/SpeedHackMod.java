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
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.MOVEMENT,
	description = "Gotta go fast!\n"
		+ "Note: This mod only semi bypasses NCP. It might be blocked\n"
		+ "in newer verisons of NoCheat+ or throws checks.",
	name = "SpeedHack")
public class SpeedHackMod extends Mod implements UpdateListener
{
	private int mode = 0;
	private String[] modes = new String[]{"Wurst", "Old", "Bhop", "Gomme"};
	private int speedupstage = 0;
	private int gommestage = 0;
	
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
	}
	
	@Override
	public void onUpdate()
	{
		if(canSpeed())
		switch(mode)
		{
		case 0:
		// return if sneaking or not walking
		if(mc.thePlayer.isSneaking() || mc.thePlayer.moveForward == 0
			&& mc.thePlayer.moveStrafing == 0)
			return;
		
		// activate sprint if walking forward
		if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isCollidedHorizontally)
			mc.thePlayer.setSprinting(true);
		
		// activate mini jump if on ground
		if(mc.thePlayer.onGround)
		{
			mc.thePlayer.motionY += 0.1;
			mc.thePlayer.motionX *= 1.8;
			mc.thePlayer.motionZ *= 1.8;
			double currentSpeed =
				Math.sqrt(Math.pow(mc.thePlayer.motionX, 2)
					+ Math.pow(mc.thePlayer.motionZ, 2));
			
			// limit speed to highest value that works on NoCheat+ version
			// 3.13.0-BETA-sMD5NET-b878
			// UPDATE: Patched in NoCheat+ version 3.13.2-SNAPSHOT-sMD5NET-b888
			double maxSpeed = 0.66F;
			if(currentSpeed > maxSpeed)
			{
				mc.thePlayer.motionX =
					mc.thePlayer.motionX / currentSpeed * maxSpeed;
				mc.thePlayer.motionZ =
					mc.thePlayer.motionZ / currentSpeed * maxSpeed;
			}
		}
			break;
		case 1:
			 if(speedupstage == 1) {
                 mc.thePlayer.motionX *= 1.94D;
                 mc.thePlayer.motionZ *= 1.94D;
              } else if(speedupstage == 2) {
                 mc.thePlayer.motionX /= 1.9D;
                 mc.thePlayer.motionZ /= 1.9D;
              } else if(speedupstage == 3) {
                 mc.thePlayer.motionX *= 1.2000000476837158D;
                 mc.thePlayer.motionZ *= 1.2000000476837158D;
              } else if(speedupstage == 4) {
                 mc.thePlayer.motionX /= 1.9D;
                 mc.thePlayer.motionZ /= 1.9D;
              } else if(speedupstage >= 5) {
                 mc.thePlayer.motionX *= 1.94D;
                 mc.thePlayer.motionZ *= 1.94D;
                 speedupstage = 0;
              } else
                 mc.timer.timerSpeed = 1.0F;
			 speedupstage++;
			 break;
		case 2:
			if(mc.thePlayer.onGround) {
	            mc.thePlayer.motionY = 0.06499999761581421D;
	            mc.thePlayer.motionX *= 1.5499999523162842D;
	            mc.thePlayer.motionZ *= 1.5499999523162842D;
	         }
			break;
		case 3:
			if(gommestage == 1) {
				mc.thePlayer.motionY = 0.07999999821186066D;
	            mc.thePlayer.motionX *= 2.299999952316284D;
	            mc.thePlayer.motionZ *= 2.299999952316284D;
			} else if(gommestage >= 2) {
	            mc.thePlayer.motionX /= 1.4500000476837158D;
	            mc.thePlayer.motionZ /= 1.4500000476837158D;
	            gommestage = 0;
	        }

	            gommestage++;
			break;
		default:
			return;
		}
	}
	
	public boolean canSpeed()
	{
	      boolean moving = mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
	      return !mc.thePlayer.isInWater() && !BlockUtils.isInLiquid(mc.thePlayer) &&
	    	  !BlockUtils.isOnLiquid(mc.thePlayer) && !mc.thePlayer.isCollidedHorizontally && 
	    	  !BlockUtils.isOnLadder(mc.thePlayer) && 
	    	  !mc.thePlayer.isSneaking() && mc.thePlayer.onGround && moving;
	}
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
	
	public int getMode()
	{
		return mode;
	}
	
	public void setMode(int mode)
	{
		((ModeSetting)settings.get(1)).setSelected(mode);
	}
	
	public String[] getModes()
	{
		return modes;
	}
}
