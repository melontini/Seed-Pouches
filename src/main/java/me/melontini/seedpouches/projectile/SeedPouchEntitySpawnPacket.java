package me.melontini.seedpouches.projectile;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SeedPouchEntitySpawnPacket {
    public static Packet<ClientPlayPacketListener> create(Entity entity, Identifier packetID) {
        if (entity.world.isClient)
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(Registries.ENTITY_TYPE.getRawId(entity.getType()));
        byteBuf.writeUuid(entity.getUuid());
        byteBuf.writeVarInt(entity.getId());

        byteBuf.writeDouble(entity.getPos().x);
        byteBuf.writeDouble(entity.getPos().y);
        byteBuf.writeDouble(entity.getPos().z);

        byteBuf.writeByte((byte) MathHelper.floor(entity.getPitch() * 256 / 360));
        byteBuf.writeByte((byte) MathHelper.floor(entity.getYaw() * 256 / 360));

        return ServerPlayNetworking.createS2CPacket(packetID, byteBuf);
    }
}
