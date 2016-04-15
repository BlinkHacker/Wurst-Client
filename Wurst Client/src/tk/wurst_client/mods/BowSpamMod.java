package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.SliderSetting;
//Credit to null-dev
@Info(category = Category.COMBAT,
        description = "Spams low power arrows.\n" + "Tip: This works with BowAimbot.",
        name = "BowSpam",
        noCheatCompatible = false)
public class BowSpamMod extends Mod implements UpdateListener 
{
    public long delay = 20;

    @Override
    public void initSettings()
    {
        settings.add(new SliderSetting("Delay", delay, 1, 20, 1,
            ValueDisplay.INTEGER)
        {
            @Override
            public void update()
            {
                delay = (long)getValue();
            }
        });
    }
    @Override
    public void onEnable()
    {
        wurst.events.add(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() 
    {
        if (mc.thePlayer.getHealth() > 0 && (mc.thePlayer.onGround ||
                mc.thePlayer.capabilities.isCreativeMode) &&
                mc.thePlayer.inventory.getCurrentItem() != null &&
                mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow &&
                mc.gameSettings.keyBindUseItem.pressed) 
        {
            new Thread(() -> 
            {
                mc.playerController
                        .sendUseItem(mc.thePlayer, mc.theWorld,
                                mc.thePlayer.inventory.getCurrentItem());
                mc.thePlayer.inventory.getCurrentItem().getItem()
                        .onItemRightClick(mc.thePlayer.inventory.getCurrentItem(),
                                mc.theWorld, mc.thePlayer);


                for (int i = 0; i < delay; i++)
                   try {
                        Thread.sleep(delay,10);
                   } catch (InterruptedException ignored) {}
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));


                mc.getNetHandler().addToSendQueue(
                        new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                new BlockPos(0, 0, 0), EnumFacing.DOWN));
                mc.thePlayer.inventory.getCurrentItem().getItem()
                        .onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(),
                                mc.theWorld, mc.thePlayer, 10);

            }).start();
        }
    }

    @Override
    public void onDisable() 
    {
        wurst.events.remove(UpdateListener.class, this);
    }
}