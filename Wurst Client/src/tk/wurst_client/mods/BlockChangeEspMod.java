/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see blocks that have changed.\n"
		+ "If more than 400 blocks have changed, the blocks reset.\n"
		+ "Very good for PropHunt and tracking far players.",
	name = "BlockChangeESP")
public class BlockChangeEspMod extends Mod implements RenderListener, PacketInputListener
{
	private ArrayList<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
	private int maxblocks = 400;
	public boolean notify = true;
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.prophuntEspMod};
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
		wurst.events.add(PacketInputListener.class, this);
	}

	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		PacketInputEvent receive = (PacketInputEvent)event;
		    if(receive.getPacket() instanceof S23PacketBlockChange) {
		       S23PacketBlockChange blockchange = (S23PacketBlockChange)receive.getPacket();
		       matchingBlocks.add(blockchange.func_179827_b());
					if(matchingBlocks.size() >= maxblocks && notify)
					{
						wurst.chat.warning(getName() + " found over " + maxblocks + " blocks.");
						wurst.chat
							.message("To prevent lag, the blocks highlighted have been reset.");
						matchingBlocks.clear();
						notify = false;
					}else if(matchingBlocks.size() < maxblocks)
						notify = true;
		     }
	}

	@Override
	public void onRender()
	{
			for(BlockPos blockPos : matchingBlocks)
				RenderUtils.blockChangeBox(blockPos);    
			if(wurst.commands.blockChangeCmd.clearblocks)
			{
				matchingBlocks.clear();
			wurst.commands.blockChangeCmd.clearblocks = false;
			}
	}
	
	@Override
	public void onDisable()
	{
		matchingBlocks.clear();
		wurst.events.remove(RenderListener.class, this);
		wurst.events.remove(PacketInputListener.class, this);
	}
	
}