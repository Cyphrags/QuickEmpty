package com.cyphrags.minecraft.quickempty;

import com.cyphrags.minecraft.quickempty.event.EventHandler;
import com.cyphrags.minecraft.quickempty.networking.PacketBlock;
import com.cyphrags.minecraft.quickempty.networking.PacketBlockHandler;
import com.cyphrags.minecraft.quickempty.networking.PacketEntity;
import com.cyphrags.minecraft.quickempty.networking.PacketEntityHandler;
import com.cyphrags.minecraft.quickempty.registry.HandlerRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

/**
 * Created by Cyphrags at 08:37 on 02.06.2017.
 */
@Mod(modid = QuickEmptyMod.MODID, name = QuickEmptyMod.MODNAME, version = QuickEmptyMod.VERSION, guiFactory = "com.cyphrags.minecraft.quickempty.config.GuiFactoryQuickEmpty")
public class QuickEmptyMod
{
	public static final String MODNAME = "QuickEmpty";
	public static final String MODID = "quickempty";
	public static final String VERSION = "1.0.1";
	
	public static File configurationFile;
	public static Configuration configuration;
	public static boolean chatOutput = true;
	
	public static SimpleNetworkWrapper snw;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configurationFile = event.getSuggestedConfigurationFile();
		configuration = new Configuration(configurationFile);
		configuration.load();
		
		syncConfig();
		
		snw = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		snw.registerMessage(new PacketBlockHandler(), PacketBlock.class, 0, Side.SERVER);
		snw.registerMessage(new PacketEntityHandler(), PacketEntity.class, 1, Side.SERVER);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		if(event.getSide() == Side.CLIENT)
		{
			MinecraftForge.EVENT_BUS.register(new EventHandler());
		}
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		// [Vanilla] Register facings
		
		// [Vanilla] Register indices
		HandlerRegistry.Blocks.putIndices(Blocks.FURNACE, new int[]{2});
		HandlerRegistry.Blocks.putIndices(Blocks.LIT_FURNACE, new int[]{2});
		
		// Create ISHs
		HandlerRegistry.ISpecialHandler handler = new HandlerRegistry.ISpecialHandler()
		{
			@Override
			public IInventory fromBlock(World world, BlockPos pos, EntityPlayer entityPlayer)
			{
				if(world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST)
				{
					return entityPlayer.getInventoryEnderChest();
				}
				return null;
			}
			
			@Override
			public IInventory fromEntity(World world, EntityPlayer player, Entity target)
			{
				return null;
			}
		};
		
		// [Vanilla] Register ISH for Blocks
		HandlerRegistry.Special.putBlock(Blocks.ENDER_CHEST, handler);
		
		// [Vanilla] Register ISH for Entities
	}
	
	public static void syncConfig()
	{
		chatOutput = configuration.getBoolean("Chat output", Configuration.CATEGORY_CLIENT, true, "Shows you in chat exactly what you have taken out of an inventory (if any)");

		if(configuration.hasChanged()) configuration.save();
	}
}