package me.gidonyou.ConvenientChat.utils;

import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.BLUE;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatMethod {

	private static String intro = DARK_GREEN + "[" + BLUE + "Convenient Chat"
			+ DARK_GREEN + "] " + RESET;

	public static void sendMsg(Player player, String msg) {
		player.sendMessage(intro + msg);
	}

	public static void broadcastPlayer(String msg) {
		for (Player player : Bukkit.getOnlinePlayers())
			ChatMethod.sendMsg(player, msg);
	}

	public static void serverMsg(Player player, String prefix, String Msg) {
		player.sendMessage(ChatColor.BLUE + prefix + ">  " + ChatColor.RESET
				+ Msg);
	}

	public static void senderMsg(CommandSender sender, String msg) {
		sender.sendMessage(intro + msg);
	}

	public static void broadcastServerMsg(String prefix, String Msg, boolean op) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (op) {
				if (player.isOp())
					serverMsg(player, prefix, Msg);
			}else{
				serverMsg(player, prefix, Msg);
			}
		}
	}

}
