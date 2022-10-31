package com.github.elic0de.report.discord;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum DiscordMessageFormat {

    EMBEDDED("""
            {
              "avatar_url": "https://prd.storage.lit.link/images/assets/profile/img-qrcode.png",
              "username": "Report",
              "content": null,
              "embeds": [
                {
                  "description": "{CHAT_MESSAGE}",
                  "color": 64410,
                  "footer": {
                    "text": "{SENDER_USERNAME}",
                    "icon_url": "https://crafatar.com/avatars/{SENDER_UUID}?size=64"
                  },
                  "timestamp": "{CURRENT_TIMESTAMP}"
                }
              ]
            }"""),

    INLINE("""
            {
              "avatar_url": "https://crafatar.com/avatars/{SENDER_UUID}?size=128",
              "username": "{SENDER_USERNAME}",
              "content": "{CHAT_MESSAGE}"
            }""");

    public final String postMessageFormat;

    DiscordMessageFormat(@NotNull String postMessageFormat) {
        this.postMessageFormat = postMessageFormat;
    }

    public static Optional<DiscordMessageFormat> getMessageFormat(@NotNull String formatName) {
        for (DiscordMessageFormat messageFormat : DiscordMessageFormat.values()) {
            if (messageFormat.name().equalsIgnoreCase(formatName)) {
                return Optional.of(messageFormat);
            }
        }
        return Optional.empty();
    }

}

