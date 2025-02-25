package me.kuma.aooni.mains;


import me.kuma.aooni.Aooni;
import me.kuma.aooni.others.AooniTimer;
import me.kuma.aooni.others.GrantPotionEffect;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
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

    public AooniManager() {
        //reloadしたとき
        gameStatus = "waitingPeriod";
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
    }

    public void startCountdown(int countdownTime) {
        new BukkitRunnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() < 2) {
                    for(Player player:Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.RED+"中断されました！");
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

    private void gameRest() {

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
        if (existingObjective != null) {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            existingObjective.unregister();
        }

        Objective objective = scoreboard.registerNewObjective("AooniGame", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.RED + "Aooni Game");

        Score aooniScore = objective.getScore(ChatColor.BLUE + "青鬼: " + String.valueOf(aooniCounter));
        Score survivorScore = objective.getScore(ChatColor.GREEN + "生存者: " + String.valueOf(hiroshiteam.getSize()));

        int timeleft = AooniTimer.aooniTimeLeft;
        int second = timeleft % 60;
        timeleft /= 60;
        int minutes = timeleft;

        Score tm = objective.getScore(ChatColor.BLUE + "残り時間： " + String.valueOf(minutes) + "分 " + String.valueOf(second) + "秒");

        survivorScore.setScore(1);
        aooniScore.setScore(2);
        tm.setScore(3);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void revival(Player player) {
        hiroshiteam.addEntry(player.getName());
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(ChatColor.GREEN + player.getName() + "が復活しました！");
        Location targetLocation = new Location(player.getWorld(), -30, 12, -70);
        player.teleport(targetLocation);
        setScoreboard();
    }

    public void GameEnd(int a) {
        //1->ひろし勝ち　2->青鬼勝ち 3->青鬼全滅で勝ち
        //ゲーム結果を表示
        //
        for(Player player : Bukkit.getOnlinePlayers()){
            Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
            player.teleport(targetLocation);
        }
        if(a==2) {
            for(Player player : Bukkit.getOnlinePlayers())player.sendMessage(ChatColor.RED+"ひろしが全滅しました "+ChatColor.BLUE+"青鬼の勝ち！");
        }
        else{
            if(winnerPlayers.size() == 0){
               for(Player player : Bukkit.getOnlinePlayers()) player.sendMessage(ChatColor.RED+"青鬼が全滅しました！");
            }
            else {

            }
        }

        deleteGame();
    }

    private void deleteGame(){

        if(aooniteam!=null)aooniteam.unregister();
        if(hiroshiteam!=null)hiroshiteam.unregister();

        aooniteam = scoreboard.registerNewTeam("Aooni");
        hiroshiteam = scoreboard.registerNewTeam("Hiroshi");

        aooniCounter = 0;
        aooniSize = 1;
        winnerPlayers = new ArrayList<>();
        scoreTimes = new ArrayList<>();
        gameStatus = "waiting";

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
        VotingPlayer();
    }

    private void VotingPlayer() {

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
                    boolean flag = (-71.5f<=x&&x<=-67.0f&&11.5f<=z&&z<=16f);
                    if (flag) {
                        player.sendMessage(ChatColor.BLUE+"現在青鬼に投票しています！");
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0f, 1.0f);
                        if(!votedPlayers.contains(player)) votedPlayers.add(player);
                    }
                    else{
                        if(votedPlayers.contains(player)){
                            player.sendMessage(ChatColor.RED+"青鬼への投票をやめました！");
                            player.playSound(player.getLocation(), Sound.DIG_STONE, 3.0f, 1.0f);
                            votedPlayers.remove(player);
                        }
                    }
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 20);
    }

}
