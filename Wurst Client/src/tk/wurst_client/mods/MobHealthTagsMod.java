/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Adds a nametag that shows the health of mobs\n"
		+ "with no nametags.",
	name = "MobHealthTags")
public class MobHealthTagsMod extends Mod implements RenderListener
{	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.healthTagsMod};
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		for(Object entity : mc.theWorld.loadedEntityList)
			if(entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer))
			{
				EntityLivingBase en = (EntityLivingBase)entity;
				int health = (int)en.getHealth();
				int maxhealth = (int)en.getMaxHealth();
				String healthtag = "";
				float percenthealth = health * 100 / maxhealth;
				if(percenthealth <= 25)
					healthtag += "�4";
				else if(percenthealth <= 50)
					healthtag += "�6";
				else if(percenthealth <= 75)
					healthtag += "�e";
				else if(percenthealth <= 100)
					healthtag += "�a";
				healthtag += health + "/" + maxhealth;
				if(!en.hasCustomName())
					RenderUtils.renderTag(healthtag, en, 1, 16777215, 0.5D, false);
				else 
					RenderUtils.renderTag(healthtag, en, 1, 16777215, 1.0D, false);
			}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
	}
}
