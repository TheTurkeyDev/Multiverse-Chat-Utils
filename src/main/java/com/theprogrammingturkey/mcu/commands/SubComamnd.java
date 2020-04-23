package com.theprogrammingturkey.mcu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubComamnd
{
	private String permission;
	private boolean playerOnly;

	public SubComamnd(String permission, boolean playerOnly)
	{
		this.permission = permission;
		this.playerOnly = playerOnly;
	}

	public boolean canRunCommand(CommandSender sender)
	{
		if(!(sender instanceof Player) && playerOnly)
		{
			sender.sendMessage("Sorry only players can run that command!");
			return false;
		}

		if(playerOnly)
		{
			Player player = (Player) sender;
			if(!player.hasPermission(permission))
			{
				sender.sendMessage("Sorry you don't have permission to do that!");
				return false;
			}
		}
		return true;
	}

	public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
