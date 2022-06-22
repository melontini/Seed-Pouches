package me.melontini.seedpouches.projectile;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.access.ChestBlockEntityAccess;
import me.melontini.seedpouches.registries.EntityRegistry;
import me.melontini.seedpouches.registries.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.melontini.seedpouches.SeedPouches.SEED_POUCH_PACKET_ID;

public class FlowerPouchEntity extends ThrownItemEntity {
    public FlowerPouchEntity(EntityType<? extends FlowerPouchEntity> entityType, World world) {
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
    protected void onCollision(@NotNull HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        Vec3d pos = hitResult.getPos();

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
            Identifier LOOT_ID = new Identifier(SeedPouches.ID, "dropped_flowers");
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
                    spawnItem(pos, stack);
                }
            }
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(@NotNull BlockHitResult blockHitResult) {
        if (!world.isClient) {
            Vec3d pos = blockHitResult.getPos();
            BlockPos blockPos = blockHitResult.getBlockPos();
            Identifier LOOT_ID = new Identifier(SeedPouches.ID, "dropped_flowers");
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
            if (world.getBlockState(blockPos).getBlock() instanceof ChestBlock chest) {
                ChestBlockEntity chestBlockEntity = (ChestBlockEntity) world.getBlockEntity(blockPos);
                assert chestBlockEntity != null;
                //System.out.println(((ChestBlockEntityAccess) chestBlockEntity).isOpen());

                for (ItemStack stack : stacks) {
                    if (((ChestBlockEntityAccess) chestBlockEntity).isOpen()) {
                        Inventory inventory = ChestBlock.getInventory(chest, world.getBlockState(blockPos), world, blockPos, true);
                        assert inventory != null;
                        int slot = getEmptyOrIdenticalSlotIndex(stack, inventory);
                        //omg a switch statement. I can already feel them performance improvements.
                        switch (slot) {
                            case -1:
                                spawnItem(pos, stack);
                                break;
                            default:
                                if (!inventory.getStack(slot).isEmpty()) {
                                    int count = inventory.getStack(slot).getCount();
                                    int stackCount = stack.getCount();
                                    stack.setCount(stackCount + count);
                                    inventory.setStack(slot, stack);
                                } else {
                                    inventory.setStack(slot, stack);
                                }
                                break;
                        }
                    } else {
                        spawnItem(pos, stack);
                    }
                }
                this.discard();
            } else if (world.getBlockState(blockPos).getBlock() instanceof BarrelBlock) {
                BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity) world.getBlockEntity(blockPos);
                assert barrelBlockEntity != null;

                for (ItemStack stack : stacks) {
                    if (((ChestBlockEntityAccess) barrelBlockEntity).isOpen()) {
                        int slot = getBarrelEmptyOrIdenticalSlotIndex(stack, barrelBlockEntity);
                        switch (slot) {
                            case -1:
                                spawnItem(pos, stack);
                                break;
                            default:
                                if (!barrelBlockEntity.getStack(slot).isEmpty()) {
                                    int count = barrelBlockEntity.getStack(slot).getCount();
                                    int stackCount = stack.getCount();
                                    stack.setCount(stackCount + count);
                                    barrelBlockEntity.setStack(slot, stack);
                                } else {
                                    barrelBlockEntity.setStack(slot, stack);
                                }
                                break;
                        }
                    } else {
                        spawnItem(pos, stack);
                    }
                    this.discard();
                }
            } else if (world.getBlockState(blockPos).getBlock() instanceof ShulkerBoxBlock) {
                ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) world.getBlockEntity(blockPos);
                assert shulkerBoxBlockEntity != null;

                for (ItemStack stack : stacks) {
                    if (((ChestBlockEntityAccess) shulkerBoxBlockEntity).isOpen()) {
                        int slot = getShulkerEmptyOrIdenticalSlotIndex(stack, shulkerBoxBlockEntity);
                        switch (slot) {
                            case -1:
                                spawnItem(pos, stack);
                                break;
                            default:
                                if (!shulkerBoxBlockEntity.getStack(slot).isEmpty()) {
                                    int count = shulkerBoxBlockEntity.getStack(slot).getCount();
                                    int stackCount = stack.getCount();
                                    stack.setCount(stackCount + count);
                                    shulkerBoxBlockEntity.setStack(slot, stack);
                                } else {
                                    shulkerBoxBlockEntity.setStack(slot, stack);
                                }
                                break;
                        }
                    } else {
                        spawnItem(pos, stack);
                    }
                    this.discard();
                }
            } else if (world.getBlockState(blockPos).getBlock() instanceof DispenserBlock) {
                DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity) world.getBlockEntity(blockPos);
                assert dispenserBlockEntity != null;
                for (ItemStack stack : stacks) {
                    int slot = getDispenserEmptyOrIdenticalSlotIndex(stack, dispenserBlockEntity);
                    switch (slot) {
                        case -1:
                            spawnItem(pos, stack);
                            break;
                        default:
                            if (!dispenserBlockEntity.getStack(slot).isEmpty()) {
                                int count = dispenserBlockEntity.getStack(slot).getCount();
                                int stackCount = stack.getCount();
                                stack.setCount(stackCount + count);
                                dispenserBlockEntity.setStack(slot, stack);
                            } else {
                                dispenserBlockEntity.setStack(slot, stack);
                            }
                            break;
                    }
                }
                this.discard();
            } else if (world.getBlockState(blockPos).getBlock() instanceof DropperBlock) {
                DropperBlockEntity dropperBlockEntity = (DropperBlockEntity) world.getBlockEntity(blockPos);
                assert dropperBlockEntity != null;
                for (ItemStack stack : stacks) {
                    int slot = getDropperEmptyOrIdenticalSlotIndex(stack, dropperBlockEntity);
                    switch (slot) {
                        case -1:
                            spawnItem(pos, stack);
                            break;
                        default:
                            if (!dropperBlockEntity.getStack(slot).isEmpty()) {
                                int count = dropperBlockEntity.getStack(slot).getCount();
                                int stackCount = stack.getCount();
                                stack.setCount(stackCount + count);
                                dropperBlockEntity.setStack(slot, stack);
                            } else {
                                dropperBlockEntity.setStack(slot, stack);
                            }
                            break;
                    }
                }
                this.discard();
            } else {
                for (ItemStack stack : stacks) {
                    spawnItem(pos, stack);
                }
                this.discard();
            }
        }
        BlockState blockState = this.world.getBlockState(blockHitResult.getBlockPos());
        blockState.onProjectileHit(this.world, blockState, blockHitResult, this);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return SeedPouchEntitySpawnPacket.create(this, SEED_POUCH_PACKET_ID);
    }

    private void spawnItem(BlockPos pos, ItemStack stack) {
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

    private void spawnItem(Vec3d pos, ItemStack stack) {
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

    //ok
    public int getEmptyOrIdenticalSlotIndex(ItemStack stack, Inventory inventory) {
        assert inventory != null;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).getItem() == stack.getItem()) {
                int a = stack.getCount();
                int b = inventory.getStack(i).getCount();
                if (!((a + b) > stack.getMaxCount())) {
                    return i;
                }
            } else if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public int getBarrelEmptyOrIdenticalSlotIndex(ItemStack stack, BarrelBlockEntity entity) {
        for (int i = 0; i < entity.size(); i++) {
            if (entity.getStack(i).getItem() == stack.getItem()) {
                int a = stack.getCount();
                int b = entity.getStack(i).getCount();
                if (!((a + b) > stack.getMaxCount())) {
                    return i;
                }
            } else if (entity.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public int getShulkerEmptyOrIdenticalSlotIndex(ItemStack stack, ShulkerBoxBlockEntity entity) {
        for (int i = 0; i < entity.size(); i++) {
            if (entity.getStack(i).getItem() == stack.getItem()) {
                int a = stack.getCount();
                int b = entity.getStack(i).getCount();
                if (!((a + b) > stack.getMaxCount())) {
                    return i;
                }
            } else if (entity.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public int getDispenserEmptyOrIdenticalSlotIndex(ItemStack stack, DispenserBlockEntity entity) {
        for (int i = 0; i < entity.size(); i++) {
            if (entity.getStack(i).getItem() == stack.getItem()) {
                int a = stack.getCount();
                int b = entity.getStack(i).getCount();
                if (!((a + b) > stack.getMaxCount())) {
                    return i;
                }
            } else if (entity.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public int getDropperEmptyOrIdenticalSlotIndex(ItemStack stack, DispenserBlockEntity entity) {
        for (int i = 0; i < entity.size(); i++) {
            if (entity.getStack(i).getItem() == stack.getItem()) {
                int a = stack.getCount();
                int b = entity.getStack(i).getCount();
                if (!((a + b) > stack.getMaxCount())) {
                    return i;
                }
            } else if (entity.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
}
