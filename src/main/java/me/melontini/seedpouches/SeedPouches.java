package me.melontini.seedpouches;

import me.melontini.seedpouches.registries.DispenserBehaviorRegistry;
import me.melontini.seedpouches.registries.EntityRegistry;
import me.melontini.seedpouches.registries.ItemRegistry;
import me.melontini.seedpouches.registries.TradesRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public class SeedPouches implements ModInitializer {
    public static final String ID = "seed_pouches";
    public static final Identifier SEED_POUCH_PACKET_ID = new Identifier(ID, "spawn_packet");

    public static final GameRules.Key<GameRules.BooleanRule> AUTO_PLANT_WHEN_POSSIBLE =
            GameRuleRegistry.register("autoPlantCropsWhenPossible", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    @Override
    public void onInitialize() {
        DispenserBehaviorRegistry.register();
        EntityRegistry.register();
        ItemRegistry.register();
        TradesRegistry.register();
    }
}
