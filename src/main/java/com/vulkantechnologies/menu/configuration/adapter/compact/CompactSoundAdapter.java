package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class CompactSoundAdapter implements CompactAdapter<Sound> {

    public static final CompactSoundAdapter INSTANCE = new CompactSoundAdapter();

    @Override
    public Sound adapt(CompactContext context) {
        Sound.Builder builder = Sound.sound();

        String soundName = context.popFirstArg();
        if (soundName == null || soundName.isEmpty())
            throw new IllegalArgumentException("Sound name cannot be null or empty");
        else if (!Key.parseable(soundName))
            throw new IllegalArgumentException("Invalid sound name: " + soundName);

        builder.type(Key.key(soundName));

        if (context.argumentCount() >= 2)
            builder.volume(Float.parseFloat(context.popFirstArg()));
        if (context.argumentCount() >= 3)
            builder.pitch(Float.parseFloat(context.popFirstArg()));
        if (context.argumentCount() >= 4)
            builder.source(Sound.Source.valueOf(context.popFirstArg().toUpperCase()));

        return builder.build();
    }

    @Override
    public Class<Sound> type() {
        return Sound.class;
    }
}
