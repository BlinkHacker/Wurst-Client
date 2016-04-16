/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import tk.wurst_client.events.BlockReachEvent;
import tk.wurst_client.events.PacketOutputEvent;
import tk.wurst_client.events.listeners.BlockReachListener;
import tk.wurst_client.events.listeners.PacketOutputListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.SliderSetting;
import tk.wurst_client.utils.EntityUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.MOVEMENT,
	description = "Teleports you every time you right click a block.\n"
		+ "Teleporting too far will get you held back in NoCheat+ servers\n"
		+ "Note: There must be no blocks in the way, or you get held back.",
	name = "ClickTeleport")
public class ClickTeleportMod extends Mod implements BlockReachListener, PacketOutputListener, RenderListener,
UpdateListener
{
	private boolean canDraw;
	private BlockPos teleportPosition;
	private int delay = 0;
	public float tpdist = 50; 
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Distance", tpdist, 30, 120, 5,
			ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				tpdist = (float)getValue();
			}
		});
	}
	@Override
	public void onEnable()
	{
		wurst.events.add(BlockReachListener.class, this);
		wurst.events.add(PacketOutputListener.class, this);
		wurst.events.add(RenderListener.class, this);
		wurst.events.add(UpdateListener.class, this);
	}

	@Override
	public void onRender()
	{
		 if (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && canDraw) 
		 {
             for (float offset = -2.0F; offset < 18.0F; offset++) 
             {
                 double[] mouseOverPos = new double[]{mc.objectMouseOver.getBlockPos().
                	 getX(), mc.objectMouseOver.getBlockPos().getY() + offset, 
                	 mc.objectMouseOver.getBlockPos().getZ()};

                 BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                 Block blockBelow = mc.theWorld.getBlockState(blockBelowPos).getBlock();

                 if (canRenderBox(mouseOverPos)) 
                 {
                	 GL11.glPushMatrix();
                     RenderHelper.enableStandardItemLighting();
                     GL11.glDisable(GL11.GL_LIGHTING);
                     GL11.glEnable(GL11.GL_BLEND);
                     GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                     GL11.glDisable(GL11.GL_DEPTH);
                     GL11.glDepthMask(false);
                     GL11.glDisable(GL11.GL_TEXTURE_2D);
                     GL11.glEnable(GL11.GL_LINE_SMOOTH);
                     GL11.glLineWidth(1.0F);
                     drawBox(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                     drawNametags(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                     GL11.glLineWidth(2.0F);
                     GL11.glDisable(GL11.GL_LINE_SMOOTH);
                     GL11.glEnable(GL11.GL_TEXTURE_2D);
                     GL11.glDepthMask(true);
                     GL11.glEnable(GL11.GL_DEPTH);
                     GL11.glDisable(GL11.GL_BLEND);
                     GL11.glEnable(GL11.GL_LIGHTING);
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     RenderHelper.disableStandardItemLighting();
                     GL11.glPopMatrix();

                     if (mc.inGameHasFocus) 
                         teleportPosition = blockBelowPos;
                     else
                         teleportPosition = null;
                 }
             }
         } else if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) 
         {
             for (float offset = -2.0F; offset < 18.0F; offset++) 
             {
                 double[] mouseOverPos = new double[]{mc.objectMouseOver.entityHit.posX, 
                	 mc.objectMouseOver.entityHit.posY + offset, mc.objectMouseOver.entityHit.posZ};

                 BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                 Block blockBelow = mc.theWorld.getBlockState(blockBelowPos).getBlock();

                 if (canRenderBox(mouseOverPos)) 
                 {
                	 GL11.glPushMatrix();
                     RenderHelper.enableStandardItemLighting();
                     GL11.glDisable(GL11.GL_LIGHTING);
                     GL11.glEnable(GL11.GL_BLEND);
                     GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                     GL11.glDisable(GL11.GL_DEPTH);
                     GL11.glDepthMask(false);
                     GL11.glDisable(GL11.GL_TEXTURE_2D);
                     GL11.glEnable(GL11.GL_LINE_SMOOTH);
                     GL11.glLineWidth(1.0F);
                     drawBox(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                     drawNametags(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                     GL11.glLineWidth(2.0F);
                     GL11.glDisable(GL11.GL_LINE_SMOOTH);
                     GL11.glEnable(GL11.GL_TEXTURE_2D);
                     GL11.glDepthMask(true);
                     GL11.glEnable(GL11.GL_DEPTH);
                     GL11.glDisable(GL11.GL_BLEND);
                     GL11.glEnable(GL11.GL_LIGHTING);
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     RenderHelper.disableStandardItemLighting();
                     GL11.glPopMatrix();

                     if (mc.inGameHasFocus)
                         teleportPosition = blockBelowPos;
                     else
                         teleportPosition = null;
                 }
             }
         } else
             teleportPosition = null;
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if (event.getPacket() instanceof C08PacketPlayerBlockPlacement && 
			(canDraw))
            event.cancel();
	}
	
	@Override
	public void onBlockReach(BlockReachEvent event)
	{
		 if ((!Mouse.isButtonDown(0) && mc.inGameHasFocus || 
			 !mc.inGameHasFocus) && mc.thePlayer.getItemInUse() == null) 
		 {
             event.setRange(tpdist + 1);
             canDraw = true;
         } else
             canDraw = false;
	}
	
	@Override
	public void onUpdate()
	{
		if (teleportPosition != null && delay == 0 && Mouse.isButtonDown(1)) 
		{
            double[] playerPosition = new double[]{EntityUtils.getReference().posX, EntityUtils.getReference().posY
            	, EntityUtils.getReference().posZ};
            double[] blockPosition = new double[]{teleportPosition.getX() + 0.5F, teleportPosition.getY() 
            	+ getOffset(mc.theWorld.getBlockState(teleportPosition).getBlock(), 
            		teleportPosition) + 1.0F, teleportPosition.getZ() + 0.5F};

            if (wurst.mods.freecamMod.isActive())
                wurst.mods.freecamMod.setEnabled(false);


            EntityUtils.teleportToPosition(playerPosition, blockPosition, 0.25D, 0.0D, true, true);
            mc.thePlayer.setPosition(blockPosition[0], blockPosition[1], blockPosition[2]);

            teleportPosition = null;
            delay = 5;
        }

        if (delay > 0)
            delay--;
	}
	
	public boolean canRenderBox(double[] mouseOverPos) 
	{
		boolean canTeleport = false;

	    Block blockBelowPos = mc.theWorld.getBlockState(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 
	    	1.0F, mouseOverPos[2])).getBlock();
	    Block blockPos = mc.theWorld.getBlockState(new BlockPos(mouseOverPos[0], mouseOverPos[1], 
	    	mouseOverPos[2])).getBlock();
	    Block blockAbovePos = mc.theWorld.getBlockState(new BlockPos(mouseOverPos[0], mouseOverPos[1] + 
	    	1.0F, mouseOverPos[2])).getBlock();

	    boolean validBlockBelow = blockBelowPos.getCollisionBoundingBox(mc.theWorld, new BlockPos(
	    	mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2]), mc.theWorld.getBlockState(
	    		new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2]))) != null;
	    boolean validBlock = isValidBlock(blockPos);
	    boolean validBlockAbove = isValidBlock(blockAbovePos);

	    if ((validBlockBelow && validBlock && validBlockAbove))
	    	canTeleport = true;
	    	return canTeleport;
	}
	
	public double getOffset(Block block, BlockPos pos) 
	{
        IBlockState state = mc.theWorld.getBlockState(pos);

        double offset = 0;

        if (block instanceof BlockSlab && !((BlockSlab)block).isDouble())
            offset -= 0.5F;
        else if (block instanceof BlockEndPortalFrame)
            offset -= 0.2F;
        else if (block instanceof BlockBed) 
            offset -= 0.44F;
        else if (block instanceof BlockCake)
            offset -= 0.5F;
        else if (block instanceof BlockDaylightDetector)
            offset -= 0.625F;
        else if (block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater)
            offset -= 0.875F;
        else if (block instanceof BlockChest || block == Blocks.ender_chest)
            offset -= 0.125F;
        else if (block instanceof BlockLilyPad)
            offset -= 0.95F;
        else if (block == Blocks.snow_layer)
        {
            offset -= 0.875F;
            offset += 0.125F * ((Integer) state.getValue(BlockSnow.LAYERS_PROP) - 1);
        }
        else if (isValidBlock(block))
            offset -= 1.0F;
        return offset;
    }
	
	public boolean isValidBlock(Block block) 
	{
        return block == Blocks.portal || block == Blocks.snow_layer || 
        	block instanceof BlockTripWireHook || block instanceof BlockTripWire || 
        	block instanceof BlockDaylightDetector || block instanceof BlockRedstoneComparator || 
        	block instanceof BlockRedstoneRepeater || block instanceof BlockSign || 
        	block instanceof BlockAir || block instanceof BlockPressurePlate || 
        	block instanceof BlockTallGrass || block instanceof BlockFlower || 
        	block instanceof BlockMushroom || block instanceof BlockDoublePlant || 
        	block instanceof BlockReed || block instanceof BlockSapling || 
        	block == Blocks.carrots || block == Blocks.wheat || block == Blocks.nether_wart || 
        	block == Blocks.potatoes || block == Blocks.pumpkin_stem || block == Blocks.melon_stem || 
        	block == Blocks.heavy_weighted_pressure_plate || block == Blocks.light_weighted_pressure_plate || 
        	block == Blocks.redstone_wire || block instanceof BlockTorch || block instanceof BlockRedstoneTorch || 
        	block == Blocks.lever || block instanceof BlockButton;
    }
	
	private void drawNametags(Block block, BlockPos pos) 
	{
        BlockPos blockPosBelow = new BlockPos(pos.getX(), pos.getY() - 1.0F, pos.getZ());
        Block blockBelow = mc.theWorld.getBlockState(blockPosBelow).getBlock();
        double offset = getOffset(blockBelow, blockPosBelow);

        double x = pos.getX() + 0.5F - mc.getRenderManager().renderPosX;
        double y = pos.getY() - 1.0F - mc.getRenderManager().renderPosY + offset;
        double z = pos.getZ() + 0.5F - mc.getRenderManager().renderPosZ;
        double dist = EntityUtils.getReference().getDistance(pos.getX(), pos.getY(), pos.getZ());
        final String text = Math.round(dist) + "m";
        double far = mc.gameSettings.renderDistanceChunks * 12.8D;
        double dl = Math.sqrt(x * x + z * z + y * y);
        double d;

        if (dl > far) 
        {
            d = far / dl;
            dist *= d;
            x *= d;
            y *= d;
            z *= d;
        }

        float var13 = 2.5F + ((float) dist / 5 <= 2 ? 2.0F : (float) dist / 5);
        float var14 = 0.016666668F * var13;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 1.5F, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        if (mc.gameSettings.thirdPersonView == 2) 
        {
            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(mc.getRenderManager().playerViewX, -1.0F, 0.0F, 0.0F);
        } else 
        {
            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        }
        GL11.glScalef(-var14, -var14, var14);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        worldRenderer.startDrawingQuads();
        int var18 = mc.fontRendererObj.getStringWidth(text) / 2;
        worldRenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        worldRenderer.addVertex(-var18 - 2, -2, 0.0D);
        worldRenderer.addVertex(-var18 - 2, 9, 0.0D);
        worldRenderer.addVertex(var18 + 2, 9, 0.0D);
        worldRenderer.addVertex(var18 + 2, -2, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        mc.fontRendererObj.drawStringWithShadow(text, -var18, 0, 0xFFFFFFFF);
        GL11.glPopMatrix();
    }
	
	private void drawBox(Block block, BlockPos pos) 
	{
		double x = pos.getX() - mc.getRenderManager().renderPosX;
	    double y = pos.getY() - mc.getRenderManager().renderPosY;
	    double z = pos.getZ() - mc.getRenderManager().renderPosZ;

	    BlockPos blockPosBelow = new BlockPos(pos.getX(), pos.getY() - 1.0F, pos.getZ());
	    Block blockBelow = mc.theWorld.getBlockState(blockPosBelow).getBlock();

	    double offset = getOffset(blockBelow, blockPosBelow);

	    AxisAlignedBB box = AxisAlignedBB.fromBounds(x, y + offset, z, x + 1, y + offset + 0.06F, z + 1);
	    GL11.glColor4f(0.49F, 0.8F, 1F, 0.11F);
	    RenderUtils.drawColorBox(box);
	    GL11.glColor4f(0.49F, 0.8F, 1F, 0.60F);
	    RenderUtils.drawLines(box);
	    RenderGlobal.drawOutlinedBoundingBox(box, -1);
	}
	
	public void setDelay(int delay) 
	{
		this.delay = delay;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(BlockReachListener.class, this);
		wurst.events.remove(PacketOutputListener.class, this);
		wurst.events.remove(RenderListener.class, this);
		wurst.events.remove(UpdateListener.class, this);
	}
}
