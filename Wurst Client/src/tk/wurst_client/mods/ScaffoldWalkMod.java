/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import tk.wurst_client.events.listeners.PostUpdateListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.BLOCKS,
	description = "Allows you to automatically place blocks under you\n"
		+ "while you are walking. Good for fast bridge building.\n"
		+ "Note: This may impair walking while not bridge building.",
	name = "ScaffoldWalk")
public class ScaffoldWalkMod extends Mod implements UpdateListener, PostUpdateListener
{
	private BlockData blockData = null;
	public final CheckboxSetting slower = new CheckboxSetting(
		"Slower ScaffoldWalk", false);
	
	@Override
	public void initSettings()
	{
		settings.add(slower);
	}
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(PostUpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		blockData = null;
		if (mc.thePlayer.getHeldItem() != null && !mc.thePlayer.isSneaking() && 
	    	  mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)
		{
			BlockPos blockBelow = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
	        if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air)
	        {
	        	blockData = getBlockData(blockBelow);
	        	if (blockData != null)
	        		BlockUtils.faceBlockPacket(new BlockPos(blockData.position.getX(), blockData.position.getY(),
	        			blockData.position.getZ()));
	        }
		}
	}
	
	@Override
	public void onPostUpdate()
	{
		updateMS();
		if (blockData != null)
	      if(hasTimePassedM(slower.isChecked() ? 250 : 75))
	      {
	    	  mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 
	    		  C0BPacketEntityAction.Action.START_SNEAKING));
	          if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), 
	        	  blockData.position, blockData.face, new Vec3(blockData.position.getX(), 
	        		  blockData.position.getY(), blockData.position.getZ())))
	            mc.thePlayer.swingItem();
	          mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 
	        	  C0BPacketEntityAction.Action.STOP_SNEAKING));
	          updateLastMS();
	      }
	}
	
	public BlockData getBlockData(BlockPos blockpos)
	{
		List<Block> blacklist = Arrays.asList(new Block[] { 
			Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava});
	    return mc.theWorld.getBlockState(blockpos.add(0, 0, 1)).getBlock() != blacklist ? 
	    	new BlockData(blockpos.add(0, 0, 1), EnumFacing.NORTH) : 
	    		mc.theWorld.getBlockState(blockpos.add(0, 0, -1)).getBlock() != blacklist ? 
	    			new BlockData(blockpos.add(0, 0, -1), EnumFacing.SOUTH) : 
	    				mc.theWorld.getBlockState(blockpos.add(1, 0, 0)).getBlock() != blacklist ? 
	    						new BlockData(blockpos.add(1, 0, 0), EnumFacing.WEST) 
	    				: mc.theWorld.getBlockState(blockpos.add(-1, 0, 0)).getBlock() != blacklist ? 
	    					new BlockData(blockpos.add(-1, 0, 0), EnumFacing.EAST) : 
	    						mc.theWorld.getBlockState(blockpos.add(0, -1, 0)).getBlock() != blacklist 
	    						? new BlockData(blockpos.add(0, -1, 0), EnumFacing.UP) : null;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(PostUpdateListener.class, this);
	}
	
	public class BlockData
	{
		public BlockPos position;
		public EnumFacing face;
	  
		public BlockData(BlockPos position, EnumFacing face)
		{
			this.position = position;
			this.face = face;
		}
	}
}
