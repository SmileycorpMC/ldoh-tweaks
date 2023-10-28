package net.smileycorp.ldoh.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.util.EnumTFClass;
import net.tangotek.tektopia.ModItems;
import net.tangotek.tektopia.ProfessionType;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

public class ItemTF2ProfessionToken extends ItemBase implements IMetaItem {

    public ItemTF2ProfessionToken() {
        super("TF2_Profession_Token");
        setMaxStackSize(1);
    }

    @Override
    public String byMeta(int meta) {
        return EnumTFClass.values()[meta].getClassName();
    }

    @Override
    public int getMaxMeta() {
        return EnumTFClass.values().length;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < getMaxMeta(); i++) {
                ItemStack stack = new ItemStack(this, 1, i);
                stack.setTagCompound(new NBTTagCompound());
                items.add(stack);
            }
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        World world = player.world;
        if (!world.isRemote) {
            if (target instanceof EntityVillagerTek) {
                EntityVillagerTek villager = (EntityVillagerTek) target;
                if (villager.canConvertProfession(ProfessionType.GUARD) && villager.isMale() && villager.getIntelligence() >= 50 &&
                        (ModItems.isItemVillageBound(stack, villager.getVillage()) || !ModItems.isItemVillageBound(stack))) {
                    int meta = stack.getMetadata();
                    try {
                        EntityTF2Character entity = EnumTFClass.values()[meta].createEntity(world);
                        if (player.getTeam() != null && (player.getTeam().getName().equals("RED") || player.getTeam().getName().equals("BLU"))) {
                            world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName());
                            entity.setEntTeam(player.getTeam().getName().equals("RED") ? 0 : 1);
                        }
                        entity.setPosition(target.posX, target.posY, target.posZ);
                        entity.renderYawOffset = target.renderYawOffset;
                        entity.setCustomNameTag(target.getCustomNameTag());
                        if (entity.hasCapability(LDOHCapabilities.VILLAGE_DATA, null) && villager.hasVillage()) {
                            entity.getCapability(LDOHCapabilities.VILLAGE_DATA, null).setVillage(villager.getVillage());
                            entity.setHomePosAndDistance(villager.getVillage().getCenter(), 75);
                        }
                        target.setDead();
                        world.spawnEntity(entity);
                        player.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
                        stack.shrink(1);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata();
        if (meta >= getMaxMeta()) return super.getUnlocalizedName(stack);
        return this.getUnlocalizedName() + "." + EnumTFClass.values()[meta].getClassName();
    }

}
