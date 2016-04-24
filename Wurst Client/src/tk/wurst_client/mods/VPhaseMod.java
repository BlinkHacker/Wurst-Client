/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import tk.wurst_client.events.BlockBBEvent;
import tk.wurst_client.events.listeners.BlockBBListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
	description = "Phase through blocks.\n"
		+ "Tip: You can climb blocks with this.",
	name = "VPhase",
	tags = "CheckerClimb")
public class VPhaseMod extends Mod implements BlockBBListener, UpdateListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(BlockBBListener.class, this);
	}
	
	@Override
	public void onBlockBB(BlockBBEvent event)
	{
		 if (event.getBoundingBox() != null && event.getBoundingBox().maxY > 
		 mc.thePlayer.boundingBox.minY)
		      event.setBoundingBox(null);
	}
	
	@Override
	public void onUpdate()
	{
		 if (isInsideBlock() && mc.thePlayer.isSneaking())
	      {
			 float yaw = mc.thePlayer.rotationYaw;
			 mc.thePlayer.boundingBox.offsetAndUpdate(1.2 * Math.cos(Math.toRadians(yaw + 90.0F)),
				 0.0D, 1.2 * Math.sin(Math.toRadians(yaw + 90.0F)));
	      }
	}
	
	private boolean isInsideBlock() 
	{
		for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < 
			MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) 
			for(int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < 
				MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; ++y) 
				for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < 
					MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) 
				{
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					if(block != null && !(block instanceof BlockAir)) 
					{
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, 
							new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
						 if (block instanceof BlockHopper)
				              boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
						if(boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox)) 
							return true;
	               }
				}

		return false;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(BlockBBListener.class, this);
	}
}
