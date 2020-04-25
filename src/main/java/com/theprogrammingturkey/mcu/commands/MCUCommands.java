package com.theprogrammingturkey.mcu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MCUCommands implements CommandExecutor
{
	private Map<String, SubComamnd> commands = new HashMap<>();

	public MCUCommands()
	{
		commands.put("globalsend", new GlobalSendCommand("mvcu.global.send", true));
		commands.put("globalread", new GlobalReadCommand("mvcu.global.read", true));
		commands.put("debug", new GlobalReadCommand("mvcu.debug", true));
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		SubComamnd subComamnd = commands.get(args[0]);
		if(subComamnd != null)
		{
			if(subComamnd.canRunCommand(sender))
				return subComamnd.onCommand(sender, command, label, args);
			return true;
		}

		return false;
	}
}
