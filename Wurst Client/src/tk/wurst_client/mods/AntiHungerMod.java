/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.events.PacketOutputEvent;
import tk.wurst_client.events.listeners.PacketOutputListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Prevents or reduces hunger loss.\n"
		+ "Note: This may break mods that spam packets like FastEat.",
	name = "AntiHunger")
public class AntiHungerMod extends Mod implements PacketOutputListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer) event.getPacket();
            double yDifference = mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
            boolean groundCheck = yDifference == 0.0D;
            if (groundCheck && !mc.playerController.isHittingBlock)
                player.setOnGround(false);
        }
		
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketOutputListener.class, this);
	}

}
