package tk.wurst_client.events;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockBreakingEvent extends Event
{
  private EnumBlock state;
  private BlockPos pos;
  private EnumFacing side;
  
  public BlockBreakingEvent(EnumBlock state, BlockPos pos, EnumFacing side)
  {
    this.side = side;
    this.state = state;
    this.pos = pos;
  }
  
  public void setState(EnumBlock state)
  {
    this.state = state;
  }
  
  public void setPos(BlockPos pos)
  {
    this.pos = pos;
  }
  
  public void setSide(EnumFacing side)
  {
    this.side = side;
  }
  
  public BlockPos getPos()
  {
    return pos;
  }
  
  public EnumBlock getState()
  {
    return state;
  }
  
  public EnumFacing getSide()
  {
    return side;
  }
  
  public static enum EnumBlock
  {	
	  CLICK("CLICK", 0),
      DAMAGE("DAMAGE", 1),
      DESTROY("DESTROY", 2);
    
    private EnumBlock(String var1, int var2) {}
  }

  @Override
  public String getAction()
  {
	return "Breaking block";
  }
}