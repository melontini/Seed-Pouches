package me.melontini.seedpouches.registries;

import com.samebutdifferent.morevillagers.MoreVillagers;
import com.samebutdifferent.morevillagers.registry.MVProfessions;
import me.melontini.seedpouches.SeedPouches;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class TradesRegistry {
    public static void register() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, factories -> {
            factories.add(((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(ItemRegistry.SEED_POUCH, 1),
                    12, 4, 0.06f
            )));
        });
        VillagerProfession flower_profession;
        if (FabricLoader.getInstance().isModLoaded("morevillagers")) {
            flower_profession = MVProfessions.FLORIST.get();
        } else {
            flower_profession = VillagerProfession.FARMER;
        }
        TradeOfferHelper.registerVillagerOffers(flower_profession, 2, factories -> {
            factories.add(((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ItemRegistry.FLOWER_POUCH, 1),
                    12, 4, 0.06f
            )));
        });
    }
}
