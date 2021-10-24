package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import org.apache.commons.lang3.ArrayUtils;

public class LDOHMessage implements IMessage {
	
	private int day;
	private String text;
	
	public LDOHMessage() {}
	
	public LDOHMessage(int day, String text) {
		this.text=text;
		this.day=day;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(day);
		buf.writeBytes(text.getBytes());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		day = buf.readInt();
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);
		byte[] bytes = {};
		for (byte b : data) {
			if (b != 0x0) bytes = ArrayUtils.add(bytes, b);
		}
		text = new String(bytes);
	}
	
	public String getText() {
		return text;
	}
	
	public int getDay() {
		return day;
	}
}
