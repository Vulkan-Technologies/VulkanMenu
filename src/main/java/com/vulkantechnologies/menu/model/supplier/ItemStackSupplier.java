package com.vulkantechnologies.menu.model.supplier;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.vulkantechnologies.menu.model.menu.Menu;


public interface ItemStackSupplier {

    List<ItemStack> get(Menu menu, Player player);

    String namespace();

}
