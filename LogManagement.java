import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.FilerException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class LogManagement {
    public static Object[] tableData() {
        String[] columnNames = null;
        List<String[]> rowList = new ArrayList<>();
        List<String> logIDs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("tableDisplay.txt"))) {
            String line;
            boolean isFirstLine = true;

            while((line = br.readLine()) != null) {
                List<String> values = new ArrayList<>(Arrays.asList(line.split("\\|")));
                String logID = values.remove(0);

                if(isFirstLine) {
                    isFirstLine = false;
                    columnNames = values.toArray(new String[0]);
                }
                else if (line.charAt(0) != '\t'){
                    rowList.add(values.toArray(new String[0]));
                    logIDs.add(logID);
                }
                else {
                    continue;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        String[][] data = rowList.toArray(new String[0][]);
        String[] logIDArray = logIDs.toArray(new String[0]);
        return new Object[] {columnNames, data, logIDArray};
    }

    public static String formatNetCallEntry(String[][] entries, int logID, String LoggedBy,String entryLabel) {
        StringBuilder netCallBuilder = new StringBuilder();
        String dateStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        String timeStamp = new SimpleDateFormat("HHmm'H'").format(new Date());
        
        // Header with empty location field
        netCallBuilder.append(logID).append("|")
               .append(dateStamp).append("|")
               .append(timeStamp).append("|")
               .append(entryLabel).append("|")
               .append(" ").append("|")
               .append(LoggedBy).append("\n");
        
        // Add individual entries
        for (String[] entry : entries) {
            String callsign = entry.length > 0 ? entry[0] : "Unknown";
            String location = entry.length > 1 ? entry[1] : "Unknown";
            netCallBuilder.append(String.format("\tCallsign: %-15s Location: %s\n", callsign, location));
        }
    
        return netCallBuilder.toString();
    }
    public static void appendLogEntry(String[] labels, String[] data, String entryLabel, JFrame parentFrame) throws IOException {
        StringBuilder entryBuilder = new StringBuilder();
        StringBuilder entryDetails = new StringBuilder();

        //header
        String dateStamp = new SimpleDateFormat("MM/dd/YYYY").format(new Date());
        String timeStamp = new SimpleDateFormat("HHmm'H'").format(new Date());
        String LoggedByValue = data[labels.length - 1];
        
        int lastLogID = Integer.parseInt(SetUI.lastID);
        int nextLogID = lastLogID + 1;
        entryBuilder.append(nextLogID).append("|");
        entryBuilder.append(dateStamp).append("|");
        entryBuilder.append(timeStamp).append("|");
        entryBuilder.append(entryLabel).append("|");
        
        if(labels[1] == "Location: "){    
            entryBuilder.append(data[1]).append("|");
        }
        else if(labels[1] == "Callsign: " || labels[2] == "Callsign: ") {
            entryBuilder.append("Base").append("|");
        }
        else if(labels[4] == "Equipment Returned: ") {
            entryBuilder.append("Back To Base").append("|");
        }
        else if(labels.length == 2 || labels.length == 1){
            entryBuilder.append("|").append(" |");
        }
        entryBuilder.append(LoggedByValue).append("\n");
        
        for(int i=0; i<labels.length; i++){
            if(labels[i].trim().equalsIgnoreCase("Logged By: ")) {
                LoggedByValue = data[i];
            }
            else {
                entryDetails.append(String.format("\t%-23s %s\n", labels[i], data[i]));
            }
        }
        
        entryBuilder.append(entryDetails.toString());
        String entry = entryBuilder.toString();
        FileWriter writer = new FileWriter("tableDisplay.txt", true);
        try(writer){
            writer.write(entry);
            JOptionPane.showMessageDialog(parentFrame, "Changes saved successfulyly!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void deleteLogEntry(String logIDtoDelete) throws IOException {
        File inputFile = new File("tableDisplay.txt");
        File tempFile = new File("temptableDisplay.txt");

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile)); FileWriter writer = new FileWriter(tempFile)){
            String line;
            boolean skipEntry = false;
            int currentLogID = 0;
            boolean isFirstLine = true;

            while((line = reader.readLine()) != null) {
                if(isFirstLine) {
                    writer.write(line + System.lineSeparator());
                    isFirstLine = false;
                    continue;
                }

                if(!line.startsWith("\t")) {
                    String[] parts = line.split("\\|", 2);
                    String foundLogID = parts[0].trim();

                    if(foundLogID.equals(logIDtoDelete)){
                        skipEntry = true;
                    }
                    else {
                        skipEntry = false;
                        currentLogID++;
                        String reIndexedLine = currentLogID + "|" + parts[1];
                        writer.write(reIndexedLine + System.lineSeparator());
                    }
                }
                else {
                    if(!skipEntry) {
                        writer.write(line + System.lineSeparator());
                    }
                }
            }
        }

         if (!inputFile.delete()) {
            throw new IOException("Could not delete original file: " + inputFile.getName());
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new IOException("Could not rename temp file to original: " + tempFile.getName());
        }
    }
}