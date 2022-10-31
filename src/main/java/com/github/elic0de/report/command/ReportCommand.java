package com.github.elic0de.report.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.github.elic0de.report.message.ReportMessage;
import com.github.elic0de.report.player.BungeePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.StringJoiner;

public class ReportCommand extends BaseCommand {

    @Default
    @Subcommand("report")
    @CommandPermission("report")
    public void onReport(ProxiedPlayer player, String[] args) {
        StringJoiner message = new StringJoiner(" ");
        for (String arg : args) {
            message.add(arg);
        }

        new ReportMessage(BungeePlayer.adaptCrossPlatform(player), message.toString()).dispatch();
    }
}
