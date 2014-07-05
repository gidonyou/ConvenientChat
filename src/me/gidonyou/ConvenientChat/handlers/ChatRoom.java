package me.gidonyou.ConvenientChat.handlers;

import java.util.ArrayList;
import java.util.List;

import me.gidonyou.ConvenientChat.config.CCCustomConfig;
import me.gidonyou.ConvenientChat.utils.ChatMethod;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ChatRoom {
	public static boolean addPlayer(Player player) {
		if (CCCustomConfig.getConfig("playerdata.yml").getCustomConfig()
				.getString(player.getUniqueId().toString()) != null)
			return false;

		FileConfiguration config = CCCustomConfig.getConfig("playerdata.yml")
				.getCustomConfig();

		config.set(player.getUniqueId().toString() + ".username",
				player.getName());
		config.set(player.getUniqueId().toString() + ".room", "null");

		CCCustomConfig.getConfig("playerdata.yml").saveCustomConfig();
		return true;

	}

	public static boolean playerHasChatroom(Player player) {
		String UUID = player.getUniqueId().toString();
		try {
			if (CCCustomConfig.getConfig("playerdata.yml").getCustomConfig()
					.getString(UUID + ".room").equals("null"))
				return false;
			return true;
		} catch (NullPointerException e) {
			ChatMethod.sendMsg(player, ChatColor.RED + "�����߻� �������ּ���");
			e.printStackTrace();
			return false;
		}

	}

	public static boolean join(Player player, String name) {
		if (playerHasChatroom(player)) {
			ChatMethod.sendMsg(player, ErrorMsg.alreadyinChatRm.getMsg());
			return false;
		}

		if (CCCustomConfig.getConfig("roomdata.yml").getCustomConfig()
				.get(name) == null) {
			ChatMethod.sendMsg(player, ErrorMsg.unKnown_Room.getMsg());
			return false;
		}

		if (isLocked(name)) {
			ChatMethod.sendMsg(player, ErrorMsg.isLocked.getMsg());
			return false;
		}

		CCCustomConfig.getConfig("playerdata.yml").getCustomConfig()
				.set(player.getUniqueId() + ".room", name);

		CCCustomConfig.getConfig("playerdata.yml").saveCustomConfig();

		for (String people : ChatRoom.getAllChatPlayer(name)) {
			Player member = Bukkit.getPlayer(CCCustomConfig
					.getConfig("playerdata.yml").getCustomConfig()
					.getString(people + ".username"));
			if (member == null)
				continue;
			member.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_GREEN
					+ name + ChatColor.GREEN + "] " + ChatColor.YELLOW
					+ player.getDisplayName() + ChatColor.GOLD + "���� �����Ͽ����ϴ�.");
		}
		return true;
	}

	public static boolean createChatRoom(Player player, String name) {
		if (playerHasChatroom(player)) {
			ChatMethod.sendMsg(player, ErrorMsg.alreadyinChatRm.getMsg());
			return false;
		}
		FileConfiguration config = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig();
		if (config.getString(name) != null) {
			ChatMethod.sendMsg(player, ChatColor.RED + "�����̸��� ü�ù��� �ֽ��ϴ�.");
			return false;
		}

		config.set(name + ".owner", player.getUniqueId().toString());
		config.set(name + ".lock", false);

		join(player, name);

		CCCustomConfig.getConfig("roomdata.yml").saveCustomConfig();

		ChatMethod.sendMsg(player, ChatColor.BLUE + "���������� " + ChatColor.GOLD
				+ name + ChatColor.BLUE + " �� ��������ϴ�.");
		return true;
	}

	public static boolean leave(Player player) {
		if (!playerHasChatroom(player)) {
			ChatMethod.sendMsg(player, ErrorMsg.Not_In_ChatRoom.getMsg());
			return false;
		}
		String uuid = player.getUniqueId().toString();
		String rmName = CCCustomConfig.getConfig("playerdata.yml")
				.getCustomConfig().getString(uuid + ".room");
		String owner = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig().getString(rmName + ".owner");

		if (owner != null && owner.equals(uuid))
			deleteRoom(rmName);

		CCCustomConfig.getConfig("playerdata.yml").getCustomConfig()
				.set(uuid + ".room", "null");
		CCCustomConfig.getConfig("playerdata.yml").saveCustomConfig();

		ChatMethod.sendMsg(player, "���������� �����̽��ϴ�.");

		for (String people : ChatRoom.getAllChatPlayer(rmName)) {
			Player member = Bukkit.getPlayer(CCCustomConfig
					.getConfig("playerdata.yml").getCustomConfig()
					.getString(people + ".username"));
			if (member == null)
				continue;
			member.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_GREEN
					+ rmName + ChatColor.GREEN + "] " + ChatColor.YELLOW
					+ player.getDisplayName() + ChatColor.LIGHT_PURPLE
					+ "���� �����̽��ϴ�.");
		}

		return true;

	}

	public static void deleteRoom(String rmName) {
		FileConfiguration config = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig();

		if (config.get(rmName) == null)
			return;

		String owner = config.getString(rmName + ".owner");
		if (owner == null)
			return;

		for (String uuid : getAllChatPlayer(rmName))
			kick(null, uuid, "ä�ù������� ä�ù濡�� �������ϴ�.");

		config.set(rmName, null);
		CCCustomConfig.getConfig("roomdata.yml").saveCustomConfig();

		for (Player player : Bukkit.getOnlinePlayers())
			if (player.getUniqueId().equals(owner))
				ChatMethod.sendMsg(player, "���������� ä�ù��� �����߽��ϴ�.");
	}

	public static void kick(Player order, String uuid, String reason) {
		FileConfiguration config = CCCustomConfig.getConfig("playerdata.yml")
				.getCustomConfig();

		if (order != null) {
			if (!playerHasChatroom(order)) {
				ChatMethod.sendMsg(order, ErrorMsg.Not_In_ChatRoom.getMsg());
				return;
			}
			if (getOwner(config.getString(uuid + ".room")) != order
					.getUniqueId().toString()) {
				ChatMethod.sendMsg(order, ErrorMsg.NoPerm.getMsg());
				return;
			}

		}

		String playername = config.getString(uuid + ".username");
		Player player = Bukkit.getPlayer(playername);

		config.set(uuid + ".room", "null");

		if (player != null)
			ChatMethod.sendMsg(player, ChatColor.RED + "ä�ù濡�� ���� �Ǿ����ϴ�.. "
					+ ChatColor.GOLD + "����: " + ChatColor.YELLOW + reason);

		CCCustomConfig.getConfig("playerdata.yml").saveCustomConfig();
	}

	public static List<String> getAllChatPlayer(String rmName) {
		FileConfiguration config = CCCustomConfig.getConfig("playerdata.yml")
				.getCustomConfig();
		List<String> result = new ArrayList<String>();

		for (String string : config.getConfigurationSection("").getKeys(false))
			if (config.getString(string + ".room").equals(rmName))
				result.add(string);

		return result;
	}

	public static String getOwner(String rmName) {
		FileConfiguration config = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig();
		return config.getString(rmName + ".owner");
	}

	public static boolean isLocked(String rmName) {
		return CCCustomConfig.getConfig("roomdata.yml").getCustomConfig()
				.getBoolean(rmName + "lock");
	}

	public static void checkPlayer(Player player) {
		FileConfiguration config = CCCustomConfig.getConfig("playerdata.yml")
				.getCustomConfig();

		String name = player.getName();
		String uuid = player.getUniqueId().toString();

		if (config.getString(uuid + ".username") == name)
			return;
		config.set(uuid + ".username", name);
		CCCustomConfig.getConfig("playerdata.yml").saveCustomConfig();
		ChatMethod.sendMsg(player, ChatColor.GOLD + "�÷��̾� ���������� ������Ʈ �Ǿ����ϴ�.");
	}

	public static void remove(CommandSender sender, String rmName) {
		FileConfiguration config = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig();

		if (config.get(rmName) == null) {
			ChatMethod.senderMsg(sender, ErrorMsg.unKnown_Room.getMsg());
			return;
		}

		String owner = config.getString(rmName + ".owner");
		if (owner == null)
			return;

		for (String uuid : getAllChatPlayer(rmName))
			kick(null, uuid, "ä�ù������� ä�ù濡�� �������ϴ�.");

		config.set(rmName, null);
		CCCustomConfig.getConfig("roomdata.yml").saveCustomConfig();

		for (Player player : Bukkit.getOnlinePlayers())
			if (player.getUniqueId().equals(owner))
				ChatMethod.sendMsg(player, "���������� ä�ù��� �����߽��ϴ�.");
		ChatMethod.senderMsg(sender, ChatColor.GREEN + "���������� "
				+ ChatColor.DARK_GREEN + rmName + ChatColor.GREEN
				+ " ä�ù��� ���x���ϴ�.");
	}

	public static void modList(CommandSender sender) {
		FileConfiguration config = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig();
		int i = 0;

		sender.sendMessage(ChatColor.DARK_GREEN + "===== "
				+ ChatColor.DARK_PURPLE + "ä�ù� ��� (mod)" + ChatColor.DARK_GREEN
				+ " =====");
		for (String string : config.getConfigurationSection("").getKeys(false)) {
			i++;
			if (config.getBoolean(string + ".lock")) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + String.valueOf(i)
						+ ". " + ChatColor.RED + string);
			} else {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + String.valueOf(i)
						+ ". " + ChatColor.GREEN + string);
			}
		}
	}

	public static void list(CommandSender sender) {
		FileConfiguration config = CCCustomConfig.getConfig("roomdata.yml")
				.getCustomConfig();
		int i = 0;
		
		sender.sendMessage(ChatColor.DARK_GREEN + "===== "
				+ ChatColor.DARK_PURPLE + "ä�ù� ���" + ChatColor.DARK_GREEN
				+ " =====");
		for (String string : config.getConfigurationSection("").getKeys(false)) {
			i++;
			if (!config.getBoolean(string + ".lock")) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + String.valueOf(i)
						+ ". " + ChatColor.GREEN + string);
			}
		}
	}
}