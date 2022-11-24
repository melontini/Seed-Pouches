package me.melontini.seedpouches.client;

import me.melontini.seedpouches.projectile.SeedPouchEntitySpawnPacket;
import me.melontini.seedpouches.registries.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

import static me.melontini.seedpouches.SeedPouches.SEED_POUCH_PACKET_ID;

@Environment(EnvType.CLIENT)
public class SeedPouchesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.SEED_POUCH_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FLOWER_POUCH_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SAPLING_POUCH_ENTITY, FlyingItemEntityRenderer::new);
        receiveEntityPacket();
    }

    public void receiveEntityPacket() {
        ClientPlayNetworking.registerGlobalReceiver(SEED_POUCH_PACKET_ID, ((client, handler, byteBuf, responseSender) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = SeedPouchEntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = SeedPouchEntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = SeedPouchEntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            client.execute(() -> {
                {
                    if (MinecraftClient.getInstance().world == null)
                        throw new IllegalStateException("Tried to spawn entity in a null world!");
                    Entity e = et.create(MinecraftClient.getInstance().world);
                    if (e == null)
                        throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                    e.updateTrackedPosition(pos.x, pos.y, pos.z);
                    e.setPos(pos.x, pos.y, pos.z);
                    e.setPitch(pitch);
                    e.setYaw(yaw);
                    e.setId(entityId);
                    e.setUuid(uuid);
                    MinecraftClient.getInstance().world.addEntity(entityId, e);
                }
            });
        }));
    }
}
