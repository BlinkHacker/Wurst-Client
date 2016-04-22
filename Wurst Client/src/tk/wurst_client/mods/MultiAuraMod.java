/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Faster Killaura that attacks multiple entities at once.",
	name = "MultiAura",
	noCheatCompatible = false)
public class MultiAuraMod extends Mod implements UpdateListener
{
	private float range = 6F;
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraMod, wurst.mods.killauraLegitMod,
			wurst.mods.clickAuraMod, wurst.mods.triggerBotMod,
			wurst.mods.aimbotMod};
	}
	
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(wurst.mods.killauraMod.isEnabled())
			wurst.mods.killauraMod.setEnabled(false);
		if(wurst.mods.killauraLegitMod.isEnabled())
			wurst.mods.killauraLegitMod.setEnabled(false);
		if(wurst.mods.clickAuraMod.isEnabled())
			wurst.mods.clickAuraMod.setEnabled(false);
		if(wurst.mods.tpAuraMod.isEnabled())
			wurst.mods.tpAuraMod.setEnabled(false);
		if(wurst.mods.triggerBotMod.isEnabled())
			wurst.mods.triggerBotMod.setEnabled(false);
		if(wurst.mods.aimbotMod.isEnabled())
			wurst.mods.aimbotMod.setEnabled(false);
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		EntityLivingBase closestEntity =
			EntityUtils.getClosestEntity(!wurst.mods.killauraMod.friends.isChecked(),
				false, true, true);
		if(closestEntity != null
			&& mc.thePlayer.getDistanceToEntity(closestEntity) <= range)
		{
			if(wurst.mods.autoSwordMod.isActive())
				AutoSwordMod.setSlot();
			wurst.mods.criticalsMod.doCritical();
			ArrayList<EntityLivingBase> entities =
				EntityUtils.getCloseEntities(true, range, true, true);
			for(int i = 0; i < Math.min(entities.size(), 64); i++)
			{
				EntityLivingBase en = entities.get(i);
				EntityUtils.faceEntityPacket(en);
				mc.thePlayer.swingItem();
				mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
					en, C02PacketUseEntity.Action.ATTACK));
			}
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
