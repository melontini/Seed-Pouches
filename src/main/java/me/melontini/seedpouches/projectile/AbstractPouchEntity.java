package me.melontini.seedpouches.projectile;

import me.melontini.seedpouches.access.OpenableBlockEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
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

public abstract class AbstractPouchEntity extends ThrownItemEntity {
    public AbstractPouchEntity(EntityType<AbstractPouchEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbstractPouchEntity(EntityType<AbstractPouchEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
    }

    public AbstractPouchEntity(EntityType<AbstractPouchEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
    }

    public static int getEmptyOrIdenticalSlotIndex(ItemStack stack, Inventory inventory) {
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

    public static void spawnItem(Vec3d pos, ItemStack stack, World world) {
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

    @Override
    protected abstract Item getDefaultItem();

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
            for (Identifier identifier : getLootId()) {
                List<ItemStack> stacks = (
                        (ServerWorld) world)
                        .getServer()
                        .getLootManager()
                        .getTable(identifier)
                        .generateLoot((
                                new LootContext.Builder((ServerWorld) world))
                                .random(world.random)
                                .build(LootContextTypes.EMPTY)
                        );
                if (entityHitResult.getEntity().isPlayer()) {
                    PlayerEntity player = (PlayerEntity) entityHitResult.getEntity();
                    for (ItemStack stack : stacks) {
                        player.getInventory().offerOrDrop(stack);
                    }
                } else if (entityHitResult.getEntity() instanceof MerchantEntity merchant) {
                    for (ItemStack stack : stacks) {
                        int slot = getEmptyOrIdenticalSlotIndex(stack, merchant.getInventory());
                        if (slot == -1) {
                            spawnItem(merchant.getPos(), stack, world);
                        } else {
                            if (!merchant.getInventory().getStack(slot).isEmpty()) {
                                int count = merchant.getInventory().getStack(slot).getCount();
                                int stackCount = stack.getCount();
                                stack.setCount(stackCount + count);
                                merchant.getInventory().setStack(slot, stack);
                            } else {
                                merchant.getInventory().setStack(slot, stack);
                            }
                        }
                    }
                } else {
                    Vec3d pos = entityHitResult.getEntity().getPos();
                    stacks.forEach(stack -> spawnItem(pos, stack, world));
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
            for (Identifier identifier : getLootId()) {
                List<ItemStack> stacks = ((ServerWorld) world).getServer()
                        .getLootManager().getTable(identifier).generateLoot((
                                new LootContext.Builder((ServerWorld) world)).random(world.random)
                                .build(LootContextTypes.EMPTY)
                        );
                if (world.getBlockState(blockPos).getBlock() instanceof BlockWithEntity blockWithEntity) {
                    BlockEntity blockEntity = world.getBlockEntity(blockPos);
                    if (blockEntity instanceof OpenableBlockEntityAccess access) {
                        if (!access.isOpen()) {
                            stacks.forEach(stack -> spawnItem(pos, stack, world));
                            this.discard();
                            return;
                        }
                    }

                    Inventory inventory;
                    if (blockEntity instanceof ChestBlockEntity) {
                        inventory = ChestBlock.getInventory((ChestBlock) blockWithEntity, world.getBlockState(blockPos), world, blockPos, true);
                    } else if (blockEntity instanceof Inventory inventory1) {
                        inventory = inventory1;
                    } else return;

                    for (ItemStack stack : stacks) {
                        assert inventory != null;
                        int slot = getEmptyOrIdenticalSlotIndex(stack, inventory);
                        if (slot == -1) {
                            spawnItem(pos, stack, world);
                        } else {
                            if (!inventory.getStack(slot).isEmpty()) {
                                int count = inventory.getStack(slot).getCount();
                                int stackCount = stack.getCount();
                                stack.setCount(stackCount + count);
                                inventory.setStack(slot, stack);
                            } else {
                                inventory.setStack(slot, stack);
                            }
                        }
                    }
                    this.discard();
                } else {
                    stacks.forEach(stack -> spawnItem(pos, stack, world));
                    this.discard();
                }
            }
        }
        BlockState blockState = this.world.getBlockState(blockHitResult.getBlockPos());
        blockState.onProjectileHit(this.world, blockState, blockHitResult, this);
    }

    public abstract Identifier[] getLootId();

    @Override
    public Packet<?> createSpawnPacket() {
        return SeedPouchEntitySpawnPacket.create(this, SEED_POUCH_PACKET_ID);
    }
}
