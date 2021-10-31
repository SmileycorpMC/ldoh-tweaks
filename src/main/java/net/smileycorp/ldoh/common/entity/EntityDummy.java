package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.util.EnumTFClass;

public class EntityDummy extends EntityLiving {

	protected static final DataParameter<Byte> TF_TEAM = EntityDataManager.createKey(EntityTFZombie.class, DataSerializers.BYTE);
	protected static final DataParameter<String> TF_CLASS = EntityDataManager.createKey(EntityTFZombie.class, DataSerializers.STRING);

	protected Team tfteam;
	protected EnumTFClass tfclass;

	public EntityDummy(World world) {
		super(world);
		setRandomClass();
		setRandomTeam();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TF_TEAM, Byte.valueOf((byte)0));
		dataManager.register(TF_CLASS, "demoman");
	}

	public void setTFTeam(Team tfteam) {
		if (tfteam == world.getScoreboard().getTeam("BLU") || tfteam == world.getScoreboard().getTeam("RED")) {
			this.tfteam = tfteam;
			dataManager.set(TF_TEAM, (byte)getTeamKey(tfteam));
		} else setRandomTeam();
	}

	public Team getTFTeam() {
		return getTeam(dataManager.get(TF_TEAM).intValue());
	}

	public void setTFClass(EnumTFClass tfclass) {
		this.tfclass = tfclass;
		dataManager.set(TF_CLASS, tfclass.name());
	}

	public EnumTFClass getTFClass() {
		return EnumTFClass.valueOf(dataManager.get(TF_CLASS));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("tfteam", getTeamKey(tfteam));
		compound.setString("tfclass", tfclass.name());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("tfteam")) {
			setTFTeam(getTeam(compound.getInteger("tfteam")));
		}
		if (compound.hasKey("tfclass")) {
			setTFClass(EnumTFClass.valueOf(compound.getString("tfclass")));
		}

	}

	private void setRandomTeam() {
		setTFTeam(getTeam(rand.nextInt(2)));
	}

	private Team getTeam(int key) {
		return key == 0 ? world.getScoreboard().getTeam("BLU") : world.getScoreboard().getTeam("RED");
	}

	private int getTeamKey(Team team) {
		return team == world.getScoreboard().getTeam("RED") ? 1 : 0;
	}

	private void setRandomClass() {
		setTFClass(EnumTFClass.getRandomClass());
	}

}
