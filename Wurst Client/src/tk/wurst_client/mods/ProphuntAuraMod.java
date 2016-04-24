/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tk.wurst_client.mods;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
    description = "Aura designed for PropHunt servers, with different modes.\n"
    	+ "Warning: You might attack (non-prophunt) falling entities.",
    name = "ProphuntAura")
public class ProphuntAuraMod extends Mod implements UpdateListener {

	private int mode = 0;
	private String[] modes = new String[]{"KillAura", "KALegit", "Aimbot", "Triggerbot"};
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.prophuntEspMod};
	}
	
	
	@Override
	public void initSettings()
	{
		settings.add(new ModeSetting("Mode", modes, mode)
		{
			@Override
			public void update()
			{
				mode = getSelected();
			}
		});
	}
	
    @Override
    public void onEnable() 
    {
        wurst.events.add(UpdateListener.class, this);
    }
    
    @Override
    public void onUpdate() 
    {
    	KillauraMod killaura = wurst.mods.killauraMod;
		Entity en = EntityUtils.getClosestNonlivingEntity(false, true, true);
		if(en != null)
			if (en instanceof EntityFallingBlock)
				switch(mode)
				{
					case 0:
						updateMS();
						if(hasTimePassedS(killaura.realSpeed) && en != null)
							if(mc.thePlayer.getDistanceToEntity(en) <= killaura.realRange)
							{
								if(wurst.mods.autoSwordMod.isActive())
									AutoSwordMod.setSlot();
								wurst.mods.criticalsMod.doCritical();
								wurst.mods.armorBreakerMod.SwapItem();
								EntityUtils.faceNonlivingEntityPacket(en);
								mc.thePlayer.swingItem();
								mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
									en, C02PacketUseEntity.Action.ATTACK));
								updateLastMS();
							}
						break;
					case 1:
						updateMS();
						if(hasTimePassedS(killaura.randomspeed.isChecked() ? killaura.yesCheatrSpeed 
							: killaura.yesCheatSpeed) && en != null)
							if(mc.thePlayer.getDistanceToEntity(en) <= killaura.yesCheatRange)
							{
								if(wurst.mods.criticalsMod.isActive() && mc.thePlayer.onGround)
									mc.thePlayer.jump();
								if(EntityUtils.getDistanceFromMouse(en) > 55)
									EntityUtils.faceNonlivingEntityClient(en);
								else
								{
									EntityUtils.faceNonlivingEntityClient(en);
									wurst.mods.armorBreakerMod.SwapItem();
									if(!killaura.mobinfront.isChecked())
									{
										mc.thePlayer.swingItem();
										mc.playerController.attackEntity(mc.thePlayer, en);
									} else
										mc.clickMouse();
								}
								updateLastMS();
							}
						break;
					case 2:
						if(en != null && mc.thePlayer.getDistanceToEntity(en) <= 
						killaura.normalRange)
							EntityUtils.faceNonlivingEntityClient(en);
						break;
					case 3:
						if(mc.objectMouseOver != null
						&& mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY
						&& mc.objectMouseOver.entityHit instanceof Entity)
					{
						updateMS();
						if(hasTimePassedS(killaura.realSpeed))
						{
							Entity entrigger =
								(Entity)mc.objectMouseOver.entityHit;
							if(en != null && mc.thePlayer.getDistanceToEntity(entrigger) <= 
								killaura.realRange
								&& EntityUtils.isCorrectNonlivingEntity(entrigger, true, true))
							{
								if(wurst.mods.autoSwordMod.isActive())
									AutoSwordMod.setSlot();
								wurst.mods.criticalsMod.doCritical();
								wurst.mods.armorBreakerMod.SwapItem();
								if(!killaura.mobinfront.isChecked())
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
						break;
					default:
						return;

				}
    }
    
    @Override
    public void onDisable() 
    {	 
         wurst.events.remove(UpdateListener.class, this);
    }
     
 	public int getMode()
 	{
 		return mode;
 	}
 	
 	public void setMode(int mode)
 	{
 		((ModeSetting)settings.get(1)).setSelected(mode);
 	}
 	
 	public String[] getModes()
 	{
 		return modes;
 	}
}