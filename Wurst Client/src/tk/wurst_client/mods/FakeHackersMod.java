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
	          double lookX = mc.thePlayer.posX - localEntityPlayer.posX;
	          double lookZ = mc.thePlayer.posZ - localEntityPlayer.posZ;
	          double lookY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (localEntityPlayer.posY + localEntityPlayer.getEyeHeight());
	          double total = MathHelper.sqrt_double((lookX * lookX + lookZ * lookZ));
	          float ZX = (float)(Math.atan2(lookZ, lookX) * 180.0D / 3.141592653589793D) - 90.0F;
	          float Y = (float)-(Math.atan2(lookY, total) * 180.0D / 3.141592653589793D);
	          localEntityPlayer.rotationYawHead += MathHelper.wrapAngleTo180_float(ZX - localEntityPlayer.rotationYawHead);
	          localEntityPlayer.rotationYaw += MathHelper.wrapAngleTo180_float(ZX - localEntityPlayer.rotationYaw);
	          localEntityPlayer.rotationPitch += MathHelper.wrapAngleTo180_float(Y - localEntityPlayer.rotationPitch);
	        }
	      }
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
