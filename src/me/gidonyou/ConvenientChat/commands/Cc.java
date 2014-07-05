package me.gidonyou.ConvenientChat.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import me.gidonyou.ConvenientChat.ConvenientChat;
import me.gidonyou.ConvenientChat.handlers.ChatRoom;
import me.gidonyou.ConvenientChat.handlers.ErrorMsg;
import me.gidonyou.ConvenientChat.utils.ChatMethod;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cc extends CCCommands {

	public Cc(ConvenientChat instance) {
		super(instance);
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(commandLabel.equalsIgnoreCase("Cc") || commandLabel.equals("채팅")))
			return false;
		if (args.length == 0) {
			HashMap<String, String> help = new HashMap<String, String>();
			help.put("채팅", "도움말");
			help.put("채팅 만들기 [이름]", "채팅방에 입장합니다.");
			help.put("채팅 나가기", "채팅방에서 나갑니다.");
			help.put("채팅 강퇴 [플레이어] [이유]", "대상플레이어를 강퇴합니다.");
			help.put("채팅 입장 [이름]", "채팅방에 들어갑니다.");
			help.put("채팅 지우기 <채팅방 이름>", "채팅방을 강제로 지웁니다.");
			help.put("채팅 목록", "채팅 목록을 알려줍니다.");

			help.put("cc eng", "Show english version of help");

			for (Entry<String, String> entry : help.entrySet())
				ChatMethod.senderMsg(sender,
						ChatColor.DARK_AQUA + "/" + entry.getKey()
								+ ChatColor.BLUE + "  |  " + ChatColor.AQUA
								+ entry.getValue());
		} else {
			String arg = args[0];
			Permission perm = plugin.perms;
			if (args[0].equals("eng")) {
				HashMap<String, String> help = new HashMap<String, String>();
				help.put("cc", "show this help");
				help.put("cc create [name]", "Create the chating Room");
				help.put("cc leave", "Leave the chating room");
				help.put("cc kick [playername] [reason]", "Kick target player");
				help.put("cc join [name]", "Joint he Chating Room");
				help.put("cc remove", "Delete the ChatRoom");
				help.put("cc list", "Show list of chating Rooms");

				for (Entry<String, String> entry : help.entrySet())
					ChatMethod.senderMsg(sender, ChatColor.DARK_AQUA + "/"
							+ entry.getKey() + ChatColor.BLUE + "  |  "
							+ ChatColor.AQUA + entry.getValue());
			}

			if (args[0].equalsIgnoreCase("create") || args[0].equals("만들기")) {
				if (!(perm.has(sender, "cc.user.create") || sender.isOp())) {
					ChatMethod.senderMsg(sender, ErrorMsg.NoPerm.getMsg());
					return false;
				}
				if (args.length != 2) {
					ChatMethod.senderMsg(sender, ChatColor.BLUE
							+ "/cc create <name>");
					return false;
				}
				if (sender instanceof Player) {
					if (ChatRoom.createChatRoom((Player) sender, args[1])) {
						ChatMethod.serverMsg((Player) sender, "debug",
								"Succeed");
						return true;
					} else {
						ChatMethod
								.serverMsg((Player) sender, "debug", "Failed");
						return true;
					}
				}
				return false;
			}
			if (args[0].equalsIgnoreCase("leave") || args[0].equals("나가기")) {
				if (!(perm.has(sender, "cc.user.leave") || sender.isOp())) {
					ChatMethod.senderMsg(sender, ErrorMsg.NoPerm.getMsg());
					return false;
				}
				if (!(sender instanceof Player)) {
					ChatMethod.senderMsg(sender, ErrorMsg.noCmd.getMsg());
					return false;
				}
				ChatRoom.leave((Player) sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("kick") || args[0].equals("강퇴")) {
				if (!(perm.has(sender, "cc.user.kick") || sender.isOp())) {
					ChatMethod.senderMsg(sender, ErrorMsg.NoPerm.getMsg());
					return false;
				}
				if (!(sender instanceof Player)) {
					ChatMethod.senderMsg(sender, ErrorMsg.noCmd.getMsg());
					return false;
				}
				Player player = (Player) sender;
				if (ChatRoom.playerHasChatroom(player)) {
					if (args.length >= 2) {
						if (Bukkit.getPlayer(args[1]) == null) {
							ChatMethod.sendMsg(player,
									ErrorMsg.args_hastobe_Player.getMsg());
							return false;
						}
						Player target = Bukkit.getPlayer(args[1]);
						String reason = "";
						for (int i = 1; i < args.length - 1; i++)
							reason += args[i] + " ";
						ChatRoom.kick(player, target.getUniqueId().toString(),
								reason);
						return true;
					} else {
						ChatMethod.senderMsg(sender, ChatColor.BLUE
								+ "/cc kick <name> <reason>");
						return false;
					}
				} else {
					ChatMethod.sendMsg(player,
							ErrorMsg.Not_In_ChatRoom.getMsg());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("join") || args[0].equals("입장")) {
				if (!(sender instanceof Player)) {
					ChatMethod.senderMsg(sender, ErrorMsg.noCmd.getMsg());
					return false;
				}
				if (!(perm.has(sender, "cc.user.join") || sender.isOp())) {
					ChatMethod.senderMsg(sender, ErrorMsg.NoPerm.getMsg());
					return false;
				}
				if (args.length == 2) {
					ChatRoom.join((Player) sender, args[1]);
					return true;
				}
				ChatMethod
						.senderMsg(sender, ChatColor.BLUE + "/cc join <name>");
				return false;
			}
			if (args[0].equalsIgnoreCase("delete") || arg.equals("지우기")) {
				if (!(perm.has(sender, "cc.mod.remove") || sender.isOp())) {
					ChatMethod.senderMsg(sender, ErrorMsg.NoPerm.getMsg());
					return false;
				}
				if (args.length == 2) {
					ChatRoom.remove(sender, args[1]);
					return true;
				}
				ChatMethod.senderMsg(sender, ChatColor.BLUE
						+ "/cc delete <name>");
				return false;
			}
			if (arg.equalsIgnoreCase("list") || arg.equals("목록")) {
				if(args.length != 1){
					ChatMethod
					.senderMsg(sender, ChatColor.BLUE + "/cc list");
					return false;
				}
				if(perm.has(sender, "cc.mod.list") || sender.isOp()){
					ChatRoom.modList(sender);
					return true;
				}
				if(perm.has(sender, "cc.user.list" ) || sender.isOp()){
					ChatRoom.list(sender);
					return true;
				}
				ChatMethod.senderMsg(sender, ErrorMsg.NoPerm.getMsg());
				return false;
			}
		}

		return false;
	}
}
