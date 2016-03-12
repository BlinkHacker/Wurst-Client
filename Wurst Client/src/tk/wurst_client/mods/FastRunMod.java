/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.SliderSetting;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;

@Info(category = Category.MOVEMENT,
description = "Moves extremely fast while running straight and on a flat area. Only works on\n" +
 "vanilla servers. Best used with NoFall enabled aswell.",
name = "FastRun",
tags = "speedhack,speed hack",
noCheatCompatible = false)
public class FastRunMod extends Mod implements UpdateListener
{	
	private double currentX;
	private double currentY;
	private double currentZ;
	
	private int defaultSpeed = 1;
	private int speed = 1;
	
	@Override
	public String getRenderName()
	{	
		String currentString = getName();
		currentString = currentString + "[Is Flat: ";
		if(isFlatArea()){
			currentString = currentString + "True]";
		}else{
			currentString = currentString + "False]";
		}
		return currentString;
	}
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Run Speed", defaultSpeed, 1, 10, 1,
			ValueDisplay.INTEGER)
		{
			@Override
			public void update()
			{
				speed = (int)getValue()*10;
			}
		});
	}
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.speedHackMod};
	}
	
	@Override
	public void onEnable(){
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate(){
		if(!isFlatArea()) return;
		int direction = MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if(mc.thePlayer.posX != mc.thePlayer.prevPosX
			|| mc.thePlayer.posZ != Minecraft.getMinecraft().thePlayer.prevPosZ
			|| mc.thePlayer.posY != Minecraft.getMinecraft().thePlayer.prevPosY){
			currentX = mc.thePlayer.posX;
			currentY = mc.thePlayer.posY;
			currentZ = mc.thePlayer.posZ;
			for(int i = 0; i < speed; i++){
				double i2 = i;
				if(direction == 0){
					if(getBlock(currentX, currentY, currentZ+i2).getMaterial() != Material.air) return;
					mc.thePlayer.setPosition(currentX, currentY, currentZ+i2/10);
				}else if(direction == 1){
					if(getBlock(currentX-i2, currentY, currentZ).getMaterial() != Material.air) return;
					mc.thePlayer.setPosition(currentX-i2/10, currentY, currentZ);
				}else if(direction == 2){
					if(getBlock(currentX, currentY, currentZ-i2).getMaterial() != Material.air) return;
					mc.thePlayer.setPosition(currentX, currentY, currentZ-i2/10);
				}else if(direction == 3){
					if(getBlock(currentX+i2, currentY, currentZ).getMaterial() != Material.air) return;
					mc.thePlayer.setPosition(currentX+i2/10, currentY, currentZ);
				}else{
					return;
				}
			}
		}
	}
	
	//Check if the 3x3 cube 'around' the player is flat. If it is, return true.
	private boolean isFlatArea(){
		double pX = mc.thePlayer.posX;
		double pY = mc.thePlayer.posY;
		double pZ = mc.thePlayer.posZ;
		boolean[] isAir = new boolean[9];
		for(int i2 = 0; i2 < 9; i2++){
			isAir[i2] = false;
		}
		isAir[8] = false;
		int onThis = 0;
		for(int i = 0; i < 9; i++){
			if(onThis == 0){
				if(getBlock(pX+1, pY, pZ).getMaterial() == Material.air) isAir[0] = true;
			}else if(onThis == 1){
				if(getBlock(pX-1, pY, pZ).getMaterial() == Material.air) isAir[1] = true;
			}else if(onThis == 2){
				if(getBlock(pX, pY, pZ+1).getMaterial() == Material.air) isAir[2] = true;
			}else if(onThis == 3){
				if(getBlock(pX, pY, pZ-1).getMaterial() == Material.air) isAir[3] = true;
			}else if(onThis == 4){
				if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.air) isAir[4] = true;
			}else if(onThis == 5){
				if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.air) isAir[5] = true;
			}else if(onThis == 6){
				if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.air) isAir[6] = true;
			}else if(onThis == 7){
				if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.air) isAir[7] = true;
			}else if(onThis == 8){
				if(getBlock(pX, pY, pZ).getMaterial() == Material.air) isAir[8] = true;
			}
			onThis++;
		}
		if(isAir[0] == true && isAir[1] == true && isAir[2] == true && isAir[3] == true && isAir[4] == true && isAir[5] == true && isAir[6] == true && isAir[7] == true && isAir[8] == true) return true;
		return false;
	}
	
	private Block getBlock(double x, double y, double z){
		return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
	
	@Override
	public void onDisable(){
		wurst.events.remove(UpdateListener.class, this);
	}

}