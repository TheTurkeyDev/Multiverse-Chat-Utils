package com.theprogrammingturkey.mcu.commands;

import com.theprogrammingturkey.mcu.MCUCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalSendCommand extends SubComamnd
{
	public GlobalSendCommand(String permission, boolean playerOnly)
	{
		super(permission, playerOnly);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		MCUCore mcuCore = MCUCore.getPlugin();
		if(mcuCore.globalSendList.remove(sender))
		{
			sender.sendMessage("Your messages will no longer be sent to all worlds!");
		}
		else
		{
			mcuCore.globalSendList.add((Player) sender);
			sender.sendMessage("Your messages will now be sent to all worlds!");
		}
		return true;
	}

}
