package me.melontini.seedpouches.registries;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.projectile.types.FlowerPouchEntity;
import me.melontini.seedpouches.projectile.types.SaplingPouchEntity;
import me.melontini.seedpouches.projectile.types.SeedPouchEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {
    public static final EntityType<AbstractPouchEntity> SEED_POUCH_ENTITY = FabricEntityTypeBuilder.<AbstractPouchEntity>create(SpawnGroup.MISC, SeedPouchEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeChunks(4).trackedUpdateRate(10).build();

    public static final EntityType<AbstractPouchEntity> FLOWER_POUCH_ENTITY = FabricEntityTypeBuilder.<AbstractPouchEntity>create(SpawnGroup.MISC, FlowerPouchEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeChunks(4).trackedUpdateRate(10).build();

    public static final EntityType<AbstractPouchEntity> SAPLING_POUCH_ENTITY = FabricEntityTypeBuilder.<AbstractPouchEntity>create(SpawnGroup.MISC, SaplingPouchEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeChunks(4).trackedUpdateRate(10).build();

    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SeedPouches.ID, "seed_pouch"), SEED_POUCH_ENTITY);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SeedPouches.ID, "flower_pouch"), FLOWER_POUCH_ENTITY);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SeedPouches.ID, "sapling_pouch"), SAPLING_POUCH_ENTITY);
    }
}
