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
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.BLOCKS,
	description = "Prevents world damage such as lava, water, and\n"
		+ "suffocation by freezing packets whenever possible.\n"
		+ "Note: You must not move for this to work.\n"
		+ "You can enable Blink for movement.",
	name = "NoWorld")
public class NoWorldMod extends Mod implements PacketOutputListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if (event.getPacket() instanceof C03PacketPlayer && (isInLiquid() || isInsideBlock())) 
                event.cancel();
	}
	
	 private boolean isInsideBlock()
	 {
	        boolean usingItem = mc.thePlayer.isUsingItem();
	        boolean swinging = mc.thePlayer.isSwingInProgress;
	        boolean moving = mc.thePlayer.motionX != 0 || !mc.thePlayer.isCollidedVertically
	        	|| mc.gameSettings.keyBindJump.getIsKeyPressed() || mc.thePlayer.motionZ != 0;
	        return BlockUtils.isInsideBlock(mc.thePlayer) && !usingItem && !swinging && !moving;
	 }
	 
	 private boolean isInLiquid()
	 {
		 boolean usingItem = mc.thePlayer.isUsingItem();
	        boolean swinging = mc.thePlayer.isSwingInProgress;
	        boolean moving = mc.thePlayer.motionX != 0 || !mc.thePlayer.isCollidedVertically 
	        	|| mc.gameSettings.keyBindJump.getIsKeyPressed() || mc.thePlayer.motionZ != 0;
	        return BlockUtils.isInLiquid(mc.thePlayer) && !usingItem && !swinging && !moving;
	 }
	 
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketOutputListener.class, this);
	}

}
