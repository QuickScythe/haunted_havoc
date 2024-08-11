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

    public BloodSplat(Location loc, double damage_taken){
        this.DAMAGE = damage_taken;
        this.LOC = loc;
        Random r = new Random();
        Utils.getLogger().log("damage taken: " + damage_taken);

        int radius = (int) Math.floor(damage_taken/7);
        Utils.getLogger().log("Radius: " + radius);

        for(int i=0;i<=damage_taken*r.nextInt(1)+1;i++){

            Location splat_loc = loc.clone().add(r.nextInt(radius)+r.nextDouble(),0.01,r.nextInt(radius)+r.nextDouble());
            splat_loc.setPitch(-90);
            TextDisplay splat = loc.getWorld().spawn(splat_loc, TextDisplay.class);
            splat.text(Component.text("B"));
            splat.setBackgroundColor(Color.fromARGB(0,0,0,0));
//            splat.setTextOpacity(r.nextDouble());
//            splat.teleport();

        }
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), this::remove, 20*5);
    }

    public void remove(){
        for(TextDisplay splat : SPLATS){
            splat.text(Component.text(""));
        }
    }
}
