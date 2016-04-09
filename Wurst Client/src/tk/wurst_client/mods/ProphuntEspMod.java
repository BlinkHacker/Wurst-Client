/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see fake blocks in Prophunt.",
	name = "ProphuntESP")
public class ProphuntEspMod extends Mod implements RenderListener, PacketInputListener
{
	private ArrayList<BlockPos> changed = new ArrayList<BlockPos>();
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.prophuntAuraMod,
			wurst.mods.blockChangeEspMod};
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
		wurst.events.add(PacketInputListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		updateMS();
		if(hasTimePassedM(2000))
		{
	    	changed.clear();
	    	updateLastMS();
		}
		for(Object entity : mc.theWorld.loadedEntityList)
			if(entity instanceof EntityFallingBlock)
			{
				double x = ((Entity)entity).posX;
				double y = ((Entity)entity).posY;
				double z = ((Entity)entity).posZ;
				Color color;
				if(mc.thePlayer.getDistanceToEntity((Entity)entity) >= 0.5)
					color =
						new Color(1F, 0F, 0F, 0.5F - MathHelper.abs(MathHelper
							.sin(Minecraft.getSystemTime() % 1000L / 1000.0F
								* (float)Math.PI * 1.0F) * 0.3F));
				else
					color = new Color(0, 0, 0, 0);
				RenderUtils.box(x - 0.5, y - 0.1, z - 0.5, x + 0.5, y + 0.9,
					z + 0.5, color);
			}
		for(BlockPos blocks : changed)
		{
				double xb = blocks.getX();
				double yb = blocks.getY();
				double zb = blocks.getZ();
				Color colorb;
				if(mc.thePlayer.getDistance(xb, yb, zb) >= 0.5)
					colorb =
						new Color(1F, 0F, 0F, 0.3F - MathHelper.abs(MathHelper
							.sin(Minecraft.getSystemTime() % 1000L / 1000.0F
								* (float)Math.PI * 1.0F) * 0.3F));
				else
					colorb = new Color(0, 0, 0, 0);
				RenderUtils.genericBox(blocks, colorb, new Color(0, 0, 0, 0.5F));
		}
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		PacketInputEvent receive = (PacketInputEvent)event;
	    if(receive.getPacket() instanceof S23PacketBlockChange) {
	       S23PacketBlockChange blockchange = (S23PacketBlockChange)receive.getPacket();
	       changed.add(blockchange.func_179827_b());
	    }
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
		wurst.events.remove(PacketInputListener.class, this);
	}
}
