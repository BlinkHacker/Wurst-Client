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
    public void onEnable() {
    	
        wurst.events.add(UpdateListener.class, this);
    }
    
    @Override
    public void onUpdate() 
    {
		Entity en = EntityUtils.getClosestNonlivingEntity(false, true);
		if(en != null)
			if (en instanceof EntityFallingBlock)
				switch(mode)
				{
					case 0:
						updateMS();
						boolean yesCheatMode = wurst.mods.yesCheatMod.isActive();
						if(yesCheatMode
							&& hasTimePassedS(wurst.mods.killauraMod.yesCheatSpeed)
							|| !yesCheatMode
							&& hasTimePassedS(wurst.mods.killauraMod.normalSpeed) && en != null 
						&& EntityUtils.ticksCheck(en))
							if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.yesCheatRange || !yesCheatMode
								&& mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.normalRange)
							{
						if(wurst.mods.autoSwordMod.isActive())
							AutoSwordMod.setSlot();
						wurst.mods.criticalsMod.doCritical();
						wurst.mods.blockHitMod.doBlock();
						EntityUtils.faceNonlivingEntityPacket(en);
						mc.thePlayer.swingItem();
						mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
							en, C02PacketUseEntity.Action.ATTACK));
						updateLastMS();
							}
					case 1:
						updateMS();
						if(hasTimePassedS(wurst.mods.killauraMod.yesCheatSpeed) && en != null 
						&& EntityUtils.ticksCheck(en))
							if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.yesCheatRange)
							{
								if(wurst.mods.criticalsMod.isActive() && mc.thePlayer.onGround)
									mc.thePlayer.jump();
								if(EntityUtils.getDistanceFromMouse(en) > 55)
									EntityUtils.faceNonlivingEntityClient(en);
								else
								{
									EntityUtils.faceNonlivingEntityClient(en);
									mc.thePlayer.swingItem();
									mc.playerController.attackEntity(mc.thePlayer, en);
								}
								updateLastMS();
							}
					case 2:
						if(en != null)
							if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.realRange && EntityUtils.ticksCheck(en))
								EntityUtils.faceNonlivingEntityClient(en);
					case 3:
						if(mc.objectMouseOver != null
						&& mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY
						&& mc.objectMouseOver.entityHit instanceof Entity)
					{
						updateMS();
						boolean yesCheatMode2 = wurst.mods.yesCheatMod.isActive();
						if(yesCheatMode2
							&& hasTimePassedS(wurst.mods.killauraMod.yesCheatSpeed)
							|| !yesCheatMode2
							&& hasTimePassedS(wurst.mods.killauraMod.normalSpeed))
						{
							Entity entrigger =
								(Entity)mc.objectMouseOver.entityHit;
							if((yesCheatMode2
								&& mc.thePlayer.getDistanceToEntity(entrigger) <= wurst.mods.killauraMod.yesCheatRange || !yesCheatMode2
								&& mc.thePlayer.getDistanceToEntity(entrigger) <= wurst.mods.killauraMod.normalRange)
								&& EntityUtils.isCorrectNonlivingEntity(entrigger, true)
								&& EntityUtils.ticksCheck(entrigger))
							{
								if(wurst.mods.autoSwordMod.isActive())
									AutoSwordMod.setSlot();
								wurst.mods.criticalsMod.doCritical();
								wurst.mods.blockHitMod.doBlock();
								mc.thePlayer.swingItem();
								mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(
									en, C02PacketUseEntity.Action.ATTACK));
								updateLastMS();
							}
						}
					}
					default:
						return;

				}
    }
     @Override
     public void onDisable() {
    	 
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