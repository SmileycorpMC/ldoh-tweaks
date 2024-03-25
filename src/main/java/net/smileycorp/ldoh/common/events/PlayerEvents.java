package net.smileycorp.ldoh.common.events;

import com.mrcrayfish.furniture.init.FurnitureItems;
import com.mrcrayfish.furniture.tileentity.TileEntityCrate;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IAmbushEvent;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;
import net.smileycorp.ldoh.common.capabilities.IFollowers;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.world.WorldDataSafehouse;

public class PlayerEvents {
    
    //cache to store right-clicked block pos
    public static BlockPos BED_POS = null;

    //capability manager
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        //spawner instance for boss event
        if (!entity.hasCapability(LDOHCapabilities.FOLLOWERS, null) && entity instanceof EntityPlayer & !(entity instanceof FakePlayer)) {
            event.addCapability(ModDefinitions.getResource("Followers"), new IFollowers.Provider((EntityPlayer) entity));
        }
    }

    //prevent picking and placing lava and other hot liquids
    @SubscribeEvent
    public void fillBucket(FillBucketEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (isHot(event.getEmptyBucket(), player) || isHot(event.getFilledBucket(), player)) {
            event.setResult(Event.Result.DENY);
            event.setCanceled(true);
        } else if (event.getTarget() != null && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = event.getTarget().getBlockPos();
            IBlockState state = player.world.getBlockState(pos);
            if (state == null) return;
            if (state.getMaterial() == Material.LAVA) {
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
                return;
            } else if (!(state.getBlock() instanceof IFluidBlock)) return;
            Fluid fluid = ((IFluidBlock) state.getBlock()).getFluid();
            if (fluid == null) return;
            if (fluid == LDOHFluids.EXPERIENCE) {
                event.setResult(Event.Result.ALLOW);
                event.setFilledBucket(new ItemStack(LDOHItems.EXPERIENCE_BUCKET));
            }
            if (fluid.getTemperature() >= 450) {
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
            }
        }
    }

    private static boolean isHot(ItemStack stack, EntityPlayer player) {
        if (stack == null || player == null) return false;
        if (stack.getItem() == Items.LAVA_BUCKET & !player.capabilities.isCreativeMode) return true;
        if (stack.getItem() instanceof UniversalBucket & !player.capabilities.isCreativeMode) {
            UniversalBucket bucket = (UniversalBucket) stack.getItem();
            Fluid fluid = bucket.getFluid(stack).getFluid();
            if (fluid.getTemperature() >= 450 || fluid.getBlock().getDefaultState().getMaterial() == Material.LAVA)
                return true;
        }
        return false;
    }


    //Player ticks
    @SubscribeEvent
    public void playerTick(PlayerTickEvent event) {
        if (event.phase == Phase.END) {
            EntityPlayer player = event.player;
            World world = player.world;
            if (!world.isRemote) {
                //follower stopping
                if (player.hasCapability(LDOHCapabilities.FOLLOWERS, null)) {
                    IFollowers followers = player.getCapability(LDOHCapabilities.FOLLOWERS, null);
                    if (player.isSneaking() & !followers.isCrouching()) followers.setCrouching();
                    else if (!player.isSneaking() && followers.isCrouching()) followers.setUncrouching();
                }
                if (world.getWorldTime() == 265000) {
                    ITextComponent text = new TextComponentTranslation(ModDefinitions.ZOMBIE_EVOLUTION_MESSAGE_0);
                    text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
                    player.sendMessage(text);
                }
                if (world.getWorldTime() == 505000) {
                    ITextComponent text = new TextComponentTranslation(ModDefinitions.ZOMBIE_EVOLUTION_MESSAGE_1);
                    text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
                    player.sendMessage(text);
                }
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void playerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            WorldDataSafehouse data = WorldDataSafehouse.getData(event.getOriginal().world);
            if (data != null) data.hideBasement(event.getOriginal().world);
        }
        EntityPlayer player = event.getEntityPlayer();
        EntityPlayer original = event.getOriginal();
        if (player != null && original != null & !(player instanceof FakePlayer || original instanceof FakePlayer)) {
            if (player.hasCapability(LDOHCapabilities.AMBUSH, null) && original.hasCapability(LDOHCapabilities.AMBUSH, null)) {
                IAmbushEvent raid = player.getCapability(LDOHCapabilities.AMBUSH, null);
                raid.readFromNBT(original.getCapability(LDOHCapabilities.AMBUSH, null).writeToNBT(new NBTTagCompound()));
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
        if (player.hasCapability(LDOHCapabilities.FOLLOWERS, null) && event.getHand() == EnumHand.MAIN_HAND & !world.isRemote) {
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
        if (event.getItemStack().isEmpty() && target instanceof EntityLiving && player.hasCapability(LDOHCapabilities.FOLLOWERS, null)
                && event.getHand() == EnumHand.MAIN_HAND & !world.isRemote) {
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

    //activate when a player right clicks a block
    @SubscribeEvent
    public void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        if (!world.isRemote) {
            BlockPos pos = event.getPos();
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityCrate) {
                if (((TileEntityCrate) tile).sealed) {
                    if (event.getItemStack().getItem() != FurnitureItems.CROWBAR)
                        player.sendMessage(new TextComponentTranslation("message.ldoh.SealedCrate"));
                }
            }
        }
    }

    @SubscribeEvent
    public void playerJoin(PlayerLoggedInEvent event) {
        if (event.player == null || event.player.world.isRemote) return;
        WorldDataSafehouse data = WorldDataSafehouse.getData(event.player.world);
        if (!data.isGenerated()) data.generate(event.player.world);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        System.out.println("slimb");
        if (event.getEntity() == null) return;
        System.out.println("schlamb");
        if (event.getEntity().world.isRemote) return;
        System.out.println("schwubwub");
        //sleeping overhaul checks if the clicked block is an instance of BlockHorizontal, cache the blockstate
        //so we can make it treat cfm beds as vanilla ones
        IBlockState state = event.getEntity().world.getBlockState(event.getPos());
        if (state.getBlock() instanceof BlockHorizontal) return;
        System.out.println("galalkalal");
        BED_POS = event.getPos();
    }

}
