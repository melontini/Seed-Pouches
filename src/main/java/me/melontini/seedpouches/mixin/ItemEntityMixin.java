/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 Haven King
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.melontini.seedpouches.mixin;

import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static me.melontini.seedpouches.SeedPouches.AUTO_PLANT_WHEN_POSSIBLE;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    private final Random random = new Random();
    @Shadow
    public abstract ItemStack getStack();

    @Inject(at = @At("HEAD"), method = "tick")
    public void tryPlant(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        ItemStack stack = this.getStack();
        BlockPos pos = entity.getBlockPos();
        World world = entity.getWorld();
        if (entity.age % random.nextInt(20, 101) == 0) {
            if (world.getGameRules().getBoolean(AUTO_PLANT_WHEN_POSSIBLE)) {
                if (stack.getItem() instanceof BlockItem) {
                    if (((BlockItem) stack.getItem()).getBlock() instanceof PlantBlock) {
                        if (world.getFluidState(pos).isEmpty()) {
                            ((BlockItem) stack.getItem()).place(new ItemPlacementContext(world, null, null, stack, world.raycast(
                                    new RaycastContext(
                                            new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
                                            new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5),
                                            RaycastContext.ShapeType.COLLIDER,
                                            RaycastContext.FluidHandling.ANY,
                                            entity
                                    )
                            )));
                        }
                    }
                }
            }
        }

    }
}
