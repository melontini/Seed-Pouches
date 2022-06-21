package me.melontini.seedpouches.projectile;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.client.SeedPouchesClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.melontini.seedpouches.SeedPouches.SEED_POUCH_PACKET_ID;

public class SeedPouchEntity extends ThrownItemEntity {
    public SeedPouchEntity(EntityType<? extends SeedPouchEntity> entityType, World world) {
        super(entityType, world);
    }

    public SeedPouchEntity(LivingEntity owner, World world) {
        super(SeedPouches.SEED_POUCH_ENTITY, owner, world);
    }

    public SeedPouchEntity(double x, double y, double z, World world) {
        super(SeedPouches.SEED_POUCH_ENTITY, x,y,z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return SeedPouches.SEED_POUCH;
    }
    @Override
    protected void onCollision(@NotNull HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        Vec3d pos = hitResult.getPos();

        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onBlockHit((BlockHitResult)hitResult);
        }

        if (type != HitResult.Type.MISS) {
            this.emitGameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        }

    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!world.isClient) {
            Identifier LOOT_ID = new Identifier(SeedPouches.ID, "dropped_seeds");
            List<ItemStack> stacks = (
                    (ServerWorld) world)
                    .getServer()
                    .getLootManager()
                    .getTable(LOOT_ID)
                    .generateLoot((
                            new LootContext.Builder((ServerWorld) world))
                            .random(world.random)
                            .build(LootContextTypes.EMPTY)
                    );
            if (entityHitResult.getEntity().isPlayer()) {
                PlayerEntity player = (PlayerEntity) entityHitResult.getEntity();
                for (ItemStack stack : stacks) {
                    player.getInventory().insertStack(stack);
                }
            } else {
                Vec3d pos = entityHitResult.getEntity().getPos();
                for (ItemStack stack : stacks) {
                    //System.out.println("dropped " + stack.getName());
                    ItemEntity itemEntity = new ItemEntity(
                            world,
                            pos.getX(),
                            pos.getY() + 0.2,
                            pos.getZ(),
                            stack,
                            (Math.random() - 0.5) * 0.5,
                            0,
                            (Math.random() - 0.5) * 0.5);
                    world.spawnEntity(itemEntity);
                }
            }
            this.discard();
        }
    }
    @Override
    protected void onBlockHit(@NotNull BlockHitResult blockHitResult) {
        if (!world.isClient) {
            Identifier LOOT_ID = new Identifier(SeedPouches.ID, "dropped_seeds");
            //System.out.println("loot table should be ready");
            List<ItemStack> stacks = (
                    (ServerWorld) world)
                    .getServer()
                    .getLootManager()
                    .getTable(LOOT_ID)
                    .generateLoot((
                            new LootContext.Builder((ServerWorld) world))
                            .random(world.random)
                            .build(LootContextTypes.EMPTY)
                    );
            Vec3d pos = blockHitResult.getPos();
            for (ItemStack stack : stacks) {
                ItemEntity itemEntity = new ItemEntity(
                        world,
                        pos.getX(),
                        pos.getY() + 0.2,
                        pos.getZ(),
                        stack,
                        (Math.random() - 0.5) * 0.5,
                        0,
                        (Math.random() - 0.5) * 0.5);
                world.spawnEntity(itemEntity);
            }
            this.discard();
        }
        BlockState blockState = this.world.getBlockState(blockHitResult.getBlockPos());
        blockState.onProjectileHit(this.world, blockState, blockHitResult, this);
    }
    @Override
    public Packet<?> createSpawnPacket() {
        return SeedPouchEntitySpawnPacket.create(this, SEED_POUCH_PACKET_ID);
    }
}
