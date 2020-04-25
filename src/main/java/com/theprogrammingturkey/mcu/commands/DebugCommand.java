package com.theprogrammingturkey.mcu.commands;

import com.theprogrammingturkey.mcu.MCUCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand extends SubComamnd
{
	public DebugCommand(String permission, boolean playerOnly)
	{
		super(permission, playerOnly);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		MCUCore mcuCore = MCUCore.getPlugin();
		if(mcuCore.debugList.remove(sender))
		{
			sender.sendMessage("Your messages will no longer receive debug messages!");
		}
		else
		{
			mcuCore.debugList.add((Player) sender);
			sender.sendMessage("Your messages will now receive debug messages!");
		}
		return true;
	}

}
