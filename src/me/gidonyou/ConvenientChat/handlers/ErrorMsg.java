package me.gidonyou.ConvenientChat.handlers;

import org.bukkit.ChatColor;


public enum ErrorMsg {
	
	Not_In_ChatRoom(ChatColor.RED +"����� ü�ù濡 �����Ƚ��ϴ�."),
	NoPerm(ChatColor.DARK_RED + "������ �����ϴ�."),
	args_hastobe_Player(ChatColor.RED + "����� �¶��� �÷��̾���մϴ�."),
	noCmd(ChatColor.RED + "�÷��̾ �̿��Ҽ��ִ� Ŀ�ǵ��Դϴ�."),
	alreadyinChatRm(ChatColor.RED + "�̹� ü�ñ׷쿡 �ֽ��ϴ�."),
	isLocked(ChatColor.RED + "ä�ù��� ����ֽ��ϴ�. ü�ù������� �ʴ��ؾ��� ������ �����մϴ�."),
	unKnown_Room(ChatColor.RED + "�˼����� ü�ù��Դϴ�.")
	;
	
	private String msg = "";
	
	ErrorMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}
}
