package tk.wurst_client.events;

import net.minecraft.network.Packet;

public class PacketOutputEvent extends CancellableEvent 
{
   private Packet packet;


   public PacketOutputEvent(Packet packet) {
      this.packet = packet;
   }

   public Packet getPacket() {
      return this.packet;
   }

   public void setPacket(Packet packet) {
      this.packet = packet;
   }

@Override
public String getAction()
{
	return "Sending Packet";
}
}
