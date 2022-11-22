package me.melontini.seedpouches.items;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.projectile.types.SaplingPouchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SaplingPouchItem extends AbstractPouchItem {
    public SaplingPouchItem(Settings settings) {
        super(settings);
    }

    @Override
    public Identifier[] getLootId() {
        return new Identifier[]{new Identifier(SeedPouches.ID, "dropped_saplings")};
    }

    @Override
    public <T extends AbstractPouchEntity> T getEntity(PlayerEntity user, World world) {
        return (T) new SaplingPouchEntity(user, world);
    }
}
