package me.quickscythe.shadowutils.extras.blood;

import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        for(int i=0;i<=damage_taken*r.nextInt(9)+1;i++){
            Location splat_loc = loc.clone().add(r.nextInt(1)+r.nextDouble(),0,r.nextInt(1)+r.nextDouble());
            splat_loc.setPitch(-90);
            TextDisplay splat = loc.getWorld().spawn(splat_loc, TextDisplay.class);
            splat.text(Component.text("B"));
//            splat.setTextOpacity(r.nextDouble());
//            splat.teleport();

        }
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), this::remove, 20*30);
    }

    public void remove(){
        for(TextDisplay splat : SPLATS){
            splat.remove();
        }
    }
}
