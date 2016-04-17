/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.events;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class BlockBBEvent extends Event 
{

   private int x;
   private int y;
   private int z;
   private final Block block;
   private AxisAlignedBB bb;


   public BlockBBEvent(int x, int y, int z, Block block, AxisAlignedBB bb) 
   {
      this.bb = bb;
      this.x = x;
      this.y = y;
      this.z = z;
      this.block = block;
   }

   public AxisAlignedBB getBoundingBox() 
   {
      return bb;
   }

   public int getX() 
   {
      return x;
   }

   public int getY() 
   {
      return y;
   }

   public int getZ() 
   {
      return z;
   }

   public Block getBlock() 
   {
      return block;
   }

   public void setBoundingBox(AxisAlignedBB bb) 
   {
      this.bb = bb;
   }

   @Override
   public String getAction()
   {
	   return "setting boundingBox";
   }
}
