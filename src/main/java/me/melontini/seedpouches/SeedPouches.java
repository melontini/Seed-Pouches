package me.melontini.seedpouches;

import me.melontini.seedpouches.items.SeedPouchItem;
import me.melontini.seedpouches.projectile.SeedPouchEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class SeedPouches implements ModInitializer {
    public static final String ID = "seed_pouches";
    public static final Identifier SEED_POUCH_PACKET_ID = new Identifier(ID, "spawn_packet");
    public static final Item SEED_POUCH = new SeedPouchItem(new FabricItemSettings().maxCount(16).group(ItemGroup.TOOLS));
    public static final GameRules.Key<GameRules.BooleanRule> AUTO_PLANT_WHEN_POSSIBLE =
            GameRuleRegistry.register("autoPlantCropsWhenPossible", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final EntityType<SeedPouchEntity> SEED_POUCH_ENTITY = FabricEntityTypeBuilder.<SeedPouchEntity>create(SpawnGroup.MISC, SeedPouchEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
            .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(ID, "seed_pouch"), SEED_POUCH);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "seed_pouch"), SEED_POUCH_ENTITY);
        //Yooo
        DispenserBlock.registerBehavior(SeedPouches.SEED_POUCH, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position pos, ItemStack stack) {
                return new SeedPouchEntity(pos.getX(), pos.getY(), pos.getZ(), world);
            }
        });
        //trades
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, factories -> {
            factories.add(((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(SeedPouches.SEED_POUCH, 1),
                    12, 4, 0.06f
            )));
        });
    }
}
