package com.github.elic0de.report.message;

import com.github.elic0de.report.Report;
import com.github.elic0de.report.config.Settings;
import com.github.elic0de.report.player.BungeePlayer;
import de.themoep.minedown.MineDown;
import de.themoep.minedown.MineDownParser;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;

public class ReportMessage {

    public BungeePlayer sender;
    public String message;

    public ReportMessage(BungeePlayer sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public void dispatch() {
        StringBuilder msg = new StringBuilder(message);

        message = msg.toString();

        HashSet<ProxiedPlayer> messageRecipients = new HashSet<>();
        messageRecipients.addAll(Report.getInstance().getOnlinePlayers());


        // Dispatch message to all applicable users in the scope with permission who are not on a restricted server
        MESSAGE_DISPATCH:
        for (ProxiedPlayer recipient : messageRecipients) {
            BungeePlayer bungeePlayer = BungeePlayer.adaptCrossPlatform(recipient);
            if (!recipient.hasPermission("report") && !(bungeePlayer.getUuid().equals(sender.getUuid()))) {
                continue;
            }
            sendFormattedChannelMessage(recipient, sender, message);
        }

        // Dispatch message to a Discord webhook if enabled
        if (Settings.doDiscordIntegration) {
            Report.getInstance().getWebhookDispatcher().ifPresent(dispatcher -> dispatcher.dispatchWebhook(this));
        }
    }

    public void sendFormattedChannelMessage(ProxiedPlayer target, BungeePlayer sender, String message) {
        final ComponentBuilder componentBuilder = new ComponentBuilder();
        if (sender.hasPermission("report.formatted_chat")) {
            componentBuilder.append(new MineDown(message).disable(MineDownParser.Option.ADVANCED_FORMATTING)
                    .toComponent());
        } else {
            componentBuilder.append(message);
        }
        BungeePlayer.adaptBungee(target).ifPresent(bungeePlayer -> bungeePlayer.sendMessage(componentBuilder.create()));
    }
}
