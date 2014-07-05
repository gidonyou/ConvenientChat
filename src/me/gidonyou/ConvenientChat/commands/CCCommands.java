package me.gidonyou.ConvenientChat.commands;

import me.gidonyou.ConvenientChat.ConvenientChat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CCCommands implements CommandExecutor {

	ConvenientChat plugin;

	public CCCommands(ConvenientChat instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

}
