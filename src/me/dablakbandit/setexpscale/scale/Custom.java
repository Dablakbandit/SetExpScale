package me.dablakbandit.setexpscale.scale;

import org.bukkit.entity.Player;

public class Custom extends Scale{

	private static Custom custom = new Custom();
	
	private double scale = 17.0, ratio = 0.75, bar = 1/17.0;
	
	private Custom(){
		
	}
	
	public static Custom getInstance(){
		return custom;
	}
	
	public void setScale(double d){
		scale = d;
		bar = 1/d;
		System.out.print(bar);
	}
	
	public void setRatio(double d){
		ratio = d;
	}
	
	private void set(Player p, double amount){
		double prelevel = convert(amount);//Converts it to a level for the Scale
		int levelset = (int) Math.floor(prelevel);//Get level as an int
		double preset = (prelevel-levelset);//Remove int from double to get decimal left
		float percentset = (float)preset;//convert decimal to float
		p.setLevel(levelset);//Set the level
		p.setExp(percentset);//Set the percentage in bar
	}
	
	private double getExp(Player p){
		double percent = p.getExp();//Percentage in bar currently
		int level = p.getLevel();//We set this the last time they got exp
		double totalset = level * this.scale;//Times by the double to get exp to add later
		double curpercent = (percent*this.scale);//Gets the current % they have
		totalset = totalset + curpercent;//Add the current % they have
		return totalset;
	}

	@Override
	public void giveExp(Player player, double amount) {
		set(player, getExp(player)+amount);
	}

	@Override
	public void removeExp(Player player, double amount) {
		set(player, getExp(player)-amount);
	}

	@Override
	public void setExp(Player player, double amount) {
		set(player, amount);
	}

	public double convert(double amount) {
		return amount/this.scale;//Get level as a double
	}

	@Override
	public int getDroppedAmount(Player player) {
		return (int)(getExp(player)*ratio);//Change the dropped amount to work with the Scale
	}
}
