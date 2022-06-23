package me.melontini.seedpouches.mixin;

import com.nhoryzon.mc.farmersdelight.entity.block.PantryBlockEntity;
import me.melontini.seedpouches.access.OpenableBlockEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(PantryBlockEntity.class)
public class PantryBlockEntityMixin implements OpenableBlockEntityAccess {
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
