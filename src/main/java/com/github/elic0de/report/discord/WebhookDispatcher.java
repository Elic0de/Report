package com.github.elic0de.report.discord;

import com.github.elic0de.report.config.Settings;
import com.github.elic0de.report.message.ReportMessage;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class WebhookDispatcher {

    private final URL channelWebhook;


    public WebhookDispatcher(@NotNull URL channelWebhook) {
        this.channelWebhook = channelWebhook;
    }

    public void dispatchWebhook(@NotNull ReportMessage message) {
        CompletableFuture.runAsync(() -> {
            try {
                final HttpURLConnection webhookConnection = (HttpURLConnection) channelWebhook.openConnection();
                webhookConnection.setRequestMethod("POST");
                webhookConnection.setDoOutput(true);

                final byte[] jsonMessage = getChatMessageJson(Settings.webhookMessageFormat, message);
                final int messageLength = jsonMessage.length;
                webhookConnection.setFixedLengthStreamingMode(messageLength);
                webhookConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                webhookConnection.connect();
                try (OutputStream messageOutputStream = webhookConnection.getOutputStream()) {
                    messageOutputStream.write(jsonMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private byte[] getChatMessageJson(@NotNull DiscordMessageFormat format, @NotNull ReportMessage message) {
        return format.postMessageFormat
                .replace("{SENDER_UUID}", message.sender.getUuid().toString())
                .replace("{CURRENT_TIMESTAMP}", ZonedDateTime.now()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .replace("{SENDER_USERNAME}", message.sender.getName())
                .replace("{CHAT_MESSAGE}", message.message
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\""))
                .getBytes(StandardCharsets.UTF_8);
    }

}