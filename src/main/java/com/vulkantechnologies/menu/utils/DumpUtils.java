package com.vulkantechnologies.menu.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vulkantechnologies.menu.VulkanMenu;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DumpUtils {

    private static final String HASTEBIN_URL = "https://dump.vulkantechnologies.com/";

    public static CompletableFuture<String> createDump(String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String result = post(HASTEBIN_URL + "documents", content);
                if (result == null || result.isEmpty() || !result.contains("\"key\":\""))
                    throw new IOException("Failed to create dump");

                JsonObject json = JsonParser.parseString(result).getAsJsonObject();
                if (!json.has("key"))
                    throw new IOException("Failed to create dump");
                String key = json.get("key").getAsString();

                return HASTEBIN_URL + key;
            } catch (IOException e) {
                throw new RuntimeException("Failed to create dump", e);
            }
        });
    }

    public static String get(String reqURL) throws IOException {
        URL url = new URL(reqURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", getUserAgent());

        return connectionToString(con);
    }

    public static String post(String reqURL, String postContent) throws IOException {
        URL url = new URL(reqURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("User-Agent", getUserAgent());
        con.setDoOutput(true);

        OutputStream out = con.getOutputStream();
        out.write(postContent.getBytes(StandardCharsets.UTF_8));
        out.close();

        return connectionToString(con);
    }

    private static String connectionToString(HttpURLConnection con) throws IOException {
        // Send the request (we dont use this but its required for getErrorStream() to work)
        con.getResponseCode();

        // Read the error message if there is one if not just read normally
        InputStream inputStream = con.getErrorStream();
        if (inputStream == null) {
            inputStream = con.getInputStream();
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }

            con.disconnect();
        }

        return content.toString();
    }

    public static String getUserAgent() {
        return "VMenu-paper/" + VulkanMenu.get().getPluginMeta().getVersion();
    }
}
