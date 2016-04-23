/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.events;

import net.minecraft.network.Packet;

public class PacketOutputEvent extends CancellableEvent 
{
   private Packet packet;


   public PacketOutputEvent(Packet packet) 
   {
      this.packet = packet;
   }

   public Packet getPacket() 
   {
      return this.packet;
   }

   public void setPacket(Packet packet) 
   {
      this.packet = packet;
   }

   @Override
   public String getAction()
   {
	   return "Sending Packet";
   }
}
