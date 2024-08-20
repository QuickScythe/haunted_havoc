package me.quickscythe.shadowutils.extras.entity.entities;

import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.inventory.meta.ItemMeta;

public class Ghoulem extends SnowGolem {
    public Ghoulem(Level world) {
        super(EntityType.SNOW_GOLEM, world);
        setPumpkin(false);
        setCustomName(net.minecraft.network.chat.Component.literal("Ghoulem").withColor(NamedTextColor.GRAY.value()));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entityliving) -> true));
    }
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {


            if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return;
            }

            BlockState iblockdata = Blocks.SNOW.defaultBlockState();

            for (int i = 0; i < 4; ++i) {
                int j = Mth.floor(this.getX() + (double) ((float) (i % 2 * 2 - 1) * 0.25F));
                int k = Mth.floor(this.getY());
                int l = Mth.floor(this.getZ() + (double) ((float) (i / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockposition = new BlockPos(j, k, l);

                if (this.level().getBlockState(blockposition).isAir() && iblockdata.canSurvive(this.level(), blockposition)) {
                    // CraftBukkit start
                    if (!CraftEventFactory.handleBlockFormEvent(this.level(), blockposition, iblockdata, this)) {
                        continue;
                    }
                    // CraftBukkit end
                    this.level().gameEvent((Holder) GameEvent.BLOCK_PLACE, blockposition, GameEvent.Context.of(this, iblockdata));
                }
            }
        }

    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        Snowball entitysnowball = new Snowball(this.level(), this);
        ItemStack stack = entitysnowball.getItem();
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(10));
        entitysnowball.setItem(stack);
        double d0 = target.getEyeY() - 1.100000023841858D;
        double d1 = target.getX() - this.getX();
        double d2 = d0 - entitysnowball.getY();
        double d3 = target.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224D;

        entitysnowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(entitysnowball);
    }

}
