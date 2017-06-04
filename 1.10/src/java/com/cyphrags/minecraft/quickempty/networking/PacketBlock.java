package com.cyphrags.minecraft.quickempty.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.Charset;

/**
 * Created by Cyphrags at 19:38 on 02.06.2017.
 */
public class PacketBlock implements IMessage
{
	public BlockPos blockPos;
	public int dimension;
	public String playerUUID;
	
	public PacketBlock(){}
	
	public PacketBlock(String player, int dimension, BlockPos blockPos)
	{
		this.playerUUID = player;
		this.dimension = dimension;
		this.blockPos = blockPos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		int len = buf.readInt();
		ByteBuf strUUID = buf.readBytes(len);
		len = buf.readInt();
		ByteBuf strDIM = buf.readBytes(len);
		len = buf.readInt();
		ByteBuf strBP = buf.readBytes(len);
		
		this.playerUUID = strUUID.toString(Charset.forName("utf-8"));
		this.dimension = Integer.valueOf(strDIM.toString(Charset.forName("utf-8")));
		String[] pos = strBP.toString(Charset.forName("utf-8")).split(";");
		this.blockPos = new BlockPos(Integer.valueOf(pos[0]), Integer.valueOf(pos[1]), Integer.valueOf(pos[2]));
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		String strUUID = playerUUID;
		String strDIM = Integer.toString(dimension);
		String strBP = blockPos.getX() + ";" + blockPos.getY() + ";" + blockPos.getZ();
		
		buf.writeInt(strUUID.length());
		buf.writeBytes(Charset.forName("utf-8").encode(strUUID));
		buf.writeInt(strDIM.length());
		buf.writeBytes(Charset.forName("utf-8").encode(strDIM));
		buf.writeInt(strBP.length());
		buf.writeBytes(Charset.forName("utf-8").encode(strBP));
	}
}
