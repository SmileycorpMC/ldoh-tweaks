package net.smileycorp.ldoh.common.item;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.common.ProjectileFactory;
import com.mrcrayfish.guns.common.SpreadHandler;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.network.message.MessageMuzzleFlash;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.network.MessageBurstShoot;

public class ItemBurstGun extends ItemGun {

	protected final int shots, burstRate;

	private static final Predicate<EntityLivingBase> NOT_AGGRO_EXEMPT = entity -> !(entity instanceof EntityPlayer) && !GunConfig.AggroMobs.exemptClasses.contains(entity.getClass());

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
		//if (modifiedGun.general.auto) return super.onItemRightClick(worldIn, playerIn, handIn);
		System.out.println("cunk");
		if(worldIn.isRemote) {
			System.out.println("kronk");
			if(!MrCrayfishGunMod.proxy.canShoot()) {
				return new ActionResult<>(EnumActionResult.FAIL, heldItem);
			}

			System.out.println("krungus");

			if(hasBurstAmmo(heldItem) || playerIn.capabilities.isCreativeMode) {
				System.out.println("krombus");
				if(playerIn.isHandActive()) {
					return new ActionResult<>(EnumActionResult.FAIL, heldItem);
				}
				playerIn.setActiveHand(handIn);
				CooldownTracker tracker = playerIn.getCooldownTracker();
				if(!tracker.hasCooldown(heldItem.getItem())) {
					tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
					PacketHandler.INSTANCE.sendToServer(new MessageShoot());
					System.out.println("kringus");
					for (int i = 0; i < shots -1; i++) {
						System.out.println("krongus");
						LDOHTweaks.DELAYED_THREAD_EXECUTOR.schedule(()-> {
							System.out.println("krillbus");
							net.smileycorp.ldoh.common.network.PacketHandler.NETWORK_INSTANCE.sendToServer(new MessageBurstShoot());
						}, 50*burstRate*(i+1), TimeUnit.MILLISECONDS);
					}


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

	public static void fire(World worldIn, EntityPlayer playerIn, ItemStack heldItem)
	{
		if(worldIn.isRemote)
		{
			return;
		}

		ItemGun item = (ItemGun) heldItem.getItem();

		if(playerIn.getDataManager().get(CommonEvents.RELOADING))
		{
			playerIn.getDataManager().set(CommonEvents.RELOADING, false);
		}

		boolean silenced = Gun.getAttachment(IAttachment.Type.BARREL, heldItem).getItem() == ModGuns.SILENCER;

		Gun modifiedGun = item.getModifiedGun(heldItem);
		if(!modifiedGun.general.alwaysSpread && modifiedGun.general.spread > 0F)
		{
			SpreadHandler.getSpreadTracker(playerIn.getUniqueID()).update(item);
		}

		for(int i = 0; i < modifiedGun.general.projectileAmount; i++)
		{
			ProjectileFactory factory = AmmoRegistry.getInstance().getFactory(modifiedGun.projectile.item);
			EntityProjectile bullet = factory.create(worldIn, playerIn, item, modifiedGun);
			bullet.setWeapon(heldItem);
			bullet.setAdditionalDamage(getAdditionalDamage(heldItem));
			if(silenced) {
				bullet.setDamageModifier(0.75F);
			}
			worldIn.spawnEntity(bullet);

			if(!modifiedGun.projectile.visible)
			{
				MessageBullet messageBullet = new MessageBullet(bullet.getEntityId(), bullet.posX, bullet.posY, bullet.posZ, bullet.motionX, bullet.motionY, bullet.motionZ, modifiedGun.projectile.trailColor, modifiedGun.projectile.trailLengthMultiplier);
				PacketHandler.INSTANCE.sendToAllAround(messageBullet, new NetworkRegistry.TargetPoint(playerIn.dimension, playerIn.posX, playerIn.posY, playerIn.posZ, GunConfig.SERVER.network.projectileTrackingRange));
				PacketHandler.INSTANCE.sendTo(messageBullet, (EntityPlayerMP) playerIn);
			}
		}

		if(GunConfig.SERVER.aggroMobs.enabled)
		{
			double r = silenced ? GunConfig.SERVER.aggroMobs.rangeSilenced : GunConfig.SERVER.aggroMobs.rangeUnsilenced;
			double x = playerIn.posX + 0.5;
			double y = playerIn.posY + 0.5;
			double z = playerIn.posZ + 0.5;
			AxisAlignedBB box = new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r);
			r *= r;
			double dx, dy, dz;
			for(EntityLivingBase entity : playerIn.world.getEntitiesWithinAABB(EntityLivingBase.class, box, NOT_AGGRO_EXEMPT))
			{
				dx = x - entity.posX;
				dy = y - entity.posY;
				dz = z - entity.posZ;
				if(dx * dx + dy * dy + dz * dz <= r)
				{
					entity.setRevengeTarget(GunConfig.SERVER.aggroMobs.angerHostileMobs ? playerIn : entity);
				}
			}
		}

		if(silenced)
		{
			String silencedSound = modifiedGun.sounds.getSilencedFire(modifiedGun);
			SoundEvent event = ModSounds.getSound(silencedSound);
			if(event == null)
			{
				event = SoundEvent.REGISTRY.getObject(new ResourceLocation(silencedSound));
			}
			if(event != null)
			{
				worldIn.playSound(null, playerIn.getPosition(), event, SoundCategory.HOSTILE, 1F, 0.8F + itemRand.nextFloat() * 0.2F);
			}
		}
		else
		{
			String fireSound = modifiedGun.sounds.getFire(modifiedGun);
			SoundEvent event = ModSounds.getSound(fireSound);
			if(event == null)
			{
				event = SoundEvent.REGISTRY.getObject(new ResourceLocation(fireSound));
			}
			if(event != null)
			{
				worldIn.playSound(null, playerIn.getPosition(), event, SoundCategory.HOSTILE, 5.0F, 0.8F + itemRand.nextFloat() * 0.2F);
			}
		}

		if(modifiedGun.display.flash != null)
		{
			PacketHandler.INSTANCE.sendTo(new MessageMuzzleFlash(), (EntityPlayerMP) playerIn);
		}

		if(!playerIn.capabilities.isCreativeMode)
		{
			NBTTagCompound tag = ItemStackUtil.createTagCompound(heldItem);
			if(!tag.getBoolean("IgnoreAmmo"))
			{
				tag.setInteger("AmmoCount", Math.max(0, tag.getInteger("AmmoCount") - 1));
			}
		}

	}

	private static float getAdditionalDamage(ItemStack gunStack)
	{
		NBTTagCompound tag = ItemStackUtil.createTagCompound(gunStack);
		return tag.getFloat("AdditionalDamage");
	}

}
