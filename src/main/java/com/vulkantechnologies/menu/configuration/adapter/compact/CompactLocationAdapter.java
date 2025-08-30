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
        // Check initial argument count
        int initialArgCount = context.remainingArgCount();
        if (initialArgCount < 4)
            throw new IllegalArgumentException("Location requires at least 4 arguments: x, y, z, world (got " + initialArgCount + ")");

        // Parse x, y, z coordinates
        double x = Double.parseDouble(context.popFirstArg());
        double y = Double.parseDouble(context.popFirstArg());
        double z = Double.parseDouble(context.popFirstArg());
        
        // Check remaining arguments after consuming x, y, z
        int remainingArgs = context.remainingArgCount();
        float yaw = 0;
        float pitch = 0;
        String worldName;
        
        if (remainingArgs == 1) {
            // Format: x y z world
            worldName = context.popFirstArg();
        } else if (remainingArgs == 3) {
            // Format: x y z yaw pitch world
            yaw = Float.parseFloat(context.popFirstArg());
            pitch = Float.parseFloat(context.popFirstArg());
            worldName = context.popFirstArg();
        } else {
            throw new IllegalArgumentException("Invalid number of arguments for Location. Expected 4 (x,y,z,world) or 6 (x,y,z,yaw,pitch,world), but got " + initialArgCount);
        }

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