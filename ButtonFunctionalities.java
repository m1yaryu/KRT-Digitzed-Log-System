import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionListener;

public class ButtonFunctionalities {

    public static void deleteLine(JTextArea textArea, int selectedLine, JFrame parenFrame){
        if(selectedLine < 0) {
                JOptionPane.showMessageDialog(parenFrame, "No line selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        String selectedText = textArea.getSelectedText();
        if(selectedText==null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(parenFrame, "No entry selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int start = textArea.getSelectionStart();
            int end = textArea.getSelectionEnd();
            StringBuilder updatedText = new StringBuilder(textArea.getText());
            updatedText.delete(start, end);
            textArea.setText(updatedText.toString());
            textArea.select(0, 0);
        }
        catch(Exception ex) {
            JOptionPane.showMessageDialog(parenFrame, "Error deleting entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Deleted text but did not save.");

    } 

    public static void saveChangesFunctionality(JTextArea textArea, File currentFile, JFrame parentFrame){
        if(currentFile!=null) {
            try(FileWriter writer = new FileWriter(currentFile)){
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(parentFrame, "Changes saved successfulyly!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(parentFrame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("Explicit save triggered.");

        }
    }

    public static void refresh(JTextArea textArea, File currentFile, JFrame parenFrame) {
        try {
            if(currentFile!=null) {
                textArea.setText(java.nio.file.Files.readString(currentFile.toPath()));
            }
        }
        catch(Exception ex) {
            JOptionPane.showMessageDialog(parenFrame, "Failed to refresh logs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}