import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TimeApp implements ActionListener {
	JFrame appFrame;
	JButton startEndBtn;
	//JButton finish;
	FileWriter myWriter;
	LocalDate curDate;
	LocalDate myDateObj;
	LocalTime myTimeObj;
	DateTimeFormatter myFormatDate;
	DateTimeFormatter myFormatTime;
	Boolean start;
	public TimeApp() {
		start = true;
		try {
			File myTimes = new File("timeStamps.txt");
			
			if (myTimes.createNewFile()) {
			System.out.println("File created: " + myTimes.getName());
			myWriter = new FileWriter("timeStamps.txt");
			myWriter.write("Date\t\tStart Time\tEnd Time\n");
			myWriter.close();
			} else {
				curDate = LocalDate.now();
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}  



		myFormatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");//change format that only decade appeares in year
		myFormatTime = DateTimeFormatter.ofPattern("HH:mm");//change format that only decade appeares in year

		appFrame = new JFrame("Working Time");
		startEndBtn = new JButton("Start");
		startEndBtn.setBackground(Color.green);
		//finish = new JButton("Finish");
		startEndBtn.addActionListener(this);
		//	finish.addActionListener(this);
		appFrame.add(startEndBtn);
	
		appFrame.addWindowListener(new WindowAdapter() {
			
			
		public void windowClosing(WindowEvent e) { //not going in here
			
				if(!start) {
					myTimeObj = LocalTime.now();
					try {
						myWriter = new FileWriter("timeStamps.txt",true);
						myWriter.write("\t\t"+myTimeObj.format(myFormatTime)+"\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file.");
					}catch (IOException e1) {
						System.out.println("An error occurred.");
						e1.printStackTrace();
					}
				}
			}
		});
		
		appFrame.setSize(80, 80);
		appFrame.setVisible(true);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startEndBtn) {
			boolean newDate = false;
			myDateObj = LocalDate.now();
			myTimeObj = LocalTime.now();
			if(curDate == null || !(curDate.isEqual(myDateObj))){
				curDate=myDateObj;
				 newDate = true;
			}
			//myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm\n");//change format that only decade appeares in year
			try {
				myWriter = new FileWriter("timeStamps.txt",true);
				if (newDate) {
					myWriter.write(curDate.format(myFormatDate)); 
				}
				else
					myWriter.write("\t");
				 
				//System.out.println(myDateObj.format(myFormatObj));
				
				//System.out.println("Successfully wrote to the file.");
				if(start) {
					myWriter.write("\t"+myTimeObj.format(myFormatTime));
					startEndBtn.setBackground(Color.red);
					startEndBtn.setText("End");
					start=false;
				}
				else {
					myWriter.write("\t"+myTimeObj.format(myFormatTime)+"\n");
					startEndBtn.setBackground(Color.green);
					startEndBtn.setText("Start");
					start=true;
				}
				myWriter.close();
			} catch (IOException e1) {
				System.out.println("An error occurred.");
				e1.printStackTrace();
			}

		}

	}

}
