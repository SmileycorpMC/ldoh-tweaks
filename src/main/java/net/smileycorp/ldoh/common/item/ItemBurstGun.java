package net.smileycorp.ldoh.common.item;

import java.util.concurrent.TimeUnit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;

import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;

public class ItemBurstGun extends ItemGun {

	protected final int shots, burstRate;

	public ItemBurstGun(ResourceLocation id, int shots, int burstRate) {
		super(id);
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		this.shots = shots;
		this.burstRate = burstRate;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		Gun modifiedGun = getModifiedGun(heldItem);
		if (modifiedGun.general.auto) return super.onItemRightClick(worldIn, playerIn, handIn);
		if(worldIn.isRemote) {
			if(!MrCrayfishGunMod.proxy.canShoot()) {
				return new ActionResult<>(EnumActionResult.FAIL, heldItem);
			}

			if(hasBurstAmmo(heldItem) || playerIn.capabilities.isCreativeMode) {
				if(playerIn.isHandActive()) {
					return new ActionResult<>(EnumActionResult.FAIL, heldItem);
				}
				playerIn.setActiveHand(handIn);
				CooldownTracker tracker = playerIn.getCooldownTracker();
				if(!tracker.hasCooldown(heldItem.getItem())) {
					tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
					PacketHandler.INSTANCE.sendToServer(new MessageShoot());
					LDOHTweaks.DELAYED_THREAD_EXECUTOR.schedule(()->PacketHandler.INSTANCE.sendToServer(new MessageShoot()), 50*burstRate, TimeUnit.MILLISECONDS);
				}
			}
			else {
				MrCrayfishGunMod.proxy.playClientSound(SoundEvents.BLOCK_LEVER_CLICK);
			}
		}
		return new ActionResult<>(EnumActionResult.FAIL, heldItem);
	}

	protected boolean hasBurstAmmo(ItemStack gunStack) {
		NBTTagCompound tag = ItemStackUtil.createTagCompound(gunStack);
		return tag.getBoolean("IgnoreAmmo") || tag.getInteger("AmmoCount") >= shots;
	}

}
