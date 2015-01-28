package com.iakd.remotecreeper.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreeperRemoteItemStack extends ItemStack {

	public static final String DISPLAY_NAME = "Creeper Remote";
	public static final List<String> LORE = Arrays.asList(
			"Blows up the nearest creeper.", "Right-click to use!");

	public CreeperRemoteItemStack() {
		super(Material.SKULL_ITEM, 1, (short) SkullType.CREEPER.ordinal());
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(DISPLAY_NAME);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setLore(LORE);
		this.setItemMeta(meta);
	}

}
