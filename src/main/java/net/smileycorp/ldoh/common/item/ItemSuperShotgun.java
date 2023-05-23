package net.smileycorp.ldoh.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.network.MessageBurstShoot;

import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.object.Gun;

public class ItemSuperShotgun extends ItemGun {

	public ItemSuperShotgun() {
		super(ModDefinitions.getResource("super_shotgun"));
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		Gun modifiedGun = getModifiedGun(heldItem);
		if(worldIn.isRemote) {
			System.out.println("mogus");
			if(!MrCrayfishGunMod.proxy.canShoot()) {
				return new ActionResult<>(EnumActionResult.FAIL, heldItem);
			}

			if(hasShells(heldItem) || playerIn.capabilities.isCreativeMode) {
				System.out.println("mogussy");
				if(playerIn.isHandActive()) {
					return new ActionResult<>(EnumActionResult.FAIL, heldItem);
				}
				playerIn.setActiveHand(handIn);
				CooldownTracker tracker = playerIn.getCooldownTracker();
				if(!tracker.hasCooldown(heldItem.getItem())) {
					tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
					System.out.println("morbus");
					PacketHandler.INSTANCE.sendToServer(new MessageBurstShoot());
				}
			}
			else {
				MrCrayfishGunMod.proxy.playClientSound(SoundEvents.BLOCK_LEVER_CLICK);
			}
		}
		return new ActionResult<>(EnumActionResult.FAIL, heldItem);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("churger");
	}

	public static boolean hasShells(ItemStack gunStack) {
		NBTTagCompound tag = ItemStackUtil.createTagCompound(gunStack);
		return tag.getBoolean("IgnoreAmmo") || tag.getInteger("AmmoCount") >= 2;
	}

}
