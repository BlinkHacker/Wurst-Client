/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.EntityUtils;

/**
 * Aimbot mod based off of Colony hacked client's aimbot
 * Credit to nulldev
 */
@Mod.Info(category = Mod.Category.COMBAT,
    description = "Automatically aims to the nearest entity.",
    name = "Aimbot")
public class AimbotMod extends Mod implements UpdateListener {

	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraMod, wurst.mods.multiAuraMod,
			wurst.mods.clickAuraMod, wurst.mods.triggerBotMod,
			wurst.mods.killauraLegitMod};
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
    	if(wurst.mods.tpAuraMod.isEnabled())
    		wurst.mods.tpAuraMod.setEnabled(false);
        wurst.events.add(UpdateListener.class, this);
    }

    

   //public void faceEntityBow(EntityLivingBase var0, float var1, float var2) {
     //   double var3 = 0.0D;
       // double var5 = var0.posX - Minecraft.getMinecraft().thePlayer.posX;
       // double var7 = var0.posZ - Minecraft.getMinecraft().thePlayer.posZ;
      //  double var9 = (var0.posY - Minecraft.getMinecraft().thePlayer.posY) + 1.2D;

      //  if ((var7 > 0.0D) && (var5 > 0.0D)) {
      //      var3 = Math.toDegrees(-Math.atan(var5 / var7));
      //  } else if ((var7 > 0.0D) && (var5 < 0.0D)) {
    //        var3 = Math.toDegrees(-Math.atan(var5 / var7));
    //    } else if ((var7 < 0.0D) && (var5 > 0.0D)) {
    //        var3 = -90.0D + Math.toDegrees(Math.atan(var7 / var5));
    //    } else if ((var7 < 0.0D) && (var5 < 0.0D)) {
   //         var3 = 90.0D + Math.toDegrees(Math.atan(var7 / var5));
   //     }

    //    float var11 = (float) Math.sqrt((var7 * var7) + (var5 * var5));
   //     float var12 = (float) (-Math.toDegrees(Math.atan(var9 / var11)));
   //     Minecraft.getMinecraft().thePlayer.rotationPitch = var12 - 3.0F;
   //     Minecraft.getMinecraft().thePlayer.rotationYaw = (float) var3;
  //  }

    public void aimWithNoBow(EntityLivingBase e) {
        EntityUtils.faceEntityClient(e);
    }

    @Override
    public void onUpdate() {
        EntityLivingBase en = EntityUtils.getClosestEntity(true, true);
        if (en == null) return;
        if( mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.normalRange
			&& EntityUtils.isCorrectEntity(en, true)) {
        //    int curItem = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
       //     ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(curItem);
       //     if (item != null && item.getItem() instanceof ItemBow) {
       //         faceEntityBow(en, 100, 100);
       //     } else {
                aimWithNoBow(en);
            
        }
    }
     @Override
     public void onDisable() {
         wurst.events.remove(UpdateListener.class, this);
     }}