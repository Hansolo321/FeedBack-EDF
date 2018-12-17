package Final_Project;
/*
 * Cpre 558 Final project
 * FeedBack EDF System simulator
 * 12/12/2018
 * group member: Han Liao; Yiming bian
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class Back_End {

	public int missed=0;
	//Calculate the total units time showed
	public int unitcal(LinkedList<Task>  list) {
		int result = list.get(0).getPeriod();
		for(int i = 1; i < list.size(); i++) result = lcm(result, list.get(i).getPeriod());
		return result;
	}
	private static int gcd(int a, int b)
	{
		while (b > 0)
		{
			int temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}
	private static int lcm(int a, int b)
	{
		return a * (b / gcd(a, b));
	}

	//EDF scheduler
	public ArrayList<Task> EDF_scheduler(LinkedList<Task> tasklist, int unit) {
		ArrayList<Task> scheduled = new ArrayList<Task>();
		Stack<Task> priority = new Stack<Task>();
		priority= buildPriority(tasklist);
		//Excuteable map used to count the left excuteable time for each tasks
		//Rest map used to count the time remaining to the deadline for each tasks
		HashMap<Task,Integer> excuteable = new HashMap<Task,Integer>();
		HashMap<Task,Integer> rest = new HashMap<Task,Integer>();
		//Initial the excuteable and rest map
		for(int i = 0;i < tasklist.size();i++) {
			excuteable.put(tasklist.get(i), tasklist.get(i).getComp());
			rest.put(tasklist.get(i), tasklist.get(i).getDead());
		}
		//Calculate each unit time for the task sets depend on EDF scheduler
		for(int u = 0;u<unit;u++) {
			int earlist = Integer.MAX_VALUE;
			ArrayList<Task> usable = new ArrayList<Task>();
			priority = buildPriority(tasklist);
			//reset the excuteable and rest map when the unit time hit the period for each task
			for(int i = 0;i<tasklist.size();i++) {
				if( u%tasklist.get(i).getPeriod() == 0) {
					excuteable.replace(tasklist.get(i), excuteable.get(tasklist.get(i)), tasklist.get(i).getComp());
					rest.replace(tasklist.get(i), rest.get(tasklist.get(i)), tasklist.get(i).getDead());
				}
			}
			//Calculate the earlist deadline in current unit time
			int a = 0;
			for(int i = 0;i < tasklist.size();i++) {
				if(excuteable.get(tasklist.get(i))!=0) {
					int remain = rest.get(tasklist.get(i));
					if(remain<earlist&&remain!=0) {
						earlist = remain;
					}
				}
				else {a++;}
			}
			//Add best tasks with the same remaining time to 'usable' list
			if(a==tasklist.size()) {usable.add(null);}
			else {
				for(int i = 0; i < tasklist.size(); i++) {
					if(rest.get(tasklist.get(i)) == earlist&&excuteable.get(tasklist.get(i))!=0) {
						usable.add(tasklist.get(i));
					}
				}
			}
			//Chose the higher priority tasks in the 'usable' list and add in 'scheduled' list
			int flag = 0;
			if(usable.get(0) != null) {
				while(!priority.isEmpty() && flag==0) {
					int c = priority.lastElement().getComp();
					int d = priority.lastElement().getDead();
					int p = priority.lastElement().getPeriod();
					for(int i = 0;i < usable.size();i++) {
						if(c==usable.get(i).getComp() && d==usable.get(i).getDead() && p==usable.get(i).getPeriod()) {
							scheduled.add(usable.get(i));
							flag = 1;
							for(int j = 0;j<tasklist.size();j++) {
								if(usable.get(i).getComp()==tasklist.get(j).getComp() && usable.get(i).getDead()==tasklist.get(j).getDead() && usable.get(i).getPeriod()==tasklist.get(j).getPeriod())
									excuteable.replace(tasklist.get(j), excuteable.get(tasklist.get(j)), excuteable.get(tasklist.get(j))-1);
							}
							update(rest,tasklist);
							break;
						}
					}
					priority.pop();
				}
			}
			else {update(rest,tasklist);scheduled.add(null);}
		}
		return scheduled;
	}

	//Stack out the priority depend the period for each class
	private Stack<Task> buildPriority(LinkedList<Task> tasklist) {
		Stack<Task> priority = new Stack<Task>();
		ArrayList<Integer> array = new ArrayList<Integer>() ;
		boolean duplicate = false;
		for(int i = 0; i < tasklist.size(); i++) {
			for (int j = 0; j < array.size(); j++) {
				if(tasklist.get(i).getPeriod() == array.get(j)) {
					duplicate = true;
					break;
				}
				else {
					duplicate = false;
				}
			}
			if(!duplicate) {
				array.add(tasklist.get(i).getPeriod());
			}
		}
		int[] arr = new int[array.size()];
		for(int i = 0; i < array.size(); i++) {
			arr[i] = array.get(i);
		}
		Arrays.sort(arr);
		for(int i = arr.length-1; i >= 0; i--) {
			for(int j = 0; j < tasklist.size(); j++) {
				if(tasklist.get(j).getPeriod() == arr[i]) {
					priority.push(tasklist.get(j));
				}
			}
		}
		return priority;
	}

	private void update(HashMap<Task, Integer> rest,LinkedList<Task> tasklist) {
		for(int i = 0; i < tasklist.size(); i++) {
			if(rest.get(tasklist.get(i)) > 0) {	
				rest.replace(tasklist.get(i), rest.get(tasklist.get(i)), rest.get(tasklist.get(i))-1);}	
		}
	}
	
	//Admission Controller with EDF algorithm
	public ArrayList<Task> Adcontrol(LinkedList<Task> tasklist,int unit) {
		missed=0;
		ArrayList<Task> scheduled = new ArrayList<Task>();
		Stack<Task> priority = new Stack<Task>();
		priority= buildPriority(tasklist);
		//Excuteable map used to count the left excuteable time for each tasks
		//Rest map used to count the time remaining to the deadline for each tasks
		HashMap<Task,Integer> excuteable = new HashMap<Task,Integer>();
		HashMap<Task,Integer> rest = new HashMap<Task,Integer>();
		//Initial the executeable and rest map
		for(int i = 0;i < tasklist.size();i++) {
			excuteable.put(tasklist.get(i), tasklist.get(i).getComp());
			rest.put(tasklist.get(i), tasklist.get(i).getDead());
		}
		//Calculate each unit time for the task sets depend on EDF scheduler
		for(int u = 0;u<unit;u++) {
			int earlist = Integer.MAX_VALUE;
			ArrayList<Task> usable = new ArrayList<Task>();
			ArrayList<Task> mandalist=new ArrayList<Task>();
			priority = buildPriority(tasklist);
			//reset the excuteable and rest map when the unit time hit the period for each task and count missed task number
			for(int i = 0;i<tasklist.size();i++) {
				if( u%tasklist.get(i).getPeriod() == 0&&u!=0) {
					if(excuteable.get(tasklist.get(i))!=0) {
						missed++;
						}
					excuteable.replace(tasklist.get(i), excuteable.get(tasklist.get(i)), tasklist.get(i).getComp());
					rest.replace(tasklist.get(i), rest.get(tasklist.get(i)), tasklist.get(i).getDead());
				}
			}
			//Calculate the earliest deadline in current unit time
			int a = 0;
			for(int i = 0;i < tasklist.size();i++) {
				if(excuteable.get(tasklist.get(i))!=0) {
					int remain = rest.get(tasklist.get(i));
					if(remain<earlist&&remain!=0) {
						earlist = remain;
					}
				}
				else {a++;}
			}


			for(int i=0;i<tasklist.size();i++) {
				if(!isOpt(tasklist.get(i),u)) {
					mandalist.add(tasklist.get(i));
				}
			}
			//Add best tasks with the same remaining time to 'usable' list
			if(mandalist.isEmpty()) {
				if(a==tasklist.size()) {usable.add(null);}
				else {
					for(int i = 0; i < tasklist.size(); i++) {
						if(rest.get(tasklist.get(i)) == earlist&&excuteable.get(tasklist.get(i))!=0) {
							usable.add(tasklist.get(i));
						}
					}
				}
			}
			else {
				 a = 0;
				 earlist= Integer.MAX_VALUE;
				for(int i = 0;i < mandalist.size();i++) {
					if(excuteable.get(mandalist.get(i))!=0) {
						int remain = rest.get(mandalist.get(i));
						if(remain<earlist&&remain!=0) {
							earlist = remain;
						}
					}
					else {a++;}
				}
				
				if(a==tasklist.size()) {usable.add(null);}
				else {
					for(int i = 0; i < tasklist.size(); i++) {
						if(rest.get(tasklist.get(i)) == earlist&&excuteable.get(tasklist.get(i))!=0) {
							usable.add(tasklist.get(i));
						}
					}
				}
			}
			//Chose the higher priority tasks in the 'usable' list and add in 'scheduled' list
			int b=0;
			for(int i=0;i<mandalist.size();i++) {
				if(excuteable.get(mandalist.get(i))==0) {
					b++;
				}
			}
			if(b==mandalist.size()) {
				 a = 0;
				for(int i = 0;i < tasklist.size();i++) {
					if(excuteable.get(tasklist.get(i))!=0) {
						int remain = rest.get(tasklist.get(i));
						if(remain<earlist&&remain!=0) {
							earlist = remain;
						}
					}
					else {a++;}
				}
				if(a==tasklist.size()) {usable.add(null);}
				else {
					for(int i = 0; i < tasklist.size(); i++) {
						if(rest.get(tasklist.get(i)) == earlist&&excuteable.get(tasklist.get(i))!=0) {
							usable.add(tasklist.get(i));
						}
					}
				}
			}
			int flag = 0;
			if(usable.get(0) != null) {
				while(!priority.isEmpty() && flag==0) {
					int c = priority.lastElement().getComp();
					int d = priority.lastElement().getDead();
					int p = priority.lastElement().getPeriod();
					for(int i = 0;i < usable.size();i++) {
						if(c==usable.get(i).getComp() && d==usable.get(i).getDead() && p==usable.get(i).getPeriod()) {
							scheduled.add(usable.get(i));
							flag = 1;
							for(int j = 0;j<tasklist.size();j++) {
								if(usable.get(i).getComp()==tasklist.get(j).getComp() && usable.get(i).getDead()==tasklist.get(j).getDead() && usable.get(i).getPeriod()==tasklist.get(j).getPeriod())
									excuteable.replace(tasklist.get(j), excuteable.get(tasklist.get(j)), excuteable.get(tasklist.get(j))-1);
							}
							update(rest,tasklist);
							break;
						}
					}
					priority.pop();
				}
			}
			else {update(rest,tasklist);scheduled.add(null);}
		}
		return scheduled;
	}
	
	//Unimplemented Service Level Controller
	public ArrayList<Task> Svcntrol(LinkedList<Task> task,int unite) {

		return null;
	}

	// Check the mandatory or optional in particular task in current time
	public boolean isOpt(Task task,int time) {
		int a=time/(task.getPeriod());
		int check=(int) Math.floor(Math.ceil(a*task.getM()/task.getK())*task.getK()/task.getM());
		if(check==a) {
			return false;
		}
		return true;

	}
}