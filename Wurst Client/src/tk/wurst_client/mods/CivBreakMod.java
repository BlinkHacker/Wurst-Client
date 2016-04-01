/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.events.BlockBreakingEvent;
import tk.wurst_client.events.listeners.BlockBreakingListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.navigator.settings.SliderSetting;
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.BLOCKS,
	description = "Allows you to mine the nexus faster in Annihilation.\n"
	+ "If you get kicked, use normal civbreak.\n"
	+ "Tip: Instant FastBreak is another form of CivBreak.",
	name = "CivBreak")
public class CivBreakMod extends Mod implements BlockBreakingListener, UpdateListener
{
	private int mode = 0;
	private String[] modes = new String[]{"Normal", "Insane", "Bypass"};
	private BlockPos block;
	private EnumFacing side;
	public int civbreakspeed = 1;
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
		
		settings.add(new SliderSetting("Speed (Bypass CivBreak)", civbreakspeed, 1, 3, 1,
			ValueDisplay.INTEGER)
		{
			@Override
			public void update()
			{
				civbreakspeed = (int)getValue();
			}
		});
	}
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.fastBreakMod};
	}
	
	@Override
	public void onEnable()
	{	
		if(wurst.mods.fastBreakMod.isEnabled())
			wurst.mods.fastBreakMod.setEnabled(false);
		wurst.events.add(BlockBreakingListener.class, this);
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onBlockBreaking(BlockBreakingEvent event)
	{
		if(mode == 2)
		{
	    BlockBreakingEvent blk = (BlockBreakingEvent)event;
        if (blk.getState() != BlockBreakingEvent.EnumBlock.CLICK)
          return;
        block = blk.getPos();
        side = blk.getSide();
        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block, side));
        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block, side));
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)
          return;
        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(block, -1, mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
		}
		
	}
	
	@Override
	public void onUpdate()
	{
		if(mode == 2)
		{
		 if(block != null && mc.thePlayer.getDistanceSq(block) < 29.399999618530273D) {
			 BlockUtils.faceBlockPacket(block);
	            for(int i = 0; i < civbreakspeed; i++) {
	               mc.thePlayer.swingItem();
	               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block, side));
	               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block, side));
	               if(mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
	                  mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(block, -1, mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
	               }

	               mc.playerController.onPlayerDamageBlock(block, side);
	            }
	         }
		}
	}
	
	@Override
	public void onDisable()
	{	
		wurst.events.remove(BlockBreakingListener.class, this);
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