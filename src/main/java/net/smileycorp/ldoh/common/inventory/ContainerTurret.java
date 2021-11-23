package net.smileycorp.ldoh.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class ContainerTurret extends Container {

	protected EntityTurret turret;
	protected World world;
	protected InventoryTurret inv;
	protected InventoryPlayer playerInv;

	public ContainerTurret(EntityTurret turret, EntityPlayer player){
		this.turret = turret;
		world = turret.world;
		inv = turret.getInventory();
		playerInv = player.inventory;
		//turret inventory
		for (int i = 0; i< inv.getSizeInventory(); i++) {
			addSlotToContainer(new Slot(turret.getInventory(), i, 79 + i*18, 53));
		}
		//player inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		//hotbar
		for (int k = 0; k < 9; ++k) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		if (turret.getTeam() == null) return player.getTeam() == null;
		return turret.getTeam().equals(player.getTeam());
	}

}
