package com.iakd.remotecreeper.events;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.iakd.remotecreeper.items.CreeperRemoteItemStack;

public class CreeperRemoteEvents implements Listener {

	private final Plugin plugin;
	private final long cooldown;
	private final int range;
	private final float burstIntensity;
	private HashMap<UUID, Long> cooldownMap = new HashMap<UUID, Long>();

	public CreeperRemoteEvents(Plugin plugin) {
		this.plugin = plugin;
		cooldown = plugin.getConfig().getLong("config.cooldown") * 1000;
		range = plugin.getConfig().getInt("config.range");
		burstIntensity = plugin.getConfig().getInt("config.burstIntensity");

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		final PlayerInteractEvent evt = e;

		ItemStack item = evt.getItem();
		ItemMeta meta = item == null ? null : item.getItemMeta();
		String displayName = meta == null ? null : meta.getDisplayName();

		if ((evt.getAction() == Action.LEFT_CLICK_AIR || evt.getAction() == Action.LEFT_CLICK_BLOCK)) {

			if (displayName != null
					&& displayName.equals(CreeperRemoteItemStack.DISPLAY_NAME)) {
				return;
			}

		}

		if (displayName != null
				&& displayName.equals(CreeperRemoteItemStack.DISPLAY_NAME)
				&& (evt.getAction() == Action.RIGHT_CLICK_AIR || evt
						.getAction() == Action.RIGHT_CLICK_BLOCK)) {

			boolean allowUse = false;
			Long useTime = cooldownMap.get(evt.getPlayer().getUniqueId());

			if (useTime == null
					|| System.currentTimeMillis() - useTime > cooldown) {
				allowUse = true;
			}

			if (allowUse) {
				cooldownMap.put(evt.getPlayer().getUniqueId(),
						System.currentTimeMillis());
				this.plugin.getServer().getScheduler()
						.scheduleSyncDelayedTask(plugin, new Runnable() {

							@Override
							public void run() {
								Player p = evt.getPlayer();
								p.sendMessage(ChatColor.GRAY
										+ "[Creeper Remote] " + ChatColor.GREEN
										+ "Sending signal.");
								List<Entity> entities = p.getNearbyEntities(
										range, range, range);
								for (Entity e : entities) {
									if (e instanceof Creeper) {
										final Creeper creeper = (Creeper) e;
										p.sendMessage(ChatColor.GRAY
												+ "[Creeper Remote] "
												+ ChatColor.DARK_RED
												+ "Creeper primed!");
										creeper.setPowered(true);
										creeper.setFireTicks(20 * 3);
										plugin.getServer()
												.getScheduler()
												.scheduleSyncDelayedTask(
														plugin, new Runnable() {

															@Override
															public void run() {
																creeper.getWorld()
																		.createExplosion(
																				creeper.getLocation(),
																				burstIntensity);
																creeper.damage(999F);

															}
														}, 20 * 3);
									}
								}
							}
						}, 0);
				evt.setCancelled(true);
				return;
			} else {
				evt.getPlayer().sendMessage(
						ChatColor.GRAY + "[Creeper Remote] " + "On cooldown.");
				evt.setCancelled(true);
				return;
			}
		}
	}
}
