package net.smileycorp.ldoh.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;

import java.util.List;

public class ItemTurretTargetingUpgrade extends Item {

    String name = "Turret_Targeting_Upgrade";

    public ItemTurretTargetingUpgrade() {
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(ModDefinitions.getName(name));
        setRegistryName(ModDefinitions.getResource(name));
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    public enum TargetingMode {
        WHITELIST,
        BLACKLIST,
        PRIORITIZE;
    }

    public static final class TargetingOptions {

        protected final TargetingMode mode;
        protected final List<Class<?>> entities;

        public TargetingOptions(TargetingMode mode, List<Class<?>> entities) {
            this.mode = mode;
            this.entities = entities;
        }

        public int getPriority(Entity entity) {
            if (entities.contains(entity.getClass())) {
                switch (mode) {
                    case WHITELIST:
                        return 0;
                    case BLACKLIST:
                        return -1;
                    case PRIORITIZE:
                        return 1;
                }
            }
            return mode == TargetingMode.WHITELIST ? -1 : 0;
        }

    }

}
