import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Window.Type;
import java.awt.ComponentOrientation;

public class TimeApp implements ActionListener, WindowListener {
	JFrame appFrame;
	JButton startEndBtn;
	FileWriter myWriter;
	LocalDate curDate;
	LocalDate myDateObj;
	LocalTime myTimeObj;
	DateTimeFormatter myFormatDate;
	DateTimeFormatter myFormatTime;
	Boolean startWork;

	public TimeApp() {
		startWork = true;
		createFile();
		setTimeFormat();
		buildFrame(); 
		
	}

	private void createFile() {
		new File("My Work");

		try {
			String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			curDate = LocalDate.now();
			File myTimes = new File(monthNames[curDate.getMonthValue()]+"-Times.txt");
			
			if (myTimes.createNewFile()) {
				System.out.println("File created: " + myTimes.getName());
				myWriter = new FileWriter("timeStamps.txt");
				myWriter.write("Date\t\tStart Time\tEnd Time\n");
				myWriter.close();
			} else {
				
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}		
	}

	private void setTimeFormat() {
		myFormatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");// change format that only decade appeares in year
		myFormatTime = DateTimeFormatter.ofPattern("HH:mm");// 		
	}

	private void buildFrame() {
		appFrame = new JFrame("Working Time");
		appFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Aviad\\Desktop\\MyAppImg.png"));
		appFrame.setVisible(true);
		startEndBtn = new JButton("");
		startEndBtn.setBackground(new Color(30, 144, 255));
		startEndBtn.setIcon(new ImageIcon("C:\\Users\\Aviad\\eclipse-workspace\\WorkTime\\Aviadprogram-03.png"));
		
		startEndBtn.addActionListener(this);
		
		appFrame.getContentPane().add(startEndBtn);

		appFrame.addWindowListener(this);
		appFrame.setSize(258, 237);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		appFrame.setLocation(dim.width - appFrame.getSize().width - 200, dim.height - appFrame.getSize().height - 40);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startEndBtn) {
			boolean newDate = false;
			myDateObj = LocalDate.now();
			myTimeObj = LocalTime.now();
			if (curDate == null || !(curDate.isEqual(myDateObj))) {
				curDate = myDateObj;
				newDate = true;
			}
			// myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm\n");//change
			// format that only decade appeares in year
			try {
				myWriter = new FileWriter("timeStamps.txt", true);
				if (newDate) {
					myWriter.write(curDate.format(myFormatDate));
				} else
					myWriter.write("\t");

				// System.out.println(myDateObj.format(myFormatObj));

				// System.out.println("Successfully wrote to the file.");
				if (startWork) {
					myWriter.write("\t" + myTimeObj.format(myFormatTime));
					startEndBtn.setIcon(new ImageIcon("C:\\Users\\Aviad\\eclipse-workspace\\WorkTime\\Aviadprogram-05.png"));
	
					startWork = false;
				} else {
					myWriter.write("\t" + myTimeObj.format(myFormatTime) + "\n");
					startEndBtn.setIcon(new ImageIcon("C:\\Users\\Aviad\\eclipse-workspace\\WorkTime\\Aviadprogram-03.png"));
					
					startWork = true;
				}
				myWriter.close();
			} catch (IOException e1) {
				System.out.println("An error occurred.");
				e1.printStackTrace();
			}

		}

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (!startWork) {
			myTimeObj = LocalTime.now();
			try {
				myWriter = new FileWriter("timeStamps.txt", true);
				myWriter.write("\t\t" + myTimeObj.format(myFormatTime) + "\n");
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e1) {
				System.out.println("An error occurred.");
				e1.printStackTrace();
			}
		}
	}


	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	/*
	 * Adds the App to the system tray when minimized 
	 * Bugs- 1. if minimized again adds another icon again and again 
	 * 		 2. I want it to be always there until program is completely closed
	 * 		 3. weird picture
	 */
	public void windowIconified(WindowEvent e) {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		Image image = (Image) startEndBtn.getIcon();
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(image, "Working Dog", popup);
		final SystemTray tray = SystemTray.getSystemTray();

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
		trayIcon.addMouseListener(new MouseAdapter() {
		public void	mouseClicked(MouseEvent e){
		appFrame.setState(Frame.NORMAL);
		tray.remove(trayIcon);
		}
		});
		try {
			tray.add(trayIcon);
		} catch (AWTException e2) {
			System.out.println("TrayIcon could not be added.");
		}
	}		


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
