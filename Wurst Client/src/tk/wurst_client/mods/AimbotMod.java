/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
    description = "Automatically aims to the nearest entity.",
    name = "Aimbot")
public class AimbotMod extends Mod implements UpdateListener {

	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraMod, wurst.mods.multiAuraMod,
			wurst.mods.clickAuraMod, wurst.mods.triggerBotMod,
			wurst.mods.killauraLegitMod,wurst.mods.clickAimbotMod,
			wurst.mods.tpAuraMod};
	}
	
    @Override
    public void onEnable() {
    	//Triggerbot is not disabled because Aimbot+Triggerbot could bypass anticheat plugins.
    	if(wurst.mods.killauraMod.isEnabled())
			wurst.mods.killauraMod.setEnabled(false);
		if(wurst.mods.multiAuraMod.isEnabled())
			wurst.mods.multiAuraMod.setEnabled(false);
		if(wurst.mods.clickAuraMod.isEnabled())
			wurst.mods.clickAuraMod.setEnabled(false);
		if(wurst.mods.tpAuraMod.isEnabled())
			wurst.mods.tpAuraMod.setEnabled(false);
    	if(wurst.mods.killauraLegitMod.isEnabled())
    		wurst.mods.killauraLegitMod.setEnabled(false);
    	if(wurst.mods.clickAimbotMod.isEnabled())
			wurst.mods.clickAimbotMod.setEnabled(false);
        wurst.events.add(UpdateListener.class, this);
    }
    
    @Override
    public void onUpdate() 
    {
		EntityLivingBase en = EntityUtils.getClosestEntity(true, true, true);
		if(en != null)
			if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.normalRange)
				EntityUtils.faceEntityClient(en);
    }
     @Override
     public void onDisable() {
    	 
         wurst.events.remove(UpdateListener.class, this);
     }
}