package me.melontini.seedpouches.projectile.types;

import me.melontini.seedpouches.items.AbstractPouchItem;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.registries.EntityRegistry;
import me.melontini.seedpouches.registries.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class SeedPouchEntity extends AbstractPouchEntity {
    public SeedPouchEntity(EntityType<AbstractPouchEntity> entityType, World world) {
        super(entityType, world);
    }

    public SeedPouchEntity(LivingEntity owner, World world) {
        super(EntityRegistry.SEED_POUCH_ENTITY, owner, world);
    }

    public SeedPouchEntity(double x, double y, double z, World world) {
        super(EntityRegistry.SEED_POUCH_ENTITY, x, y, z, world);
    }

    @Override
    protected AbstractPouchItem getDefaultItem() {
        return ItemRegistry.SEED_POUCH;
    }
}
