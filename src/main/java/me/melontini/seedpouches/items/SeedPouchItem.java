package me.melontini.seedpouches.items;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.projectile.types.SeedPouchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SeedPouchItem extends AbstractPouchItem {
    public SeedPouchItem(Settings settings) {
        super(settings);
    }

    @Override
    public Identifier[] getLootId() {
        return new Identifier[]{new Identifier(SeedPouches.ID, "dropped_seeds")};
    }

    @Override
    public <T extends AbstractPouchEntity> T getEntity(PlayerEntity user, World world) {
        return (T) new SeedPouchEntity(user, world);
    }
}
