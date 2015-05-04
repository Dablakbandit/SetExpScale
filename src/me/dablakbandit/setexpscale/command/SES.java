package me.dablakbandit.setexpscale.command;

import me.dablakbandit.setexpscale.SetExpScale;
import me.dablakbandit.setexpscale.scale.Scale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SES implements CommandExecutor{

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(!s.hasPermission("setexpscale.admin"))return false;
		if(args.length==0){
			s.sendMessage("=============[SetExpScale]=============");
			s.sendMessage("/ses give <player> <exp>");
			s.sendMessage("/ses set <player> <exp>");
			s.sendMessage("/ses info <player>");
			s.sendMessage("/ses remove <player> <exp>");
			return true;
		}else{
			switch(args[0].toLowerCase()){
			case "give":{
				if(args.length<2){
					s.sendMessage("=============[SetExpScale Give]=============");
					s.sendMessage("/ses give <player> <exp>");
					return true;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if(player==null){
					s.sendMessage("Unknown player " + args[1]);
					return false;
				}
				int i = 0;
				try{
					i = Integer.parseInt(args[2]);
				}catch(Exception e){
					s.sendMessage("Unknown int " + args[2]);
					return false;
				}
				SetExpScale.getInstance().getScale().giveExp(player, i);
				return true;
			}
			case "set":{
				if(args.length<2){
					s.sendMessage("=============[SetExpScale Set]=============");
					s.sendMessage("/ses set <player> <exp>");
					return true;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if(player==null){
					s.sendMessage("Unknown player " + args[1]);
					return false;
				}
				int i = 0;
				try{
					i = Integer.parseInt(args[2]);
				}catch(Exception e){
					s.sendMessage("Unknown int " + args[2]);
					return false;
				}
				SetExpScale.getInstance().getScale().setExp(player, i);
				return true;
			}
			case "info":{
				if(args.length<1){
					s.sendMessage("=============[SetExpScale Info]=============");
					s.sendMessage("/ses give <player>");
					return true;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if(player==null){
					s.sendMessage("Unknown player " + args[1]);
					return false;
				}
				Scale sc = SetExpScale.getInstance().getScale();
				s.sendMessage(args[1] + " has " + sc.getExp(player) + " exp (level " + player.getLevel() + ") and needs " + sc.getExpToLevel(player) + " more exp to level up"); 
				return true;
			}
			case "remove":{
				if(args.length<2){
					s.sendMessage("=============[SetExpScale Remove]=============");
					s.sendMessage("/ses remove <player> <exp>");
					return true;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if(player==null){
					s.sendMessage("Unknown player " + args[1]);
					return false;
				}
				int i = 0;
				try{
					i = Integer.parseInt(args[2]);
				}catch(Exception e){
					s.sendMessage("Unknown int " + args[2]);
					return false;
				}
				SetExpScale.getInstance().getScale().removeExp(player, i);
				return true;
			}
			default:{
				s.sendMessage(ChatColor.RED + "Unknown arguments, for command /" + cmd.getLabel());
				return false;
			}
			}
		}
	}
}
