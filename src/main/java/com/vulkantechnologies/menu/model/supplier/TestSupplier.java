package com.vulkantechnologies.menu.model.supplier;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.vulkantechnologies.menu.model.menu.Menu;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TestSupplier implements ItemStackSupplier {

    @Override
    public List<ItemStack> get(Menu menu, Player player) {
        return List.of(
                new ItemStack(Material.DIAMOND, 5),
                new ItemStack(Material.GOLD_INGOT, 10),
                new ItemStack(Material.IRON_INGOT, 15),
                new ItemStack(Material.EMERALD, 20),
                new ItemStack(Material.NETHERITE_INGOT, 25),
                new ItemStack(Material.COPPER_INGOT, 30),
                new ItemStack(Material.AMETHYST_SHARD, 35),
                new ItemStack(Material.LAPIS_LAZULI, 40),
                new ItemStack(Material.REDSTONE, 45),
                new ItemStack(Material.COAL, 50),
                new ItemStack(Material.QUARTZ, 55),
                new ItemStack(Material.BOOK, 60),
                new ItemStack(Material.ENCHANTED_BOOK, 65),
                new ItemStack(Material.WRITABLE_BOOK, 70),
                new ItemStack(Material.WRITTEN_BOOK, 75),
                new ItemStack(Material.PAPER, 80),
                new ItemStack(Material.MAP, 85)
        );
    }

    @Override
    public String namespace() {
        return "test";
    }
}
