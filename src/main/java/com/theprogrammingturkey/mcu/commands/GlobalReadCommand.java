package com.theprogrammingturkey.mcu.commands;

import com.theprogrammingturkey.mcu.MCUCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalReadCommand extends SubComamnd
{
	public GlobalReadCommand(String permission, boolean playerOnly)
	{
		super(permission, playerOnly);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		MCUCore mcuCore = MCUCore.getPlugin();
		if(mcuCore.globalReadList.remove(sender))
		{
			sender.sendMessage("You will no longer see messages from all worlds!");
		}
		else
		{
			mcuCore.globalReadList.add((Player) sender);
			sender.sendMessage("You will now see messages from all worlds!");
		}
		return true;
	}

}
