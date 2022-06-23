package me.melontini.seedpouches.projectile;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.registries.EntityRegistry;
import me.melontini.seedpouches.registries.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import static me.melontini.seedpouches.SeedPouches.SEED_POUCH_PACKET_ID;

public class SaplingPouchEntity extends ThrownItemEntity {
    public SaplingPouchEntity(EntityType<? extends SaplingPouchEntity> entityType, World world) {
        super(entityType, world);
    }

    public SaplingPouchEntity(LivingEntity owner, World world) {
        super(EntityRegistry.SAPLING_POUCH_ENTITY, owner, world);
    }

    public SaplingPouchEntity(double x, double y, double z, World world) {
        super(EntityRegistry.SAPLING_POUCH_ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.SAPLING_POUCH;
    }

    @Override
    protected void onCollision(@NotNull HitResult hitResult) {
        HitResult.Type type = hitResult.getType();

        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onBlockHit((BlockHitResult) hitResult);
        }

        if (type != HitResult.Type.MISS) {
            this.emitGameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        }

    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!world.isClient) {
            Identifier LOOT_ID = new Identifier(SeedPouches.ID, "dropped_saplings");
            PouchLogicMethods.pouchEntityLogic(world, LOOT_ID, this, entityHitResult);
        }
    }

    @Override
    protected void onBlockHit(@NotNull BlockHitResult blockHitResult) {
        if (!world.isClient) {
            Identifier LOOT_ID = new Identifier(SeedPouches.ID, "dropped_saplings");
            PouchLogicMethods.pouchBlockLogic(world, LOOT_ID, this, blockHitResult);
        }
        BlockState blockState = this.world.getBlockState(blockHitResult.getBlockPos());
        blockState.onProjectileHit(this.world, blockState, blockHitResult, this);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return SeedPouchEntitySpawnPacket.create(this, SEED_POUCH_PACKET_ID);
    }
}
