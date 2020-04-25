package com.theprogrammingturkey.mcu.listeners;

import com.theprogrammingturkey.mcu.MCUCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatListener implements Listener
{
	private FileConfiguration config;

	public ChatListener(FileConfiguration config)
	{
		this.config = config;
	}

	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event)
	{
		MCUCore mcuCore = MCUCore.getPlugin();
		Player player = event.getPlayer();
		String message = event.getMessage();
		World world = player.getWorld();
		List<World> targetWorlds = new ArrayList<>();

		if(message.startsWith("!") && player.hasPermission("mvcu.global.send"))
		{
			if(!message.startsWith("!["))
			{
				event.setMessage(message.substring(1));
				addAllWorlds(targetWorlds);
			}
			else
			{
				int index = message.indexOf(']');
				if(index == -1)
				{
					player.sendRawMessage("Error missing ']'");
					event.setCancelled(true);
					return;
				}
				else if(index == 2)
				{
					event.setMessage(message.substring(3));
					addAllWorlds(targetWorlds);
				}

				String list = message.substring(2, message.indexOf(']'));
				for(String woldName : list.split(","))
					targetWorlds.add(Bukkit.getWorld(woldName.trim()));

				event.setMessage(message.substring(index + 1));
			}
		}
		else
		{
			if(mcuCore.globalSendList.contains(player))
				addAllWorlds(targetWorlds);
			else
				addAllDims(targetWorlds, world);
		}


		//Clear out all recipients and set the recipients to only be the ones in the same world
		event.getRecipients().clear();
		event.getRecipients().add(player);

		for(World targetWorld : targetWorlds)
			if(targetWorld != null)
				event.getRecipients().addAll(targetWorld.getPlayers());

		event.getRecipients().addAll(mcuCore.globalReadList);
		event.getRecipients().addAll(mcuCore.globalSendList);

		List<String> worldNames = targetWorlds.stream().filter(targetWorld -> targetWorld != null && !targetWorld.getName().endsWith("_nether") && !targetWorld.getName().endsWith("_the_end")).map(World::getName).collect(Collectors.toList());

		sendDebugMessage("Potential worlds: " + Arrays.toString(worldNames.toArray(new String[0])));

		for(String worldName : worldNames)
		{
			ConfigurationSection worldConfig = config.getConfigurationSection("multiverse." + worldName);
			if(worldConfig == null)
			{
				sendDebugMessage("World config null for: " + worldName);
				continue;
			}

			String censorlistID = worldConfig.getString("censor-list");
			if(censorlistID == null)
			{
				sendDebugMessage("Censor list null for: " + worldName);
				continue;
			}

			List<String> censorList = config.getStringList("chat-moderation.censor-list." + censorlistID);
			for(String censor : censorList)
			{
				if(event.getMessage().contains(censor))
				{
					event.setCancelled(true);
					event.getPlayer().sendRawMessage("Sorry! You cannot say " + censor + " here!");
					return;
				}
			}
		}
	}

	public void addAllWorlds(List<World> worldList)
	{
		worldList.addAll(Bukkit.getWorlds());
	}

	public void addAllDims(List<World> worldList, World world)
	{
		String targetWorld = world.getName();
		if(world.getEnvironment().equals(World.Environment.NETHER))
			targetWorld = targetWorld.substring(0, targetWorld.length() - 7);
		else if(world.getEnvironment().equals(World.Environment.THE_END))
			targetWorld = targetWorld.substring(0, targetWorld.length() - 8);
		worldList.add(Bukkit.getWorld(targetWorld));
		worldList.add(Bukkit.getWorld(targetWorld + "_nether"));
		worldList.add(Bukkit.getWorld(targetWorld + "_the_end"));

		ConfigurationSection worldGroups = config.getConfigurationSection("world-groups");
		Set<String> keys = worldGroups.getKeys(false);
		for(String key : keys)
		{
			List<String> group = worldGroups.getStringList(key);
			if(group.contains(targetWorld))
			{
				for(String worldName : group)
				{
					worldList.add(Bukkit.getWorld(worldName));
					worldList.add(Bukkit.getWorld(worldName + "_nether"));
					worldList.add(Bukkit.getWorld(worldName + "_the_end"));
				}
			}
		}
	}

	public void sendDebugMessage(String message)
	{
		for(Player player : MCUCore.getPlugin().debugList)
			player.sendRawMessage(message);
	}
}
