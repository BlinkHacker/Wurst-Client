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
	description = "Makes other players look like hackers.",
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
	        EntityPlayer localEntityPlayer = (EntityPlayer)entity;
	        if (localEntityPlayer != mc.thePlayer && mc.thePlayer.getDistanceToEntity(localEntityPlayer) <= 3.8D)
	        {
	          double d1 = mc.thePlayer.posX - localEntityPlayer.posX;
	          double d2 = mc.thePlayer.posZ - localEntityPlayer.posZ;
	          double d3 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (localEntityPlayer.posY + localEntityPlayer.getEyeHeight());
	          double d4 = MathHelper.sin((float)(d1 * d1 + d2 * d2));
	          float f1 = (float)(Math.atan2(d2, d1) * 180.0D / 3.141592653589793D) - 90.0F;
	          float f2 = (float)-(Math.atan2(d3, d4) * 180.0D / 3.141592653589793D);
	          localEntityPlayer.rotationYawHead += MathHelper.wrapAngleTo180_float(f1 - localEntityPlayer.rotationYawHead);
	          localEntityPlayer.rotationYaw += MathHelper.wrapAngleTo180_float(f1 - localEntityPlayer.rotationYaw);
	          localEntityPlayer.rotationPitch += MathHelper.wrapAngleTo180_float(f2 - localEntityPlayer.rotationPitch);
	        }
	      }
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
