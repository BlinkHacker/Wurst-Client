/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import tk.wurst_client.events.listeners.PostUpdateListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Automatically attacks the closest valid entity whenever you\n"
		+ "click.\n"
		+ "Warning: ClickAuras generally look more suspicious than Killauras\n"
		+ "and are easier to detect. It is recommended to use Killaura or\n"
		+ "TriggerBot instead.",
	name = "ClickAura",
	tags = "Click Aura,ClickAimbot,Click Aimbot")
public class ClickAuraMod extends Mod implements UpdateListener, PostUpdateListener
{
	private EntityLivingBase en;
	private boolean shouldattack = false;
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraMod, wurst.mods.killauraLegitMod,
			wurst.mods.multiAuraMod, wurst.mods.triggerBotMod, 
			wurst.mods.aimbotMod,wurst.mods.clickAimbotMod,
			wurst.mods.tpAuraMod};
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
		if(wurst.mods.triggerBotMod.isEnabled())
			wurst.mods.triggerBotMod.setEnabled(false);
		if(wurst.mods.tpAuraMod.isEnabled())
			wurst.mods.tpAuraMod.setEnabled(false);
		if(wurst.mods.aimbotMod.isEnabled())
			wurst.mods.aimbotMod.setEnabled(false);
		if(wurst.mods.clickAimbotMod.isEnabled())
			wurst.mods.clickAimbotMod.setEnabled(false);
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(PostUpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		en = EntityUtils.getClosestEntity(!wurst.mods.killauraMod.friends.isChecked(), 
			true, true, wurst.mods.killauraMod.checkarmor.isChecked());
		if (en != null && mc.thePlayer.getDistanceToEntity(en) <= 
			wurst.mods.killauraMod.realRange && mc.gameSettings.keyBindAttack.pressed)
		{
			if(wurst.mods.autoSwordMod.isActive())
				AutoSwordMod.setSlot();
			wurst.mods.criticalsMod.doCritical();
			float[] rotations = EntityUtils.getRotationsNeeded(en);
			EntityUtils.setYaw(rotations[0]);
			EntityUtils.setPitch(rotations[1]);	
			wurst.mods.criticalsMod.doCritical();
			wurst.mods.armorBreakerMod.SwapItem();
			shouldattack = true;
		}
	}
	
	@Override
	public void onPostUpdate()
	{
		updateMS();
		if(hasTimePassedS(wurst.mods.killauraMod.realSpeed) && en != null
			&& shouldattack)
			if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.realRange)
		{
			mc.thePlayer.swingItem();
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
				en, C02PacketUseEntity.Action.ATTACK));
			updateLastMS();
			shouldattack = false;
		}
	}
	
	@Override
	public void onDisable()
	{
		shouldattack = false;
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(PostUpdateListener.class, this);
	}
}
