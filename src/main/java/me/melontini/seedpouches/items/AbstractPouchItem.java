package me.melontini.seedpouches.items;

import me.melontini.seedpouches.projectile.AbstractPouchEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public abstract class AbstractPouchItem extends Item {

    public AbstractPouchItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            var entity = getEntity(user, world);
            entity.setItem(itemStack);
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(entity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = entity.getWorld();
        if (!world.isClient) {
            if (entity.isPlayer()) {
                PlayerEntity player = (PlayerEntity) entity;
                for (Identifier identifier : getLootId()) {
                    List<ItemStack> stacks = (
                            (ServerWorld) world)
                            .getServer()
                            .getLootManager()
                            .getTable(identifier)
                            .generateLoot((
                                    new LootContext.Builder((ServerWorld) world))
                                    .random(world.random)
                                    .build(LootContextTypes.EMPTY)
                            );
                    for (ItemStack lootstack : stacks) {
                        player.getInventory().offerOrDrop(lootstack);
                    }
                    if (!user.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    public abstract Identifier[] getLootId();

    public abstract <T extends AbstractPouchEntity> T getEntity(PlayerEntity user, World world);
}
