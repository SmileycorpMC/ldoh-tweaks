package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TornNoteMessage implements IMessage {

    private long seed;

    public TornNoteMessage() {}

    public TornNoteMessage(long seed) {
        this.seed = seed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(seed);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        seed = buf.readLong();
    }

    public long getSeed() {
        return seed;
    }

}
