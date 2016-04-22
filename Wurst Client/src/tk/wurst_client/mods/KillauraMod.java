/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;

import java.util.ArrayList;
import java.util.List;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import tk.wurst_client.events.listeners.PostUpdateListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.navigator.settings.SliderSetting;
import tk.wurst_client.utils.EntityUtils;


@Info(category = Category.COMBAT,
	description = "Automatically attacks everything in your range.",
	name = "Killaura",
	tags = "SwitchAura")
public class KillauraMod extends Mod implements UpdateListener, PostUpdateListener
{
	public float normalSpeed = 20F;
	public float normalRange = 5F;
	public float yesCheatSpeed = 12F;
	public float yesCheatRange = 4.25F;
	public float secondsExisted = 0F; 
	public int fov = 360;
	public float realSpeed;
	public float realRange;
	public float rSpeed;
	public float yesCheatrSpeed;
	private EntityLivingBase target;
	private List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
	public final CheckboxSetting randomspeed = new CheckboxSetting(
		"Speed Randomizer", false);
	public final CheckboxSetting mobinfront = new CheckboxSetting(
		"MobInFront Bypass (For other auras)", false);
	public final CheckboxSetting checkarmor = new CheckboxSetting(
		"Ignore ArmorLess Players", false);
	public final CheckboxSetting friends = new CheckboxSetting(
		"Attack Friends", false);
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Speed", normalSpeed, 2, 20, 0.1,
			ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				normalSpeed = (float)getValue();
				yesCheatSpeed = Math.min(normalSpeed, 12F);
				rSpeed = (float)(Math.round((Math.random() * ((normalSpeed + 1.5F) - 
					(normalSpeed - 1.5F)) + (normalSpeed - 1.5F))* 10))/10;
				yesCheatrSpeed = Math.min(rSpeed, 12.5F);
				updateSpeedAndRange();
			}
		});
		settings.add(randomspeed);
		settings.add(new SliderSetting("Range", normalRange, 1, 6, 0.05,
			ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				normalRange = (float)getValue();
				yesCheatRange = Math.min(normalRange, 4.25F);
				updateSpeedAndRange();
			}
		});
		settings.add(new SliderSetting("FOV", fov, 30, 360, 10,
			ValueDisplay.DEGREES)
		{
			@Override
			public void update()
			{
				fov = (int)getValue();
			}
		});
		settings.add(new SliderSetting("Minimum Seconds Existed", secondsExisted, 0, 4, 0.05,
			ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				secondsExisted = (float)getValue();
			}
		});
		settings.add(mobinfront);
		settings.add(checkarmor);
		settings.add(friends);
	}
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraLegitMod, wurst.mods.multiAuraMod,
			wurst.mods.clickAuraMod, wurst.mods.triggerBotMod,
			wurst.mods.criticalsMod, wurst.mods.aimbotMod,
			wurst.mods.clickAimbotMod,wurst.mods.tpAuraMod};
	}
	
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(wurst.mods.killauraLegitMod.isEnabled())
			wurst.mods.killauraLegitMod.setEnabled(false);
		if(wurst.mods.multiAuraMod.isEnabled())
			wurst.mods.multiAuraMod.setEnabled(false);
		if(wurst.mods.clickAuraMod.isEnabled())
			wurst.mods.clickAuraMod.setEnabled(false);
		if(wurst.mods.tpAuraMod.isEnabled())
			wurst.mods.tpAuraMod.setEnabled(false);
		if(wurst.mods.triggerBotMod.isEnabled())
			wurst.mods.triggerBotMod.setEnabled(false);
		if(wurst.mods.aimbotMod.isEnabled())
			wurst.mods.aimbotMod.setEnabled(false);
		if(wurst.mods.clickAimbotMod.isEnabled())
			wurst.mods.clickAimbotMod.setEnabled(false);
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(PostUpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		targets.clear();
		target = null;
		List<EntityLivingBase> close = EntityUtils.getCloseEntities(!friends.isChecked(), 
			realRange, true, checkarmor.isChecked());
		double averageX = 0.0D;
		double averageY = 0.0D;
		double averageZ = 0.0D;
		
		for (EntityLivingBase entity : close) 
			target = entity;
		
		while(close.iterator().hasNext() && targets.size() <= 6)
			targets.add(close.iterator().next());
		
		for (EntityLivingBase entity : targets)
		{
			averageX += entity.posX;
			averageY += entity.posY + entity.getEyeHeight();
			averageZ += entity.posZ;
		}
		 	averageX /= targets.size();
		 	averageY /= targets.size();
		 	averageZ /= targets.size();        
		 	float[] rotations = EntityUtils.facePosition(mc.thePlayer, averageX, averageY, averageZ);
		 	if(rotations != null)
		 	{
		 	EntityUtils.setYaw(rotations[0]);
		 	EntityUtils.setPitch(rotations[1]);
		 	}
		 	if(wurst.mods.autoSwordMod.isActive())
		 		AutoSwordMod.setSlot();
		 	wurst.mods.criticalsMod.doCritical();
		 	wurst.mods.armorBreakerMod.SwapItem();
		
	}
	
	@Override
	public void onPostUpdate()
	{
		updateMS();
		if(target != null && !targets.isEmpty() && hasTimePassedS(realSpeed))
		{
			for (int i = 0; i < targets.size(); i++) 
			{
			updateMS();
			if(hasTimePassedM(10))
			{
				attack(targets.get(i));
				updateLastMS();
			}
			}
			updateLastMS();
		}
	}
	
	public void attack(EntityLivingBase en)
	{
		if(en == null || mc.thePlayer.getDistanceToEntity(en) > realRange)
			return;
		mc.thePlayer.swingItem();
	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
		en, C02PacketUseEntity.Action.ATTACK));
	}
	 
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(PostUpdateListener.class, this);
	}
	
	private void updateSpeedAndRange()
	{
		if(wurst.mods.yesCheatMod.isActive() && !randomspeed.isChecked())
		{
			realSpeed = yesCheatSpeed;
			realRange = yesCheatRange;
		}else if(wurst.mods.yesCheatMod.isActive() && randomspeed.isChecked())
		{
			realSpeed = yesCheatrSpeed;
			realRange = yesCheatRange;
		}else if(!wurst.mods.yesCheatMod.isActive() && randomspeed.isChecked())
		{
			realSpeed = rSpeed;
			realRange = normalRange;
		} else
		{
			realSpeed = normalSpeed;
			realRange = normalRange;
		}
	}
}
