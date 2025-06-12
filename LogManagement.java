import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class LogManagement {
    public static File createNewLogFile() throws IOException{
        SimpleDateFormat fileFormatter = new SimpleDateFormat("yyyyMMdd");
        String fileName = fileFormatter.format(new Date()) + ".txt";
        File newFile = new File(fileName);

        if(newFile.createNewFile()) {
            FileWriter writer = new FileWriter(newFile);
            writer.write("New Log: " + fileFormatter.format(new Date()));
        }
        else {
            System.out.println("File already exists!");
        }
        return newFile;
    }
}