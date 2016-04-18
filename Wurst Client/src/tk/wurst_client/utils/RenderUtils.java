/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import org.darkstorm.minecraft.gui.util.RenderUtil;

public class RenderUtils
{
	/**
	 * Renders a box with any size and any color.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param color
	 */
	public static void box(double x, double y, double z, double x2, double y2,
		double z2, Color color)
	{
		x = x - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y = y - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z = z - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		x2 = x2 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y2 = y2 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z2 = z2 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		RenderUtil.setColor(color);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
		glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x2, y2,
			z2), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	/**
	 * Renders a frame with any size and any color.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param color
	 */
	public static void frame(double x, double y, double z, double x2,
		double y2, double z2, Color color)
	{
		x = x - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y = y - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z = z - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		x2 = x2 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y2 = y2 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z2 = z2 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		RenderUtil.setColor(color);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x2, y2,
			z2), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	/**
	 * Renders an ESP box with the size of a normal block at the specified
	 * coordinates.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void blockESPBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(1.0F);
		glColor4d(0, 1, 0, 0.15F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void framelessBlockESP(BlockPos blockPos, Color color)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glColor4d(color.getRed() / 255, color.getGreen() / 255,
			color.getBlue() / 255, 0.15);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void emptyBlockESPBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static int enemy = 0;
	public static int friend = 1;
	public static int other = 2;
	public static int target = 3;
	public static int team = 4;
	
	public static void entityESPBox(Entity entity, int mode)
	{
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		if(mode == 0)// Enemy
			glColor4d(
				1 - Minecraft.getMinecraft().thePlayer
					.getDistanceToEntity(entity) / 40,
				Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) / 40,
				0, 0.5F);
		else if(mode == 1)// Friend
			glColor4d(0, 0, 1, 0.5F);
		else if(mode == 2)// Other
			glColor4d(1, 1, 0, 0.5F);
		else if(mode == 3)// Target
			glColor4d(1, 0, 0, 0.5F);
		else if(mode == 4)// Team
			glColor4d(0, 1, 0, 0.5F);
		Minecraft.getMinecraft().getRenderManager();
		RenderGlobal.drawOutlinedBoundingBox(
			new AxisAlignedBB(
				entity.boundingBox.minX
					- 0.05
					- entity.posX
					+ (entity.posX - Minecraft.getMinecraft()
						.getRenderManager().renderPosX),
				entity.boundingBox.minY
					- entity.posY
					+ (entity.posY - Minecraft.getMinecraft()
						.getRenderManager().renderPosY),
				entity.boundingBox.minZ
					- 0.05
					- entity.posZ
					+ (entity.posZ - Minecraft.getMinecraft()
						.getRenderManager().renderPosZ),
				entity.boundingBox.maxX
					+ 0.05
					- entity.posX
					+ (entity.posX - Minecraft.getMinecraft()
						.getRenderManager().renderPosX),
				entity.boundingBox.maxY
					+ 0.1
					- entity.posY
					+ (entity.posY - Minecraft.getMinecraft()
						.getRenderManager().renderPosY),
				entity.boundingBox.maxZ
					+ 0.05
					- entity.posZ
					+ (entity.posZ - Minecraft.getMinecraft()
						.getRenderManager().renderPosZ)), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void nukerBox(BlockPos blockPos, float damage)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(1.0F);
		glColor4d(damage, 1 - damage, 0, 0.15F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x + 0.5 - damage / 2, y + 0.5 - damage
			/ 2, z + 0.5 - damage / 2, x + 0.5 + damage / 2, y + 0.5 + damage
			/ 2, z + 0.5 + damage / 2));
		glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x + 0.5 - damage
			/ 2, y + 0.5 - damage / 2, z + 0.5 - damage / 2, x + 0.5 + damage
			/ 2, y + 0.5 + damage / 2, z + 0.5 + damage / 2), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void searchBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(1.0F);
		float sinus =
			1F - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime()
				% 10000L / 10000.0F * (float)Math.PI * 4.0F) * 1F);
		glColor4d(1 - sinus, sinus, 0, 0.15);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		glColor4d(0, 0, 0, 0.5);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void blockChangeBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(1.0F);
		glColor4d(0.25, 0.25, 1, 0.15F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		glColor4d(0.10, 0.10, 1, 0.15F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void genericBox(BlockPos blockPos, Color color, Color outline)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(1.0F);
		RenderUtil.setColor(color);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		RenderUtil.setColor(outline);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void drawColorBox(AxisAlignedBB axisalignedbb)
	{
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer wr = ts.getWorldRenderer();
		wr.startDrawingQuads();// Starts X.
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		ts.draw();// Ends X.
		wr.startDrawingQuads();// Starts Y.
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		ts.draw();// Ends Y.
		wr.startDrawingQuads();// Starts Z.
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		ts.draw();// Ends Z.
	}
	
	public static void tracerLine(Entity entity, int mode)
	{
		double x =
			entity.posX
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			entity.posY + entity.height / 2
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			entity.posZ
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		if(mode == 0)// Enemy
			glColor4d(
				1 - Minecraft.getMinecraft().thePlayer
					.getDistanceToEntity(entity) / 40,
				Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) / 40,
				0, 0.5F);
		else if(mode == 1)// Friend
			glColor4d(0, 0, 1, 0.5F);
		else if(mode == 2)// Other
			glColor4d(1, 1, 0, 0.5F);
		else if(mode == 3)// Target
			glColor4d(1, 0, 0, 0.5F);
		else if(mode == 4)// Team
			glColor4d(0, 1, 0, 0.5F);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void tracerLine(Entity entity, Color color)
	{
		double x =
			entity.posX
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			entity.posY + entity.height / 2
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			entity.posZ
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		RenderUtil.setColor(color);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void tracerLine(int x, int y, int z, Color color)
	{
		x += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		RenderUtil.setColor(color);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void line(Vec3 from, Vec3 to, Color color, float w, boolean opacity) 
	{
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer t = ts.getWorldRenderer();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glLineWidth(w);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_LINE_SMOOTH);
		if(opacity)
		{
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		}

		RenderUtil.setColor(color);

		double[] pf = RenderUtil.renderPos(from);
		double[] pt = RenderUtil.renderPos(to);
		
		t.startDrawing(1);
		t.addVertex(pf[0], pf[1], pf[2]);
		t.addVertex(pt[0], pt[1], pt[2]);
		ts.draw();
		
		glEnable(GL_TEXTURE_2D);
		if(opacity)
		{
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		}
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_BLEND);
	}
	
	public static void enableTextures(boolean texture)
	{
	    if (texture)
	    {
	      glDepthMask(false);
	      glDisable(GL_DEPTH_TEST);
	    }
	    glDisable(GL_ALPHA_TEST);
	    glEnable(GL_BLEND);
	    glDisable(GL_TEXTURE_2D);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);   
	    glEnable(GL_LINE_SMOOTH);
	    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	    glLineWidth(1.0F);
	}
	  
	public static void disableTextures(boolean texture)
	{
		if (texture)
	    {
	      glDepthMask(true);
	      glEnable(GL_DEPTH_TEST);
	    }
	    glEnable(GL_TEXTURE_2D);
	    glDisable(GL_BLEND);
	    glEnable(GL_ALPHA_TEST);
	    glDisable(GL_LINE_SMOOTH);
	    glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	  
	public static void prophuntBox(BlockPos blockPos)
	{
		Color color;
		color =
		new Color(1F, 0F, 0F, 0.5F - MathHelper.abs(MathHelper
			.sin(Minecraft.getSystemTime() % 1000L / 1000.0F
				* (float)Math.PI * 1.0F) * 0.3F));
			double x =
				blockPos.getX()
					- Minecraft.getMinecraft().getRenderManager().renderPosX;
			double y =
				blockPos.getY()
					- Minecraft.getMinecraft().getRenderManager().renderPosY;
			double z =
				blockPos.getZ()
					- Minecraft.getMinecraft().getRenderManager().renderPosZ;
			RenderUtil.setColor(color);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_BLEND);
			glLineWidth(1.0F);
			glColor4d(1, 0, 0, 0.15F);
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);
			glDepthMask(false);
			drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
			RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
				x + 1.0, y + 1.0, z + 1.0), -1);
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_DEPTH_TEST);
			glDepthMask(true);
			glDisable(GL_BLEND);
	}
	
	public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel)
	{
	    float var7 = 0.00390625F;
	    float var8 = 0.00390625F;
	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	    worldRenderer.startDrawingQuads();
	    worldRenderer.addVertexWithUV(x + 0, y + height, zLevel, (u + 0) * var7, (v + height) * var8);
	    worldRenderer.addVertexWithUV(x + width, y + height, zLevel, (u + width) * var7, (v + height) * var8);
	    worldRenderer.addVertexWithUV(x + width, y + 0, zLevel, (u + width) * var7, (v + 0) * var8);
		worldRenderer.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) * var7, (v + 0) * var8);
		tessellator.draw();
	}
	
	public static void renderQuad(Tessellator tessellator, int x, int y, int width, int height, int color)
	{
	    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	    worldRenderer.startDrawingQuads();
	    worldRenderer.setColorOpaque_I(color);
	    worldRenderer.addVertex(x + 0, y + 0, 0.0D);
	    worldRenderer.addVertex(x + 0, y + height, 0.0D);
	    worldRenderer.addVertex(x + width, y + height, 0.0D);
	    worldRenderer.addVertex(x + width, y + 0, 0.0D);
		tessellator.draw();
	}
	
	public static void renderTag(String string, Entity entity, double sizetag, int color, 
		double height, boolean disabledepth)
	{
	    RenderManager RenderManager = Minecraft.getMinecraft().getRenderManager();
	    FontRenderer FontRenderer = Minecraft.getMinecraft().fontRendererObj;
	    int width = FontRenderer.getStringWidth(string);
	    double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
	    glPushMatrix();
	    enableTextures(disabledepth);
	    double[] arrayOfDouble = EntityUtils.EntityPos(entity);
	    double renderX = arrayOfDouble[0] - Minecraft.getMinecraft().getRenderManager().renderPosX;
	    double renderY = arrayOfDouble[1] - Minecraft.getMinecraft().getRenderManager().renderPosY + entity.height + height;
	    double renderZ = arrayOfDouble[2] - Minecraft.getMinecraft().getRenderManager().renderPosZ;
	    glTranslated(renderX, renderY, renderZ);
	    glNormal3f(0.0F, 1.0F, 0.0F);
	    glScaled(-0.027D, -0.027D, 0.027D);
	    double size = 10.0D - sizetag;
	    if (dist > size) {
	      glScaled(dist / size, dist / size, dist / size);
	    }
	    double posX = entity.posX;
	    double posY = entity.posY;
	    double posZ = entity.posZ;
		double distX = posX - Minecraft.getMinecraft().thePlayer.posX;
	  	double distY = posY - Minecraft.getMinecraft().thePlayer.posY;
	  	double distZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
	 	float yaw = (float)(Math.atan2(distZ, distX) * 180.0D / Math.PI) - 90.0F;
	 	float pitch = (float)-(Math.atan2(distY, dist) * 180.0D / Math.PI);
	  	float wrapyaw = Math.abs(MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw));
	  	float wrappitch = Math.abs(MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch));
	  	float sqrt = (float)((75.0D - Math.sqrt(wrapyaw * wrapyaw + wrappitch * wrappitch)) / 50.0D);
	  	if (sqrt > 1.0F)
	    	sqrt = 1.0F;
	      if (sqrt < 0.0F)
	        sqrt = 0.0F;
	    glScaled(sqrt, sqrt, sqrt);
	    glRotatef(RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	    glRotatef(-RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	    glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
	    glBegin(GL_QUADS);
	    glVertex3d(-width / 2 - 2, -2.0D, 0.0D);
	    glVertex3d(-width / 2 - 2, 9.0D, 0.0D);
	    glVertex3d(width / 2 + 2, 9.0D, 0.0D);
	    glVertex3d(width / 2 + 2, -2.0D, 0.0D);
	    glEnd();
	    glColor3f(1.0F, 1.0F, 1.0F);
	    glEnable(GL_TEXTURE_2D);
	    FontRenderer.drawString(string, -width / 2, 0, color);
	    glDisable(GL_TEXTURE_2D);
	    disableTextures(disabledepth);
		glPopMatrix();
	}
	
	public static void drawLines(AxisAlignedBB axisalignedbb) 
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawing(2);
		worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.draw();
	 }
}
