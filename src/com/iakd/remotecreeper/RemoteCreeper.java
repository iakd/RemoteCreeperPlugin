package com.iakd.remotecreeper;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.iakd.remotecreeper.events.CreeperRemoteEvents;
import com.iakd.remotecreeper.items.CreeperRemoteItemStack;

public class RemoteCreeper extends JavaPlugin {

	@Override
	public void onEnable() {
		loadConfig();
		addCustomRecipes();
		registerEvents();
	}

	protected void loadConfig() {
		this.getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	protected void registerEvents() {
		this.getServer().getPluginManager()
				.registerEvents(new CreeperRemoteEvents(this), this);
	}

	protected void addCustomRecipes() {
		ShapedRecipe creeperRemoteRecipe = new ShapedRecipe(
				new CreeperRemoteItemStack());
		creeperRemoteRecipe.shape(" r ", "ggg", "odo")
				.setIngredient('r', Material.REDSTONE_BLOCK)
				.setIngredient('g', Material.SULPHUR)
				.setIngredient('o', Material.OBSIDIAN)
				.setIngredient('d', Material.DIAMOND);
		this.getServer().addRecipe(creeperRemoteRecipe);
	}
}
