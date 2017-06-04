package com.cyphrags.minecraft.quickempty.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.Charset;

/**
 * Created by Cyphrags at 19:38 on 02.06.2017.
 */
public class PacketEntity implements IMessage
{
	public String playerUUID;
	public String targetUUID;
	
	public PacketEntity(){}
	
	public PacketEntity(String player, String target)
	{
		this.playerUUID = player;
		this.targetUUID = target;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		int len = buf.readInt();
		ByteBuf strUUID = buf.readBytes(len);
		len = buf.readInt();
		ByteBuf strUUID2 = buf.readBytes(len);
		
		this.playerUUID = strUUID.toString(Charset.forName("utf-8"));
		this.targetUUID = strUUID2.toString(Charset.forName("utf-8"));
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(playerUUID.length());
		buf.writeBytes(Charset.forName("utf-8").encode(playerUUID));
		buf.writeInt(targetUUID.length());
		buf.writeBytes(Charset.forName("utf-8").encode(targetUUID));
	}
}
