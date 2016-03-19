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
import tk.wurst_client.navigator.settings.CheckboxSetting;
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
	public final CheckboxSetting fastrunliquid = new CheckboxSetting(
		"FastRun On Liquids (only works when flying)", false);
	
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
		settings.add(fastrunliquid);
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
			boolean isTravelPossible0 = false;
		    boolean isTravelPossible1 = false;
		    boolean isTravelPossible2 = false;
		    boolean isTravelPossible3 = false;
		    for(int i = 0; i < speed; i++){
				double i2 = i;
				if(direction == 0){
					//TODO: Better way to do this
					if(getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.air || 
					getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.plants ||
					getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.vine ||
					getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.fire ||
					getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.carpet ||
					getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.portal ||
					getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.web)
					{
						isTravelPossible0=true;
					} else if(isTravelPossible0=false) {
					if(fastrunliquid.isChecked()) {

						if(getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.water ||
							getBlock(currentX, currentY, currentZ+i2).getMaterial() == Material.lava)
						isTravelPossible0=true;
						
					}
					}
					if(isTravelPossible0)
					mc.thePlayer.setPosition(currentX, currentY, currentZ+i2/10);
				}else if(direction == 1){
					if(getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.air || 
						getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.plants ||
						getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.vine ||
						getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.fire ||
						getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.carpet ||
						getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.portal ||
						getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.web)
					{
						isTravelPossible1=true;
					} else if(isTravelPossible1=false) {
						if(fastrunliquid.isChecked()) {
							
							if(getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.water ||
								getBlock(currentX-i2, currentY, currentZ).getMaterial() == Material.lava)
							
								isTravelPossible1=true;
							
						}
					
					}
				    if(isTravelPossible1)
					mc.thePlayer.setPosition(currentX-i2/10, currentY, currentZ);
				}else if(direction == 2){
					if(getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.air || 
						getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.plants ||
						getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.vine ||
						getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.fire ||
						getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.carpet ||
						getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.portal ||
						getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.web)
					{
						isTravelPossible2=true;
					} else if(isTravelPossible2=false) {
						if(fastrunliquid.isChecked()) {
						
							if(getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.water ||
							getBlock(currentX, currentY, currentZ-i2).getMaterial() == Material.lava)
							
								isTravelPossible2=true;
							
						}
					}

				    if(isTravelPossible2)
					mc.thePlayer.setPosition(currentX, currentY, currentZ-i2/10);
				}else if(direction == 3){
					if(getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.air || 
						getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.plants ||
						getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.vine ||
						getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.fire ||
						getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.carpet ||
						getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.portal ||
						getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.web)
					{
						isTravelPossible3=true;
					} else if(isTravelPossible3=false) {
						if(fastrunliquid.isChecked()) {
							
							if(getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.water ||
								getBlock(currentX+i2, currentY, currentZ).getMaterial() == Material.lava)
								
								    isTravelPossible3=true;
								
						}
					}
					if(isTravelPossible3)	
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
		boolean[] isPassable = new boolean[9];
		for(int i2 = 0; i2 < 9; i2++){
			isPassable[i2] = false;
		}
		isPassable[8] = false;
		int onThis = 0;
		for(int i = 0; i < 9; i++){
			if(onThis == 0){
				if(getBlock(pX+1, pY, pZ).getMaterial() == Material.air) isPassable[0] = true;
					if(getBlock(pX+1, pY, pZ).getMaterial() == Material.plants) isPassable[0] = true;
					if(getBlock(pX+1, pY, pZ).getMaterial() == Material.vine) isPassable[0] = true;
					if(getBlock(pX+1, pY, pZ).getMaterial() == Material.fire) isPassable[0] = true;
					if(getBlock(pX+1, pY, pZ).getMaterial() == Material.carpet) isPassable[0] = true;
					if(getBlock(pX+1, pY, pZ).getMaterial() == Material.portal) isPassable[0] = true;
					if(getBlock(pX+1, pY, pZ).getMaterial() == Material.web) isPassable[0] = true;
				
			}else if(onThis == 1){
				if(getBlock(pX-1, pY, pZ).getMaterial() == Material.air) isPassable[1] = true;
					if(getBlock(pX-1, pY, pZ).getMaterial() == Material.plants) isPassable[1] = true;
					if(getBlock(pX-1, pY, pZ).getMaterial() == Material.vine) isPassable[1] = true;
					if(getBlock(pX-1, pY, pZ).getMaterial() == Material.fire) isPassable[1] = true;
					if(getBlock(pX-1, pY, pZ).getMaterial() == Material.carpet) isPassable[1] = true;
					if(getBlock(pX-1, pY, pZ).getMaterial() == Material.portal) isPassable[1] = true;
					if(getBlock(pX-1, pY, pZ).getMaterial() == Material.web) isPassable[1] = true;
				
			}else if(onThis == 2){
				if(getBlock(pX, pY, pZ+1).getMaterial() == Material.air) isPassable[2] = true;
					if(getBlock(pX, pY, pZ+1).getMaterial() == Material.plants) isPassable[2] = true;
					if(getBlock(pX, pY, pZ+1).getMaterial() == Material.vine) isPassable[2] = true;
					if(getBlock(pX, pY, pZ+1).getMaterial() == Material.fire) isPassable[2] = true;
					if(getBlock(pX, pY, pZ+1).getMaterial() == Material.carpet) isPassable[2] = true;
					if(getBlock(pX, pY, pZ+1).getMaterial() == Material.portal) isPassable[2] = true;
					if(getBlock(pX, pY, pZ+1).getMaterial() == Material.web) isPassable[2] = true;
			}else if(onThis == 3){
				if(getBlock(pX, pY, pZ-1).getMaterial() == Material.air) isPassable[3] = true;
					if(getBlock(pX, pY, pZ-1).getMaterial() == Material.plants) isPassable[3] = true;
					if(getBlock(pX, pY, pZ-1).getMaterial() == Material.vine) isPassable[3] = true;
					if(getBlock(pX, pY, pZ-1).getMaterial() == Material.fire) isPassable[3] = true;
					if(getBlock(pX, pY, pZ-1).getMaterial() == Material.carpet) isPassable[3] = true;
					if(getBlock(pX, pY, pZ-1).getMaterial() == Material.portal) isPassable[3] = true;
					if(getBlock(pX, pY, pZ-1).getMaterial() == Material.web) isPassable[3] = true;
			}else if(onThis == 4){
				if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.air) isPassable[4] = true;
					if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.plants) isPassable[4] = true;
					if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.vine) isPassable[4] = true;
					if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.fire) isPassable[4] = true;
					if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.carpet) isPassable[4] = true;
					if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.portal) isPassable[4] = true;
					if(getBlock(pX+1, pY, pZ+1).getMaterial() == Material.web) isPassable[4] = true;
			}else if(onThis == 5){
				if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.air) isPassable[5] = true;
					if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.plants) isPassable[5] = true;
					if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.vine) isPassable[5] = true;
					if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.fire) isPassable[5] = true;
					if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.carpet) isPassable[5] = true;
					if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.portal) isPassable[5] = true;
					if(getBlock(pX-1, pY, pZ-1).getMaterial() == Material.web) isPassable[5] = true;
			}else if(onThis == 6){
				if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.air) isPassable[6] = true;
					if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.plants) isPassable[6] = true;
					if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.vine) isPassable[6] = true;
					if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.fire) isPassable[6] = true;
					if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.carpet) isPassable[6] = true;
					if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.portal) isPassable[6] = true;
					if(getBlock(pX+1, pY, pZ-1).getMaterial() == Material.web) isPassable[6] = true;
			}else if(onThis == 7){
				if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.air) isPassable[7] = true;
					if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.plants) isPassable[7] = true;
					if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.vine) isPassable[7] = true;
					if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.fire) isPassable[7] = true;
					if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.carpet) isPassable[7] = true;
					if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.portal) isPassable[7] = true;
					if(getBlock(pX-1, pY, pZ+1).getMaterial() == Material.web) isPassable[7] = true;
			}else if(onThis == 8){
				if(getBlock(pX, pY, pZ).getMaterial() == Material.air) isPassable[8] = true;
					if(getBlock(pX, pY, pZ).getMaterial() == Material.plants) isPassable[8] = true;
					if(getBlock(pX, pY, pZ).getMaterial() == Material.vine) isPassable[8] = true;
					if(getBlock(pX, pY, pZ).getMaterial() == Material.fire) isPassable[8] = true;
					if(getBlock(pX, pY, pZ).getMaterial() == Material.carpet) isPassable[8] = true;
					if(getBlock(pX, pY, pZ).getMaterial() == Material.portal) isPassable[8] = true;
					if(getBlock(pX, pY, pZ).getMaterial() == Material.web) isPassable[8] = true;
			}
			onThis++;
		}
		if(isPassable[0] == true && isPassable[1] == true && isPassable[2] == true && isPassable[3] == true && isPassable[4] == true && isPassable[5] == true && isPassable[6] == true && isPassable[7] == true && isPassable[8] == true) return true;
		return false;
	}
	
	private Block getBlock(double x, double y, double z){
		return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
	
	@Override
	public void onDisable(){
		wurst.events.remove(UpdateListener.class, this);
	}
	public boolean getCheckbox()
	{
		return fastrunliquid.isChecked();
	}

}