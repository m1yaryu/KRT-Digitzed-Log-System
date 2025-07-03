import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
import java.util.logging.LogManager;

public class SetUI extends JFrame implements ItemListener{
        
    JComboBox<String> entryComboBox = entryComboBox();
    JTable table = createTable();
    private JScrollPane mainViewPanel = createMainViewPanel(); 
    private JScrollPane buttonsPanel = createButtonspanel(); 
    private JPanel mainHeaderPanel = createMainHeaderPanel(); 

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
    
    public static File currentLogFile = new File("tableDisplay.txt");
    private JScrollPane createButtonspanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Logs"));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // mainViewPanel is now initialized before this is called
        JButton refreshButton = ButtonFunctionalities.refreshButton(table, mainViewPanel, this);
        JButton restartButton = ButtonFunctionalities.restartButton(this);
        JButton deleteButton = ButtonFunctionalities.deleteButton((String[])tableData[2], table,this);
        
        JButton[] buttonSet = {refreshButton, restartButton, deleteButton};
        
        for (JButton btn : buttonSet) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(210, btn.getPreferredSize().height));
            panel.add(Box.createVerticalStrut(5));
            panel.add(btn);
        }
    
        entryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        entryComboBox.setMaximumSize(new Dimension(210, entryComboBox.getPreferredSize().height));
        panel.add(Box.createVerticalStrut(5));
        panel.add(entryComboBox);
    
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 10, 210, 700);
    
        return scrollPane;
    }
    
    
    private JTextArea title;        
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
    
    private static Object[] tableData = LogManagement.tableData();
    public static String lastID = ((String[]) tableData[2])[((String[]) tableData[2]).length - 1];
    public static JTable createTable() {
        Object[] tableData = LogManagement.tableData(); 
        String[] columns = (String[]) tableData[0];
        String[][] data = (String[][]) tableData[1];
        String[] logIDs = (String[]) tableData[2];

        DefaultTableModel model = new DefaultTableModel(data, columns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } 
        };
            
        JTable table = new JTable(model);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        ButtonFunctionalities.tableRowSelection(table, logIDs);
        return table;
    }

    private JScrollPane createMainViewPanel() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(setTitleString()));
        scrollPane.setBounds(235, 90, 755, 620);
        return scrollPane;
    }

    public void entryResult(Object[] entryData, String entryLabel) {
        if (entryData == null || currentLogFile == null) {
            JOptionPane.showMessageDialog(this, "No entry data or no log file is open.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] returnedLabels = (String[]) entryData[0];

        if ("Net Call".equals(entryLabel) && entryData[1] instanceof Object[]) {
            Object[] data = (Object[]) entryData[0];
            String[][] returnedEntries = (String[][]) data[0];
            String loggedBy = (String) data[1];

            int lastLogID = Integer.parseInt(SetUI.lastID);
            int nextLogID = lastLogID + 1;

            String formattedEntry = LogManagement.formatNetCallEntry(returnedEntries, nextLogID, loggedBy,entryLabel);

            try (FileWriter writer = new FileWriter("tableDisplay.txt", true)) {
                writer.write(formattedEntry);
                JOptionPane.showMessageDialog(this, "Net Call entry saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to write Net Call entry:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } else if (entryData[1] instanceof String[][]) {
            String[][] returnedEntries = (String[][]) entryData[1];

            for (String[] values : returnedEntries) {
                try {
                    LogManagement.appendLogEntry(returnedLabels, values, entryLabel, this);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Failed to write to log file:\n" + e.getMessage());
                    e.printStackTrace();
                }
            }

        } else if (entryData[1] instanceof String[]) {
            String[] returnedValues = (String[]) entryData[1];

            try {
                LogManagement.appendLogEntry(returnedLabels, returnedValues, entryLabel, this);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to write to log file:\n" + e.getMessage());
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Unexpected input format.", "Error", JOptionPane.ERROR_MESSAGE);
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
