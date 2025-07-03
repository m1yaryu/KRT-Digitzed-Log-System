import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class Entries {
    //JTextField locationField, typeOfIncidentField, statusField; 

    public static String[] inputSetter(String title, String[] labels, int fieldCount) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField[] fields = new JTextField[fieldCount];

        for(int i=0; i<fieldCount; i++) {
            fields[i] = new JTextField();
            panel.add(new JLabel(labels[i]));
            panel.add(fields[i]);
            panel.add(Box.createVerticalStrut(10));    
        }
        
        int result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION) {
            String[] values = new String[fieldCount];
            for(int i=0; i<fieldCount; i++) {
                values[i] = fields[i].getText();
            }
            return values;
        } 
        else{
            return null;
        }
    }

    public static Object[] fireIncidentInput() {
        String[] labels = {"Type of Incident: ", "Location: ", "Status: ", "Dispatcher: ", "Logged by: "};
        String[] fireIncidentOutput = inputSetter("Fire Incident", labels, labels.length);
        return new Object[] {labels, fireIncidentOutput};
    }

    public static Object[] medicalInidentInput() {
        String[] labels = {"Patient Name: ", "Location: ", "Principal Complaint: ", "Actions Taken: ", "Personnel Present: ", "Remarks: ", "Logged by: "};
        String[] medicalIncidentOutput = inputSetter("Medical Incident", labels, labels.length);
        return new Object[] {labels, medicalIncidentOutput};
    }
    
    public static Object[] dispatchInput() {
        String[] labels = {"Firetruck Dispatched: ", "Location: ", "Delta: ", "Dispatched/Advised by: ", "Logged by: "};
        String[] dispatcherOutput = inputSetter("Dispatch", labels, labels.length);
        return new Object[] {labels, dispatcherOutput};
    }   
    public static Object[] firetruckInput() {
        String[] labels = {"Firetruck: ", "Location: ","Activity: ", "Status: ", "Logged by: "};
        String[] firetruckOutput = inputSetter("Firetruck", labels, labels.length);
        return new Object[] {labels, firetruckOutput};
        }
    public static Object[] personnelLogInput() {
        String[] labels = {"Name: ", "Callsign: ", "Status: ", "Logged by: "};
        String[] personnelOutput = inputSetter("Personnel", labels, labels.length);
        return new Object[] {labels, personnelOutput};
    }
    public static Object[] activityOutInput() {
        String[] labels = {"Activity: ", "Location: ","Personnel Present & Assigned Radios: ", "Vehicle: ", "Delta: ", "Equipment Pulled out: ", "Logged by: "};
        String[] activityOutOutput = inputSetter("Out For Activity", labels, labels.length);
        return new Object[] {labels, activityOutOutput};
    }
    public static Object[] activityBackInput() {
        String[] labels = {"Activity: ", "Personnel Present & Assigned Radios: ", "Vehicle: ", "Delta: ", "Equipment Returned: ", "Logged by: "};
        String[] activityBackOutput = inputSetter("Return From Activity", labels, labels.length);
        return new Object[] {labels, activityBackOutput};
    }
    public static Object[] activityAttendanceInput() {
        String[] labels = {"Activity: ", "Name: ", "Callsign: ", "Logged by: "};
        String[] activityAttendanceOutput = inputSetter("In-Base Activity Attendance", labels, labels.length);
        return new Object[] {labels, activityAttendanceOutput};
    }
    public static Object[] netCallInput() {
        String[] labels = {"Callsign: ", "Location: ", "Logged by: "};
        List<String[]> entries = new ArrayList<>();
        
        String[] loggedByLabel = {"Logged by: "};
        String[] loggedByInput = inputSetter("Net Call - Logged By", loggedByLabel, 1);

        if(loggedByInput != null) {
            return null;
        }
        while(true) {
            String[] result = inputSetter("Net Call", labels, labels.length);
            if(result == null) {
                break;
            }
            entries.add(result);
        }
        return new Object[] {
            new Object[] {labels, loggedByLabel},
            new Object[] {entries.toArray(new String[0][0]), loggedByInput[0]}
        };
    }

    public static Object[] customLogInput() {
        String[] labels = {"Enter log: "};
        String[] customLogOutput = inputSetter("Custom Log", labels, labels.length);
        return new Object[] {labels, customLogOutput};
    }
}