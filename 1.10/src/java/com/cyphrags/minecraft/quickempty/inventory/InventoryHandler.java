package com.cyphrags.minecraft.quickempty.inventory;

import com.cyphrags.minecraft.quickempty.QuickEmptyMod;
import com.cyphrags.minecraft.quickempty.registry.HandlerRegistry;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyphrags at 20:11 on 02.06.2017.
 */
public class InventoryHandler
{
	public static boolean checkDistance(EntityPlayer player, int dimension, BlockPos pos)
	{
		return player.dimension == dimension && pos.distanceSq(player.posX, player.posY, player.posZ) < 64;
	}
	
	public static boolean checkDistance(EntityPlayer player, Entity target)
	{
		return player.dimension == target.dimension && player.getDistanceSqToEntity(target) < 64;
	}
	
	public static void emptyOut(EntityPlayer player, IInventory inventory)
	{
		int[] indices = new int[inventory.getSizeInventory()];
		for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
			indices[i] = i;
		}
		emptyOut(player, inventory, indices);
	}
	
	public static void emptyOut(EntityPlayer player, IInventory inventory, int[] indices)
	{
		List<ItemStack> addedToPlayerInventory = new ArrayList<ItemStack>();
		for(int index : indices)
		{
			ItemStack orig = inventory.removeStackFromSlot(index);
			if(orig != null && orig.stackSize > 0)
			{
				ItemStack is = tryTransfer(player, orig.copy());
				inventory.setInventorySlotContents(index, is);
				if(is != null)
				{
					//orig.setCount(orig.getCount() - is.getCount());
					orig.stackSize = orig.stackSize - is.stackSize;
				}
				addedToPlayerInventory.add(orig);
			}
		}
		
		if(QuickEmptyMod.chatOutput)
		{
			ArrayList<ItemStack> mergedList = new ArrayList<ItemStack>();
			for(ItemStack is : addedToPlayerInventory)
			{
				boolean set = false;
				for(ItemStack mis : mergedList)
				{
					if(ItemStack.areItemsEqual(is, mis))
					{
						//mis.setCount(mis.getCount() + is.getCount());
						mis.stackSize = mis.stackSize + is.stackSize;
						set = true;
						break;
					}
				}
				//if(!set && !is.isEmpty())
				if(!set && is != null && is.stackSize > 0)
				{
					mergedList.add(is);
				}
			}
			
			//mergedList.sort((o1, o2) -> o1.getCount() == o2.getCount() ? 0 : (o1.getCount() < o2.getCount() ? 1 : -1));
			mergedList.sort((o1, o2) -> o1.stackSize == o2.stackSize ? 0 : (o1.stackSize < o2.stackSize ? 1 : -1));
			
			for(ItemStack is : mergedList)
			{
				//player.sendMessage(new TextComponentString("§a" + is.getCount() + " " + is.getDisplayName() + "§f"));
				player.addChatMessage(new TextComponentString(ChatFormatting.GREEN.toString() + is.stackSize + " " + is.getDisplayName()));
			}
		}
	}
	
	/**
	 * TODO: Rework a little bit to check if a stack can fit in another stack, BEFORE checking for empty stacks (for maximum emptying efficiency)
	 */
	public static ItemStack tryTransfer(EntityPlayer player, ItemStack itemStack)
	{
		InventoryPlayer ip = player.inventory;
		//for(int index = 0; index < ip.mainInventory.size(); index++)
		for(int index = 0; index < ip.mainInventory.length; index++)
		{
			//ItemStack inInventory = ip.mainInventory.get(index);
			ItemStack inInventory = ip.mainInventory[index];
			//if(inInventory.isEmpty())
			if(inInventory == null || inInventory.stackSize == 0)
			{
				//ip.mainInventory.set(index, itemStack);
				ip.mainInventory[index] = itemStack;
				return null;
			}
			else if(ItemStack.areItemsEqual(inInventory, itemStack))
			{
				//if(inInventory.getCount() < inInventory.getMaxStackSize())
				if(inInventory.stackSize < inInventory.getMaxStackSize())
				{
					//int diff = inInventory.getMaxStackSize() - inInventory.getCount();
					int diff = inInventory.getMaxStackSize() - inInventory.stackSize;
					//if(diff >= itemStack.getCount())
					if(diff >= itemStack.stackSize)
					{
						//inInventory.setCount(inInventory.getCount() + itemStack.getCount());
						inInventory.stackSize = inInventory.stackSize + itemStack.stackSize;
						return null;
					}
					else
					{
						//inInventory.setCount(inInventory.getCount() + diff);
						//itemStack.setCount(itemStack.getCount() - diff);
						inInventory.stackSize = inInventory.stackSize + diff;
						itemStack.stackSize = itemStack.stackSize - diff;
					}
				}
			}
		}
		return itemStack;
	}
	
	public static void emptyOutBlock(World world, EntityPlayer player, BlockPos blockPos)
	{
		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		
		if(HandlerRegistry.Special.hasBlock(block))
		{
			//LogManager.getLogger().info(block.getLocalizedName() + ": SPECIAL_HANDLER");
			HandlerRegistry.ISpecialHandler handler = HandlerRegistry.Special.getBlock(block);
			if(handler != null)
			{
				IInventory inventory = handler.fromBlock(world, blockPos, player);
				if(inventory != null)
				{
					emptyOut(player, inventory);
				}
			}
		}
		else if(world.getTileEntity(blockPos) != null && world.getTileEntity(blockPos) instanceof IInventory)
		{
			if(HandlerRegistry.Blocks.isFacing(block) && world.getTileEntity(blockPos) instanceof ISidedInventory)
			{
				//LogManager.getLogger().info(block.getLocalizedName() + ": SIDED_INVENTORY");
				ISidedInventory inventory = (ISidedInventory) world.getTileEntity(blockPos);
				if(inventory != null)
				{
					EnumFacing facing;
					if((facing = HandlerRegistry.Blocks.getFacing(block)) != null)
					{
						//LogManager.getLogger().info(block.getLocalizedName() + ": SIDED_INVENTORY > " + facing.getName());
						int[] indices = inventory.getSlotsForFace(facing);
						/*
						String out = block.getLocalizedName() + ": SIDED_INVENTORY - Indices: [";
						String out2 = block.getLocalizedName() + ": SIDED_INVENTORY - Items: [";
						for(int i = 0; i < indices.length; i++)
						{
							out += String.valueOf(indices[i]);
							out2 += inventory.getStackInSlot(indices[i]).getDisplayName();
							if(i < indices.length - 1)
							{
								out += ", ";
								out2 += ", ";
							}
						}
						LogManager.getLogger().info(out + "]");
						LogManager.getLogger().info(out2 + "]");
						*/
						emptyOut(player, inventory, indices);
					}
				}
			}
			else if(HandlerRegistry.Blocks.isIndices(block))
			{
				//LogManager.getLogger().info(block.getLocalizedName() + ": INDICES");
				int[] indices;
				if((indices = HandlerRegistry.Blocks.getIndices(block)) != null)
				{
					emptyOut(player, (IInventory) world.getTileEntity(blockPos), indices);
				}
			}
			else
			{
				//LogManager.getLogger().info(block.getLocalizedName() + ": INVENTORY");
				emptyOut(player, ((IInventory) world.getTileEntity(blockPos)));
			}
		}
	}
	
	public static void emptyOutEntity(World world, EntityPlayer player, Entity target)
	{
		if(HandlerRegistry.Special.hasEntity(target))
		{
			//LogManager.getLogger().info(target.getDisplayName().getUnformattedText() + ": SPECIAL_HANDLER");
			HandlerRegistry.ISpecialHandler handler = HandlerRegistry.Special.getEntity(target);
			if(handler != null)
			{
				IInventory inventory = handler.fromEntity(world, player, target);
				if(inventory != null)
				{
					emptyOut(player, inventory);
				}
			}
		}
		else if(target instanceof IInventory)
		{
			emptyOut(player, (IInventory) target);
		}
	}
}
