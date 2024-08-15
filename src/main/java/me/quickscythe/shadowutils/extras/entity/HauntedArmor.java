package me.quickscythe.shadowutils.extras.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Material;

public class HauntedArmor extends Skeleton implements CustomEntity {
    public HauntedArmor(Level world) {
        super(EntityType.SKELETON, world);
        setInvisible(true);

        getBukkitEntity().setInvisible(true);
        equipItemIfPossible(ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.DIAMOND_HELMET)));
        equipItemIfPossible(ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.GOLDEN_CHESTPLATE)));
        equipItemIfPossible(ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS)));
        equipItemIfPossible(ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_BOOTS)));

    }
}
