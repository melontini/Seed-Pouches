package me.melontini.seedpouches.registries;

import me.melontini.crackerutil.content.ContentBuilder;
import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.items.AbstractPouchItem;
import me.melontini.seedpouches.items.FlowerPouchItem;
import me.melontini.seedpouches.items.SaplingPouchItem;
import me.melontini.seedpouches.items.SeedPouchItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final AbstractPouchItem SEED_POUCH = ContentBuilder.ItemBuilder.create(SeedPouchItem.class, new Identifier(SeedPouches.ID, "seed_pouch"), new FabricItemSettings())
            .maxCount(16).group(ItemGroup.TOOLS).build();
    public static final AbstractPouchItem FLOWER_POUCH = ContentBuilder.ItemBuilder.create(FlowerPouchItem.class, new Identifier(SeedPouches.ID, "flower_pouch"), new FabricItemSettings())
            .maxCount(16).group(ItemGroup.TOOLS).build();
    public static final AbstractPouchItem SAPLING_POUCH = ContentBuilder.ItemBuilder.create(SaplingPouchItem.class, new Identifier(SeedPouches.ID, "sapling_pouch"), new FabricItemSettings())
            .maxCount(16).group(ItemGroup.TOOLS).build();
    public static void register() {
    }
}
