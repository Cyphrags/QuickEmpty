package com.cyphrags.minecraft.quickempty.event;

import com.cyphrags.minecraft.quickempty.QuickEmptyMod;
import com.cyphrags.minecraft.quickempty.networking.PacketBlock;
import com.cyphrags.minecraft.quickempty.networking.PacketEntity;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Cyphrags at 20:06 on 02.06.2017.
 */
public class EventHandler
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent e)
	{
		if(e.getEntityPlayer().world.isRemote)
		{
			//LogManager.getLogger().info("[CLIENT] onPlayerAttack: " + e.getEntityPlayer().getDisplayName().getUnformattedText() + " -> " + e.getTarget().getDisplayName().getUnformattedText());
			if(GuiScreen.isCtrlKeyDown())
			{
				QuickEmptyMod.snw.sendToServer(new PacketEntity(e.getEntityPlayer().getUniqueID().toString(), e.getTarget().getUniqueID().toString()));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onBlockLeftClicked(PlayerInteractEvent.LeftClickBlock e)
	{
		if(e.getEntityPlayer().world.isRemote)
		{
			//LogManager.getLogger().info("[CLIENT] onBlockLeftClicked: " + e.getEntityPlayer().getDisplayName().getUnformattedText() + " -> " + e.getEntityPlayer().world.getBlockState(e.getPos()).getBlock().getLocalizedName());
			if(GuiScreen.isCtrlKeyDown())
			{
				QuickEmptyMod.snw.sendToServer(new PacketBlock(e.getEntityPlayer().getUniqueID().toString(), e.getEntityPlayer().dimension, e.getPos()));
			}
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(QuickEmptyMod.MODID))
			QuickEmptyMod.syncConfig();
	}
}
