package Final_Project;
/*
 * Cpre 558 Final project
 * FeedBack EDF System simulator
 * 12/12/2018
 * group member: Han Liao; Yiming bian
 */
import java.awt.Color;

public class Task {
	
	private int computation;
	private int deadline;
	private int period;
	private Color color;
	private int M;
	private int K;
	
	public Task(int comp,int dead,int period,Color color) {
		this.computation=comp;
		this.deadline=dead;
		this.period=period;
		this.color=color;
	}
	public Task(int comp,int dead,int period,int M,int K,Color color) {
		this.computation=comp;
		this.period=period;
		this.M=M;
		this.K=K;
		this.deadline=dead;
		this.color=color;
	}
	public int getComp() {
		return computation;
	}
	
	public int getDead() {
		return deadline;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public int getM() {
		return M;
	}
	public int getK() {
		return K;
	}
	
	public Color getColor() {
		return color;
	}
	
}
