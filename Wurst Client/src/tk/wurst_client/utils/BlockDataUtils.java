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
