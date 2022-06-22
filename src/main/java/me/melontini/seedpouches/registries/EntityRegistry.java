package me.melontini.seedpouches.registries;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.FlowerPouchEntity;
import me.melontini.seedpouches.projectile.SeedPouchEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {
    public static final EntityType<SeedPouchEntity> SEED_POUCH_ENTITY = FabricEntityTypeBuilder.<SeedPouchEntity>create(SpawnGroup.MISC, SeedPouchEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeBlocks(4).trackedUpdateRate(10)
            .build();

    public static final EntityType<FlowerPouchEntity> FLOWER_POUCH_ENTITY = FabricEntityTypeBuilder.<FlowerPouchEntity>create(SpawnGroup.MISC, FlowerPouchEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeBlocks(4).trackedUpdateRate(10)
            .build();

    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SeedPouches.ID, "seed_pouch"), SEED_POUCH_ENTITY);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SeedPouches.ID, "flower_pouch"), FLOWER_POUCH_ENTITY);
    }
}
