package com.theprogrammingturkey.mcu;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.theprogrammingturkey.mcu.listeners.ChatListener;
import com.theprogrammingturkey.mcu.commands.MCUCommands;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MCUCore extends JavaPlugin
{
	private FileConfiguration config = getConfig();

	public List<Player> globalSendList = new ArrayList<>();
	public List<Player> globalReadList = new ArrayList<>();

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();

		getServer().getPluginManager().registerEvents(new ChatListener(config), this);
		this.getCommand("mvcu").setExecutor(new MCUCommands());

		getLogger().info("is enabled!");
	}

	@Override
	public void onDisable()
	{
		getLogger().info("is now disabled!");
	}

	public static MCUCore getPlugin()
	{
		return (MCUCore) Bukkit.getPluginManager().getPlugin("Multiverse-Chat-Utils");
	}
}
