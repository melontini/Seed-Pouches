package me.melontini.seedpouches.items;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import me.melontini.seedpouches.projectile.types.FlowerPouchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FlowerPouchItem extends AbstractPouchItem {
    public FlowerPouchItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public Identifier[] getLootId() {
        return new Identifier[]{new Identifier(SeedPouches.ID, "dropped_flowers")};
    }

    @Override
    public <T extends AbstractPouchEntity> T getEntity(PlayerEntity user, World world) {
        return (T) new FlowerPouchEntity(user, world);
    }
}
