package me.melontini.seedpouches.client;

import me.melontini.seedpouches.registries.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class SeedPouchesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.SEED_POUCH_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FLOWER_POUCH_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SAPLING_POUCH_ENTITY, FlyingItemEntityRenderer::new);
    }
}
