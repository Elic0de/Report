package com.github.elic0de.report;

import co.aikar.commands.BungeeCommandManager;
import com.github.elic0de.report.command.ReportCommand;
import com.github.elic0de.report.config.Settings;
import com.github.elic0de.report.discord.WebhookDispatcher;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public final class Report extends Plugin {

    private static Report instance;

    public static Report getInstance() {
        return instance;
    }

    private static WebhookDispatcher webhookDispatcher;

    public Optional<WebhookDispatcher> getWebhookDispatcher() {
        if (webhookDispatcher != null) {
            return Optional.of(webhookDispatcher);
        }
        return Optional.empty();
    }

    private static BungeeCommandManager commandManager;

    public static BungeeCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Load config
        reloadSettings();

        registerCommands();

        // Initialize webhook dispatcher
        if (Settings.doDiscordIntegration) {
            webhookDispatcher = new WebhookDispatcher(Settings.webhookUrl);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadSettings() {
        try {
            Settings.load(YamlDocument.create(new File(getDataFolder(), "config.yml"),
                    getResourceAsStream("config.yml"),
                    GeneralSettings.builder().setUseDefaults(false).build(),
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.builder().setEncoding(DumperSettings.Encoding.UNICODE).build(),
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()));
        } catch (IOException e) {
            //getLoggingAdapter().log(Level.SEVERE, "Failed to load config file");
        }
    }

    private void registerCommands() {
        // 1: Create Command Manager for your respective platform
        commandManager = new BungeeCommandManager(this);

        commandManager.registerCommand(new ReportCommand());
    }

        public Collection<ProxiedPlayer> getOnlinePlayers() {
        return ProxyServer.getInstance().getPlayers();
    }
}
