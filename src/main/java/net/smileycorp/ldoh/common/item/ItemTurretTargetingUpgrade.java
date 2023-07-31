package net.smileycorp.ldoh.common.item;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;

import java.util.List;

public class ItemTurretTargetingUpgrade extends Item {

    String name = "Turret_Targeting_Upgrade";

    public ItemTurretTargetingUpgrade() {
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(ModDefinitions.getName(name));
        setRegistryName(ModDefinitions.getResource(name));
        setMaxStackSize(1);
    }

    public static TargetingOptions getTargetingOptions(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        return (nbt == null) ? null : TargetingOptions.fromNBT(nbt);
    }

    public static void setTargetingOptions(ItemStack stack, TargetingOptions options) {
        if (options == null) return;
        stack.setTagCompound(options.toNBT());
    }

    public enum TargetingMode {
        WHITELIST,
        BLACKLIST,
        PRIORITIZE;

        public static TargetingMode get(int id) {
            return values()[id % values().length];
        }
    }

    public static final class TargetingOptions {

        protected final TargetingMode mode;
        protected final List<ResourceLocation> entities;

        public TargetingOptions(TargetingMode mode, List<ResourceLocation> entities) {
            this.mode = mode;
            this.entities = entities;
        }

        public int getPriority(Entity entity) {
            if (entities.contains(EntityList.getKey(entity))) {
                switch (mode) {
                    case WHITELIST:
                        return 0;
                    case BLACKLIST:
                        return -1;
                    case PRIORITIZE:
                        return 1;
                }
            }
            return mode == TargetingMode.WHITELIST ? -1 : 0;
        }

        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("mode", mode.ordinal());
            NBTTagList entityList = new NBTTagList();
            entities.forEach(loc -> entityList.appendTag(new NBTTagString(loc.toString())));
            nbt.setTag("mode", entityList);
            return nbt;
        }

        public static TargetingOptions fromNBT(NBTTagCompound tag) {
            TargetingMode mode = tag.hasKey("mode") ? TargetingMode.get(tag.getInteger("mode")) : TargetingMode.WHITELIST;
            List<ResourceLocation> entities = Lists.newArrayList();
            if (tag.hasKey("entities")) {
                ((NBTTagList) tag.getTag("entities")).forEach(nbt ->
                    entities.add(new ResourceLocation(((NBTTagString)nbt).getString())));
            }
            return new TargetingOptions(mode, entities);
        }

    }

}
