package me.kuma.aooni.others;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class AooniTimer {

    public static int aooniTimeLimit;
    public static int aooniTimeLeft;

    public AooniTimer() {
        aooniTimeLimit = 600;
        aooniTimeLeft = 600;
    }

    public static void changeTimeLimit(int t) {
        aooniTimeLimit = t;
        aooniTimeLeft = t;
    }

    public static void timerStart() {

        AooniManager manager = Aooni.getManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (aooniTimeLeft == 0) {
                    cancel();
                    manager.gameEnd(1);
                    aooniTimeLeft = aooniTimeLimit;
                    return;
                }
                if (!manager.gameStatus.equalsIgnoreCase("OnGame")) {
                    cancel();
                    return;
                }
                aooniTimeLeft--;
                manager.setScoreboard();
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 20);
    }

    public static void updatingOnGame() {

        AooniManager manager = Aooni.getManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!manager.gameStatus.equalsIgnoreCase("OnGame")) {
                    cancel();
                    return;
                }
                for(Player player: Bukkit.getOnlinePlayers()) {
                    double y=player.getLocation().getY();
                    if(y<-30){
                        Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
                        player.teleport(targetLocation);
                    }
                }
                for(String s: manager.aooniteam.getEntries()) {
                    Player player = Bukkit.getPlayer(s);
                    if(player == null) continue;
                    player.setFoodLevel(1);
                }
                for(String s: manager.hiroshiteam.getEntries()) {
                    Player player = Bukkit.getPlayer(s);
                    if(player == null) continue;
                    double x = player.getLocation().getX();
                    double z = player.getLocation().getZ();
                    boolean flag = (-152.0f<=z&&z<=-139.0f&&5<=x&&x<=11);
                    if(flag) {
                        double time = (double) System.currentTimeMillis() - manager.gameStartTime;
                        for(Player p: Bukkit.getOnlinePlayers())p.sendMessage(ChatColor.AQUA+s+"が脱出しました！");
                        manager.hiroshiteam.removeEntry(s);
                        manager.winnerPlayers.add(player);
                        manager.scoreTimes.add(time);
                        Location location = new Location(player.getWorld(),8.0f, 17.0f, -159.0f);
                        player.teleport(location);
                        launchFirework(location);
                        if(manager.hiroshiteam.getSize() == 0)manager.gameEnd(1);
                    }
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 5);
    }

    public static void launchFirework(Location location) {

        Firework firework = location.getWorld().spawn(location, Firework.class);

        FireworkMeta meta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(org.bukkit.Color.RED)
                .withFade(org.bukkit.Color.YELLOW)
                .with(FireworkEffect.Type.BALL)
                .withTrail()
                .withFlicker()
                .build();

        meta.addEffect(effect);
        meta.setPower(2);
        firework.setFireworkMeta(meta);
    }
}

