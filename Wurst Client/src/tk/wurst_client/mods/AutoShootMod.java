/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Automatically shoots, when the arrow is going to hit.",
	name = "AutoShoot")
public class AutoShootMod extends Mod implements UpdateListener
{	
	boolean shouldRelease = false;
    int shouldUse = -1;
    
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate() 
	{
        if (shouldRelease) 
        {
            shouldRelease = false;
            shouldUse = 0;
            mc.gameSettings.keyBindUseItem.pressed = false;
            return;
        }

        if (shouldUse == 1)
        {
            shouldUse = -1;
            mc.gameSettings.keyBindUseItem.pressed = true;
            return;
        }

        if (shouldUse != -1)
            shouldUse++;

        EntityPlayerSP thePlayer = mc.thePlayer;
        if (thePlayer.getCurrentEquippedItem() != null) 
        {
            Item item = thePlayer.getCurrentEquippedItem().getItem();
            if (!(item instanceof ItemBow))
                return;
        } else
            return;

        double posX = mc.getRenderManager().renderPosX - 
        	(MathHelper.cos((thePlayer.rotationYaw / 180.0F) * (float) Math.PI) * 0.16F);
        double posY = mc.getRenderManager().renderPosY + thePlayer.getEyeHeight() - 0.10000000149011612D;
        double posZ = mc.getRenderManager().renderPosZ - 
        	(MathHelper.sin((thePlayer.rotationYaw / 180.0F) * (float) Math.PI) * 0.16F);
        double motionX = -MathHelper.sin((thePlayer.rotationYaw / 180.0F) * 
        	(float) Math.PI) * MathHelper.cos((thePlayer.rotationPitch / 180.0F) * (float) Math.PI) * 1.0;
        double motionY = (-MathHelper.sin((thePlayer.rotationPitch / 180.0F) * 
        	(float) Math.PI)) * 1;
        double motionZ = MathHelper.cos((thePlayer.rotationYaw / 180.0F) * 
        	(float) Math.PI) * MathHelper.cos((thePlayer.rotationPitch / 180.0F) * (float) Math.PI) * 1.0;

        if (thePlayer.getItemInUseCount() == 0)
            return;

        float power = thePlayer.getItemInUseDuration() / 20F;
        power = ((power * power) + (power * 2F)) / 3F;

        if (wurst.mods.fastBowMod.isActive())
            power = 1;

        if (power < 0.1D)
            return;
        if (power > 1.0F)
            power = 1.0F;

        float distance = MathHelper.sqrt_double((motionX * motionX) + 
        	(motionY * motionY) + (motionZ * motionZ));
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;

        motionX *= (power * 2) * 1.5;
        motionY *= (power * 2) * 1.5;
        motionZ *= (power * 2) * 1.5;

        MovingObjectPosition landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;
        float gravity = 0.05F;
        float size = 0.3F;

        for (int limit = 0; !hasLanded && (limit < 300); limit++) 
        {
            Vec3 posBefore = new Vec3(posX, posY, posZ);
            Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            landingPosition = mc.theWorld.rayTraceBlocks(posBefore, posAfter, false, true, false);
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

            if (landingPosition != null) 
            {
                hasLanded = true;
                posAfter = new Vec3(landingPosition.hitVec.xCoord, 
                	landingPosition.hitVec.yCoord, landingPosition.hitVec.zCoord);
            }

            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, 
            	posY - size, posZ - size, posX + size, posY + size, posZ + size);
            List<Entity> entityList = getEntitiesWithinAABB(arrowBox.addCoord(motionX,
            	motionY, motionZ).expand(1.0D, 1.0D, 1.0D));

            int i;
            for (i = 0; i < entityList.size(); i++) 
            {
                Entity possibleEntity = entityList.get(i);
                if (possibleEntity.canBeCollidedWith() && (possibleEntity != thePlayer || limit >= 5)) 
                {
                    AxisAlignedBB possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox();
                    MovingObjectPosition possibleEntityLanding = 
                    	possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter);
                    if (possibleEntityLanding != null) 
                    {
                        hitEntity = true;
                        hasLanded = true;
                        landingPosition = possibleEntityLanding;
                    }
                }

            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            BlockPos var18 = new BlockPos(posX, posY, posZ);
            Block var2 = mc.theWorld.getBlockState(var18).getBlock();

            if (var2.getMaterial() == Material.water) 
            {
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            } else 
            {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY -= gravity;
        }
        if (landingPosition != null)
            if (hitEntity)
                shouldRelease = true;
    }

    private List<Entity> getEntitiesWithinAABB(AxisAlignedBB axisalignedBB) 
    {
        List<Entity> list = new ArrayList<Entity>();
        int chunkMinX = MathHelper.floor_double((axisalignedBB.minX - 2.0D) / 16.0D);
        int chunkMaxX = MathHelper.floor_double((axisalignedBB.maxX + 2.0D) / 16.0D);
        int chunkMinZ = MathHelper.floor_double((axisalignedBB.minZ - 2.0D) / 16.0D);
        int chunkMaxZ = MathHelper.floor_double((axisalignedBB.maxZ + 2.0D) / 16.0D);

        for (int x = chunkMinX; x <= chunkMaxX; ++x)
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z)
                if (mc.theWorld.getChunkProvider().chunkExists(x, z))
                    mc.theWorld.getChunkFromChunkCoords(x, z).func_177414_a(
                    	mc.thePlayer, axisalignedBB, list, IEntitySelector.field_180132_d);

        return list;
    }
    
    @Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
