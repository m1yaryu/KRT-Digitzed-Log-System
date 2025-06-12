import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionListener;

public class ButtonFunctionalities {

    public static void deleteLine(JTextArea textArea, int selectedLine, JFrame parenFrame){
        if(selectedLine == 1) {
                JOptionPane.showMessageDialog(parenFrame, "No line selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            try {
                int start = textArea.getLineStartOffset(selectedLine);
                int end = textArea.getLineEndOffset(selectedLine);
                StringBuilder updatedText = new StringBuilder(textArea.getText());
                updatedText.delete(start, end);
                textArea.setText(updatedText.toString());
                selectedLine = -1;
                textArea.select(0, 0); 
    } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(parenFrame, "Error deleting line: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    } 
}