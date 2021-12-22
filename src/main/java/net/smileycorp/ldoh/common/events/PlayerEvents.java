package net.smileycorp.ldoh.common.events;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;
import net.smileycorp.ldoh.common.capabilities.IFollowers;
import net.smileycorp.ldoh.common.capabilities.IMiniRaid;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.tangotek.tektopia.ModItems;

public class PlayerEvents {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//spawner instance for boss event
		if (!entity.hasCapability(LDOHCapabilities.FOLLOWERS, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("Followers"), new IFollowers.Provider((EntityPlayer) entity));
		}
	}

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
						event.getEntityPlayer().playSound(SoundEvents.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
						event.setCanceled(true);
						if (!world.isRemote)  {
							ITextComponent text = new TextComponentTranslation(ModDefinitions.LAVA_PICKUP_MESSAGE);
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
				//lava bucket breaking
				if (!player.isCreative()) {
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
							((EntityPlayerMP)player).connection.sendPacket(new SPacketSoundEffect(SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, player.posX, player.posX, player.posX, 1.0F, 1.0F));
							ITextComponent text = new TextComponentTranslation(ModDefinitions.LAVA_BREAK_MESSAGE);
							text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
							player.sendMessage(text);
						}
					}
				}
				//follower stopping
				if (player.hasCapability(LDOHCapabilities.FOLLOWERS, null)) {
					IFollowers followers = player.getCapability(LDOHCapabilities.FOLLOWERS, null);
					if (player.isSneaking() &! followers.isCrouching()) followers.setCrouching();
					else if (!player.isSneaking() && followers.isCrouching()) followers.setUncrouching();
				}
				if (world.getWorldTime() == 241000) {
					ITextComponent text = new TextComponentTranslation(ModDefinitions.ZOMBIE_EVOLUTION_MESSAGE_0);
					text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
					player.sendMessage(text);
				}
				if (world.getWorldTime() == 481000) {
					ITextComponent text = new TextComponentTranslation(ModDefinitions.ZOMBIE_EVOLUTION_MESSAGE_1);
					text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
					player.sendMessage(text);
				}
				if (world.getWorldTime() >= 1080000 &! GameStageHelper.hasStage(player, "town")) {
					GameStageHelper.addStage(player, "town");
					ITextComponent survivor = new TextComponentTranslation(ModDefinitions.VILLAGER_MESSAGE + ".Survivor");
					survivor.setStyle(new Style().setBold(true));
					player.sendMessage(new TextComponentTranslation(ModDefinitions.VILLAGER_MESSAGE + "0", survivor));
					ITextComponent token = new TextComponentTranslation(ModItems.structureTownHall.getUnlocalizedName());
					token.setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true));
					player.sendMessage(new TextComponentTranslation(ModDefinitions.VILLAGER_MESSAGE + "1", token));				}
			}
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public void playerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		EntityPlayer original = event.getOriginal();
		if (player != null && original != null &!(player instanceof FakePlayer || original instanceof FakePlayer)) {
			if (player.hasCapability(LDOHCapabilities.MINI_RAID, null) && original.hasCapability(LDOHCapabilities.MINI_RAID, null)) {
				IMiniRaid raid = player.getCapability(LDOHCapabilities.MINI_RAID, null);
				raid.readFromNBT(original.getCapability(LDOHCapabilities.MINI_RAID, null).writeToNBT(new NBTTagCompound()));
			}
			if (player.hasCapability(LDOHCapabilities.APOCALYPSE, null) && original.hasCapability(LDOHCapabilities.APOCALYPSE, null)) {
				IApocalypse apocalypse = player.getCapability(LDOHCapabilities.APOCALYPSE, null);
				apocalypse.readFromNBT(original.getCapability(LDOHCapabilities.APOCALYPSE, null).writeToNBT(new NBTTagCompound()));
				apocalypse.setPlayer(player);
			}
			if (player.hasCapability(LDOHCapabilities.FOLLOWERS, null) && original.hasCapability(LDOHCapabilities.FOLLOWERS, null)) {
				IFollowers followers = player.getCapability(LDOHCapabilities.FOLLOWERS, null);
				followers.readFromNBT(original.getCapability(LDOHCapabilities.FOLLOWERS, null).writeToNBT());
			}
		}
	}

	//activate when a player right clicks with an item
	@SubscribeEvent
	public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = player.world;
		if (player.hasCapability(LDOHCapabilities.FOLLOWERS, null) && event.getHand() == EnumHand.MAIN_HAND &! world.isRemote) {
			IFollowers followers = player.getCapability(LDOHCapabilities.FOLLOWERS, null);
			if (followers.isCrouching()) {
				Entity target = DirectionUtils.getPlayerRayTrace(world, player, 4.5f).entityHit;
				if (target instanceof EntityLiving) {
					if (followers.stopFollowing((EntityLiving) target)) {
						event.setCancellationResult(EnumActionResult.FAIL);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	//activate when a player right clicks an entity
	@SubscribeEvent
	public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		World world = player.world;
		if (event.getItemStack().isEmpty() && target instanceof EntityLiving &&player.hasCapability(LDOHCapabilities.FOLLOWERS, null)
				&& event.getHand() == EnumHand.MAIN_HAND &! world.isRemote) {
			IFollowers followers = player.getCapability(LDOHCapabilities.FOLLOWERS, null);
			if (followers.isCrouching()) {
				if (target instanceof EntityLiving) {
					if (followers.stopFollowing((EntityLiving) target)) {
						event.setCancellationResult(EnumActionResult.FAIL);
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
