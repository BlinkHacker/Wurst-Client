/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.utils.EntityUtils;

@Mod.Info(category = Mod.Category.EXPLOITS,
	description = "Makes the players near 1 block of you glitch.\n"
	    + "Causes rapid hunger loss in vanilla servers\n"
	    + "to either you or the player near you.\n"
	    + "Extermely buggy.",
	name = "HungerGlitcher")
public class HungerGlitcherMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		EntityLivingBase en = EntityUtils.getClosestEntity(true, true);
		if(mc.thePlayer.onGround && en != null
			&& en.getDistanceToEntity(mc.thePlayer) < 1)
			for(int i = 0; i < 1000; i++)
				mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer(true));
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
