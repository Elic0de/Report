package com.github.elic0de.report.config;

import com.github.elic0de.report.discord.DiscordMessageFormat;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public class Settings {

    // Discord integration
    public static boolean doDiscordIntegration;
    public static URL webhookUrl;
    public static DiscordMessageFormat webhookMessageFormat;

    private Settings() {}

    public static void load(YamlDocument configFile) {
        // Discord integration
        doDiscordIntegration = configFile.getBoolean("discord.enabled", false);
        webhookMessageFormat = DiscordMessageFormat.getMessageFormat(configFile.getString("discord.format_style", "inline"))
                .orElse(DiscordMessageFormat.INLINE);
        webhookUrl = fetchWebhookUrl(configFile);
    }

    private static URL fetchWebhookUrl(@NotNull YamlDocument configFile) {
        URL webhookUrl = null;
        try {
            webhookUrl = new URL(configFile.getString("discord.webhook_url"));
        } catch (MalformedURLException e) {
            doDiscordIntegration = false;
        }
        return webhookUrl;
    }
}
