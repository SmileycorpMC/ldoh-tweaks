package net.smileycorp.ldoh.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.network.TornNoteMessage;

public class ItemTornNote extends Item {

    public ItemTornNote() {
        setUnlocalizedName(ModDefinitions.getName("TornNote"));
        setRegistryName(ModDefinitions.getResource("torn_note"));
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{};
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new TornNoteMessage(world.getSeed()), (EntityPlayerMP) player);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

}
