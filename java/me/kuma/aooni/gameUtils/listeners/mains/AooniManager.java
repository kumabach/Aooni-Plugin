package me.kuma.aooni.gameUtils.listeners.mains;


import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.others.AooniTimer;
import me.kuma.aooni.gameUtils.listeners.others.FillChests;
import me.kuma.aooni.gameUtils.listeners.others.GrantPotionEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.IOException;
import java.util.*;


public class AooniManager {

    public static String gameStatus;
    public static ScoreboardManager scoreboardManager;
    public static Scoreboard scoreboard;
    public static Team aooniteam, hiroshiteam;
    public static int aooniCounter;
    public static int aooniSize;
    public static List<Player> winnerPlayers;
    public static List<Double> scoreTimes;
    public static Set<Player> votedPlayers;
    public static double gameStartTime;
    public static Set<UUID> permissionSet;
    private static Team aoonileft,survivorleft,gametimeleft;
    public static int Keys;

    public AooniManager() {
        //reloadしたとき
        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        aooniteam = scoreboard.registerNewTeam("Aooni");
        hiroshiteam = scoreboard.registerNewTeam("Hiroshi");
        aooniCounter = 0;
        aooniSize = 1;
        permissionSet = new HashSet<>();
        permissionSet.add(UUID.fromString("8a580817-88ba-46e8-9de6-638c316dbdf1"));
        permissionSet.add(UUID.fromString("8bdb1040-f181-48e3-90ad-5b58c2798802"));
        permissionSet.add(UUID.fromString("6c8dfe58-b49a-4edf-96da-56d9f8ac26c4"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
            player.setMaxHealth(20);
            player.setHealth(20);
            player.setFoodLevel(20);
        }
        deleteGame();
        //removeFloatingText(Bukkit.getWorld("bach11"));
    }

    public void startCountdown(int countdownTime) {
        new BukkitRunnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() < 2) {
                    for(Player player:Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.RED+"中断されました！");
                    gameStatus = "waiting";
                    cancel();
                    return;
                }
                if (timeLeft <= 0) {
                    for(Player player:Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.BLUE + "ゲーム開始");
                    gameRest();
                    cancel();
                } else {
                    for(Player player:Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.BLUE + String.valueOf(timeLeft));
                    timeLeft--;
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 20);
    }

    private void gameRest(){

        chooseAooni(aooniSize);

        for (String s : aooniteam.getEntries()) {
            Player player = Bukkit.getPlayer(s);
            player.setMaxHealth(0.5);
            player.setHealth(0.5);
            player.setFoodLevel(1);
            aooniCounter++;
            ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
            ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
            ItemStack stonePressurePlates = new ItemStack(Material.STONE_PLATE, 64);
            player.getInventory().addItem(stonePressurePlates);
            player.getInventory().setArmorContents(new ItemStack[]{boots, leggings, chestplate, helmet});
            Location targetLocation = new Location(player.getWorld(), -3, 12, -70);
            player.teleport(targetLocation);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (aooniteam.getEntries().contains(player.getName())) continue;
            hiroshiteam.addEntry(player.getName());
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.setMaxHealth(20);
            player.setHealth(20);
            player.setFoodLevel(20);
            Location targetLocation = new Location(player.getWorld(), 8, 13, -108);
            player.teleport(targetLocation);
        }

        //aooniteam.setPrefix(ChatColor.BLUE+"[青鬼]");
        //hiroshiteam.setPrefix(ChatColor.GREEN+"[ひろし]");
        aooniteam.setNameTagVisibility(NameTagVisibility.NEVER);
        hiroshiteam.setNameTagVisibility(NameTagVisibility.NEVER);

        GrantPotionEffect.GrantEffect();

        setScoreboard();
        AooniTimer.timerStart();
        AooniTimer.updatingOnGame();
        gameStartTime = (double) System.currentTimeMillis();
        chestManage();
    }

    private void chestManage() {
        FillChests.resetChest();
        try {
            FillChests.fillChests();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FillChests.fillChestsOthers();
//        try {
//            FillChests.fillChestsOthers();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    private void chooseAooni(int siz) {
        siz = Math.min(siz, Bukkit.getOnlinePlayers().size() - 1);
        Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
        for(Player player: votedPlayers)players.remove(player);
        List<Player> nonvoted = new ArrayList<>(players);
        List<Player> voted = new ArrayList<>(votedPlayers);
        Collections.shuffle(nonvoted);
        Collections.shuffle(voted);
        voted.addAll(nonvoted);
        for (int i = 0; i < siz; i++) {
            aooniteam.addEntry(voted.get(i).getName());
        }

    }

    public void setScoreboard() {

        Objective existingObjective = scoreboard.getObjective("AooniGame");

        if (existingObjective == null) {

            existingObjective = scoreboard.registerNewObjective("AooniGame", "dummy");
            existingObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            existingObjective.setDisplayName(ChatColor.DARK_PURPLE + "Bahha Server￤青鬼ごっこ");

            aoonileft = scoreboard.registerNewTeam ( "aoonileft" );
            survivorleft = scoreboard.registerNewTeam("survivorleft");
            gametimeleft = scoreboard.registerNewTeam("gametimelimit");

            aoonileft.addEntry(ChatColor.BLACK+""+ChatColor.BLUE);
            survivorleft.addEntry(ChatColor.BLACK+""+ChatColor.GRAY);
            gametimeleft.addEntry(ChatColor.BLACK+""+ChatColor.AQUA);

            aoonileft.setPrefix(ChatColor.BLUE + "青鬼" + aooniCounter + "体");
            survivorleft.setPrefix(ChatColor.AQUA + "生存者" + hiroshiteam.getSize() + "人");

            int second = AooniTimer.aooniTimeLeft%60;
            int minute = AooniTimer.aooniTimeLeft/60;
            gametimeleft.setPrefix(ChatColor.BLUE + "残り時間：" + minute + "分" + second + "秒");

            Score blank1 = existingObjective.getScore("");
            Score blank2 = existingObjective.getScore(" ");
            Score blank3 = existingObjective.getScore("  ");
            Score blank4 = existingObjective.getScore("   ");

            blank1.setScore(5);
            existingObjective.getScore(ChatColor.BLACK+""+ChatColor.BLUE).setScore(4);
            existingObjective.getScore(ChatColor.BLACK+""+ChatColor.GRAY).setScore(3);
            existingObjective.getScore(ChatColor.BLACK+""+ChatColor.AQUA).setScore(2);
            blank3.setScore(1);

        } else { //update
            aoonileft.setPrefix(ChatColor.BLUE + "青鬼: " + ChatColor.WHITE+ aooniCounter +"体");
            survivorleft.setPrefix(ChatColor.BLUE + "生存者: " + ChatColor.WHITE+hiroshiteam.getSize() +"人");

            int second = AooniTimer.aooniTimeLeft%60;
            int minute = AooniTimer.aooniTimeLeft/60;
            gametimeleft.setPrefix(ChatColor.BLUE + "残り時間: " + ChatColor.WHITE + minute + "分" + second + "秒");
        }
        for(Player player: Bukkit.getOnlinePlayers()) player.setScoreboard(scoreboard);
    }

    public void revival(Player player) {
        hiroshiteam.addEntry(player.getName());
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(ChatColor.GREEN + player.getName() + "が復活しました！");
        Location targetLocation = new Location(player.getWorld(), -30, 12, -70);
        player.teleport(targetLocation);
        setScoreboard();
    }

    public int endCnt;

    public void gameEnd(int a) {
        //1->ひろし勝ち　2->青鬼勝ち 3->青鬼全滅で勝ち
        //ゲーム結果を表示

        for(Player player : Bukkit.getOnlinePlayers()){
            Location targetLocation = new Location(player.getWorld(), 8, 16, -155);
            player.teleport(targetLocation);
        }
        if(a==2) {
            for(Player player : Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.RED+"ひろしが全滅しました "+ChatColor.BLUE+"青鬼の勝ち！");
        }
        if(a==3) {
            for(Player player : Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.RED+"青鬼が全滅しました "+ChatColor.BLUE+"ひろしの勝ち！");
        }
        if(a==1) {
            String announce="";
            for(Player player: winnerPlayers)announce+=player.getName()+",";
            announce = announce.substring(0, announce.length() - 1);
            for(Player player : Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.DARK_AQUA+"脱出者"+announce);
        }

        endCnt = 10;

        new BukkitRunnable() {
            @Override
            public void run() {
                endCnt--;
                AooniTimer.launchFirework(new Location(Bukkit.getWorld("world"), 8, 16, -155));
                if(endCnt <= 0 ){
                    deleteGame();
                    cancel();
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 20);

    }


    private void deleteGame(){

        for(Team team: scoreboard.getTeams()) {
            team.unregister();
        }

        aooniteam = scoreboard.registerNewTeam("Aooni");
        hiroshiteam = scoreboard.registerNewTeam("Hiroshi");

        aooniCounter = 0;
        aooniSize = 1;
        winnerPlayers = new ArrayList<>();
        scoreTimes = new ArrayList<>();
        gameStatus = "waiting";
        Keys=0;

        Block block1 = new Location(Bukkit.getWorld("world"),38.0,12.0,-91.0).getBlock();
        Block block2 = new Location(Bukkit.getWorld("world"),38.0,13.0,-91.0).getBlock();
        block1.setType(Material.GLASS);
        block2.setType(Material.GLASS);
        Block block3 = new Location(Bukkit.getWorld("world"),0.0,31.0,-98.0).getBlock();
        block3.setType(Material.AIR);
        Block block4 = new Location(Bukkit.getWorld("world"),4.0,33.0,-66.0).getBlock();
        Block block5 = new Location(Bukkit.getWorld("world"),4.0,34.0,-66.0).getBlock();
        block4.setType(Material.AIR);
        block5.setType(Material.AIR);
        Block block6 = new Location(Bukkit.getWorld("world"),4.0,36.0,-66.0).getBlock();
        Block block7 = new Location(Bukkit.getWorld("world"),4.0,37.0,-66.0).getBlock();
        block6.setType(Material.SAND);
        block7.setType(Material.SAND);

        Objective existingObjective = scoreboard.getObjective("AooniGame");
        if (existingObjective != null) {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            existingObjective.unregister();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
            player.setMaxHealth(20);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
            Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
            player.teleport(targetLocation);
        }
        votedPlayers =new HashSet<>();
        votePlayer();
    }

    private void votePlayer() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (gameStatus.equalsIgnoreCase("OnGame")) {
                    cancel();
                    return;
                }
                for(Player player: Bukkit.getOnlinePlayers()){
                    player.setFoodLevel(20);
                    double y=player.getLocation().getY();
                    if(y<-30){
                        Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
                        player.teleport(targetLocation);
                    }
                }
                for (Player player:Bukkit.getOnlinePlayers()) {
                    Location location = player.getLocation();
                    double x =location.getX();
                    double z =location.getZ();
                    double y = location.getY();
                    boolean flag = (-71.5f<=x&&x<=-67.0f&&11.5f<=z&&z<=16f&&y<=6.0f);
                    if (flag) {
                        if(!votedPlayers.contains(player)) {
                            player.sendMessage(ChatColor.BLUE+"現在青鬼に投票しています！");
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0f, 1.0f);
                            votedPlayers.add(player);
                        }
                    }
                    else{
                        if(votedPlayers.contains(player)){
                            player.sendMessage(ChatColor.RED+"青鬼への投票をやめました！");
                            player.playSound(player.getLocation(), Sound.DIG_STONE, 3.0f, 1.0f);
                            votedPlayers.remove(player);
                        }
                    }
                }
                Location location = new Location(Bukkit.getWorld("world"), -69, 5, 14);
                for (int i = 0; i < 20; i++) { // 20回エフェクトを表示
                    double offsetX = (Math.random() - 0.5) * 2; // -1.0から1.0の範囲
                    double offsetY = (Math.random() - 0.5) * 2; // -1.0から1.0の範囲
                    double offsetZ = (Math.random() - 0.5) * 2; // -1.0から1.0の範囲
                    Location offsetLocation = location.clone().add(offsetX, offsetY, offsetZ);
                    offsetLocation.getWorld().playEffect(offsetLocation, Effect.PORTAL, 0);
                }
            }

        }.runTaskTimer(Aooni.getPlugin(), 0, 20);
    }
}
