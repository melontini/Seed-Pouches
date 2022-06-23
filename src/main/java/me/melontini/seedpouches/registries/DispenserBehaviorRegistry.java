package me.melontini.seedpouches.registries;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.FlowerPouchEntity;
import me.melontini.seedpouches.projectile.SaplingPouchEntity;
import me.melontini.seedpouches.projectile.SeedPouchEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class DispenserBehaviorRegistry {
    public static void register() {
        DispenserBlock.registerBehavior(ItemRegistry.SEED_POUCH, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position pos, ItemStack stack) {
                return new SeedPouchEntity(pos.getX(), pos.getY(), pos.getZ(), world);
            }
        });
        DispenserBlock.registerBehavior(ItemRegistry.FLOWER_POUCH, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position pos, ItemStack stack) {
                return new FlowerPouchEntity(pos.getX(), pos.getY(), pos.getZ(), world);
            }
        });
        DispenserBlock.registerBehavior(ItemRegistry.SAPLING_POUCH, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position pos, ItemStack stack) {
                return new SaplingPouchEntity(pos.getX(), pos.getY(), pos.getZ(), world);
            }
        });
    }
}
