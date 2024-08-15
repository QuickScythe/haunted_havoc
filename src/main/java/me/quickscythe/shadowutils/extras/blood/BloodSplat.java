package me.quickscythe.shadowutils.extras.blood;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BloodSplat {

    private final List<TextDisplay> SPLATS = new ArrayList<>();
    private final char[] SPLAT_CHARS = new char[]{'\uEFF1', '\uEFF2', '\uEFF3', '\uEFF4', '\uEFF5', '\uEFF6', '\uEFF7', '\uEFF8'};

    public BloodSplat(Location loc, double damage_taken) {
        Random r = new Random();
        int radius = (int) Math.floor(damage_taken / 20);
        int loops = (int) (damage_taken * (r.nextBoolean() ? 1 : 2));
        if (loops > 100) loops = 100;
        if (radius > 6) radius = 6;
        Registry<BlockType> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK);
        final BlockType block = registry.getOrThrow(TypedKey.create(RegistryKey.BLOCK, Key.key("minecraft:redstone_block")));
        BlockData data = block.createBlockData();
        for (int i = 0; i <= loops; i++) {
            loc.getWorld().spawnParticle(Particle.BLOCK, loc.getX(), loc.getY(), loc.getZ(), 10, 0, 0, 0, data);
            double x = (r.nextBoolean() ? 1 : -1) * ((radius > 0 ? r.nextInt(radius) : 0) + r.nextDouble());
            double z = (r.nextBoolean() ? 1 : -1) * ((radius > 0 ? r.nextInt(radius) : 0) + r.nextDouble());
            Location splat_loc = loc.clone().add(x, 0, z);
            splat_loc.setY(splat_loc.getBlockY() + radius);
            while (!splat_loc.clone().add(0, -1, 0).getBlock().isSolid()) {
                splat_loc.add(0, -1, 0);
            }
            splat_loc.add(0, 0.01, 0);
            splat_loc.setPitch(-90);
            TextDisplay splat = loc.getWorld().spawn(splat_loc, TextDisplay.class);
            splat.text(Component.text(SPLAT_CHARS[r.nextInt(SPLAT_CHARS.length)]));
            splat.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            SPLATS.add(splat);
        }
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), this::remove, r.nextInt(10 * 20) + 20 * 5);
    }

    public void remove() {
        for (TextDisplay splat : SPLATS)
            splat.remove();
    }


}
