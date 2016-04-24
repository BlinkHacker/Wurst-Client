/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.utils;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MathHelper;
import tk.wurst_client.WurstClient;
import tk.wurst_client.special.TargetSpf;

public class EntityUtils
{
	public static boolean lookChanged;
	public static float yaw;
	public static float pitch;
	
	public synchronized static void faceNonlivingEntityClient(Entity entity)
	{
		float[] rotations = getRotationsNeeded(entity);
		if(rotations != null)
		{
			Minecraft.getMinecraft().thePlayer.rotationYaw =
				limitAngleChange(
					Minecraft.getMinecraft().thePlayer.prevRotationYaw,
					rotations[0], 55);// NoCheat+
			// bypass!!!
			Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
		}
	}
	
	public synchronized static void faceNonlivingEntityPacket(Entity entity)
	{
		float[] rotations = getRotationsNeeded(entity);
		if(rotations != null)
		{
			yaw =
				limitAngleChange(
					Minecraft.getMinecraft().thePlayer.prevRotationYaw,
					rotations[0], 1337);// NoCheat+
			pitch = rotations[1];
			lookChanged = true;
		}
	}
	
	public synchronized static void faceEntityClient(EntityLivingBase entity)
	{
		float[] rotations = getRotationsNeeded(entity);
		if(rotations != null)
		{
			Minecraft.getMinecraft().thePlayer.rotationYaw =
				limitAngleChange(
					Minecraft.getMinecraft().thePlayer.prevRotationYaw,
					rotations[0], 55);// NoCheat+
			// bypass!!!
			Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
		}
	}
	
	public synchronized static void faceEntityPacket(EntityLivingBase entity)
	{
		float[] rotations = getRotationsNeeded(entity);
		if(rotations != null)
		{
			yaw =
				limitAngleChange(
					Minecraft.getMinecraft().thePlayer.prevRotationYaw,
					rotations[0], 55);// NoCheat+
			pitch = rotations[1];
			lookChanged = true;
		}
	}
	
	public static float[] getRotationsNeeded(Entity entity)
	{
		if(entity == null)
			return null;
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double diffY;
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
			diffY =
				entityLivingBase.posY
					+ entityLivingBase.getEyeHeight()
					* 0.9
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft
						.getMinecraft().thePlayer.getEyeHeight());
		}else
			diffY =
				(entity.boundingBox.minY + entity.boundingBox.maxY)
					/ 2.0D
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft
						.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[]{
			Minecraft.getMinecraft().thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw
					- Minecraft.getMinecraft().thePlayer.rotationYaw),
			Minecraft.getMinecraft().thePlayer.rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch
					- Minecraft.getMinecraft().thePlayer.rotationPitch)};
		
	}
	
	private final static float limitAngleChange(final float current,
		final float intended, final float maxChange)
	{
		float change = intended - current;
		if(change > maxChange)
			change = maxChange;
		else if(change < -maxChange)
			change = -maxChange;
		return current + change;
	}
	
	public static int getDistanceFromMouse(Entity entity)
	{
		float[] neededRotations = getRotationsNeeded(entity);
		if(neededRotations != null)
		{
			float neededYaw =
				Minecraft.getMinecraft().thePlayer.rotationYaw
					- neededRotations[0], neededPitch =
				Minecraft.getMinecraft().thePlayer.rotationPitch
					- neededRotations[1];
			float distanceFromMouse =
				MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch
					* neededPitch);
			return (int)distanceFromMouse;
		}
		return -1;
	}

	public static boolean isCorrectEntity(Object o, boolean ignoreFriends, boolean ticksExisted, boolean 
		armorCheck)
	{
		// non-entities
		if(!(o instanceof Entity))
			return false;
		
		// friends
		if(ignoreFriends && o instanceof EntityPlayer)
			if(WurstClient.INSTANCE.friends.contains(((EntityPlayer)o)
				.getName()))
				return false;
		
		//ticks Existed check
		if(ticksExisted)
		if(!(o instanceof Entity))
		{
			return false;
		} else if(o instanceof Entity)
		{
			if(((Entity)o).ticksExisted < WurstClient.INSTANCE.mods.killauraMod.secondsExisted * 20)
				return false;
		}
		
		if(armorCheck && o instanceof EntityPlayer)
		{
			ItemStack boots = ((EntityPlayer)o).inventory.armorInventory[0];
			ItemStack pants = ((EntityPlayer)o).inventory.armorInventory[1];
			ItemStack chest = ((EntityPlayer)o).inventory.armorInventory[2];
			ItemStack head = ((EntityPlayer)o).inventory.armorInventory[3];
			if (boots == null && pants == null && chest == null && head == null)
			    return false;
		}
		
		TargetSpf targetSpf = WurstClient.INSTANCE.special.targetSpf;
		
		// invisible entities
		if(((Entity)o).isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
			return targetSpf.invisibleMobs.isChecked()
				&& o instanceof EntityLiving
				|| targetSpf.invisiblePlayers.isChecked()
				&& o instanceof EntityPlayer;
		
		// players
		if(o instanceof EntityPlayer)
			return (((EntityPlayer)o).isPlayerSleeping()
				&& targetSpf.sleepingPlayers.isChecked() || !((EntityPlayer)o)
				.isPlayerSleeping() && targetSpf.players.isChecked())
				&& (!targetSpf.teams.isChecked() || checkName(((EntityPlayer)o)
					.getDisplayName().getFormattedText()));
		
		// animals
		if(o instanceof EntityAgeable || o instanceof EntityAmbientCreature
			|| o instanceof EntityWaterMob)
			return targetSpf.animals.isChecked()
				&& (!targetSpf.teams.isChecked()
					|| !((Entity)o).hasCustomName() || checkName(((Entity)o)
						.getCustomNameTag()));
		
		// monsters
		if(o instanceof EntityMob || o instanceof EntitySlime
			|| o instanceof EntityFlying || o instanceof EntityDragon)
			return targetSpf.monsters.isChecked()
				&& (!targetSpf.teams.isChecked()
					|| !((Entity)o).hasCustomName() || checkName(((Entity)o)
						.getCustomNameTag()));
		
		// golems
		if(o instanceof EntityGolem)
			return targetSpf.golems.isChecked()
				&& (!targetSpf.teams.isChecked()
					|| !((Entity)o).hasCustomName() || checkName(((Entity)o)
						.getCustomNameTag()));
		
		return false;
	}
	
	private static boolean checkName(String name)
	{
		// check colors
		String[] colors =
			{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
				"d", "e", "f"};
		boolean[] teamColors =
			WurstClient.INSTANCE.special.targetSpf.teamColors.getSelected();
		boolean hasKnownColor = false;
		for(int i = 0; i < 16; i++)
			if(name.contains("§" + colors[i]))
			{
				hasKnownColor = true;
				if(teamColors[i])
					return true;
			}
		
		// no known color => white
		return !hasKnownColor && teamColors[15];
	}
	
	public static EntityLivingBase getClosestEntity(boolean ignoreFriends,
		boolean useFOV, boolean ticksExisted, boolean armorCheck)
	{
		EntityLivingBase closestEntity = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, ignoreFriends, ticksExisted, armorCheck)
				&& getDistanceFromMouse((Entity)o) <= WurstClient.INSTANCE.mods.killauraMod.fov / 2)
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP)
					&& !en.isDead
					&& en.getHealth() > 0
					&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en)
					&& (!en.getName().equals(
						Minecraft.getMinecraft().thePlayer.getName()) || !(en instanceof EntityPlayer)))
					if(closestEntity == null
						|| Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(closestEntity))
						closestEntity = en;
			}
		return closestEntity;
	}
	
	public static ArrayList<EntityLivingBase> getCloseEntities(
		boolean ignoreFriends, float range, boolean ticksExisted, boolean armorCheck)
	{
		ArrayList<EntityLivingBase> closeEntities =
			new ArrayList<EntityLivingBase>();
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, ignoreFriends, ticksExisted, armorCheck))
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP)
					&& !en.isDead
					&& en.getHealth() > 0
					&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en)
					&& ((!en.getName().equals(
						Minecraft.getMinecraft().thePlayer.getName()) || !(en instanceof EntityPlayer)))
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= range)
					closeEntities.add(en);
			}
		return closeEntities;
	}
	
	public static EntityLivingBase getClosestEntityRaw(boolean ignoreFriends, boolean ticksExisted,
		boolean armorCheck)
	{
		EntityLivingBase closestEntity = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, ignoreFriends, ticksExisted, armorCheck))
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP) && !en.isDead
					&& en.getHealth() > 0)
					if(closestEntity == null
						|| Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(closestEntity))
						closestEntity = en;
			}
		return closestEntity;
	}
	
	public static EntityLivingBase getClosestEnemy(EntityLivingBase friend, boolean ticksExisted, 
		boolean armorCheck)
	{
		EntityLivingBase closestEnemy = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, true, ticksExisted, armorCheck))
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP) && o != friend && !en.isDead
					&& en.getHealth() <= 0 == false
					&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en))
					if(closestEnemy == null
						|| Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(closestEnemy))
						closestEnemy = en;
			}
		return closestEnemy;
	}
	
	public static EntityLivingBase searchEntityByIdRaw(UUID ID)
	{
		EntityLivingBase newEntity = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, false, false, false))
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP) && !en.isDead)
					if(newEntity == null && en.getUniqueID().equals(ID))
						newEntity = en;
			}
		return newEntity;
	}
	
	public static EntityLivingBase searchEntityByName(String name)
	{
		EntityLivingBase newEntity = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, false, false, false))
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP) && !en.isDead
					&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en))
					if(newEntity == null && en.getName().equals(name))
						newEntity = en;
			}
		return newEntity;
	}
	
	public static EntityLivingBase searchEntityByNameRaw(String name)
	{
		EntityLivingBase newEntity = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, false, false, false))
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP) && !en.isDead)
					if(newEntity == null && (en.getName().equals(name)))
						newEntity = en;
			}
		return newEntity;
	}
	
	public static Entity getClosestNonlivingEntity(boolean ignoreFriends,
		boolean useFOV, boolean ticksExisted)
	{
		Entity closestNLEntity = null;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectNonlivingEntity(o, ignoreFriends, ticksExisted))
			if (getDistanceFromMouse((Entity)o) <= WurstClient.INSTANCE.mods.killauraMod.fov / 2)
			{
				Entity en = (Entity)o;
				if(o instanceof EntityFallingBlock)
				if(!(o instanceof EntityPlayerSP)
					&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en))
					if(closestNLEntity == null
						|| Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer
							.getDistanceToEntity(closestNLEntity))
						closestNLEntity = en;
			}
		return closestNLEntity;
	}
	
	public static boolean isCorrectNonlivingEntity(Object o, boolean ignoreFriends, boolean ticksExisted)
	{
		// non-entities
		if(!(o instanceof Entity))
					return false;
				
		//ticks Existed check
		if(ticksExisted)
		if(!(o instanceof Entity))
		{
			return false;
		} else if(o instanceof Entity)
		{
			if(((Entity)o).ticksExisted < WurstClient.INSTANCE.mods.killauraMod.secondsExisted * 20)
						return false;
		}
		if(!(o instanceof EntityFallingBlock) || !Minecraft.getMinecraft().thePlayer.
			canEntityBeSeen((Entity)o))
			return false;
		return true;

	}
	
	public static double[] EntityPos(Entity entity)
	{
	    double d = Minecraft.getMinecraft().timer.renderPartialTicks;
	    double[] arrayOfDouble = {entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * d, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * d, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * d };
	    return arrayOfDouble;
	}
	
	public static double[] teleportToPosition(double[] startPosition, double[] endPosition, double setOffset, double slack, boolean extendOffset, boolean onGround) {
        boolean wasSneaking = false;

        if (Minecraft.getMinecraft().thePlayer.isSneaking())
            wasSneaking = true;

        double startX = startPosition[0];
        double startY = startPosition[1];
        double startZ = startPosition[2];

        double endX = endPosition[0];
        double endY = endPosition[1];
        double endZ = endPosition[2];

        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

        int count = 0;
        while (distance > slack) 
        {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

            if (count > 120)
                break;

            double offset = extendOffset && (count & 0x1) == 0 ? setOffset + 0.15D : setOffset;

            double diffX = startX - endX;
            double diffY = startY - endY;
            double diffZ = startZ - endZ;

            if (diffX < 0.0D) 
                if (Math.abs(diffX) > offset)
                    startX += offset;
                else
                    startX += Math.abs(diffX);
            if (diffX > 0.0D)
                if (Math.abs(diffX) > offset)
                    startX -= offset;
                else
                    startX -= Math.abs(diffX);
            if (diffY < 0.0D)
                if (Math.abs(diffY) > offset)
                    startY += offset;
                else
                    startY += Math.abs(diffY);
            if (diffY > 0.0D)
                if (Math.abs(diffY) > offset)
                    startY -= offset;
                else
                    startY -= Math.abs(diffY);
            if (diffZ < 0.0D)
                if (Math.abs(diffZ) > offset)
                    startZ += offset;
                else
                    startZ += Math.abs(diffZ);
            if (diffZ > 0.0D)
                if (Math.abs(diffZ) > offset)
                    startZ -= offset;
                else
                    startZ -= Math.abs(diffZ);

            if (wasSneaking)
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C0BPacketEntityAction
                	(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));

            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(
            	new C03PacketPlayer.C04PacketPlayerPosition(startX, startY, startZ, onGround));
            count++;
        }

        if (wasSneaking)
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C0BPacketEntityAction
            	(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));

        return new double[]{startX, startY, startZ};
    }
	
    private static EntityPlayer reference;

    public static EntityPlayer getReference() 
    {
        return reference == null ? reference = Minecraft.getMinecraft().thePlayer : reference;
    }
    
    public static ArrayList<EntityLivingBase> getCloseEntitiesWithFOV(
		boolean ignoreFriends, float range, boolean ticksExisted, boolean armorCheck)
	{
		ArrayList<EntityLivingBase> closeEntities =
			new ArrayList<EntityLivingBase>();
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(isCorrectEntity(o, ignoreFriends, ticksExisted, armorCheck)
				&& getDistanceFromMouse((Entity)o) <= WurstClient.INSTANCE.mods.killauraMod.fov / 2)
			{
				EntityLivingBase en = (EntityLivingBase)o;
				if(!(o instanceof EntityPlayerSP)
					&& !en.isDead
					&& en.getHealth() > 0
					&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en)
					&& ((!en.getName().equals(
						Minecraft.getMinecraft().thePlayer.getName()) || !(en instanceof EntityPlayer)))
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= range)
					closeEntities.add(en);
			}
		return closeEntities;
	}

	public static float[] faceEntityXYZ(Entity entity, double x, double y, double z) 
	{
		double xdiff = x - entity.posX;
		double zdiff = z - entity.posZ;
		double ydiff = y - (entity.posY + entity.getEyeHeight());
		double dist = MathHelper.sqrt_double(xdiff * xdiff + zdiff * zdiff);
		float yaw = (float)(Math.atan2(zdiff, xdiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float)-(Math.atan2(ydiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { entity.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - entity.rotationYaw), 
			entity.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - entity.rotationPitch)};
	}
	
	public synchronized static void setYaw(float yawset)
	{
		yaw = yawset;
		lookChanged = true;
	}
	
	public synchronized static void setPitch(float pitchset)
	{
		pitch = pitchset;
		lookChanged = true;
	}

	public static float[] facePosition(Entity entity, double x, double y, double z) 
	{
		double diffX = x - entity.posX;
		double diffZ = z - entity.posZ;
		double diffY = y - (entity.posY + entity.getEyeHeight());
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] { entity.rotationYaw + MathHelper.wrapAngleTo180_float(
			yaw - entity.rotationYaw), entity.rotationPitch + MathHelper.wrapAngleTo180_float(
				pitch - entity.rotationPitch)};
	}
}
