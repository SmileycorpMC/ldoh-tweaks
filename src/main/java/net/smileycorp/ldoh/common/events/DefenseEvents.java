package net.smileycorp.ldoh.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.ldoh.common.damage.DamageSourceBarbedWire;
import net.smileycorp.ldoh.common.entity.zombie.EntityIncendiaryProjectile;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

public class DefenseEvents {

    //apply looting to barbed wire kills
    @SubscribeEvent
    public void getLootingModifier(LootingLevelEvent event) {
        DamageSource cause = event.getDamageSource();
        if (cause instanceof DamageSourceBarbedWire) {
            TileBarbedWire tile = ((DamageSourceBarbedWire) cause).getSource();
            if (tile.hasEnchantment(Enchantments.LOOTING)) {
                event.setLootingLevel(tile.getEnchantmentLevel(Enchantments.LOOTING));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDamage(LivingDamageEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Entity attacker = event.getSource().getImmediateSource();
        if (attacker instanceof EntityIncendiaryProjectile) {
            entity.setFire(2);
        }
    }

}
