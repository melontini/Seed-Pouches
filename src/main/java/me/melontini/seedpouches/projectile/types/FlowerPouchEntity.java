package me.melontini.seedpouches.projectile.types;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.registries.EntityRegistry;
import me.melontini.seedpouches.registries.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FlowerPouchEntity extends AbstractPouchEntity {
    public FlowerPouchEntity(EntityType<AbstractPouchEntity> entityType, World world) {
        super(entityType, world);
    }

    public FlowerPouchEntity(LivingEntity owner, World world) {
        super(EntityRegistry.FLOWER_POUCH_ENTITY, owner, world);
    }

    public FlowerPouchEntity(double x, double y, double z, World world) {
        super(EntityRegistry.FLOWER_POUCH_ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.FLOWER_POUCH;
    }

    @Override
    public Identifier[] getLootId() {
        return new Identifier[]{new Identifier(SeedPouches.ID, "dropped_flowers")};
    }
}
