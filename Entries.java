import javax.swing.*;

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
        String[] labels = {"Type of Incident: ", "Location: ", "Status: ", "Dispatcher: "};
        String[] fireIncidentOutput = inputSetter("Fire Incident", labels, labels.length);
        return new Object[] {labels, fireIncidentOutput};
    }

    public static Object[] medicalInidentInput() {
        String[] labels = {"Patient Name: ", "Age: ", "Location: ", "Principal Complaint: ", "Actions Taken: ", "Personnel Present: ", "Remarks: "};
        String[] medicalIncidentOutput = inputSetter("Fire Incident", labels, 7);
        return new Object[] {labels, medicalIncidentOutput};
    }
    
    public static Object[] dispatchInput() {
        String[] labels = {"Location: ", "Firetruck Dispatched: ", "Delta: ", "Dispatched/Advised by: "};
        String[] dispatcherOutput = inputSetter("Dispatch", labels, 4);
        return new Object[] {labels, dispatcherOutput};
    }
    public static Object[] firetruckInput() {
        String[] labels = {"Firetruck: ", "Activity: ", "Status: "};
        String[] firetruckOutput = inputSetter("Firetruck", labels, 3);
        return new Object[] {labels, firetruckOutput};
        }
    public static Object[] personnelLogInput() {
        String[] labels = {"Name: ", "Callsign: ", "Status: "};
        String[] personnelOutput = inputSetter("Personnel", labels, 3);
        return new Object[] {labels, personnelOutput};
    }
    public static Object[] activityAttendanceInput() {
        String[] labels = {"Activity: ", "Name/Callsign of attended: "};
        String[] activityAttendanceOutput = inputSetter("Activity Attendance", labels, 2);
        return new Object[] {labels, activityAttendanceOutput};
    }
    public static Object[] netCallInput() {
        String[] labels = {"Callsign: ", "Location: "};
        String[] netCallOutput = inputSetter("Net Call", labels, 2);
        return new Object[] {labels, netCallOutput};
    }
    public static Object[] customLogInput() {
        String[] labels = {"Enter log: "};
        String[] customLogOutput = inputSetter("Custom Log", labels, 1);
        return new Object[] {labels, customLogOutput};
    }
     
    
    //String[] entries = {"(Add Entry)", "Fire incident", "Dispatch", "Firetruck log", "Personnel log", "Activity Attendance", "Net Call", "Custom Log"};
}