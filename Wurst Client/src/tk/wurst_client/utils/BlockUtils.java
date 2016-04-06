/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtils
{
	public static void faceBlockClient(BlockPos blockPos)
	{
		double diffX =
			blockPos.getX() + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY =
			blockPos.getY()
				+ 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft
					.getMinecraft().thePlayer.getEyeHeight());
		double diffZ =
			blockPos.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Minecraft.getMinecraft().thePlayer.rotationYaw =
			Minecraft.getMinecraft().thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw
					- Minecraft.getMinecraft().thePlayer.rotationYaw);
		Minecraft.getMinecraft().thePlayer.rotationPitch =
			Minecraft.getMinecraft().thePlayer.rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch
					- Minecraft.getMinecraft().thePlayer.rotationPitch);
	}
	
	public static void faceBlockPacket(BlockPos blockPos)
	{
		double diffX =
			blockPos.getX() + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY =
			blockPos.getY()
				+ 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft
					.getMinecraft().thePlayer.getEyeHeight());
		double diffZ =
			blockPos.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft
				.getMinecraft().thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw
					- Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
					+ MathHelper.wrapAngleTo180_float(pitch
						- Minecraft.getMinecraft().thePlayer.rotationPitch),
				Minecraft.getMinecraft().thePlayer.onGround));
	}
	
	public static void faceBlockClientHorizontally(BlockPos blockPos)
	{
		double diffX =
			blockPos.getX() + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffZ =
			blockPos.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		Minecraft.getMinecraft().thePlayer.rotationYaw =
			Minecraft.getMinecraft().thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw
					- Minecraft.getMinecraft().thePlayer.rotationYaw);
	}
	
	public static float getPlayerBlockDistance(BlockPos blockPos)
	{
		return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(),
			blockPos.getZ());
	}
	
	public static float getPlayerBlockDistance(double posX, double posY,
		double posZ)
	{
		float xDiff = (float)(Minecraft.getMinecraft().thePlayer.posX - posX);
		float yDiff = (float)(Minecraft.getMinecraft().thePlayer.posY - posY);
		float zDiff = (float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
		return getBlockDistance(xDiff, yDiff, zDiff);
	}
	
	public static float getBlockDistance(float xDiff, float yDiff, float zDiff)
	{
		return MathHelper
			.sqrt_float((xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F)
				* (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
	}
	
	public static float getHorizontalPlayerBlockDistance(BlockPos blockPos)
	{
		float xDiff =
			(float)(Minecraft.getMinecraft().thePlayer.posX - blockPos.getX());
		float zDiff =
			(float)(Minecraft.getMinecraft().thePlayer.posZ - blockPos.getZ());
		return MathHelper.sqrt_float((xDiff - 0.5F) * (xDiff - 0.5F)
			+ (zDiff - 0.5F) * (zDiff - 0.5F));
	}
	
	public static float[] getBlockRotations(double x, double y, double z)
	 {
	    double xDiff = x - Minecraft.getMinecraft().thePlayer.posX + 0.5D;
	    double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ + 0.5D;
	    
	    double yDiff = y - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().
	    	thePlayer.getEyeHeight() - 1.0D);
	    double total = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
	    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
	    
	    return new float[] {yaw, (float)-(Math.atan2(yDiff, total) * 180.0D / 3.141592653589793D) };
	  }
	
	public static boolean isInsideBlock(Entity entity) 
	{
        for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x <
        	MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(entity.getEntityBoundingBox().minY); y < 
            	MathHelper.floor_double(entity.getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < 
                	MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(
                        	Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), 
                        	Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }

                        if (boundingBox != null && entity.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

	 public static boolean isInLiquid(Entity entity) {
	        if (entity == null)
	            return false;
	        boolean inLiquid = false;
	        final int y = (int) entity.getEntityBoundingBox().minY;
	        for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < 
	        	MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.
	        		getEntityBoundingBox().maxX) + 1; x++) {
	            for (int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < 
	            	MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1; z++) {
	                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(
	                	new BlockPos(x, y, z)).getBlock();
	                if (block != null && !(block instanceof BlockAir)) {
	                    if (!(block instanceof BlockLiquid))
	                        return false;
	                    inLiquid = true;
	                }
	            }
	        }
	        return inLiquid || Minecraft.getMinecraft().thePlayer.isInWater();
	    }
}
