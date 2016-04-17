/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.RENDER,
	description = "Shows the fired arrows of other players.",
	name = "ArrowTrajectories")
public class ArrowTrajectoriesMod extends Mod implements RenderListener
{
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
	    Object mpplayer;
	    double arrowx;
	    double arrowy;
	    double arrowz;
	    double yaw1;
	    for (Object player : mc.theWorld.playerEntities)
	    {
	      if ((player instanceof EntityPlayer))
	      {
	        mpplayer = (EntityPlayer)player;
	        if ((mpplayer != mc.thePlayer) && (((EntityPlayer)mpplayer).getHeldItem() != null))
	        {
	          Item localItem = ((EntityPlayer)mpplayer).getHeldItem().getItem();
	          if ((localItem instanceof ItemBow))
	          {
	            float useduration = ((EntityPlayer)mpplayer).getItemInUseDuration() / 20.0F;
	            useduration = (useduration * useduration + useduration * 2.0F) / 3.0F;
	            if (useduration >= 0.1D)
	            {
	              if (useduration > 1.0F)
	            	  useduration = 1.0F;
	              float dur3 = useduration * 3.0F;
	              arrowx = ((EntityPlayer)mpplayer).posX - MathHelper.cos(((EntityPlayer)mpplayer).
	            	  rotationYaw / 180.0F * 3.1415927F) * 0.16F;
	              arrowy = ((EntityPlayer)mpplayer).posY + ((EntityPlayer)mpplayer).getEyeHeight() - 
	            	  0.10000000149011612D;
	              arrowz= ((EntityPlayer)mpplayer).posZ - MathHelper.sin(((EntityPlayer)mpplayer).
	            	  rotationYaw / 180.0F * 3.1415927F) * 0.16F;
	              yaw1 = -MathHelper.sin(((EntityPlayer)mpplayer).rotationYaw / 180.0F * 3.1415927F) * 
	            	  MathHelper.cos(((EntityPlayer)mpplayer).rotationPitch / 180.0F * 3.1415927F);
	              double pitch = -MathHelper.sin(((EntityPlayer)mpplayer).rotationPitch / 180.0F * 3.1415927F);
	              double yaw2 = MathHelper.cos(((EntityPlayer)mpplayer).rotationYaw / 180.0F * 3.1415927F) * 
	            	  MathHelper.cos(((EntityPlayer)mpplayer).rotationPitch / 180.0F * 3.1415927F);
	              float total = MathHelper.sin((float)(yaw1 * yaw1 + pitch * pitch + yaw2 * yaw2));
	              yaw1 /= total;
	              pitch /= total;
	              yaw2 /= total;
	              yaw1 *= dur3;
	              pitch *= dur3;
	              yaw2 *= dur3;
	              MovingObjectPosition movingobject = null;
	              int j = 0;
	              //GL Settings
	            GL11.glPushMatrix();
	      		GL11.glEnable(GL11.GL_LINE_SMOOTH);
	      		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	      		GL11.glEnable(GL11.GL_BLEND);
	      		GL11.glDisable(GL11.GL_TEXTURE_2D);
	      		GL11.glDisable(GL11.GL_DEPTH_TEST);
	      		GL11.glEnable(GL13.GL_MULTISAMPLE);
	      		GL11.glDepthMask(false);
	      		GL11.glLineWidth(1.8F);
	              for (int m = 0; (j == 0) && (m < 300); m++)
	              {
	                Vec3 arrowvec = new Vec3(arrowx, arrowy, arrowz);
	                Vec3 arrowyaw = new Vec3(arrowx + yaw1, arrowy + pitch, arrowz+ yaw2);
	                movingobject = mc.theWorld.rayTraceBlocks(arrowvec, arrowyaw, false, true, false);
	                if (movingobject != null)
	                  j = 1;
	                arrowx += yaw1;
	                arrowy += pitch;
	                arrowz+= yaw2;
	                BlockPos localBlockPos2 = new BlockPos(arrowx, arrowy, arrowz);
	                Block localBlock2 = mc.theWorld.getBlockState(localBlockPos2).getBlock();
	                if (localBlock2.getMaterial() == Material.water)
	                {
	                  yaw1 *= 0.6D;
	                  pitch *= 0.6D;
	                  yaw2 *= 0.6D;
	                }
	                else
	                {
	                  yaw1 *= 0.99D;
	                  pitch *= 0.99D;
	                  yaw2 *= 0.99D;
	                }
	                pitch -= 0.05000000074505806D;
	                GL11.glColor3d(0, 1, 0);
	        		GL11.glBegin(GL11.GL_LINE_STRIP);
	                GL11.glVertex3d(arrowx - mc.getRenderManager().renderPosX, arrowy - mc.getRenderManager().renderPosY, arrowz- mc.getRenderManager().renderPosZ);
	              }
	            GL11.glEnd();
	            GL11.glDisable(GL11.GL_BLEND);
	      		GL11.glEnable(GL11.GL_TEXTURE_2D);
	      		GL11.glEnable(GL11.GL_DEPTH_TEST);
	      		GL11.glDisable(GL13.GL_MULTISAMPLE);
	      		GL11.glDepthMask(true);
	      		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	      		GL11.glPopMatrix();
	            }
	          }
	        }
	      }
	    }
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
	}
}