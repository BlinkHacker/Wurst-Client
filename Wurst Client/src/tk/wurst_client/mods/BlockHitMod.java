/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.navigator.settings.SliderSetting;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Automatically blocks whenever an entity is near.\n"
		+ "Tip: You can use this with NoSlowdown to bypass NCP in blocking.",
	name = "BlockHit",
	tags = "autoblock")
public class BlockHitMod extends Mod implements UpdateListener
{
	public float Range = 4F;
	public final CheckboxSetting alwaysblock = new CheckboxSetting(
		"Always Block", false);
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Range", Range, 2, 9, 0.1,
		ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				Range = (float)getValue();
			}
		});
		settings.add(alwaysblock);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		EntityLivingBase en = EntityUtils.getClosestEntity(!wurst.mods.killauraMod.friends.isChecked(), 
			true, true, 
			wurst.mods.killauraMod.checkarmor.isChecked());
		if(mc.thePlayer.getCurrentEquippedItem() != null && 
			mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && en != null &&
			mc.thePlayer.getDistanceToEntity(en) <= Range)
			mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
		if(alwaysblock.isChecked() && mc.thePlayer.getCurrentEquippedItem() != null && 
			mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)
			mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
		if(mc.gameSettings.keyBindAttack.pressed && mc.thePlayer.getCurrentEquippedItem() != null && 
			mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword &&
			hasTimePassedS(wurst.mods.killauraMod.realSpeed))
		{
			mc.clickMouse();
			updateLastMS();
		}
		
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
