/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Shows armor status, potion effects, and direction on screen.",
	name = "HUD")
public class HudMod extends Mod implements RenderListener
{
	public final CheckboxSetting armorhud = new CheckboxSetting(
		"ArmorHUD", true);
	public final CheckboxSetting potionhud = new CheckboxSetting(
		"PotionHUD", true);
	private int yOffset = 2;
	private int xOffset = 2;
	private Map<PotionEffect, Integer> potionMaxDurationMap = new HashMap<PotionEffect,Integer>();
	ScaledResolution screenRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	
	@Override
	public void initSettings()
	{
		settings.add(armorhud);
		settings.add(potionhud);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		if(potionhud.isChecked())
		{
			Collection<?> activeEffects = mc.thePlayer.getActivePotionEffects();
		    if (!activeEffects.isEmpty())
		    {
		      int yOffset = 20;
		      int yBase = getY(activeEffects.size(), yOffset);
		      for (Iterator<?> iteratorPotionEffect = activeEffects.iterator(); iteratorPotionEffect.hasNext(); yBase += yOffset)
		      {
		        PotionEffect potionEffect = (PotionEffect)iteratorPotionEffect.next();
		        if ((!potionMaxDurationMap.containsKey(potionEffect)) || 
		        	(((Integer)potionMaxDurationMap.get(potionEffect)).intValue() < potionEffect.getDuration()))
		            potionMaxDurationMap.put(potionEffect, new Integer(potionEffect.getDuration()));
		        Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
		        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		        int xBase = getX(22 + mc.fontRendererObj.getStringWidth("0:00"));
		        String potionName = "";
		          potionName = I18n.format(potion.getName());
		          if (potionEffect.getAmplifier() == 1) {
		            potionName = potionName + " II";
		          } else if (potionEffect.getAmplifier() == 2) {
		            potionName = potionName + " III";
		          } else if (potionEffect.getAmplifier() == 3) {
		            potionName = potionName + " IV";
		          } else if (potionEffect.getAmplifier() == 4) {
		            potionName = potionName + " V";
		          } else if (potionEffect.getAmplifier() == 5) {
		            potionName = potionName + " VI";
		          } else if (potionEffect.getAmplifier() == 6) {
		            potionName = potionName + " VII";
		          } else if (potionEffect.getAmplifier() == 7) {
		            potionName = potionName + " VIII";
		          } else if (potionEffect.getAmplifier() == 8) {
		            potionName = potionName + " IX";
		          } else if (potionEffect.getAmplifier() == 9) {
		            potionName = potionName + " X";
		          } else if (potionEffect.getAmplifier() > 9) {
		            potionName = potionName + " " + (potionEffect.getAmplifier() + 1);
		          }
		          xBase = getX(22 + mc.fontRendererObj.getStringWidth(potionName));
		        String effectDuration = Potion.getDurationString(potionEffect);
		        xBase = getX(0);
		        if ((potion).hasStatusIcon())
		        {
		            int potionStatusIcon = potion.getStatusIconIndex();
		            if (shouldRender(potionEffect, potionEffect.getDuration(), 10))
		              RenderUtils.drawTexturedModalRect(xBase - 18, yBase, 0 + potionStatusIcon % 
		            	  8 * 18, 198 + potionStatusIcon / 8 * 18, 18, 18, -150);
		        }
		          int stringWidth = mc.fontRendererObj.getStringWidth(potionName);
		          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		          mc.fontRendererObj.drawString("§f" + potionName + "§r", xBase - 22 -
		        	  stringWidth, yBase, 16777215);
		          int stringWidth2 = mc.fontRendererObj.getStringWidth(effectDuration);
		          if (shouldRender(potionEffect, potionEffect.getDuration(), 10))
		              mc.fontRendererObj.drawString("§f" + effectDuration + "§r", xBase
		            	  - 22 - stringWidth2, yBase + 10, 16777215);
		      }
		      List<PotionEffect> toRemove = new LinkedList<PotionEffect>();
		      for (PotionEffect pe : potionMaxDurationMap.keySet())
		        if (!activeEffects.contains(pe))
		          toRemove.add(pe);
		      for (PotionEffect pe : toRemove) 
		        potionMaxDurationMap.remove(pe);
		    }
		}
		if(armorhud.isChecked())
		{
			if(mc.playerController.isNotCreative()) {
		         int x = 15;
		         GL11.glPushMatrix();

		         for(int index = 3; index >= 0; --index) {
		            ItemStack stack = mc.thePlayer.inventory.armorInventory[index];
		            if(stack != null) {
		               mc.getRenderItem().func_180450_b(stack, screenRes.getScaledWidth() / 2 + x - 1, screenRes.getScaledHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water)?65:55) - 2);
		               mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, screenRes.getScaledWidth() / 2 + x - 1, screenRes.getScaledHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water)?65:55) - 2);
		               x += 18;
		            }
		         }

		         GlStateManager.disableCull();
		         GlStateManager.enableAlpha();
		         GlStateManager.disableBlend();
		         GlStateManager.disableLighting();
		         GlStateManager.disableCull();
		         GlStateManager.clear(256);
		         GL11.glPopMatrix();
		      }
		}
	}
	
	private int getX(int width)
	{
		return screenRes.getScaledWidth() - width - xOffset;
	}
	
	private int getY(int rowCount, int height)
	{
		return screenRes.getScaledHeight() - rowCount * height - yOffset;
	}
	
	 private boolean shouldRender(PotionEffect pe, int ticksLeft, int thresholdSeconds)
	 {
	    if ((((Integer)potionMaxDurationMap.get(pe)).intValue() > 400) && 
	      (ticksLeft / 20 <= thresholdSeconds)) {
	      return ticksLeft % 20 < 10;
	    }
	    return true;
	 }
	 
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
	}
}
