package com.vulkantechnologies.menu.model.requirement.vulkan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.utils.VariableUtils;

@ComponentName("compare")
public record CompareRequirement(String raw) implements Requirement {

    public static final Pattern PATTERN = Pattern.compile("(.*)(>=|<=|==|>|<|!=)(.*)");

    @Override
    public boolean test(Player player, Menu menu) {
        VulkanMenu plugin = VulkanMenu.get();
        String raw = plugin.processPlaceholders(player, menu, this.raw).trim();

        Matcher matcher = PATTERN.matcher(raw);
        if (!matcher.matches() || matcher.groupCount() != 3)
            throw new IllegalArgumentException("Invalid placeholder format: " + raw);

        String rawStart = plugin.processPlaceholders(player, menu, matcher.group(1)).trim();
        String operator = matcher.group(2).trim();
        String rawEnd = plugin.processPlaceholders(player, menu, matcher.group(3)).trim();

        if (operator.equalsIgnoreCase("==")) {
            if (VariableUtils.isNumeric(rawStart) && VariableUtils.isNumeric(rawEnd))
                return Double.parseDouble(rawStart) == Double.parseDouble(rawEnd);
            return rawStart.equals(rawEnd);
        } else if (operator.equalsIgnoreCase("!=")) {
            if (VariableUtils.isNumeric(rawStart) && VariableUtils.isNumeric(rawEnd))
                return Double.parseDouble(rawStart) != Double.parseDouble(rawEnd);
            return !rawStart.equals(rawEnd);
        }

        if (!VariableUtils.isNumeric(rawStart) || !VariableUtils.isNumeric(rawEnd))
            return false;

        double start = Double.parseDouble(rawStart);
        double end = Double.parseDouble(rawEnd);

        return switch (operator) {
            case ">=" -> start >= end;
            case "<=" -> start <= end;
            case ">" -> start > end;
            case "<" -> start < end;
            default -> false;
        };
    }

}
