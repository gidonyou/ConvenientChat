package me.gidonyou.ConvenientChat.listeners;

import me.gidonyou.ConvenientChat.ConvenientChat;
import me.gidonyou.ConvenientChat.config.CCCustomConfig;
import me.gidonyou.ConvenientChat.handlers.ChatRoom;
import me.gidonyou.ConvenientChat.handlers.ErrorMsg;
import me.gidonyou.ConvenientChat.utils.ChatMethod;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat extends CCListeners {

	public AsyncPlayerChat(ConvenientChat instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}

	// private Essentials ess;

	@EventHandler
	public void onAsyncPlayerChatEventt(AsyncPlayerChatEvent event) {
		Permission perm = plugin.perms;

		Player player = event.getPlayer();

		if (!ChatRoom.playerHasChatroom(player))
			return;
		event.setCancelled(true);
		if (!(perm.has(player, "cc.user.chat") || player.isOp())) {
			ChatMethod.sendMsg(player, ErrorMsg.NoPerm.getMsg());
			return;
		}
		FileConfiguration config = CCCustomConfig.getConfig("playerdata.yml")
				.getCustomConfig();
		String uuid = player.getUniqueId().toString();

		String rmName = config.getString(uuid + ".room");
		
		
		for (String people : ChatRoom.getAllChatPlayer(rmName)) {
			Player member = Bukkit.getPlayer(config.getString(people
					+ ".username"));
			if (member == null)
				continue;
			member.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_GREEN
					+ rmName + ChatColor.GREEN + "] " + ChatColor.RESET
					+ player.getDisplayName() + ": " + ChatColor.RESET
					+ event.getMessage());
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			if ((!(perm.has(player, "cc.mod.spy") || player.isOp())))
				continue;
			if(p.getName() == player.getName())
				continue;
			p.sendMessage(ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + rmName
					+ ChatColor.GREEN + "] " + ChatColor.RESET
					+ player.getDisplayName() + ": " + ChatColor.RESET
					+ event.getMessage());
		}
		Bukkit.getConsoleSender().sendMessage(
				ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + rmName
						+ ChatColor.GREEN + "] " + ChatColor.RESET
						+ player.getDisplayName() + ": " + ChatColor.RESET
						+ event.getMessage());

	}
	/*
	 * private void setupEssentials() { RegisteredServiceProvider<Essentials>
	 * essProvider = plugin.getServer()
	 * .getServicesManager().getRegistration(Essentials.class); if (essProvider
	 * != null) { ess = essProvider.getProvider(); } }
	 */

}