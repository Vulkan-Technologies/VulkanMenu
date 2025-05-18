package com.vulkantechnologies.menu.configuration.adapter.compact;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

public class CompactLocationAdapter implements CompactAdapter<Location> {

    public static final CompactLocationAdapter INSTANCE = new CompactLocationAdapter();

    @Override
    public Location adapt(CompactContext context) {
        if (context.argumentCount() < 4)
            throw new IllegalArgumentException("Location requires 4 arguments: x, y, z, world");

        double x = Double.parseDouble(context.popFirstArg());
        double y = Double.parseDouble(context.popFirstArg());
        double z = Double.parseDouble(context.popFirstArg());
        float yaw = 0;
        float pitch = 0;
        String worldName = context.popFirstArg();
        if (context.argumentCount() == 3) {
            worldName = context.popFirstArg();
        } else if (context.argumentCount() == 6) {
            yaw = Float.parseFloat(context.popFirstArg());
            pitch = Float.parseFloat(context.popFirstArg());
            worldName = context.popFirstArg();
        } else
            throw new IllegalArgumentException("Invalid number of arguments for Location: " + context.argumentCount());

        World world = Bukkit.getWorld(worldName);
        if (world == null)
            throw new IllegalArgumentException("World not found: " + worldName);

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public Class<Location> type() {
        return Location.class;
    }

}
