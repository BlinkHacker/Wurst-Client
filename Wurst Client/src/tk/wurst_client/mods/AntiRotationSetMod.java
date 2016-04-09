/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Prevents anti-cheat plugins from forcing you\n"
		+ "to look at a certain place. Note that this will cause\n"
		+ "NCP to throw some checks, so its best to leave this\n"
		+ "disabled unless you really need it.",
	name = "AntiRotationSet")
public class AntiRotationSetMod extends Mod implements PacketInputListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketInputListener.class, this);
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
	         if(event.getPacket() instanceof S08PacketPlayerPosLook) 
	         {
	            S08PacketPlayerPosLook poslook = (S08PacketPlayerPosLook)event.getPacket();
	            if(mc.thePlayer != null && mc.thePlayer.rotationYaw != -180.0F && mc.thePlayer.rotationPitch != 0.0F) 
	            {
	               poslook.field_148936_d = mc.thePlayer.rotationYaw;
	               poslook.field_148937_e = mc.thePlayer.rotationPitch;
	            }
	         }


	}
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketInputListener.class, this);
	}

	
}
