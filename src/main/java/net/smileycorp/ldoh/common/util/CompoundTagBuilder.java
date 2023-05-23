package net.smileycorp.ldoh.common.util;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class CompoundTagBuilder {

	protected NBTTagCompound internal =  new NBTTagCompound();

	public CompoundTagBuilder setTag(String key, NBTBase value) {
		internal.setTag(key, value);
		return this;
	}

	public CompoundTagBuilder setByte(String key, byte value) {
		internal.setByte(key, value);
		return this;
	}

	public CompoundTagBuilder setShort(String key, short value) {
		internal.setShort(key, value);
		return this;
	}

	public CompoundTagBuilder setInteger(String key, int value) {
		internal.setInteger(key, value);
		return this;
	}

	public CompoundTagBuilder setLong(String key, long value) {
		internal.setLong(key, value);
		return this;
	}

	public CompoundTagBuilder setUniqueId(String key, UUID value) {
		internal.setUniqueId(key, value);
		return this;
	}

	public CompoundTagBuilder setFloat(String key, float value) {
		internal.setFloat(key, value);
		return this;
	}

	public CompoundTagBuilder setDouble(String key, double value) {
		internal.setDouble(key, value);
		return this;
	}

	public CompoundTagBuilder setString(String key, String value) {
		internal.setString(key, value);
		return this;
	}

	public CompoundTagBuilder setByteArray(String key, byte[] value) {
		internal.setByteArray(key, value);
		return this;
	}

	public CompoundTagBuilder setIntArray(String key, int[] value) {
		internal.setIntArray(key, value);
		return this;
	}

	public CompoundTagBuilder setBoolean(String key, boolean value) {
		internal.setBoolean(key, value);
		return this;
	}

	public NBTTagCompound build() {
		return internal;
	}

}
