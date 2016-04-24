/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import tk.wurst_client.events.listeners.PostUpdateListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.MathUtils;

@Info(category = Category.MOVEMENT,
	description = "Gotta go fast!\n"
		+ "Note: This mod only semi bypasses NCP. It might be blocked\n"
		+ "in newer verisons of NoCheat+ or throws checks.",
	name = "SpeedHack")
public class SpeedHackMod extends Mod implements UpdateListener, PostUpdateListener
{
	private int mode = 0;
	private String[] modes = new String[]{"Wurst", "Bhop", "Rapid", "YPort"};
	private int lateststage = -1;
	private int ystage = 0;
	private double moveSpeed;
	private double lastDist;
	private boolean changedtimer = false;
	
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
		wurst.events.add(PostUpdateListener.class, this);
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
			if(mc.thePlayer.onGround) 
			{
	            mc.thePlayer.motionY = 0.06499999761581421D;
	            mc.thePlayer.motionX *= 1.5499999523162842D;
	            mc.thePlayer.motionZ *= 1.5499999523162842D;
	        }
			break;
		case 2:
			if ((!mc.gameSettings.keyBindForward.pressed) && (!mc.gameSettings.keyBindLeft.pressed) && 
				(!mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindBack.pressed) && (
				(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, 
					mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).
					size() > 0) || (mc.thePlayer.isCollidedVertically)))
				lateststage = 1;
			if (MathUtils.round(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == 
				MathUtils.round(0.138D, 3)) 
			{
				EntityPlayerSP thePlayer = mc.thePlayer;
				thePlayer.motionY -= 0.08D;
				mc.thePlayer.motionY -= 0.09316090325960147D;
				EntityPlayerSP thePlayer2 = mc.thePlayer;
				thePlayer2.posY -= 0.09316090325960147D;
			}
			if (lateststage == 1 && mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) 
			{
				lateststage = 2;
				mc.timer.timerSpeed = 1.0F;
				moveSpeed = (2.3D * getBaseMoveSpeed() - 0.01D);
				} else if (lateststage == 2) 
				{
					lateststage = 3;
					if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, 
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() < 1)
						return;
					mc.thePlayer.motionY = 0.4D;
					if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(
						mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ)).size() > 0)
						return;
					mc.timer.timerSpeed = 1.0F;
					mc.thePlayer.motionY = 0.0D;
					moveSpeed *= 2.149D;
				} else if (lateststage == 3) 
				{
					mc.timer.timerSpeed = 1.0F;
					lateststage = 4;
					double difference = 0.66D * (lastDist - getBaseMoveSpeed());
					moveSpeed = (lastDist - difference);
				} else 
				{
					mc.timer.timerSpeed = 1.0F;
					if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F)
						mc.timer.timerSpeed = 1.6F;
					if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(
						0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) || (mc.thePlayer.isCollidedVertically)) {
						lateststage = 1;
						}
					moveSpeed = (lastDist - lastDist / 159.0D);
					}
			moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = mc.thePlayer.rotationYaw;
			if (forward == 0.0F && strafe == 0.0F) 
			{
				mc.thePlayer.motionX = 0.0D;
				mc.thePlayer.motionZ = 0.0D;
			}else if (forward != 0.0F) 
			{
				if (strafe >= 1.0F) 
				{
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				}else if (strafe <= -1.0F)
				{
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F)
					forward = 1.0F;
				else if (forward < 0.0F)
				         forward = -1.0F;
				}
				     double mx = Math.cos(Math.toRadians(yaw + 90.0F));
				     double mz = Math.sin(Math.toRadians(yaw + 90.0F));
				     double motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
				     double motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;  
				     mc.thePlayer.motionX = motionX;
				     mc.thePlayer.motionZ = motionZ;
				     lastDist = getMotion();
				     if (changedtimer) 
				     {
				    	 mc.timer.timerSpeed = 1.0F;
				    	 changedtimer = false;
				    	 }
				     if (mc.timer.timerSpeed != 1.0F) 
				    	 changedtimer = true;
				     break;
		case 3:
			if(!BlockUtils.isOnSoulSand(mc.thePlayer) && !BlockUtils.isOnIce(mc.thePlayer) &&
				!BlockUtils.isOnSlimeBlock(mc.thePlayer))
			{
				if(ystage == 1)
				{
					mc.thePlayer.motionY = 0.3994F;
					mc.thePlayer.motionX *= 1.38F - 0.01F;
					mc.thePlayer.motionZ *= 1.38F - 0.01F; 
				} else if(ystage == 2) 
				{
					mc.thePlayer.motionY = 0.3994F;
					mc.timer.timerSpeed = 1.1115F;
					mc.thePlayer.motionX *= 1.649F;
					mc.thePlayer.motionZ *= 1.649F;
				} else if(ystage >= 3) 
				{
					ystage = 0;
					mc.thePlayer.motionY = 0.3994F;
					mc.thePlayer.motionX *= 1.55F;
					mc.thePlayer.motionZ *= 1.55F;
				} else
	                 mc.timer.timerSpeed = 1.0F;
				 ystage++;
			}
			break;
		default:
			return;
		}
	}
	
	@Override
	public void onPostUpdate()
	{
		if(mode == 3 && mc.thePlayer.fallDistance <= 3.994)
		{
			mc.thePlayer.jumpMovementFactor *= 1.3F;
			mc.thePlayer.motionY = -100F;
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
	
	public double getBaseMoveSpeed() 
	{
		double baseSpeed = 0.2873D;
		 if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 
		 {
			 int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			 baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		 }
		 return baseSpeed;
	}
	
	private double getMotion()
	{
		 double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
	     double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
	     return Math.sqrt(xDist * xDist + zDist * zDist);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(PostUpdateListener.class, this);
		wurst.events.remove(UpdateListener.class, this);
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