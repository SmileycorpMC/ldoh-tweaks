package net.smileycorp.ldoh.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.smileycorp.ldoh.common.item.ItemSuperShotgun;

import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;

public class MessageSuperShotgunShoot extends MessageShoot {

	@Override
	public IMessage onMessage(MessageShoot message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
		{
			System.out.println("milbus");
			EntityPlayer player = ctx.getServerHandler().player;
			World world = player.world;
			ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun && (ItemSuperShotgun.hasShells(heldItem) || player.capabilities.isCreativeMode))
			{
				ItemGun item = (ItemGun) heldItem.getItem();
				Gun gun = item.getModifiedGun(heldItem);
				if(gun != null)
				{
					CooldownTracker tracker = player.getCooldownTracker();
					if(!tracker.hasCooldown(heldItem.getItem()))
					{
						tracker.setCooldown(heldItem.getItem(), gun.general.rate);
						ItemGun.fire(world, player, heldItem);
						System.out.println("wawowowoweewa");
						if(!player.capabilities.isCreativeMode)
						{
							NBTTagCompound tag = ItemStackUtil.createTagCompound(heldItem);
							if(!tag.getBoolean("IgnoreAmmo"))
							{
								tag.setInteger("AmmoCount", Math.max(0, tag.getInteger("AmmoCount") - 1));
							}
						}
					}
				}
			}
			else
			{
				world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
			}
		});
		return null;
	}

}
