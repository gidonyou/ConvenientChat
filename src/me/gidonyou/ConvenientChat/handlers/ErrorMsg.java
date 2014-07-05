package me.gidonyou.ConvenientChat.handlers;

import org.bukkit.ChatColor;


public enum ErrorMsg {
	
	Not_In_ChatRoom(ChatColor.RED +"당신은 체팅방에 있지안습니다."),
	NoPerm(ChatColor.DARK_RED + "권한이 없습니다."),
	args_hastobe_Player(ChatColor.RED + "대상은 온라인 플레이어야합니다."),
	noCmd(ChatColor.RED + "플레이어만 이용할수있는 커맨드입니다."),
	alreadyinChatRm(ChatColor.RED + "이미 체팅그룹에 있습니다."),
	isLocked(ChatColor.RED + "채팅방이 장겨있습니다. 체팅방주인이 초대해야지 접속이 가능합니다."),
	unKnown_Room(ChatColor.RED + "알수없는 체팅방입니다.")
	;
	
	private String msg = "";
	
	ErrorMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}
}
