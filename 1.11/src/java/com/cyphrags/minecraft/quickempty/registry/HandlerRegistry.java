package com.cyphrags.minecraft.quickempty.registry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by Cyphrags at 20:24 on 02.06.2017.
 */
public class HandlerRegistry
{
	public static class Blocks
	{
		private static HashMap<Block, int[]> _INDICIES = new HashMap<Block, int[]>();
		private static HashMap<Block, EnumFacing> _FACING = new HashMap<Block, EnumFacing>();
		
		public static void putIndices(Block b, int[] indices)
		{
			if(!has(b))
			{
				_INDICIES.put(b, indices);
			}
		}
		
		public static void putFacing(Block b, EnumFacing facing)
		{
			if(!has(b))
			{
				_FACING.put(b, facing);
			}
		}
		
		public static boolean has(Block b)
		{
			return _INDICIES.containsKey(b) || _FACING.containsKey(b);
		}
		
		public static boolean isIndices(Block b)
		{
			return _INDICIES.containsKey(b);
		}
		
		public static boolean isFacing(Block b)
		{
			return _FACING.containsKey(b);
		}
		
		public static int[] getIndices(Block b)
		{
			return isIndices(b) ? _INDICIES.get(b) : new int[]{};
		}
		
		public static EnumFacing getFacing(Block b)
		{
			return isFacing(b) ? _FACING.get(b) : EnumFacing.DOWN;
		}
	}
	
	public static interface ISpecialHandler
	{
		IInventory fromBlock(World world, BlockPos pos, EntityPlayer entityPlayer);
		IInventory fromEntity(World world, EntityPlayer player, Entity target);
	}
	
	public static class Special
	{
		private static HashMap<Block, ISpecialHandler> blocks = new HashMap<Block, ISpecialHandler>();
		private static HashMap<Class<? extends Entity>, ISpecialHandler> entities = new HashMap<Class<? extends Entity>, ISpecialHandler>();
		
		public static void putBlock(Block b, ISpecialHandler iSH)
		{
			blocks.put(b, iSH);
		}
		
		public static void putEntity(Class<? extends Entity> e, ISpecialHandler iSH)
		{
			entities.put(e, iSH);
		}
		
		public static boolean hasBlock(Block b)
		{
			return blocks.containsKey(b);
		}
		
		public static boolean hasEntity(Entity e)
		{
			return entities.containsKey(e.getClass());
		}
		
		public static ISpecialHandler getBlock(Block b)
		{
			return hasBlock(b) ? blocks.get(b) : null;
		}
		
		public static ISpecialHandler getEntity(Entity e)
		{
			return hasEntity(e) ? entities.get(e.getClass()) : null;
		}
	}
}
