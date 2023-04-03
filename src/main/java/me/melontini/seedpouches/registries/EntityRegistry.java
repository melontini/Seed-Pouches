package me.melontini.seedpouches.registries;

import me.melontini.crackerutil.content.RegistryUtil;
import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.projectile.types.FlowerPouchEntity;
import me.melontini.seedpouches.projectile.types.SaplingPouchEntity;
import me.melontini.seedpouches.projectile.types.SeedPouchEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;

public class EntityRegistry {
    public static final EntityType<AbstractPouchEntity> SEED_POUCH_ENTITY = RegistryUtil.createEntityType(new Identifier(SeedPouches.ID, "seed_pouch"), EntityType.Builder.<AbstractPouchEntity>create(SeedPouchEntity::new, SpawnGroup.MISC)
            .setDimensions(0.25F, 0.25F)
            .maxTrackingRange(4).trackingTickInterval(10));

    public static final EntityType<AbstractPouchEntity> FLOWER_POUCH_ENTITY = RegistryUtil.createEntityType(new Identifier(SeedPouches.ID, "flower_pouch"), EntityType.Builder.<AbstractPouchEntity>create(FlowerPouchEntity::new, SpawnGroup.MISC)
            .setDimensions(0.25F, 0.25F)
            .maxTrackingRange(4).trackingTickInterval(10));

    public static final EntityType<AbstractPouchEntity> SAPLING_POUCH_ENTITY = RegistryUtil.createEntityType(new Identifier(SeedPouches.ID, "sapling_pouch"), EntityType.Builder.<AbstractPouchEntity>create(SaplingPouchEntity::new, SpawnGroup.MISC)
            .setDimensions(0.25F, 0.25F)
            .maxTrackingRange(4).trackingTickInterval(10));

    public static void register() {
    }
}
