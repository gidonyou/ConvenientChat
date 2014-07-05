package me.gidonyou.ConvenientChat.config;

import me.gidonyou.ConvenientChat.ConvenientChat;

public class CCConfigs {

	ConvenientChat plugin;

	public CCConfigs(ConvenientChat instance) {
		plugin = instance;
	}

	public void enable() {
		new CCCustomConfig(plugin, "playerdata.yml").saveDefaultConfig();
		new CCCustomConfig(plugin, "roomdata.yml").saveDefaultConfig();
		
		CCCustomConfig.getConfig("playerdata.yml").reload();
		CCCustomConfig.getConfig("roomdata.yml").reload();
	}

}
