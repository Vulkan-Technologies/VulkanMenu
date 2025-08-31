package com.vulkantechnologies.menu.model.provider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.spongepowered.configurate.serialize.SerializationException;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

public class HeadItemProvider implements ItemStackProvider {

    private static final Pattern BASE64_PATTERN = Pattern.compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    private static final Pattern URL_PATTERN = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)");

    @Override
    public ItemStack provide(String value) throws SerializationException {
        if (value == null || value.isEmpty())
            throw new SerializationException("Value for head provider cannot be null or empty");

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        // Base 64 texture
        if (BASE64_PATTERN.matcher(value).matches()) {
            itemStack.editMeta(SkullMeta.class, skullMeta -> {
                PlayerProfile profile = Bukkit.createProfile(null, null);
                profile.setProperty(new ProfileProperty("textures", value));
                skullMeta.setPlayerProfile(profile);
            });
            return itemStack;
        } else if (URL_PATTERN.matcher(value).matches()) {
            itemStack.editMeta(SkullMeta.class, skullMeta -> {
                PlayerProfile profile = Bukkit.createProfile(null, null);
                PlayerTextures textures = profile.getTextures();
                try {
                    textures.setSkin(new URI(value).toURL());
                } catch (URISyntaxException | MalformedURLException e) {
                    throw new RuntimeException("Invalid URL for head texture: " + value, e);
                }
                skullMeta.setPlayerProfile(profile);
            });
            return itemStack;
        }

        // Player head
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(value);
        itemStack.editMeta(SkullMeta.class, skullMeta -> skullMeta.setOwningPlayer(offlinePlayer));

        return itemStack;
    }

    @Override
    public String prefix() {
        return "head";
    }
}
