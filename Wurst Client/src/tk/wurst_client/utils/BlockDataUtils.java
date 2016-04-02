/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.utils;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockDataUtils
{
  public BlockPos position;
  public EnumFacing face;
  
  public BlockDataUtils(BlockPos position, EnumFacing face)
  {
    this.position = position;
    this.face = face;
  }
}
