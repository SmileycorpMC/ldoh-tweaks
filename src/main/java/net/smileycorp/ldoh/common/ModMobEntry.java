package net.smileycorp.ldoh.common;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.ldoh.common.entity.zombie.EntityTF2Zombie;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.util.TF2Class;

import javax.annotation.Nullable;

public class ModMobEntry {

    protected TF2Class tfclass = null;
    protected Class<? extends EntityLiving> clazz;
    protected String unlocalisedName = "";
    protected final int foreground;
    protected final int background;
    protected String team = "";
    protected NBTTagCompound nbt;

    public ModMobEntry(TF2Class tfclass, int foreground, int background, String team) {
        this.tfclass = tfclass;
        this.foreground = foreground;
        this.background = background;
        this.team = team;
    }

    public ModMobEntry(Class<? extends EntityLiving> clazz, String unlocalisedName, int foreground, int background) {
        this(clazz, unlocalisedName, foreground, background, null);
    }

    public ModMobEntry(Class<? extends EntityLiving> clazz, String unlocalisedName, int foreground, int background, @Nullable NBTTagCompound nbt) {
        this.clazz = clazz;
        this.unlocalisedName = unlocalisedName;
        this.foreground = foreground;
        this.background = background;
        this.nbt = nbt;
    }

    public int getForegroundColour() {
        return foreground;
    }

    public int getBackgroundColour() {
        return background;
    }

    //get the entities name
    public String getLocalisedName() {
        if (tfclass != null) {
            return TextUtils.toProperCase(team) + " " + I18n.translateToLocal("entity.Zombie.name") + " " + tfclass.getLocalizedName();
        } else {
            return I18n.translateToLocal(unlocalisedName);
        }
    }

    public EntityLiving getEntityToSpawn(World world, BlockPos pos) {
        EntityLiving entity = null;
        try {
            //create tf2 zombie with the right class
            if (tfclass != null) {
                EntityTF2Character baseentity = tfclass.createEntity(world);
                baseentity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
                entity = new EntityTF2Zombie(baseentity);
                ((EntityTF2Zombie) entity).setTFTeam(world.getScoreboard().getTeam(team));
                //spawn entity regularly if entry isn't for a tf2 zombie
            } else {
                entity = clazz.getConstructor(World.class).newInstance(world);
                if (nbt != null) entity.readFromNBT(nbt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    //does this item contain the specified entity
    public boolean doesMatch(EntityLiving entity) {
        if (entity instanceof EntityTF2Zombie) {
            if (tfclass == null || team == null) return false;
            return tfclass == ((EntityTF2Zombie) entity).getTFClass() && team.equals(((EntityTF2Zombie) entity).getTFTeam().getName());
        }
        return entity.getClass() == clazz;
    }

}
