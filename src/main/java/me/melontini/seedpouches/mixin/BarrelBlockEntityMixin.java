package me.melontini.seedpouches.mixin;

import me.melontini.seedpouches.access.ChestBlockEntityAccess;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin implements ChestBlockEntityAccess {
    private boolean seed_pouches$open;
    private int seed_pouches$playersinside;

    @Inject(at = @At("HEAD"), method = "onOpen")
    public void onOpen(PlayerEntity player, CallbackInfo ci) {
        this.seed_pouches$playersinside = seed_pouches$playersinside + 1;
        this.seed_pouches$open = true;
    }

    @Inject(at = @At("HEAD"), method = "onClose")
    public void onClose(PlayerEntity player, CallbackInfo ci) {
        this.seed_pouches$playersinside = seed_pouches$playersinside - 1;
        if (seed_pouches$playersinside == 0) {
            this.seed_pouches$open = false;
        }
    }

    @Override
    public boolean isOpen() {
        return this.seed_pouches$open;
    }
}
