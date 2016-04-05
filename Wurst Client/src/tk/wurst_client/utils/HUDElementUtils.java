/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class HUDElementUtils
{
  public ItemStack itemStack;
  public int iconW;
  public int iconH;
  public int padW;
  private int elementW;
  private int elementH;
  private String itemName = "";
  private int itemNameW;
  private String itemDamage = "";
  private int itemDamageW;
  private boolean isArmor;
  
  public HUDElementUtils(ItemStack itemStack, int iconW, int iconH, int padW, boolean isArmor)
  {
    this.itemStack = itemStack;
    this.iconW = iconW;
    this.iconH = iconH;
    this.padW = padW;
    this.isArmor = isArmor;
    
    initSize();
  }
  
  public int width()
  {
    return elementW;
  }
  
  public int height()
  {
    return elementH;
  }

  private void initSize()
  {
    elementH = (Math.max(Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * 2, iconH));
    if (itemStack != null)
    {
      int damage = 1;
      int maxDamage = 1;
      int percentdamage = 1;
      if ((isArmor) || ((!isArmor) && (itemStack.isItemDamaged())))
      {
        maxDamage = itemStack.getMaxDamage() + 1;
        damage = maxDamage - this.itemStack.getItemDamage();
        percentdamage = damage * 100 / maxDamage;
        itemDamage = ("§" + getColorCode(percentdamage) + damage + "/" + maxDamage);
      }
      itemDamageW = Minecraft.getMinecraft().fontRendererObj.getStringWidth
    	  (stripCtrl(itemDamage));
      elementW = (padW + iconW + padW + itemDamageW);
      itemName = itemStack.getDisplayName();
      elementW = (padW + iconW + padW + Math.max(Minecraft.getMinecraft()
    	  .fontRendererObj.getStringWidth(
        	(stripCtrl(itemName))), itemDamageW));
      itemNameW = Minecraft.getMinecraft().fontRendererObj.getStringWidth(stripCtrl(itemName));
    }
  }
  
  public void renderToHud(int x, int y)
  {
    RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, 
    	GL11.GL_NONE);
    RenderHelper.enableGUIStandardItemLighting();
    itemRenderer.zLevel = 200.0F;
      itemRenderer.func_180450_b(itemStack, x - (iconW + padW), y);
      renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, itemStack, x - 
    	  (iconW + padW), y, true, true);
      
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(itemName + "§r", x - (padW + 
    	  iconW + padW) - itemNameW, y, 16777215);
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(itemDamage + "§r", x - (padW + 
    	  iconW + padW) - itemDamageW, y + (elementH / 2), 16777215);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public String stripCtrl(String string)
  {
    return string.replaceAll("(?i)§[0-9a-fklmnor]", "");
  }
  
  public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y, boolean showDamageBar, boolean showCount)
  {
    if ((itemStack != null) && ((showDamageBar) || (showCount)))
    {
      if ((itemStack.isItemDamaged()) && (showDamageBar))
      {
        int var11 = (int)Math.round(13.0D - itemStack.getItemDamage() * 13.0D / itemStack.getMaxDamage());
        int var7 = (int)Math.round(255.0D - itemStack.getItemDamage() * 255.0D / itemStack.getMaxDamage());
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();;
        GlStateManager.func_179090_x();
        GlStateManager.disableAlpha();;
        GlStateManager.disableBlend();;
        Tessellator var8 = Tessellator.getInstance();
        int var9 = 255 - var7 << 16 | var7 << 8;
        int var10 = (255 - var7) / 4 << 16 | 0x3F00;
        RenderUtils.renderQuad(var8, x + 2, y + 13, 13, 2, 0);
        RenderUtils.renderQuad(var8, x + 2, y + 13, 12, 1, var10);
        RenderUtils.renderQuad(var8, x + 2, y + 13, var11, 1, var9);
        GlStateManager.enableAlpha();
        GlStateManager.func_179098_w();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }
      if (showCount)
      {
        int count = 0;
        if (itemStack.getMaxStackSize() > 1) {
          count = countInInventory(Minecraft.getMinecraft().thePlayer, itemStack.getItem(), 
        	  itemStack.getItemDamage());
        } else if (itemStack.getItem().equals(Items.bow)) {
          count = countInInventory(Minecraft.getMinecraft().thePlayer, Items.arrow);
        }
        if (count > 1)
        {
          String var6 = "" + count;
          GlStateManager.disableLighting();
          GlStateManager.disableDepth();
          GlStateManager.disableBlend();
          fontRenderer.drawStringWithShadow(var6, x + 19 - 2 - fontRenderer.getStringWidth(var6), y + 6 + 3, 16777215);
          GlStateManager.enableLighting();
          GlStateManager.enableDepth();
        }
      }
    }
  }
  
  public int countInInventory(EntityPlayer player, Item item, int metadata)
  {
    int count = 0;
    for (int i = 0; i < player.inventory.mainInventory.length; i++) {
      if ((player.inventory.mainInventory[i] != null) && (item.equals(player.inventory.mainInventory[i].
    	  getItem())) && ((metadata == -1) || (player.inventory.mainInventory[i].getMetadata() == metadata)))
        count += player.inventory.mainInventory[i].stackSize;
    }
    return count;
  }
  
  public int countInInventory(EntityPlayer player, Item item)
  {
    return countInInventory(player, item, -1);
  }
  
  public String getColorCode(int value)
  {
   if(value >= 0 && value < 10)
	   return "4";
   else if(value >= 10 && value < 25)
	   return "c";
   else if(value >= 25 && value < 40)
	   return "6";
   else if(value >= 40 && value < 60)
	   return "e";
   else if(value >= 60 && value < 80)
	   return "7";
   else if(value >= 80)
	   return "f";
   else
    return "f";
  }
}
