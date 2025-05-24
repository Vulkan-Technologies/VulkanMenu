package com.vulkantechnologies.menu.utils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateChecker {

    private static final int RESOURCE_ID = 125208;

    public static CompletableFuture<String> fetchLatestVersion() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String content = DumpUtils.get("https://api.spigotmc.org/legacy/update.php?resource=%d/~".formatted(RESOURCE_ID));
                if (content == null || content.isEmpty())
                    throw new IOException("Failed to fetch latest version");

                return content.trim();
            } catch (IOException e) {
                throw new RuntimeException("Failed to fetch latest version", e);
            }
        });
    }
}
