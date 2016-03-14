package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.SliderSetting;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
//Credit to null-dev
@Info(category = Category.COMBAT,
        description = "Spams low power arrows.\n" + "Tip: This works with BowAimbot.",
        name = "BowSpam",
        noCheatCompatible = false)
public class BowSpamMod extends Mod implements UpdateListener {
	public int iters = 20;
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("BowSpam Delay", iters, 1, 20, 1,
			ValueDisplay.INTEGER)
		{
			@Override
			public void update()
			{
				iters = (int)getValue();
			}
		});
	}
    @Override
    public void onEnable() {
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        if (Minecraft.getMinecraft().thePlayer.getHealth() > 0 && (Minecraft.getMinecraft().thePlayer.onGround ||
                Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) &&
                Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null &&
                Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow &&
                Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed) {
            new Thread(() -> {
                Minecraft.getMinecraft().playerController
                        .sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld,
                                Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
                Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem()
                        .onItemRightClick(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
                                Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer);
      
                
       //         for (int i = 0; i < iters; i++) {
      //              try {
                    	
       //             }
               //         Thread.sleep(getInt(iters, 10));
                //   } catch (InterruptedException ignored) {
             //       }
                //    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
      //          }
                
                if(hasTimePassedS(iters)) {
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(
                        new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                new BlockPos(0, 0, 0), EnumFacing.DOWN));
                Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem()
                        .onPlayerStoppedUsing(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
                                Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, 10);
                }
            }).start();}
        }
   // }


   // public int getInt(String key, int defaultValue) {
     //   String s = String.valueOf(defaultValue);
       // String ret = getString(key, s);
      //  if (!ret.equals(s)) {
        //    try {
        //        return Integer.parseInt(ret);
         //   } catch (NumberFormatException e) {
          //      return defaultValue;
         //   }
       // }
       // return defaultValue;
	@Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
    }
}