package Final_Project;
/*
 * Cpre 558 Final project
 * FeedBack EDF System simulator
 * 12/12/2018
 * group member: Han Liao; Yiming bian
 */

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;


import java.awt.CardLayout;

public class EDF_GUI {

	//All variables for tradition EDF system
	private JFrame myFrame;
	public String tasks="";
	public long starttime,endtime,avgtime;
	public int x=20;
	public int hmyunit = 0;
	public JLabel numlabel = new JLabel();
	public JPanel panelgraph = new JPanel();
	private boolean animation=false;
	private boolean animation1=false;
	LinkedList<Task> tasklist=new LinkedList<Task>();
	ArrayList<Color> color = new ArrayList<Color>();
	ArrayList<Task> schedulelist= new ArrayList<Task>();
	Timer tm=null;
	private int k=1;
	private int k1=1;
	private int j1=1;
	private int j2=1;
	private double utilization;
	public int mode=0;
	private int missed1;

	//All variables for tradition Feedback-EDF system
	ArrayList<Task> taskall=new ArrayList<Task>();
	LinkedList<Task> acceptask= new LinkedList<Task>();
	ArrayList<Task> endlist= new ArrayList<Task>();
	private double utilization1;
	private JPanel FDpanel= new JPanel();
	private int hmy=1;
	private int fk=0;
	private int fk1=0;
	private double ratio=0;
	private int taskindex=0;
	private double currentR=0;
	Back_End backend=new Back_End();
	boolean isfile=false;
	boolean isratio=false;
	private int fkyou;
	private JLabel startlb2= new JLabel();
	private JLabel startlb3= new JLabel();


	public static void main (String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EDF_GUI window = new EDF_GUI();
					window.myFrame.setVisible(true);
					window.myFrame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EDF_GUI() {
		initialize();
	}

	private void initialize() {
		//*************************************************************************************************************************
		// Tradition EDF system GUI
		//*************************************************************************************************************************
		tm=new Timer(0,null);
		//8 available colors for task
		color.add(Color.RED);
		color.add(Color.GREEN);
		color.add(Color.BLUE);
		color.add(Color.ORANGE);
		color.add(Color.PINK);
		color.add(Color.YELLOW);
		color.add(Color.WHITE);
		color.add(Color.BLACK);
		HashMap<Color,String> col = new HashMap<Color,String>();
		col.put(Color.RED, "RED");col.put(Color.GREEN, "GREEN");col.put(Color.BLUE, "BLUE");
		col.put(Color.ORANGE, "ORANGE");col.put(Color.PINK, "PINK");col.put(Color.YELLOW, "YELLOW");
		col.put(Color.WHITE, "WHITE");col.put(Color.BLACK, "BLACK");

		//Frame of the windows
		myFrame = new JFrame();
		myFrame.setTitle(" Feedback Control Scheduling with EDF ");
		myFrame.setBounds(100, 100, 1700, 546);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.getContentPane().setLayout(null);

		panelgraph = new JPanel(){
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int u = 0;
				int t = fk;
				int a = 0;
				g.setColor(Color.BLACK);
				g.drawLine(20, 120, 740, 120);
				g.drawLine(20, 119, 740, 119);

				for(u = 0;u<= hmyunit;u++) {
					for(a=0;a<tasklist.size();a++) {
						if(t!=0 && t%tasklist.get(a).getPeriod()==0) {
							g.setColor(Color.BLACK);
							g.drawLine(x, 120, x, 60);
							g.drawLine(x+1, 120, x+1, 60);
							g.drawLine(x-10, 70, x+1, 60);
							g.drawLine(x-11, 71, x+1, 61);
							g.drawLine(x+10, 70, x+1, 60);
							g.drawLine(x+11, 71, x+1, 61);
						}
					}
					fk++;
					g.setColor(Color.BLACK);
					g.drawLine(x, 120, x, 100);
					g.drawLine(x+1, 120,x+1, 100);
					//Draw the scheduled task construct in corresponding task color.
					if(u!=0) {
						if(schedulelist.get(u-1)!=null) {
							g.setColor(schedulelist.get(u-1).getColor());
							g.fillRect(x-(720/hmyunit)+2,90,720/hmyunit-2,28);
							//Label the task number
							String str1 = "";
							if(schedulelist.get(u-1)!= null) {
								int C = schedulelist.get(u-1).getComp();
								int D = schedulelist.get(u-1).getDead();
								int P = schedulelist.get(u-1).getPeriod();
								for(int b = 0;b<tasklist.size();b++) {
									if (C==tasklist.get(b).getComp() && D==tasklist.get(b).getDead() && P==tasklist.get(b).getPeriod()) {
										str1 = "T"+String.valueOf(b+1);
									}
								}
							}
							g.setColor(Color.BLACK);
							g.setFont(new Font("default", Font.BOLD, 18));
							g.drawString(str1, x-(500/hmyunit), 110);
						}
					}
					t++;
					x = x + (720/hmyunit);
				}

				if(animation) {
					tm.start();
					tm.setRepeats(false);
				}
			}

		};

		//Label of the user guide
		JLabel Lable1 = new JLabel("<html><br/>Type in the tasks and parameter need to be schedueld:<br/>(Computation, Deadline, Period)<br/>\r\neg: (3, 4, 6),(2, 5, 8) ...</html>", SwingConstants.CENTER);
		Lable1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Lable1.setBounds(0, 0, 399, 90);
		myFrame.getContentPane().add(Lable1);

		//Generate typing box
		JTextArea TypeArea = new JTextArea();
		TypeArea.setFont(new Font("Mongolian Baiti", Font.BOLD, 17));
		TypeArea.setText("User type in there:\n");
		TypeArea.setBounds(404, 0, 374, 76);
		myFrame.getContentPane().add(TypeArea);
		TypeArea.setLineWrap(true);
		TypeArea.setWrapStyleWord(true);
		JScrollPane scroll1 = new JScrollPane(TypeArea);
		scroll1.setBounds(404, 0, 374, 76);                     
		myFrame.getContentPane().add(scroll1);
		myFrame.setLocationRelativeTo ( null );

		//Green button to start scheduling
		JButton startBtn = new JButton("Start");
		startBtn.setBackground(Color.GREEN);
		myFrame.getContentPane().add(startBtn);



		//Separation line
		JPanel sepLine1 = new JPanel() {	   
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.drawLine(0, 2, 778, 2);
				g.drawLine(0, 3, 778, 3);
				g.drawLine(0, 4, 778, 4);


			}
		};
		sepLine1.setBounds(0, 125, 778, 31);
		myFrame.getContentPane().add(sepLine1);
		JPanel sepLine2 = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.drawLine(0, 10, 778, 10);
				g.drawLine(0, 11, 778, 11);
				g.drawLine(0, 12, 778, 12);
			}
		};
		sepLine2.setBounds(0, 282, 778, 31);
		myFrame.getContentPane().add(sepLine2);
		JPanel sepLine3 = new JPanel() {	   
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.drawLine(0, 0, 0, 500);
				g.drawLine(1, 0, 1, 500);
				g.drawLine(2, 0, 2, 500);

				g.drawLine(10, 0, 10, 500);
				g.drawLine(11, 0, 11, 500);
				g.drawLine(12, 0, 12, 500);

			}
		};
		sepLine3.setBounds(779, 0, 28, 500);
		myFrame.getContentPane().add(sepLine3);

		JPanel panel = new JPanel();
		panel.setBounds(0, 320, 780, 180);
		myFrame.getContentPane().add(panel);
		panel.setLayout(new CardLayout(0, 0));
		JLabel startlb= new JLabel();
		startlb.setText("            Need task sets to be scheduled!!");
		startlb.setFont(new Font("Mongolian Baiti", Font.PLAIN, 40));
		startlb.setForeground(Color.RED);
		panel.add(startlb);

		//Generate task list box
		JTextArea TaskArea = new JTextArea();
		TaskArea.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
		TaskArea.setBounds(0, 157, 385, 124);
		TaskArea.setText("Number of tasks:  ");
		TaskArea.setLineWrap(true);
		TaskArea.setWrapStyleWord(true);
		TaskArea.setCaretColor(TaskArea.getBackground());
		myFrame.getContentPane().add(TaskArea);
		JScrollPane scroll = new JScrollPane(TaskArea);
		scroll.setBounds(0, 157, 385, 124);                     
		myFrame.getContentPane().add(scroll);
		myFrame.setLocationRelativeTo ( null );

		//Generate processing analysis box
		JTextArea Analysis = new JTextArea();
		Analysis.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
		Analysis.setBounds(404, 157, 374, 124);
		Analysis.setLineWrap(true);
		Analysis.setWrapStyleWord(true);
		Analysis.setCaretColor(Analysis.getBackground());
		Analysis.setText("No task sets in progressing now.");
		Analysis.setForeground(Color.RED);
		myFrame.getContentPane().add(Analysis);
		JScrollPane scroll2 = new JScrollPane(Analysis);
		scroll2.setBounds(404, 157, 374, 124);                     
		myFrame.getContentPane().add(scroll2);
		myFrame.setLocationRelativeTo ( null );

		//Red button to undo the typing
		JButton clearBtn = new JButton("Clear/Stop");
		clearBtn.setBackground(new Color(240, 128, 128));
		clearBtn.setBounds(540, 87, 100, 25);
		myFrame.getContentPane().add(clearBtn);	
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TypeArea.setText("Retype your task sets:\n");
				tm.stop();
				animation=false;
				FDpanel.removeAll();
				FDpanel.repaint();
				k=1;
				Analysis.setForeground(Color.RED);
				Analysis.setText("Animation is OFF\nClick 'Animation' button to restart.");
			}
		});

		//ActionListener for start scheduling button
		startBtn.setBounds(420, 87, 100, 25);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//reset all variable needed
				k=1;
				panel.remove(startlb);
				x = 20;
				fk=0;
				panelgraph.removeAll();
				schedulelist.clear();
				tasklist.clear();
				utilization = 0.0;
				TaskArea.setText("");

				//Scan the user typing into task list
				Scanner scr = new Scanner(TypeArea.getText());
				scr.nextLine();
				if(scr.hasNext()) {
					tasks = scr.next();
					scr.close();
					Scanner scr1 = new Scanner(tasks);
					tasks = tasks.replace("(", "");
					tasks = tasks.replace(")", "");
					String[] list = tasks.split(",");
					int c,d,p;
					String str;
					int i = 0;
					int colorindex = 0;
					while(i<list.length) {
						c = Integer.parseInt(list[i]);
						d = Integer.parseInt(list[i+1]);
						p = Integer.parseInt(list[i+2]);
						i = i + 3;
						tasklist.add(new Task(c,d,p,color.get(colorindex)));
						colorindex++;
					}	
					scr1.close();
					TaskArea.setText("Number of tasks: "+tasklist.size()+"\n");
					int z = 0;
					for(int j = 0;j<tasklist.size();j++) {
						c = tasklist.get(j).getComp();
						d = tasklist.get(j).getDead();
						p = tasklist.get(j).getPeriod();
						utilization = utilization+(c*1.0)/(p*1.0);
						str = col.get(tasklist.get(j).getColor());
						z = j+ 1;
						TaskArea.append("Task " + z +":  "+"Ci:"+c+","+"  Di:"+d+","+"  Pi:"+p+", Color: "+str+"\n");
					}
					Analysis.setForeground(Color.BLACK);

					//Calculate the units in total and use EDF scheduler to schedule the task in back_end class.
					//10 iterations of the schedules
					hmyunit = backend.unitcal(tasklist);
					for(int avg = 0;avg < 10; avg++) {
						starttime = System.nanoTime();
						if(utilization <= 1) {
							schedulelist = backend.EDF_scheduler(tasklist,hmyunit);

						}
						else {
							for(int a = 0;a< hmyunit;a++) {
								schedulelist.add(null);
							}
						}
						endtime = System.nanoTime();
						avgtime = avgtime+(endtime-starttime);
					}
					avgtime = avgtime/10;

					if(utilization <= 1) {
						Analysis.setText("Utilization test passed!!\n");
						Analysis.append("Utilization: "+ Math.round(utilization * 100.0) / 100.0+"\n");
						Analysis.append("System start time(ns):  "+starttime+"\n");
						Analysis.append("Processing....\n");	
					}
					else {
						Analysis.setText("Utilization test cant pass.\nPlease try other task sets. \n");
						Analysis.setForeground(Color.RED);
					}

					//Label the unit time
					for(int u1 = 0;u1 <= hmyunit;u1++) {
						panelgraph.setLayout(null);
						numlabel = new JLabel(String.valueOf(u1), SwingConstants.CENTER);
						numlabel.setBounds(11+u1*(720/hmyunit), 117, 20, 20);
						panelgraph.add(numlabel);
					}

					JLabel lb = new JLabel("EDF constructure",SwingConstants.CENTER);
					lb.setBounds(0, 0, 780, 50);
					lb.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
					panelgraph.add(lb);
					panelgraph.setBounds(0, 320, 780, 20);		
					panel.add(panelgraph);
					panelgraph.revalidate();
					panelgraph.repaint();
					panel.revalidate();//Updating panel.

					//Show the time consuming
					if(utilization <= 1.0) {
						Analysis.append("System end time(ns):  "+endtime+"\n");
						Analysis.append("Total time used in prosessor(ns): "+ (endtime-starttime) + "\n");
						Analysis.append("Average time: " + avgtime + " (10 inerations)");
					}
				}
				else {
					TypeArea.setForeground(Color.RED);
					TypeArea.setText("No task need to schedule!! Retype it blew:\n");
				}
			}

		});

		JButton animBtn = new JButton("Animation");
		animBtn.setBackground(Color.GREEN);
		myFrame.getContentPane().add(animBtn);
		animBtn.setBounds(660, 87, 100, 25);
		animBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Analysis.setForeground(Color.BLACK);
				animation=true;
				tm=new Timer(500,this);
				tm.setRepeats(false);
				panel.remove(startlb);
				x = 20;
				panelgraph.removeAll();

				Task temp=schedulelist.get(0);
				schedulelist.remove(0);
				schedulelist.add(temp);

				//Label the unit time
				for(int u1 = 0;u1 <= hmyunit;u1++) {
					panelgraph.setLayout(null);
					numlabel = new JLabel(String.valueOf(u1+k), SwingConstants.CENTER);
					numlabel.setBounds(11+u1*(720/hmyunit), 117, 20, 20);
					panelgraph.add(numlabel);
				}
				k++;
				JLabel lb = new JLabel("EDF constructure",SwingConstants.CENTER);
				lb.setBounds(0, 0, 780, 50);
				lb.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
				panelgraph.add(lb);
				panelgraph.setBounds(0, 320, 780, 20);		
				panel.add(panelgraph);
				panelgraph.revalidate();
				panelgraph.repaint();
				panel.revalidate();//Updating panel.
				Analysis.setText("Utilizaton test passed!\nUtilization:"+utilization+"\nAnimation:ON\nProcessing");
				int i=0;
				while(i<=j1) {
					Analysis.append(".");
					i++;
				}
				j1++;
				if(k%5==0) {
					j1=1;
				}
			}
		});


		//*************************************************************************************************************************	
		// Feedback EDF system and GUI
		//*************************************************************************************************************************

		//Feedback EDF constructor and graphics
		FDpanel = new JPanel(){
			private static final long serialVersionUID = 2L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int u = 0;
				int t = fk1;
				int a = 0;
				g.setColor(Color.BLACK);
				g.drawLine(20, 120, 740, 120);
				g.drawLine(20, 119, 740, 119);
				for(u = 0;u<= hmy;u++) {
					for(a=0;a<acceptask.size();a++) {
						if(t!=0 && t%acceptask.get(a).getPeriod()==0) {
							g.setColor(Color.BLACK);
							g.drawLine(x, 120, x, 60);
							g.drawLine(x+1, 120, x+1, 60);
							g.drawLine(x-10, 70, x+1, 60);
							g.drawLine(x-11, 71, x+1, 61);
							g.drawLine(x+10, 70, x+1, 60);
							g.drawLine(x+11, 71, x+1, 61);
						}
					}
					fk1++;
					g.setColor(Color.BLACK);
					g.drawLine(x, 120, x, 100);
					g.drawLine(x+1, 120,x+1, 100);
					//Draw the scheduled task construct in corresponding task color.
					if(u!=0) {
						if(endlist.get(u-1)!=null) {
							g.setColor(endlist.get(u-1).getColor());
							g.fillRect(x-(720/hmy)+2,90,720/hmy-2,28);
							//Label the task number
							String str1 = "";
							if(endlist.get(u-1)!= null) {
								int C = endlist.get(u-1).getComp();
								int D = endlist.get(u-1).getDead();
								int P = endlist.get(u-1).getPeriod();
								for(int b = 0;b<acceptask.size();b++) {
									if (C==acceptask.get(b).getComp() && D==acceptask.get(b).getDead() && P==acceptask.get(b).getPeriod()) {
										str1 = "T"+String.valueOf(b+1);
									}
								}
							}
							g.setColor(Color.BLACK);
							g.setFont(new Font("default", Font.BOLD, 18));
							g.drawString(str1, x-(500/hmy), 110);
						}
					}
					t++;
					x = x + (720/hmy);
				}

				if(animation1) {
					tm.start();
					tm.setRepeats(false);
				}
			}
		};



		// Show all the submitted tasks in TaskAll
		JTextArea TaskAll = new JTextArea();
		TaskAll.setFont(new Font("Mongolian Baiti", Font.BOLD, 16));
		TaskAll.setText("All submitted tasks will be shown there:");
		TaskAll.setLineWrap(true);
		TaskAll.setWrapStyleWord(true);
		TaskAll.setBounds(822, 66, 300, 197);
		myFrame.getContentPane().add(TaskAll);
		JScrollPane scroll3 = new JScrollPane(TaskAll);
		scroll3.setBounds(822, 66, 221, 197);                     
		myFrame.getContentPane().add(scroll3);
		myFrame.setLocationRelativeTo ( null );

		JTextArea missR = new JTextArea();
		missR.setBounds(977, 10, 100, 25);
		myFrame.getContentPane().add(missR);

		JLabel lblNewLabel_3 = new JLabel("No setting");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setBounds(1400, 13, 100, 22);
		myFrame.getContentPane().add(lblNewLabel_3);

		//Get the user input for set point
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(missR.getText().isEmpty()) {
					missR.setForeground(Color.RED);
					missR.setText("Invalid");
				}else {
					missR.setForeground(Color.BLACK);
					isratio=true;
					lblNewLabel_3.setForeground(Color.BLACK);
					ratio=Double.valueOf(missR.getText());
					lblNewLabel_3.setText(missR.getText());
				}
			}
		});	
		btnEnter.setBackground(Color.GREEN);
		btnEnter.setBounds(1089, 9, 97, 25);
		myFrame.getContentPane().add(btnEnter);
		JLabel lblNewLabel = new JLabel("Enter the set points:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(822, 8, 143, 25);
		myFrame.getContentPane().add(lblNewLabel);

		//catch the file inputed by user, it has to follow the specific format  c,p,m,k
		JButton btnInput = new JButton("INPUT FILE");
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isfile=true;
				TaskAll.setText("");	
				utilization1=0;
				taskall.clear();
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Browse the folder to process");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
					File file=chooser.getSelectedFile();
					Scanner scr = null;
					try {
						scr = new Scanner(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					int colorindex=0;
					while(scr.hasNext()) {
						String a=scr.nextLine();	
						String[] str=a.split(",");
						int c=Integer.valueOf(str[0]);
						int p=Integer.valueOf(str[1]);
						int m=Integer.valueOf(str[2]);
						int k=Integer.valueOf(str[3]);
						taskall.add (new Task( c, p, p, m, k,color.get(colorindex))) ;	
						colorindex++;
						if(colorindex>color.size()-1) {
							colorindex=0;
						}
					}
				} else {
					System.out.println("No Selection ");
				}
				TaskAll.setFont(new Font("Mongolian Baiti", Font.BOLD, 16));
				TaskAll.setText("All task lists shown below:\n");
				for(int i=0;i<taskall.size();i++) {
					TaskAll.append("task"+(i+1)+": c="+taskall.get(i).getComp()+";p="+taskall.get(i).getPeriod()+";m="+taskall.get(i).getM()+";k="+taskall.get(i).getK()+".\n");
				}
			}
		});
		btnInput.setBackground(Color.ORANGE);
		btnInput.setBounds(1089, 65, 97, 25);
		myFrame.getContentPane().add(btnInput);

		JLabel lblNewLabel_1 = new JLabel("Miss Ratio:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(1252, 12, 86, 25);
		myFrame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Controller Type:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(1252, 58, 126, 16);
		myFrame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_4 = new JLabel("No setting");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_4.setForeground(Color.RED);
		lblNewLabel_4.setBounds(1400, 59, 270, 31);
		myFrame.getContentPane().add(lblNewLabel_4);


		JTextArea acceptarea = new JTextArea();
		acceptarea.setWrapStyleWord(true);
		acceptarea.setLineWrap(true);
		acceptarea.setFont(new Font("Mongolian Baiti", Font.BOLD, 15));
		acceptarea.setText("All accepted tasks will be shown there:");
		acceptarea.setBounds(1252, 100, 282, 163);

		//Swtich to admission controller
		JButton btnNewButton = new JButton("<html>Admission<br />Controller</html>");
		btnNewButton.setBackground(new Color(0,191,255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode=1;
				acceptarea.setForeground(Color.BLACK);
				acceptarea.setText("The admission controller is ready!!");
				lblNewLabel_4 .setForeground(Color.BLACK);
				lblNewLabel_4.setText("Admission Controller");
			}
		});
		btnNewButton.setBounds(1089, 125, 107, 52);
		myFrame.getContentPane().add(btnNewButton);

		// Switch to Service Level Controller
		JButton btnNewButton_1 = new JButton("<html>Service Level<br />Controller</html>");
		btnNewButton_1.setBackground(new Color(0,191,255));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode=2;
				acceptarea.setForeground(Color.RED);
				acceptarea.setText("Since the time problem, this controller is unimplement. Sorry about this!");
				lblNewLabel_4 .setForeground(Color.BLACK);
				lblNewLabel_4.setText("Service Level Controller");
			}
		});
		btnNewButton_1.setBounds(1089, 211, 107, 52);
		myFrame.getContentPane().add(btnNewButton_1);


		myFrame.getContentPane().add(acceptarea);
		JScrollPane scroll4 = new JScrollPane(acceptarea);
		scroll4.setBounds(1252, 100, 282, 163);                     
		myFrame.getContentPane().add(scroll4);


		JLabel startlb1= new JLabel();
		startlb1.setVerticalAlignment(SwingConstants.CENTER);
		startlb1.setText("Adjust the setting and start!!");
		startlb1.setFont(new Font("Mongolian Baiti", Font.PLAIN, 40));
		startlb1.setForeground(Color.RED);
		startlb1.setBounds(979, 341, 490, 145);
		myFrame.getContentPane().add(startlb1);
		myFrame.getContentPane().repaint();

		//Generate the first accepted task followed the rules of EDF and MK
		JButton startbtn = new JButton("START");
		startbtn.setBackground(Color.GREEN);
		startbtn.setForeground(Color.BLACK);
		startbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startlb1.removeAll();
				myFrame.getContentPane().remove(startlb1);
				myFrame.getContentPane().repaint();
				if(mode==0) {
					acceptarea.setForeground(Color.RED);
					acceptarea.setText("Please choose the controller you want to implement!!");}
				else {
					acceptarea.setForeground(Color.BLACK);
					animation1=false;
					tm=new Timer(500,this);
					tm.setRepeats(false);
					FDpanel.removeAll();
					FDpanel.remove(startlb1);
					FDpanel.revalidate();
					FDpanel.repaint();
					x = 20;
					taskindex=0;
					//get accepttask depend on ratio and mode
					if(!isfile) {acceptarea.setForeground(Color.RED);
					acceptarea.setText("Please choose the correct input file!!");}
					else {
						acceptarea.setForeground(Color.BLACK);
						acceptask.add(taskall.get(taskindex));

						hmy = backend.unitcal(acceptask);
						acceptarea.setText("");
						for(int i=0;i<acceptask.size();i++) {
							acceptarea.append("task"+(i+1)+": c="+acceptask.get(i).getComp()+";p="+acceptask.get(i).getPeriod()+";m="+acceptask.get(i).getM()+";k="+acceptask.get(i).getK()+".\n");
						}
						if(mode==1) {
							endlist=backend.Adcontrol(acceptask,hmy);
						}
						else {
							endlist=backend.Svcntrol(acceptask,hmy);
						}

						FDpanel.setBounds(819, 320, 851, 166);
						myFrame.getContentPane().add(FDpanel);
						myFrame.getContentPane().revalidate();
						FDpanel.revalidate();
						FDpanel.repaint();
						for(int u1 = 0;u1 <= hmy;u1++) {
							FDpanel.setLayout(null);
							numlabel = new JLabel(String.valueOf(u1), SwingConstants.CENTER);
							numlabel.setBounds(11+u1*(720/hmy), 130, 20, 20);
							FDpanel.add(numlabel);
						}
					}
				}
			}
		});
		startbtn.setBounds(1546, 115, 97, 25);
		myFrame.getContentPane().add(startbtn);

		//Stop the animation and clear all acceptask and endlist.
		JButton stopbtn = new JButton("STOP");
		stopbtn.setBackground(Color.RED);
		stopbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tm.stop();
				acceptask.clear();
				hmy=0;
				endlist.clear();
				//				myFrame.getContentPane().remove(startlb2);
				//				myFrame.getContentPane().remove(startlb3);
				//				myFrame.getContentPane().repaint();
				FDpanel.removeAll();
				animation1=false;
				k1=1;
				acceptarea.setForeground(Color.RED);
				acceptarea.setText("Animation is OFF\nClick 'Animation' button to restart.");
			}
		});
		stopbtn.setBounds(1546, 217, 97, 25);
		myFrame.getContentPane().add(stopbtn);



		JPanel panel_2 = new JPanel() {	   
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.drawLine(0, 2, this.getWidth(), 2);
				g.drawLine(0, 3, this.getWidth(), 3);
				g.drawLine(0, 4, this.getWidth(), 4);


			}
		};
		panel_2.setBounds(807, 271, 875, 25);
		myFrame.getContentPane().add(panel_2);

		//Generator the future constructor and update all corresponding values.
		JButton btnNewButton_2 = new JButton("Animation");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrame.getContentPane().remove(startlb2);
				myFrame.getContentPane().remove(startlb3);
				myFrame.repaint();
				if(!isratio) {acceptarea.setForeground(Color.RED);
				acceptarea.setText("Please enter the set point!!");
				}
				else {
					acceptarea.setForeground(Color.BLACK);
					animation1=true;
					tm=new Timer(50,this);
					tm.setRepeats(false);
					FDpanel.removeAll();
					FDpanel.remove(startlb1);
					FDpanel.revalidate();
					FDpanel.repaint();
					x = 20;
					hmy = backend.unitcal(acceptask);
					acceptarea.setText("");
					for(int i=0;i<acceptask.size();i++) {
						acceptarea.append("task"+(i+1)+": c="+acceptask.get(i).getComp()+";p="+acceptask.get(i).getPeriod()+";m="+acceptask.get(i).getM()+";k="+acceptask.get(i).getK()+".\n");
					}

					Task temp=endlist.get(0);
					endlist.remove(0);
					endlist.add(temp);

					FDpanel.setBounds(819, 320, 851, 166);
					myFrame.getContentPane().add(FDpanel);
					myFrame.getContentPane().revalidate();
					FDpanel.revalidate();
					FDpanel.repaint();
					for(int u1 = 0;u1 <= hmy;u1++) {
						FDpanel.setLayout(null);
						numlabel = new JLabel(String.valueOf(u1+k1), SwingConstants.CENTER);
						numlabel.setBounds(11+u1*(720/hmy), 130, 20, 20);
						FDpanel.add(numlabel);
					}
					k1++;
					int i=0;
					acceptarea.append("Processing");
					while(i<=j2) {
						acceptarea.append(".");
						i++;
					}
					j2++;
					if(j2%5==0) {
						j2=1;
					}
					acceptarea.append("\n");

					if((k1+1)%(hmy-1)==0) {
						int total=0;
						for(int q=0;q<acceptask.size();q++) {
							total=total+hmy/acceptask.get(q).getPeriod();
						}
						missed1=backend.missed;
						currentR=((Math.round (missed1 * 100000000000000.0) /100000000000000.0))/((Math.round (total * 100000000000000.0) /100000000000000.0));
						admcontroller(currentR);
					}

					missed1=backend.missed;
					double count=0;
					for(int p=0;p<endlist.size();p++) {
						if(endlist.get(p)==null) {
							count++;
						}
					}
					utilization1=(endlist.size()*1.0-count)/(endlist.size()*1.0);
					acceptarea.append("Current missing ratio:"+(Math.round (currentR * 100000000000000.0) /100000000000000.0)+"\n");
					//	acceptarea.append("Current utilization:"+(Math.round (utilization1 * 100000000000000.0) /100000000000000.0));

					startlb2= new JLabel();
					startlb2.setVerticalAlignment(SwingConstants.CENTER);
					startlb2.setText("Utilization:");
					startlb2.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
					startlb2.setForeground(Color.BLACK);
					startlb2.setBounds(1400,230, 100, 145);
					myFrame.getContentPane().add(startlb2);

					startlb3= new JLabel();
					startlb3.setVerticalAlignment(SwingConstants.CENTER);
					startlb3.setText(String.valueOf(utilization1));
					startlb3.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
					startlb3.setForeground(Color.BLUE);
					startlb3.setBounds(1500, 230, 100, 145);
					myFrame.getContentPane().add(startlb3);
					myFrame.getContentPane().repaint();
				}
			}
		});
		btnNewButton_2.setBackground(Color.GREEN);
		btnNewButton_2.setBounds(1546, 165, 97, 25);
		myFrame.getContentPane().add(btnNewButton_2);

	}	

	//Control the accepted task depend on current missing ratio and set point
	public void admcontroller(double currentR) {
		if(taskindex!=taskall.size()-2) {
			if(currentR<=ratio) {
				if(taskindex==taskall.size()-1) {
					return;
				}
				else {
					taskindex++;
					acceptask.add(taskall.get(taskindex));
					hmy = backend.unitcal(acceptask);
					endlist=backend.Adcontrol(acceptask,hmy);}
			}
			else {
				taskindex++;
				if(taskindex==taskall.size()-2) {
					taskindex=taskall.size()-2;
				}
				acceptask.removeLast();
				acceptask.add(taskall.get(taskindex));
				hmy = backend.unitcal(acceptask);
				endlist=backend.Adcontrol(acceptask,hmy);
			}
		}
	}
}