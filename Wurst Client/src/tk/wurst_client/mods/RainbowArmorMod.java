/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;

@Info(category = Category.FUN,
	description = "Rapidly switches the items in your armor slots,\n"
		+ "making it look like you have rainbow armor.\n"
		+ "Creative mode only.",
	name = "RainbowArmor")
public class RainbowArmorMod extends Mod implements UpdateListener
{
	public CheckboxSetting headderp = new CheckboxSetting(
		"Use Blocks for Head Slot", true);
	public CheckboxSetting possibleblocks = new CheckboxSetting(
		"Don't Use Blocks Not in Creative Inventory", false);
	public CheckboxSetting restrictedblocks = new CheckboxSetting(
		"Disable Blocks Normally Blocked", false);
	public CheckboxSetting armorderp = new CheckboxSetting(
		"Rainbow Leather Armor", false);
	
	@Override
	public void initSettings()
	{
		settings.add(headderp);
		settings.add(possibleblocks);
		settings.add(restrictedblocks);
		settings.add(armorderp);
	}
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if (!mc.playerController.isInCreativeMode() || mc.theWorld == null)
		{
			wurst.chat.error("Creative mode only.");
			setEnabled(false);
		}
		if(!possibleblocks.isChecked() && mc.playerController.isInCreativeMode() &&
			headderp.isChecked())
		{
		ItemStack block = getRandomBlock();
	      if (Item.getIdFromItem(block.getItem()) != 0 &&
	    	  Item.getIdFromItem(block.getItem()) != 86)
	        {
	          mc.thePlayer.inventoryContainer.putStackInSlot(5, block);
	          mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, block));
	        }
		}
		if(possibleblocks.isChecked() && mc.playerController.isInCreativeMode() &&
			headderp.isChecked())
		{
			ItemStack block = getRandomBlock();
			boolean restricted = Item.getIdFromItem(block.getItem()) == 46 ||
				Item.getIdFromItem(block.getItem()) == 97 && restrictedblocks.isChecked();
			if (Item.getIdFromItem(block.getItem()) != 0 &&
		    	  Item.getIdFromItem(block.getItem()) != 86 &&
		    	  Item.getIdFromItem(block.getItem()) != 52 &&
		    	  Item.getIdFromItem(block.getItem()) != 99 &&
		    	  Item.getIdFromItem(block.getItem()) != 100 &&
		    	  Item.getIdFromItem(block.getItem()) != 137 &&
		    	  Item.getIdFromItem(block.getItem()) != 166 &&
				  Item.getIdFromItem(block.getItem()) != 122 &&
				  Item.getIdFromItem(block.getItem()) != 60 &&
				  Item.getIdFromItem(block.getItem()) != 62 &&
				  Item.getIdFromItem(block.getItem()) != 31 && !restricted)
		        {
		          mc.thePlayer.inventoryContainer.putStackInSlot(5, block);
		          mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, block));
		        }
		}
		if(armorderp.isChecked() && mc.playerController.isInCreativeMode())
		{
			ItemStack helm = new ItemStack(Items.leather_helmet);
			ItemStack chest = new ItemStack(Items.leather_chestplate);
			ItemStack legs = new ItemStack(Items.leather_leggings);
			ItemStack feet = new ItemStack(Items.leather_boots);
			int color = (int)Math.round(Math.random() * 16777215);
			String nbt = "{display: {color:" + color + "}}";
			try
			{
				chest.setTagCompound(JsonToNBT.func_180713_a(nbt));
				legs.setTagCompound(JsonToNBT.func_180713_a(nbt));
				feet.setTagCompound(JsonToNBT.func_180713_a(nbt));
				if(!headderp.isChecked())
					helm.setTagCompound(JsonToNBT.func_180713_a(nbt));	
			}catch(NBTException e)
			{
				wurst.chat.error("An error occured while trying to generate item.");
			}
			 mc.thePlayer.inventoryContainer.putStackInSlot(6, chest);
			 mc.thePlayer.inventoryContainer.putStackInSlot(7, legs);
			 mc.thePlayer.inventoryContainer.putStackInSlot(8, feet);
			 mc.thePlayer.sendQueue
				.addToSendQueue(new C10PacketCreativeInventoryAction(
					6, chest));
			 mc.thePlayer.sendQueue
				.addToSendQueue(new C10PacketCreativeInventoryAction(
					7, legs));
			 mc.thePlayer.sendQueue
				.addToSendQueue(new C10PacketCreativeInventoryAction(
					8, feet));
			 if(!headderp.isChecked())
			 {
				 mc.thePlayer.inventoryContainer.putStackInSlot(5, helm);
				 mc.thePlayer.sendQueue
					.addToSendQueue(new C10PacketCreativeInventoryAction(
						5, helm));
			 }
		}
	}
	
	private ItemStack getRandomBlock()
	{
		Random random = new Random();
	    ArrayList<Block> blockreg = Lists.newArrayList(Block.blockRegistry.iterator());
	    int i = 0;
	    int size = blockreg.size() - 1;
	    int randblock = random.nextInt(size - i) + i;
	    return new ItemStack(blockreg.get(randblock));
	}
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
