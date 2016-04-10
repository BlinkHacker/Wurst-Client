/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.FUN,
	description = "Makes other players look like aimbot hackers.",
	name = "FakeHackers")
public class FakeHackersMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		for(Object entity : mc.theWorld.loadedEntityList)
		 if(entity instanceof EntityPlayer)
		 {
	        EntityPlayer otherplayer = (EntityPlayer)entity;
	        if (otherplayer != mc.thePlayer && mc.thePlayer.getDistanceToEntity(otherplayer) <= 3.8D)
	        {
	          double lookX = mc.thePlayer.posX - otherplayer.posX;
	          double lookZ = mc.thePlayer.posZ - otherplayer.posZ;
	          double lookY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (otherplayer.posY + otherplayer.getEyeHeight());
	          double dist = MathHelper.sqrt_double((lookX * lookX + lookZ * lookZ));
	          float yaw = (float)(Math.atan2(lookZ, lookX) * 180.0D / Math.PI) - 90.0F;
	          float pitch = (float)-(Math.atan2(lookY, dist) * 180.0D / Math.PI);
	          otherplayer.rotationYawHead += MathHelper.wrapAngleTo180_float(yaw - otherplayer.rotationYawHead);
	          otherplayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - otherplayer.rotationYaw);
	          otherplayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - otherplayer.rotationPitch);
	        }
	      }
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
