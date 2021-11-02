package net.smileycorp.ldoh.common.events;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.smileycorp.atlas.api.SimpleStringMessage;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;
import net.smileycorp.ldoh.common.capabilities.IMiniRaid;
import net.smileycorp.ldoh.common.network.CommonPacketHandler;

public class PlayerEvents {

	//randomly prevent picking up lava
	@SubscribeEvent
	public void fillBucket(FillBucketEvent event) {
		World world = event.getWorld();
		RayTraceResult ray = event.getTarget();
		if (ray != null) {
			if (ray.typeOfHit == Type.BLOCK) {
				if (world.getBlockState(ray.getBlockPos()).getMaterial() == Material.LAVA) {
					//50% chance to break the bucket
					if (world.rand.nextInt(2) == 0) {
						event.getEmptyBucket().shrink(1);
						event.setCanceled(true);
						if (!world.isRemote)  {
							ITextComponent text = new TextComponentTranslation(ModDefinitions.lavaPickupMessage);
							text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
							event.getEntityPlayer().sendMessage(text);
							event.getEntityPlayer().attackEntityFrom(DamageSource.LAVA, 3);
						}
					}
				}
			}
		}
	}

	//Player ticks
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		if (event.phase == Phase.END) {
			EntityPlayer player = event.player;
			World world = player.world;
			if (!world.isRemote) {
				//toxic gas
				if (player.getPosition().getY()<30) {
					ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
					if (player.ticksExisted%35==0 && !player.isCreative()) {
						//check if player has a gas mask and damage it instead, check damage to prevent it from fully breaking
						if (helm.getItem() == ModContent.GAS_MASK && helm.getMetadata() < ModContent.GAS_MASK.getMaxDamage()) {
							helm.damageItem(1, player);
						} else {
							//deal damage if not wearing it and display message
							player.attackEntityFrom(ModContent.TOXIC_GAS_DAMAGE, 1);
							if (player instanceof EntityPlayerMP) {
								CommonPacketHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(ModDefinitions.gasMessage), (EntityPlayerMP) player);
							}
						}
					}
				}
				//lava bucket breaking
				if (player.inventory.hasItemStack(new ItemStack(Items.LAVA_BUCKET))) {
					//20% chance every second
					if (player.ticksExisted%20 == 0 && world.rand.nextInt(5)==0) {
						//place lava and destroy bucket
						world.setBlockState(player.getPosition(), Blocks.LAVA.getDefaultState());
						if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.LAVA_BUCKET) player.getHeldItem(EnumHand.OFF_HAND).shrink(1);
						else for (ItemStack stack : player.inventory.mainInventory) {
							if (stack.getItem() == Items.LAVA_BUCKET) stack.shrink(1);
							break;
						}
						ITextComponent text = new TextComponentTranslation(ModDefinitions.lavaBreakMessage);
						text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
						player.sendMessage(text);
					}
				}
			}
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public void playerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		EntityPlayer original = event.getOriginal();
		if (player != null && original != null &!(player instanceof FakePlayer || original instanceof FakePlayer)) {
			if (player.hasCapability(ModContent.MINI_RAID, null) && original.hasCapability(ModContent.MINI_RAID, null)) {
				IMiniRaid raid = player.getCapability(ModContent.MINI_RAID, null);
				raid.readFromNBT(original.getCapability(ModContent.MINI_RAID, null).writeToNBT(new NBTTagCompound()));
				raid.setPlayer(player);
			}
			if (player.hasCapability(ModContent.APOCALYPSE, null) && original.hasCapability(ModContent.APOCALYPSE, null)) {
				IApocalypse apocalypse = player.getCapability(ModContent.APOCALYPSE, null);
				apocalypse.readFromNBT(original.getCapability(ModContent.APOCALYPSE, null).writeToNBT(new NBTTagCompound()));
				apocalypse.setPlayer(player);
			}
		}
	}

}
