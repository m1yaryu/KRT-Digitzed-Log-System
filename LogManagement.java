import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.processing.FilerException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class LogManagement {
    public static File createNewLogFile() throws IOException{
        SimpleDateFormat fileFormatter = new SimpleDateFormat("yyyyMMdd");
        String fileName = fileFormatter.format(new Date()) + ".txt";
        File logDir = new File("logFiles");
        
        if(!logDir.exists()) {
            boolean dirCreated = logDir.mkdir();
            if(!dirCreated) {
                throw new IOException("Failed to create directory: " + logDir.getAbsolutePath());
            }
        }
        
        File newFile = new File(logDir, fileName);
        
        if(newFile.createNewFile()) {
            FileWriter writer = new FileWriter(newFile);
            writer.write("New Log: " + fileFormatter.format(new Date()));
        }
        else {
            System.out.println("File already exists!");
        }
        return newFile;
    }

    public static Object[] tableData() {
        String[] columnNames = null;
        List<String[]> rowList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("logEntries.txt"))) {
            String line;
            boolean isFirstLine = true;

            while((line = br.readLine()) != null) {
                String[] values = line.split("\t\t");
                if(isFirstLine) {
                    columnNames = values;
                    isFirstLine = false;
                }
                else {
                    rowList.add(values);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        String[][] data = rowList.toArray(new String[0][]);

        return new Object[] {columnNames, data};
    }

    public static void appendLogEntry(String[] labels, String[] data, String entryLabel, JFrame parentFrame) throws IOException {
        StringBuilder entryBuilder = new StringBuilder();

        //header
        String timeStamp = new SimpleDateFormat("HHmm'H'").format(new Date());
        entryBuilder.append(timeStamp).append("\t");
        entryBuilder.append(entryLabel).append("\t");

        entryBuilder.append(String.format("%-23s %s\n", labels[0], data[0]));
        for(int i=1; i<labels.length; i++){
            entryBuilder.append(String.format("%-23s %s\n", labels[i], data[i]));
        }
        
        entryBuilder.append("[foobar]");

        String entry = entryBuilder.toString();
        FileWriter writer = new FileWriter("logEntries.txt");
        try(writer){
            writer.write(entry);
            JOptionPane.showMessageDialog(parentFrame, "Changes saved successfulyly!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}