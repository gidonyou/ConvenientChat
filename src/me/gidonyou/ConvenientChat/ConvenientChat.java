package me.gidonyou.ConvenientChat;


import me.gidonyou.ConvenientChat.commands.Cc;
import me.gidonyou.ConvenientChat.config.CCConfigs;
import me.gidonyou.ConvenientChat.listeners.AsyncPlayerChat;
import me.gidonyou.ConvenientChat.listeners.PlayerJoin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ConvenientChat extends JavaPlugin {

	public final CCConfigs cconfig = new CCConfigs(this);
	
	public Permission perms = null;
	public Chat chat = null;

	public void onEnable() {
		System.out.println("Covenient Chat: Copyright (C) 2014 gidonyou");
		
		cconfig.enable();
		setupVault();

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new AsyncPlayerChat(this), this);
		pm.registerEvents(new PlayerJoin(this), this);
		
		getCommand("cc").setExecutor(new Cc(this));
	}
	
	private void setupVault() {
		setupPermissions();
		setupChat();
	}

	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
}
