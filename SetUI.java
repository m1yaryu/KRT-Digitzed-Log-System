import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SetUI extends JFrame implements ItemListener{

    public void setUI() {
        setTitle("KRT Logs");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);
        setLayout(null);
        
        add(createLogHistoryPanel());
        add(createMainHeaderPanel());
        add(createMainViewPanel());
        add(createButtonsPanel());
        //add(addWindowScrollPane());

        setVisible(true);
    }
    
    private JScrollPane createLogHistoryPanel() {
        JPanel logHistoryPanel = new JPanel();
        logHistoryPanel.setBorder(BorderFactory.createTitledBorder("Logs"));
        logHistoryPanel.setBackground(Color.LIGHT_GRAY);
        logHistoryPanel.setLayout(new BoxLayout(logHistoryPanel, BoxLayout.Y_AXIS));
        
        File currentDirectory = new File(".");
        File[] txtFiles = currentDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        //use try catch later
		java.util.Arrays.sort(txtFiles);
		if (txtFiles != null) {
            for (File file : txtFiles) {
                JButton fileButton = new JButton(file.getName());
                fileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                fileButton.setMaximumSize(new Dimension(210, fileButton.getPreferredSize().height));
                
				fileButton.addActionListener(e -> {
					try {
						logText.setText(java.nio.file.Files.readString(file.toPath()));
					} 
					catch (Exception ex){
                        JOptionPane.showMessageDialog(this,"Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				});
                
                logHistoryPanel.add(fileButton);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(logHistoryPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 10, 210, 700);
        return scrollPane;
    }
    
    /*
    private JScrollPane addWindowScrollPane() {
        JScrollPane windowScrollPane = new JScrollPane();
        windowScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        windowScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(windowScrollPane);
    } 
         */


	private JTextArea logText, title;
    private JPanel createMainHeaderPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tite l"));
        panel.setBounds(235, 10, 755, 70);
        
		title = new JTextArea();
		title.setEditable(false);
		title.setLineWrap(true);
		title.setWrapStyleWord(true);
		title.setFont(new Font("Sans Serif", Font.BOLD, 34));
		title.setText("KRT Logs");

		//panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
		//panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);
		//panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
		//panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
		panel.add(title, BorderLayout.CENTER);
		return panel;
    }
	
    int selectedLine = -1;
    private JPanel createMainViewPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Main Otin"));
        panel.setBounds(235, 90, 755, 550);
        panel.setBackground(Color.RED);
		
		logText = new JTextArea();
		logText.setEditable(false);
		logText.setLineWrap(true);
		logText.setWrapStyleWord(true);

        logText.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int clickPosition = logText.viewToModel2D(e.getPoint());
                try {
                    selectedLine = logText.getLineOfOffset(clickPosition);
                    int start = logText.getLineStartOffset(selectedLine);
                    int end = logText.getLineEndOffset(selectedLine);
                    logText.setSelectionStart(start);
                    logText.setSelectionEnd(end);
                }
                catch(Exception ex) {
                    selectedLine = -1;
                    logText.select(0, 0);
                }
            }
        });

		panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
		panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);
		panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
		panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);

		JScrollPane scrollPane = new JScrollPane(logText);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    public void entryResult(Object[] entryData) {
        String[] returnedLabels = (String[]) entryData[0];
        String[] returnedValues = (String[]) entryData[1];

        for(int i=0; i<returnedLabels.length; i++) {
            System.out.println(returnedLabels[i] + returnedValues[i]);
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            String selected = (String) e.getItem();

            switch(selected) {
                case "Fire Incident":
                    Object[] fireIncidentData = Entries.fireIncidentInput();
                    entryResult(fireIncidentData);
                    break;
                case 
                "Medical Incident":
                    Object[] medicalIncidentData = Entries.medicalInidentInput();
                    entryResult(medicalIncidentData);
                    break;
                case "Dispatch":
                    Object[] dispatchData = Entries.dispatchInput();
                    entryResult(dispatchData);
                    break;
                case "Firetruck Log":
                    Object[] firetruckData = Entries.firetruckInput();
                    entryResult(firetruckData);
                    break;
                case "Personnel Log":
                    Object[] personnelLogData = Entries.personnelLogInput();
                    entryResult(personnelLogData);
                    break;
                case "Activity Attendance":
                    Object[] activityAttendanceData = Entries.activityAttendanceInput();
                    entryResult(activityAttendanceData);
                    break;
                case "Net Call":
                    Object[] netCallData = Entries.netCallInput();
                    entryResult(netCallData);
                    break;
                case "Custom Log":
                    Object[] customLogData = Entries.customLogInput();
                    entryResult(customLogData);
                    break;
            }
        }
    }

    private JComboBox<String> entryComboBox() {
        String[] entries = {"(Add Entry)", "Fire Incident","Medical Incident", "Dispatch", "Firetruck Log", "Personnel Log", "Activity Attendance", "Net Call", "Custom Log"};
        JComboBox<String> entriesCombobox = new JComboBox<>(entries);
        entriesCombobox.addItemListener(this);
        return entriesCombobox;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Button pututoy"));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBounds(235, 650, 755, 60);
        panel.setBackground(Color.YELLOW);

        JButton addNewLog = new JButton("Add New Log");
        JButton deleteButton = new JButton("Delete");
        JButton saveChangesButton = new JButton("Save Changes");
        JButton resetButton = new JButton("Reset");

        //LogManagement LogManagement = new LogManagement();
        addNewLog.addActionListener(e -> {
            try {
                LogManagement.createNewLogFile();
                JOptionPane.showMessageDialog(this, "New log file created!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(this, "Error creating file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        deleteButton.addActionListener(e -> {ButtonFunctionalities.deleteLine(logText, selectedLine, this);});

        //save changes button and reset button
        panel.add(entryComboBox());
        panel.add(addNewLog);
        panel.add(deleteButton);
        panel.add(saveChangesButton);
        panel.add(resetButton);

        return panel;
    }
}
