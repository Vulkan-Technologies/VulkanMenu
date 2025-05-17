package com.vulkantechnologies.menu.model.requirement.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("experience")
public record ExperienceRequirement(int level) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        return player.getLevel() >= level;
    }

}
