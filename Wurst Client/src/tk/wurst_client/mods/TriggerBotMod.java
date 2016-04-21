/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Automatically attacks the entity you're looking at.",
	name = "TriggerBot")
public class TriggerBotMod extends Mod implements UpdateListener
{
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraMod, wurst.mods.killauraLegitMod,
			wurst.mods.multiAuraMod, wurst.mods.clickAuraMod,
			wurst.mods.criticalsMod, wurst.mods.aimbotMod,
			wurst.mods.clickAimbotMod, wurst.mods.tpAuraMod};
	}
	
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(wurst.mods.killauraMod.isEnabled())
			wurst.mods.killauraMod.setEnabled(false);
		if(wurst.mods.killauraLegitMod.isEnabled())
			wurst.mods.killauraLegitMod.setEnabled(false);
		if(wurst.mods.multiAuraMod.isEnabled())
			wurst.mods.multiAuraMod.setEnabled(false);
		if(wurst.mods.clickAuraMod.isEnabled())
			wurst.mods.clickAuraMod.setEnabled(false);
		if(wurst.mods.tpAuraMod.isEnabled())
			wurst.mods.tpAuraMod.setEnabled(false);
		if(wurst.mods.clickAimbotMod.isEnabled())
			wurst.mods.clickAimbotMod.setEnabled(false);
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(mc.objectMouseOver != null
			&& mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY
			&& mc.objectMouseOver.entityHit instanceof EntityLivingBase)
		{
			updateMS();
			if(hasTimePassedS(wurst.mods.killauraMod.realSpeed))
			{
				EntityLivingBase en =
					(EntityLivingBase)mc.objectMouseOver.entityHit;
				if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.realRange
					&& EntityUtils.isCorrectEntity(en, true, true))
				{
					if(wurst.mods.autoSwordMod.isActive())
						AutoSwordMod.setSlot();
					wurst.mods.criticalsMod.doCritical();
					wurst.mods.armorBreakerMod.SwapItem();
					if(!wurst.mods.killauraMod.mobinfront.isChecked())
					{
					mc.thePlayer.swingItem();
					mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
						en, C02PacketUseEntity.Action.ATTACK));
					} else
					mc.clickMouse();
					updateLastMS();
				}
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
