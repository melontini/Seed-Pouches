package me.melontini.seedpouches.projectile;

import com.nhoryzon.mc.farmersdelight.block.PantryBlock;
import com.nhoryzon.mc.farmersdelight.entity.block.PantryBlockEntity;
import me.melontini.seedpouches.access.OpenableBlockEntityAccess;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class PouchLogicMethods {
    protected static void pouchEntityLogic(World world, Identifier lootId, Entity entity, EntityHitResult entityHitResult) {
        List<ItemStack> stacks = (
                (ServerWorld) world)
                .getServer()
                .getLootManager()
                .getTable(lootId)
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
                PouchLogicMethods.spawnItem(pos, stack, world);
            }
        }
        entity.discard();
    }
    protected static void pouchBlockLogic(World world, Identifier lootId, Entity entity, BlockHitResult blockHitResult) {
        Vec3d pos = blockHitResult.getPos();
        BlockPos blockPos = blockHitResult.getBlockPos();
        List<ItemStack> stacks = (
                (ServerWorld) world)
                .getServer()
                .getLootManager()
                .getTable(lootId)
                .generateLoot((
                        new LootContext.Builder((ServerWorld) world))
                        .random(world.random)
                        .build(LootContextTypes.EMPTY)
                );
        if (world.getBlockState(blockPos).getBlock() instanceof BlockWithEntity) {
            if (world.getBlockState(blockPos).getBlock() instanceof ChestBlock chest) {
                ChestBlockEntity chestBlockEntity = (ChestBlockEntity) world.getBlockEntity(blockPos);
                assert chestBlockEntity != null;

                for (ItemStack stack : stacks) {
                    if (((OpenableBlockEntityAccess) chestBlockEntity).isOpen()) {
                        Inventory inventory = ChestBlock.getInventory(chest, world.getBlockState(blockPos), world, blockPos, true);
                        assert inventory != null;
                        int slot = PouchLogicMethods.getEmptyOrIdenticalSlotIndex(stack, inventory);
                        //omg a switch statement. I can already feel them performance improvements.
                        switch (slot) {
                            case -1:
                                PouchLogicMethods.spawnItem(pos, stack, world);
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
                        PouchLogicMethods.spawnItem(pos, stack, world);
                    }
                }
            } else if (world.getBlockState(blockPos).getBlock() instanceof BarrelBlock) {
                BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity) world.getBlockEntity(blockPos);
                assert barrelBlockEntity != null;

                for (ItemStack stack : stacks) {
                    if (((OpenableBlockEntityAccess) barrelBlockEntity).isOpen()) {
                        int slot = PouchLogicMethods.getBarrelEmptyOrIdenticalSlotIndex(stack, barrelBlockEntity);
                        switch (slot) {
                            case -1:
                                PouchLogicMethods.spawnItem(pos, stack, world);
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
                        PouchLogicMethods.spawnItem(pos, stack, world);
                    }
                }
            } else if (world.getBlockState(blockPos).getBlock() instanceof ShulkerBoxBlock) {
                ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) world.getBlockEntity(blockPos);
                assert shulkerBoxBlockEntity != null;

                for (ItemStack stack : stacks) {
                    if (((OpenableBlockEntityAccess) shulkerBoxBlockEntity).isOpen()) {
                        int slot = PouchLogicMethods.getShulkerEmptyOrIdenticalSlotIndex(stack, shulkerBoxBlockEntity);
                        switch (slot) {
                            case -1:
                                PouchLogicMethods.spawnItem(pos, stack, world);
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
                        PouchLogicMethods.spawnItem(pos, stack, world);
                    }
                }
            } else if (world.getBlockState(blockPos).getBlock() instanceof DispenserBlock) {
                DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity) world.getBlockEntity(blockPos);
                assert dispenserBlockEntity != null;
                for (ItemStack stack : stacks) {
                    int slot = PouchLogicMethods.getDispenserEmptyOrIdenticalSlotIndex(stack, dispenserBlockEntity);
                    switch (slot) {
                        case -1:
                            PouchLogicMethods.spawnItem(pos, stack, world);
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
            } else if (world.getBlockState(blockPos).getBlock() instanceof DropperBlock) {
                DropperBlockEntity dropperBlockEntity = (DropperBlockEntity) world.getBlockEntity(blockPos);
                assert dropperBlockEntity != null;
                for (ItemStack stack : stacks) {
                    int slot = PouchLogicMethods.getDropperEmptyOrIdenticalSlotIndex(stack, dropperBlockEntity);
                    switch (slot) {
                        case -1:
                            PouchLogicMethods.spawnItem(pos, stack, world);
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
            } else if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
                if (world.getBlockState(blockPos).getBlock() instanceof PantryBlock) {
                    PantryBlockEntity pantryBlockEntity = (PantryBlockEntity) world.getBlockEntity(blockPos);
                    assert pantryBlockEntity != null;
                    for (ItemStack stack : stacks) {
                        if (((OpenableBlockEntityAccess) pantryBlockEntity).isOpen()) {
                            int slot = PouchLogicMethods.getPantryEmptyOrIdenticalSlotIndex(stack, pantryBlockEntity);
                            switch (slot) {
                                case -1:
                                    PouchLogicMethods.spawnItem(pos, stack, world);
                                    break;
                                default:
                                    if (!pantryBlockEntity.getStack(slot).isEmpty()) {
                                        int count = pantryBlockEntity.getStack(slot).getCount();
                                        int stackCount = stack.getCount();
                                        stack.setCount(stackCount + count);
                                        pantryBlockEntity.setStack(slot, stack);
                                    } else {
                                        pantryBlockEntity.setStack(slot, stack);
                                    }
                                    break;
                            }
                        } else {
                            PouchLogicMethods.spawnItem(pos, stack, world);
                        }
                    }
                }
            } else {
                for (ItemStack stack : stacks) {
                    PouchLogicMethods.spawnItem(pos, stack, world);
                }
            }
        } else {
            for (ItemStack stack : stacks) {
                PouchLogicMethods.spawnItem(pos, stack, world);
            }
        }
        entity.discard();
    }
    //cringe
    public void spawnItem(BlockPos pos, ItemStack stack, World world) {
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

    //ok
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

    public static int getBarrelEmptyOrIdenticalSlotIndex(ItemStack stack, BarrelBlockEntity entity) {
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

    public static int getShulkerEmptyOrIdenticalSlotIndex(ItemStack stack, ShulkerBoxBlockEntity entity) {
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

    public static int getDispenserEmptyOrIdenticalSlotIndex(ItemStack stack, DispenserBlockEntity entity) {
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

    public static int getDropperEmptyOrIdenticalSlotIndex(ItemStack stack, DispenserBlockEntity entity) {
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
    public static int getPantryEmptyOrIdenticalSlotIndex(ItemStack stack, PantryBlockEntity entity) {
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
    //test 101 pls ignor
}
