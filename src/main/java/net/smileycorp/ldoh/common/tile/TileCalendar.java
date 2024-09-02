package net.smileycorp.ldoh.common.tile;

import net.minecraft.tileentity.TileEntity;

import java.time.LocalDate;

public class TileCalendar extends TileEntity {
    
    public LocalDate getDate() {
        return LocalDate.of(2041, 9, 4).plusDays(getDay());
    }
    
    private int getDay() {
        return (int) (world.getWorldTime() / 240000);
    }
    
    
}
