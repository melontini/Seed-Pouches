package me.melontini.seedpouches.registries;

import me.melontini.seedpouches.SeedPouches;
import me.melontini.seedpouches.items.FlowerPouchItem;
import me.melontini.seedpouches.items.SaplingPouchItem;
import me.melontini.seedpouches.items.SeedPouchItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final Item SEED_POUCH = new SeedPouchItem(new FabricItemSettings().maxCount(16).group(ItemGroup.TOOLS));
    public static final Item FLOWER_POUCH = new FlowerPouchItem(new FabricItemSettings().maxCount(16).group(ItemGroup.TOOLS));
    public static final Item SAPLING_POUCH = new SaplingPouchItem(new FabricItemSettings().maxCount(16).group(ItemGroup.TOOLS));
    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(SeedPouches.ID, "seed_pouch"), SEED_POUCH);
        Registry.register(Registry.ITEM, new Identifier(SeedPouches.ID, "flower_pouch"), FLOWER_POUCH);
        Registry.register(Registry.ITEM, new Identifier(SeedPouches.ID, "sapling_pouch"), SAPLING_POUCH);
    }
}
