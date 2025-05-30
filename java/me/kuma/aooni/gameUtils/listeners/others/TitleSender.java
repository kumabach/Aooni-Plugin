package me.kuma.aooni.gameUtils.listeners.others;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * 1.8から追加された/titleコマンドと同等の事が出来るclassです。
 */
public class TitleSender {

    // 注意::パケットを直接送信しています、良く分からない場合は変更しないで下さい(バグりますよ?

    // class取得用の変数
    private String netMinecraftserver = "net.minecraft.server.";

    private Object enumTitle, enumSubtitle, enumActionBar;
    private Constructor<?> constructorTitle, constructorTime, constructorActionBar;
    private Method methodChatSerializer, methodHandle, methodSendpacket;
    private Field fieldConnection;

    /**
     * 初期化を行います。 必ずBukkitが起動した後(onEnableなど)で呼び出してください
     */
    public TitleSender() {
        try {
            // 一時的に使用する変数です、Bukkitのバージョンを取得しています。
            String tmpPackage[] = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            String tmpVersion = tmpPackage[tmpPackage.length - 1] + ".";

            // サーババージョンの取得&結合
            netMinecraftserver += tmpVersion;

            // 一時的に使用する変数です、必要なclassを取得しています。
            Class<?> tmpPacketPlayout = getNMSClass("PacketPlayOutTitle"),
                    tmpIchatBase = getNMSClass("IChatBaseComponent"),
                    tmpEnumTitleAction = getNMSClass("PacketPlayOutTitle$EnumTitleAction"),
                    tmpChatMessageType = getNMSClass("ChatMessageType");

            // 必要なclassを取得します。
            enumTitle = tmpEnumTitleAction.getDeclaredField("TITLE").get(null);
            enumSubtitle = tmpEnumTitleAction.getDeclaredField("SUBTITLE").get(null);

            constructorTitle = tmpPacketPlayout.getConstructor(tmpEnumTitleAction, tmpIchatBase);
            constructorTime = tmpPacketPlayout.getConstructor(int.class, int.class, int.class);

            constructorActionBar = getNMSClass("PacketPlayOutChat").getConstructor(tmpIchatBase, tmpChatMessageType);
            enumActionBar = tmpChatMessageType.getField("GAME_INFO").get(null);
            methodChatSerializer = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
            methodSendpacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
            try {
                methodHandle = Class.forName("org.bukkit.craftbukkit." + tmpVersion + "entity.CraftPlayer")
                        .getMethod("getHandle");
            } catch (Exception e) {
                e.printStackTrace();
            }

            fieldConnection = getNMSClass("EntityPlayer").getField("playerConnection");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * プレイヤーに表示されているタイトルをリセットします。
     *
     * @param player
     */
    public void resetTitle(Player player) {
        sendTitle(player, "", "", "");
    }

    /**
     * タイトルを送信します。
     *
     * @param player 対象のプレイヤー
     * @param title 表示するメインタイトル、無い場合はnull
     * @param subtitle 表示するサブタイトル、無い場合はnull
     * @param actionBar 表示するアクションバー、無い場合はnull
     */
    public void sendTitle(Player player, String title, String subtitle, String actionBar) {
        try {
            if (title != null) {
                sendPacket(player, constructorTitle.newInstance(
                        enumTitle,
                        methodChatSerializer.invoke(null, "{\"text\":\"" + title + "\"}")));
            }

            if (subtitle != null) {
                sendPacket(player, constructorTitle.newInstance(
                        enumSubtitle,
                        methodChatSerializer.invoke(null, "{\"text\":\"" + subtitle + "\"}")));
            }

            if (actionBar != null) {
                sendPacket(player, constructorActionBar.newInstance(
                        methodChatSerializer.invoke(null, "{\"text\":\"" + actionBar + "\"}"),
                        enumActionBar));
            }
        } catch (IllegalAccessException
                 | InstantiationException
                 | IllegalArgumentException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * タイトルを表示する時間を設定します(単位::second)
     *
     * @param player 対象のプレイヤー
     * @param feedIn タイトルのフェードイン
     * @param titleShow タイトルの表示時間
     * @param feedOut タイトルのフェードアウト
     */
    public void setTime(Player player, double feedIn, double titleShow, double feedOut) {
        setTime(player, (int) (feedIn * 20), (int) (titleShow * 20), (int) (feedOut * 20));
    }

    /**
     * タイトルを表示する時間を設定します(単位::tick)
     *
     * @param player 対象のプレイヤー
     * @param feedIn タイトルのフェードイン
     * @param titleShow タイトルの表示時間
     * @param feedOut タイトルのフェードアウト
     */
    public void setTime(Player player, int feedIn, int titleShow, int feedOut) {
        try {
            sendPacket(player, constructorTime.newInstance(feedIn, titleShow, feedOut));
        } catch (IllegalAccessException
                 | InstantiationException
                 | IllegalArgumentException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 作成したパケットを送出します。
     *
     * @param player
     *            対象のプレイヤー
     * @param packet
     *            送信するパケット
     */
    private void sendPacket(Player player, Object packet) {
        try {
            // パケットの送信
            methodSendpacket.invoke(fieldConnection.get(methodHandle.invoke(player)), packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * net.minecraft.serverのclassを取得します。
     *
     * @param name
     *            取得したいclassの名前
     * @return 取得したclassを返却します、例外が発生した場合にはprintしてnullを返却します。
     */
    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName(netMinecraftserver + name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}