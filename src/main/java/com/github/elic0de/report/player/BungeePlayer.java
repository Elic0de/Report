package com.github.elic0de.report.player;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Optional;
import java.util.UUID;
public class BungeePlayer {

    private BungeePlayer() {
    }

    private ProxiedPlayer player;

    public String getName() {
        return player.getName();
    }

    public UUID getUuid() {
        return player.getUniqueId();
    }

    public int getPing() {
        return player.getPing();
    }

    public String getServerName() {
        return player.getServer().getInfo().getName();
    }

    public int getPlayersOnServer() {
        return player.getServer().getInfo().getPlayers().size();
    }

    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }

    public static Optional<ProxiedPlayer> adaptBungee(ProxiedPlayer player) {
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(player.getUniqueId());
        if (proxiedPlayer != null) {
            return Optional.of(proxiedPlayer);
        } else {
            return Optional.empty();
        }
    }

    public static BungeePlayer adaptCrossPlatform(ProxiedPlayer player) {
        BungeePlayer bungeePlayer = new BungeePlayer();
        bungeePlayer.player = player;
        return bungeePlayer;
    }
}