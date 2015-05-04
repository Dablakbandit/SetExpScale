package me.dablakbandit.setexpscale.scale;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ja.ClassPool;
import ja.CtClass;
import ja.CtMethod;
import ja.CtNewMethod;
import ja.LoaderClassPath;
import me.dablakbandit.setexpscale.SetExpScale;

import org.bukkit.entity.Player;

public class Advanced extends Scale{

	private static Advanced advanced = new Advanced();
	private double ratio = 0.75;
	
	private Advanced(){}
	
	public void setRatio(double d){
		ratio = d;
	}
	
	private Class<?> c = null;
	private Method m;
	
	public void setup(List<String> method){
		try{
			ClassPool cp = ClassPool.getDefault();
			cp.appendClassPath(new LoaderClassPath(this.getClass().getClassLoader()));
			int i = 1;
			while(true){
				try{
					Class.forName("temp.Scale" + i);
				}catch(Exception e){
					break;
				}
				i++;
			}
			CtClass ctc = cp.makeClass("temp.Scale" + i);
			String m = "public static double getAmountToLevelUp(int level){";
			for(String s : method){
				m = m + s;
			}
			m = m + "}";
			CtMethod ctm = CtNewMethod.make(m, ctc);
			ctc.addMethod(ctm);
			this.c = ctc.toClass();
			this.m = c.getMethod("getAmountToLevelUp", int.class);
		}catch(Exception e){
			e.printStackTrace();
			SetExpScale.getInstance().setScale(Scales.Normal);
		}
	}
	
	public static Advanced getInstance(){
		return advanced;
	}
	
	private void set(Player p, double amount){
		double prelevel = convert(amount);//Converts it to a level for the Scale
		int levelset = (int) Math.floor(prelevel);//Get level as an int
		double preset = (prelevel-levelset);//Remove int from double to get decimal left
		float percentset = (float)preset;//convert decimal to float
		p.setLevel(levelset);//Set the level
		p.setExp(percentset);//Set the percentage in bar
	}
	
	public int getExp(Player p){
		double percent = p.getExp();//Percentage in bar currently
		int level = p.getLevel();//We set this the last time they got exp
		double totalset = getExpToLevel(level)[0];//Times by the double to get exp to add later
		double curpercent = (percent*getExpToLevel(level)[1]);//Gets the current % they have
		totalset = totalset + curpercent;//Add the current % they have
		return (int)totalset;
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
	
	public Map<Integer, double[]> levels = new HashMap<Integer, double[]>();

	public double convert(double amount) {
		double d = 0.0;
		int i = 0;
		while(true){
			double d1 = getExpToLevel(i)[1];
			if(d1<amount){
				d+=1.0;
				amount-=d1;
			}else{
				d+=(amount/d1);
				break;
			}
			i++;
		}
		return d;
	}
	
	public double getExpTotal(int level){
		double d = 0.0;
		while(level>0){
			double d1 = getExpToLevel(level)[0];
			d+=d1;
			level--;
		}
		return d;
	}
	
	public double[] getExpToLevel(int level){
		if(levels.containsKey(level)){
			return levels.get(level);
		}
		try{
			if(level==0){
				double d = (double)m.invoke(null, level);
				double[] d1 = new double[]{0, d};
				levels.put(level, d1);
				return d1;
			}
			double d = (double)m.invoke(null, level);
			double[] d1 = getExpToLevel(level-1);
			double d2 = d1[0] + d1[1];
			double[] d3 = new double[]{d2, d};
			levels.put(level, d3);
			return d3;
		}catch(Exception e){}
		return null;
	}

	@Override
	public int getDroppedAmount(Player player) {
		return (int)(getExp(player)*ratio);//Change the dropped amount to work with the Scale
	}

	@Override
	public int getExpToLevel(Player p) {
		double percent = p.getExp();//Percentage in bar currently
		int level = p.getLevel();//We set this the last time they got exp
		double etl = getExpToLevel(level)[1];
		double curpercent = (percent*etl);//Gets the current % they have
		return (int)(etl - curpercent);
	}

}
