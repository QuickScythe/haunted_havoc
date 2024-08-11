package me.quickscythe.shadowutils.extras.blood;

import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BloodSplat {

    private final Location LOC;
    private final double DAMAGE;
    private final List<TextDisplay> SPLATS = new ArrayList<>();
    private final char[] SPLAT_CHARS = new char[] {'\u0001','\u0002','\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\u0008'};

    public BloodSplat(Location loc, double damage_taken){
        this.DAMAGE = damage_taken;
        this.LOC = loc;
        Random r = new Random();
        int radius = (int) Math.floor(damage_taken/10);
        int loops = (int) (damage_taken*(r.nextBoolean() ? 1 : 2));
        for(int i=0;i<=loops;i++){
            double x = (r.nextBoolean() ? 1 : -1)*(radius > 0 ? r.nextInt(radius) : 0)+r.nextDouble();
            double z = (r.nextBoolean() ? 1 : -1)*(radius > 0 ? r.nextInt(radius) : 0)+r.nextDouble();
            Location splat_loc = loc.clone().add(x,0.01,z);
            splat_loc.setPitch(-90);
            TextDisplay splat = loc.getWorld().spawn(splat_loc, TextDisplay.class);
            splat.text(Component.text(SPLAT_CHARS[r.nextInt(SPLAT_CHARS.length)]));
            splat.setBackgroundColor(Color.fromARGB(0,0,0,0));
//            splat.setTextOpacity(r.nextDouble());
//            splat.teleport();
            SPLATS.add(splat);

        }
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), this::remove, 20*5);
    }

    public void remove(){
        for(TextDisplay splat : SPLATS){
            splat.text(Component.text(""));
        }
    }
}
