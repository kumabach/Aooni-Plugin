package me.kuma.aooni.gameUtils.listeners.others;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;


public class GrantPotionEffect {

    public static Set<Tuple> sects;

    public GrantPotionEffect() {
        sects = new HashSet<>();
        AddGrants(6, 10, -137, -113);
    }

    public static void GrantEffect() {
        AooniManager manager = Aooni.getManager();
        new BukkitRunnable() {

            @Override
            public void run() {
                if (!manager.gameStatus.equalsIgnoreCase("OnGame")) {
                    cancel();
                    return;
                }
                for (String s : manager.aooniteam.getEntries()) {
                    Player player = Bukkit.getPlayer(s);
                    for (Tuple t : sects) {
                        double a, b, c, d;
                        a = (double) t.getFirst();
                        b = (double) t.getSecond();
                        c = (double) t.getThird();
                        d = (double) t.getFourth();

                        double x = player.getLocation().getX();
                        double y = player.getLocation().getZ();
                        double z = player.getLocation().getY();

                        if (a <= x && x <= b && c <= y && y <= d && z>=10.0) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 3));
                        } else player.removePotionEffect(PotionEffectType.SPEED);
                    }
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 1);
    }

    public void AddGrants(double a, double b, double c, double d) {
        Tuple t = new Tuple<>(a, b, c, d);
        sects.add(t);
    }


}

