package com.cyphrags.minecraft.quickempty.networking;

import com.cyphrags.minecraft.quickempty.inventory.InventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * Created by Cyphrags at 20:04 on 02.06.2017.
 */
public class PacketEntityHandler implements IMessageHandler
{
	@Override
	public IMessage onMessage(final IMessage message, final MessageContext ctx)
	{
		if(ctx.side == Side.SERVER)
		{
			IThreadListener tl = (WorldServer) ctx.getServerHandler().playerEntity.world;
			tl.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					if(message instanceof PacketEntity)
					{
						PacketEntity pb = (PacketEntity) message;
						EntityPlayer player = ctx.getServerHandler().playerEntity.world.getPlayerEntityByUUID(UUID.fromString(pb.playerUUID));
						Entity target = ((WorldServer) ctx.getServerHandler().playerEntity.world).getEntityFromUuid(UUID.fromString(pb.targetUUID));
						if(InventoryHandler.checkDistance(player, target))
						{
							InventoryHandler.emptyOutEntity(ctx.getServerHandler().playerEntity.world, player, target);
						}
					}
				}
			});
		}
		return null;
	}
}