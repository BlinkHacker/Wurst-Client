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
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import tk.wurst_client.events.BlockBBEvent;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.BlockBBListener;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.events.listeners.PostUpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Phase through blocks vertically.\n"
		+ "Can help you attach to walls.",
	name = "VPhase")
public class VPhaseMod extends Mod implements PostUpdateListener, PacketInputListener, BlockBBListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(PacketInputListener.class, this);
		wurst.events.add(PostUpdateListener.class, this);
		wurst.events.add(BlockBBListener.class, this);
	}
	
	@Override
	public void onPostUpdate()
	{
		mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
			mc.thePlayer.posX, mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
        if(!mc.thePlayer.isCollidedVertically && isInsideBlock())
           setEnabled(false);
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		if(event.getPacket() instanceof S12PacketEntityVelocity && ((
			S12PacketEntityVelocity)event.getPacket()).func_149412_c() ==
			mc.thePlayer.getEntityId() && isInsideBlock())
           event.cancel();
	}
	
	@Override
	public void onBlockBB(BlockBBEvent event)
	{
		if((double)event.getY() > mc.thePlayer.boundingBox.minY - 0.3D && (double)event.getY() 
			< mc.thePlayer.boundingBox.maxY && !isInsideBlock() && mc.thePlayer.isCollidedHorizontally)
            event.setBoundingBox((AxisAlignedBB)null);
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
						if(boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox)) 
							return true;
	               }
				}

		return false;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(PacketInputListener.class, this);
		wurst.events.remove(PostUpdateListener.class, this);
		wurst.events.remove(BlockBBListener.class, this);
	}
}
