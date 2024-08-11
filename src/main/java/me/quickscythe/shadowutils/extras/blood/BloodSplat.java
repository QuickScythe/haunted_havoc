package me.quickscythe.shadowutils.extras.blood;

import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BloodSplat {

    private final Location LOC;
    private final double DAMAGE;
    private final List<TextDisplay> SPLATS = new ArrayList<>();
    private final char[] SPLAT_CHARS = new char[] {'\uEFF1','\uEFF2','\uEFF3', '\uEFF4', '\uEFF5', '\uEFF6', '\uEFF7', '\uEFF8'};
    public BloodSplat(Location loc, double damage_taken){
        this.DAMAGE = damage_taken;
        this.LOC = loc;
//        String s = " is going home!";
        Random r = new Random();
        int radius = (int) Math.floor(damage_taken/15);
        int loops = (int) (damage_taken*(r.nextBoolean() ? 1 : 2));
        for(int i=0;i<=loops;i++){
            double x = (r.nextBoolean() ? 1 : -1)*((radius > 0 ? r.nextInt(radius) : 0)+r.nextDouble());
            double z = (r.nextBoolean() ? 1 : -1)*((radius > 0 ? r.nextInt(radius) : 0)+r.nextDouble());
            Location splat_loc = loc.clone().add(x,radius,z);
//            splat_loc.setY(splat_loc.getBlockY());
            while(splat_loc.clone().add(0,-1,0).getBlock().getType().equals(Material.AIR)){
                Utils.getLogger().log("Splat in air, moving down");
                splat_loc.add(0,-1,0);
            }
            splat_loc.add(0,0.01,0);
            splat_loc.setPitch(-90);
            TextDisplay splat = loc.getWorld().spawn(splat_loc, TextDisplay.class);
            Utils.getLogger().log("Splat at (" + splat_loc.getX() + ", " + splat_loc.getY() + ", " + splat_loc.getZ() + ").");
            splat.text(Component.text(SPLAT_CHARS[r.nextInt(SPLAT_CHARS.length)]));
            splat.setBackgroundColor(Color.fromARGB(0,0,0,0));
//            splat.setTextOpacity(r.nextDouble());
//            splat.teleport();
//            SPLATS.add(splat);
            Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), splat::remove, r.nextInt(14*20)+20);
        }
//        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), this::remove, 20*5);
    }

    public void remove(){
        for(TextDisplay splat : SPLATS){
//            splat.text(Component.text(""));
//            splat.remove();
        }
    }
}
