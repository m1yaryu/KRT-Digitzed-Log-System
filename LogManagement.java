import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void appendLogEntry(String[] labels, String[] data, JTextArea textArea) throws IOException {
        StringBuilder entry = new StringBuilder();

        String timeStamp = new SimpleDateFormat("HHmm'H'").format(new Date());
        entry.append(timeStamp).append("\t");

        entry.append(String.format("%-20s %s\n", labels[0], data[0]));
        for(int i=1; i<labels.length; i++){
            entry.append("\t");
            entry.append(String.format("%-20s %s\n", labels[i], data[i]));
        }
        
        entry.append("\n");
        textArea.append(entry.toString());
    }
}