/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import tk.wurst_client.events.listeners.GUIRenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.utils.HUDElementUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Shows armor status and potion effects on screen.",
	name = "HUD")
public class HudMod extends Mod implements GUIRenderListener
{
	public final CheckboxSetting armorhud = new CheckboxSetting(
		"ArmorHUD", true);
	public final CheckboxSetting potionhud = new CheckboxSetting(
		"PotionHUD", true);
	private int yOffsetP = 20;
	private int xOffsetP = 2;
	private int yOffsetA = 270;
	private int xOffsetA = 100;
	private Map<PotionEffect, Integer> potionMaxDurationMap = new HashMap<PotionEffect,Integer>();
	private List<HUDElementUtils> elements = new ArrayList<HUDElementUtils>();
	
	@Override
	public void initSettings()
	{
		settings.add(armorhud);
		settings.add(potionhud);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(GUIRenderListener.class, this);
	}
	
	@Override
	public void onRenderGUI()
	{
		ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		if(potionhud.isChecked())
		{
			Collection<?> activeEffects = mc.thePlayer.getActivePotionEffects();
		    if (!activeEffects.isEmpty())
		    {
		      int yOffset = 20;
		      int yBase = getYP(activeEffects.size(), yOffset, scaledRes);
		      for (Iterator<?> iteratorPotionEffect = activeEffects.iterator(); iteratorPotionEffect.hasNext(); yBase += yOffset)
		      {
		        PotionEffect potionEffect = (PotionEffect)iteratorPotionEffect.next();
		        if ((!potionMaxDurationMap.containsKey(potionEffect)) || 
		        	(((Integer)potionMaxDurationMap.get(potionEffect)).intValue() < potionEffect.getDuration()))
		            potionMaxDurationMap.put(potionEffect, new Integer(potionEffect.getDuration()));
		        Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
		        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		        int xBase = getXP(22 + mc.fontRendererObj.getStringWidth("0:00"), scaledRes);
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
		          xBase = getXP(22 + mc.fontRendererObj.getStringWidth(potionName), scaledRes);
		        String effectDuration = Potion.getDurationString(potionEffect);
		        xBase = getXP(0, scaledRes);
		        if ((potion).hasStatusIcon())
		        {
		            int potionStatusIcon = potion.getStatusIconIndex();
		            if (shouldRender(potionEffect, potionEffect.getDuration(), 10))
		              RenderUtils.drawTexturedModalRect(xBase - 18, yBase, 0 + potionStatusIcon % 
		            	  8 * 18, 198 + potionStatusIcon / 8 * 18, 18, 18, -150);
		        }
		          int stringWidth = mc.fontRendererObj.getStringWidth(potionName);
		          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		          mc.fontRendererObj.drawString(potionName + "§r", xBase - 22 -
		        	  stringWidth, yBase, 16777215);
		          int stringWidth2 = mc.fontRendererObj.getStringWidth(effectDuration);
		          if (shouldRender(potionEffect, potionEffect.getDuration(), 10))
		              mc.fontRendererObj.drawString(effectDuration + "§r", xBase
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
			getHUDElements();
		    int yBase;
		    if (elements.size() > 0)
		    {
		      int yOffset = 18;
		        yBase = getYA(elements.size(), yOffset, scaledRes);
		        for (HUDElementUtils e : elements)
		        {
		          e.renderToHud(getXA(0, scaledRes), yBase);
		          yBase += yOffset;
		        }
		    }
		}
	}
	
	private int getXP(int width, ScaledResolution scaledres)
	{
		return scaledres.getScaledWidth() - width - xOffsetP;
	}
	
	private int getYP(int rowCount, int height, ScaledResolution scaledres)
	{
		return scaledres.getScaledHeight() - rowCount * height - yOffsetP;
	}
	
	private int getXA(int width, ScaledResolution scaledres)
	{
		return scaledres.getScaledWidth() - width - xOffsetA;
	}
	
	private int getYA(int rowCount, int height, ScaledResolution scaledres)
	{
		return scaledres.getScaledHeight() - rowCount * height - yOffsetA;
	}
	
	 private boolean shouldRender(PotionEffect pe, int ticksLeft, int thresholdSeconds)
	 {
	    if ((((Integer)potionMaxDurationMap.get(pe)).intValue() > 400) && 
	      (ticksLeft / 20 <= thresholdSeconds)) {
	      return ticksLeft % 20 < 10;
	    }
	    return true;
	 }
	 
	 private void getHUDElements()
	  {
	    elements.clear();
	    for (int i = 3; i >= -1; i--)
	    {
	      ItemStack itemStack = null;
	      if ((i == -1)) {
	        itemStack = mc.thePlayer.getCurrentEquippedItem();
	      } else if (i != -1) {
	        itemStack = mc.thePlayer.inventory.armorInventory[i];
	      }
	      if (itemStack != null) {
	        elements.add(new HUDElementUtils(itemStack, 16, 16, 2, i > -1));
	      }
	    }
	  }
	 
	@Override
	public void onDisable()
	{
		wurst.events.remove(GUIRenderListener.class, this);
	}
}
