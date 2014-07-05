package me.gidonyou.ConvenientChat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gidonyou.ConvenientChat.ConvenientChat;
import me.gidonyou.ConvenientChat.handlers.ChatRoom;

public class PlayerJoin extends CCListeners{

	public PlayerJoin(ConvenientChat instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}
	
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event){
		ChatRoom.addPlayer(event.getPlayer());
		ChatRoom.checkPlayer(event.getPlayer());
	}
	


}
