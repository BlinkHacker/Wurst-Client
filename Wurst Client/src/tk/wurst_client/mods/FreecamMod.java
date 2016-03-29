/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.RENDER,
	description = "Allows you to fly out of your body.\n"
		+ "Looks similar to spectator mode."
		+ "If anti-cheat plugins glitch you, enable slient freecam.",
	name = "Freecam")
public class FreecamMod extends Mod implements UpdateListener, PacketInputListener
{
	public final CheckboxSetting slientfreecam = new CheckboxSetting(
		"Slient Freecam (prevent server from glitching freecam)", false);
	private EntityOtherPlayerMP fakePlayer = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	
	@Override
	public void initSettings()
	{
		settings.add(slientfreecam);
	}
	
	@Override
	public void onEnable()
	{
		oldX = mc.thePlayer.posX;
		oldY = mc.thePlayer.posY;
		oldZ = mc.thePlayer.posZ;
		fakePlayer =
			new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
		fakePlayer.clonePlayer(mc.thePlayer, true);
		fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
		fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
		mc.theWorld.addEntityToWorld(-69, fakePlayer);
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(PacketInputListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
		mc.thePlayer.jumpMovementFactor = wurst.mods.flightMod.speed / 10;
		if(mc.gameSettings.keyBindJump.pressed)
			mc.thePlayer.motionY += wurst.mods.flightMod.speed;
		if(mc.gameSettings.keyBindSneak.pressed)
			mc.thePlayer.motionY -= wurst.mods.flightMod.speed;
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		if(slientfreecam.isChecked())
		{
			PacketInputEvent receive = (PacketInputEvent)event;
			if(receive.getPacket() instanceof S08PacketPlayerPosLook ||
				receive.getPacket() instanceof S07PacketRespawn)
				event.cancel();
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(PacketInputListener.class, this);
		mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ,
			mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
		mc.theWorld.removeEntityFromWorld(-69);
		fakePlayer = null;
		mc.renderGlobal.loadRenderers();
	}
}
