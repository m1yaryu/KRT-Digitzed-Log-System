import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionListener;

public class ButtonFunctionalities {
    public static JButton addNewLogButton(JFrame parenFrame) {
        JButton addNewlogButton = new JButton("Add New Log");
        
        addNewlogButton.addActionListener(e -> {
            try {
                LogManagement.createNewLogFile();
                JOptionPane.showMessageDialog(parenFrame, "New log file created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                restartUI(parenFrame);
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(parenFrame, "Error creating file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        return addNewlogButton;
    }

    public static JButton deleteButton(JTextArea textArea, int selectedLine, JFrame parenFrame){
        JButton deleteButton = new JButton("Delete");
        
        deleteButton.addActionListener(e -> {

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
        });
        return deleteButton;
    } 

    public static JButton saveChangesButton(JTextArea textArea, File currentFile, JFrame parentFrame){
        JButton saveChangesButton = new JButton("Save Changes");
        
        saveChangesButton.addActionListener(e -> {
            if(currentFile!=null) {
                try(FileWriter writer = new FileWriter(currentFile)){
                    writer.write(textArea.getText());
                    JOptionPane.showMessageDialog(parentFrame, "Changes saved successfulyly!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(parentFrame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return saveChangesButton;
    }

    public static JButton refreshButton(JTextArea textArea, File currentFile, JFrame parenFrame) {
        JButton refreshButton = new JButton("Refresh");
        
        refreshButton.addActionListener(e -> {
            try {
                if(currentFile!=null) {
                    textArea.setText(java.nio.file.Files.readString(currentFile.toPath()));
                }
            }
            catch(Exception ex) {
                JOptionPane.showMessageDialog(parenFrame, "Failed to refresh logs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return refreshButton;
    }

    public static JButton restartButton(JFrame parenFrame) {
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {restartUI(parenFrame);});
        return restartButton;
    }

    public static void restartUI(JFrame parenFrame) {
        parenFrame.dispose();

        SwingUtilities.invokeLater(() -> {
            SetUI newUI = new SetUI();
            newUI.setUI();
        });
    }
}