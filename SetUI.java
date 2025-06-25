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
    
    JComboBox<String> entryComboBox = entryComboBox();
    private JPanel mainHeaderPanel = createMainHeaderPanel(); 
    private JScrollPane buttonsPanel = createButtonspanel(); 
    private JScrollPane mainViewPanel = createMainViewPanel(); 

    public void setUI() {
        setTitle("KRT Logs");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        getContentPane().setBackground(Color.DARK_GRAY);
        setLayout(null);
        
        add(buttonsPanel);
        add(mainHeaderPanel);
        add(mainViewPanel);

        setVisible(true);
    }
    
    public static File currentLogFile;
    private JScrollPane createButtonspanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Logs"));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton addNewLogButton = ButtonFunctionalities.addNewLogButton(this);
        //JButton deleteButton = ButtonFunctionalities.deleteButton(logText, selectedLine, this);
        //JButton saveChangesButton = ButtonFunctionalities.saveChangesButton(logText, currentLogFile, this);
        JButton refreshButton = ButtonFunctionalities.refreshButton(table, mainViewPanel, this);
        JButton restartButton = ButtonFunctionalities.restartButton(this);
        JButton[] buttonSet = {addNewLogButton, deleteButton, refreshButton, restartButton};

        for(JButton btn : buttonSet) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(210, btn.getPreferredSize().height));
            panel.add(Box.createVerticalStrut(5));
            panel.add(btn);
        }

        entryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        entryComboBox.setMaximumSize(new Dimension(210, entryComboBox.getPreferredSize().height));
        panel.add(add(Box.createVerticalStrut(5)));
        panel.add(entryComboBox);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 10, 210, 700);
        return scrollPane;
    }
    
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
    
		panel.add(title, BorderLayout.CENTER);
		return panel;
    }
	
    public static String currentFileString;
    public static String setTitleString() {
        if(currentLogFile==null) {
            currentFileString = "";
        }
        else {
            currentFileString = currentLogFile.toString();
        }
        return currentFileString;
    }
    
    public static JTable createTable() {
        Object[] tableData = LogManagement.tableData();
        String[] columns = (String[]) tableData[0];
        String[][] data = (String[][]) tableData[1];
        JTable table = new JTable(data, columns);

        return table;
    }

    int selectedLine = -1;
    JTable table = createTable();
    private JScrollPane createMainViewPanel() {
        JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(setTitleString()));
        panel.setBounds(235, 90, 755, 620);
        panel.setBackground(Color.RED);
        
        /*
		logText = new JTextArea();
        logText.setFont(new Font("Monospaced", Font.PLAIN, 15));
		logText.setEditable(false);
		logText.setLineWrap(false);

        logText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickPosition = logText.viewToModel2D(e.getPoint()); 
                selectEntry(clickPosition);
                }
                });
         */
		panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
		panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);
		panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
		panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(table);
        return scrollPane;
    }
    /*
    public void selectEntry(int clickPosition){
        try {
            selectedLine = logText.getLineOfOffset(clickPosition);  
            String[] lines = logText.getText().split("\n");
            int startLine = selectedLine;
            int endLine = selectedLine;
            
            while(startLine > 0 && lines[startLine].startsWith("\t")) {
                startLine--;
            }
            
            while(endLine+1<lines.length && lines[endLine+1].startsWith("\t")) {
                endLine++;
            }
            
            int startOffset = logText.getLineStartOffset(startLine);
            int endOffset = logText.getLineEndOffset(endLine);
            
            logText.setSelectionStart(startOffset);
            logText.setSelectionEnd(endOffset);
        }
        catch(Exception ex) {
            selectedLine = -1;
            logText.select(0, 0);
        }
    } 
    */
    public void entryResult(Object[] entryData, String entryLabel) {
        if (entryData == null || currentLogFile == null) {
            JOptionPane.showMessageDialog(this, "No entry data or no log file is open.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String[] returnedLabels = (String[]) entryData[0];
        String[] returnedValues = (String[]) entryData[1];
        
        try{
            LogManagement.appendLogEntry(returnedLabels, returnedValues, entryLabel, this);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to write to log file:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            String selected = (String) e.getItem();
            
            switch(selected) {
                case "Fire Incident":
                    Object[] fireIncidentData = Entries.fireIncidentInput();
                    entryResult(fireIncidentData, "Fire Incident");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Medical Incident":
                    Object[] medicalIncidentData = Entries.medicalInidentInput();
                    entryResult(medicalIncidentData, "Medical Incident");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Dispatch":
                    Object[] dispatchData = Entries.dispatchInput();
                    entryResult(dispatchData, "Dispatch");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Firetruck Log":
                    Object[] firetruckData = Entries.firetruckInput();
                    entryResult(firetruckData, "Firetruck Log");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Personnel Log":
                    Object[] personnelLogData = Entries.personnelLogInput();
                    entryResult(personnelLogData, "Personnel Log");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "In-Base Activity Attendance":
                    Object[] activityAttendanceData = Entries.activityAttendanceInput();
                    entryResult(activityAttendanceData, "In-Base Activity Attendance");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Out For Activity":
                    Object[] activityOutData = Entries.activityOutInput();
                    entryResult(activityOutData, "Out For Activity");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Back From Activity":
                    Object[] activityInData = Entries.activityBackInput();
                    entryResult(activityInData, "Back From Activity");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Net Call":
                    Object[] netCallData = Entries.netCallInput();
                    entryResult(netCallData, "Net Call");
                    entryComboBox.setSelectedIndex(0);
                    break;
                case "Custom Log":
                    Object[] customLogData = Entries.customLogInput();
                    entryResult(customLogData, "Custom Log");
                    entryComboBox.setSelectedIndex(0);
                    break;
                }
            }
        }

    public static String[] entries = {"(Add Entry)", "Fire Incident","Medical Incident", "Dispatch", "Firetruck Log", "Personnel Log", "Out For Activity", "Back From Activity", "In-Base Activity Attendance", "Net Call", "Custom Log"};
    public JComboBox<String> entryComboBox() {
        JComboBox<String> entriesCombobox = new JComboBox<>(entries);
        entriesCombobox.addItemListener(this);
        return entriesCombobox;
    }
}
