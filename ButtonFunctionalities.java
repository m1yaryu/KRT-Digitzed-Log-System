import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.table.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Dimension;
public class ButtonFunctionalities {
    public static JButton deleteButton(String[] logIDs, JTable table,JFrame parenFrame){
        JButton deleteButton = new JButton("Delete");
        
        deleteButton.addActionListener(e -> {
            int selectedLine = table.getSelectedRow();
            String selectedLogID = logIDs[selectedLine];

            int confirmation = JOptionPane.showConfirmDialog(parenFrame, "Are sure you want to delete this entry?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if(selectedLine < 0) {
                    JOptionPane.showMessageDialog(parenFrame, "No line selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            if(confirmation == JOptionPane.YES_OPTION) {
                try {
                    LogManagement.deleteLogEntry(selectedLogID);
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(parenFrame, "Error deleting entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(parenFrame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        return deleteButton;
    }
    
    public static void displayLogDetails(String selectedLogID) {
        StringBuilder output = new StringBuilder();
        Pattern logIdPattern = Pattern.compile("^(\\d+)\\|");  // capture the logID number
        boolean entryStarted = false;
        boolean matchedEntry = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("tableDisplay.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = logIdPattern.matcher(line);

                if (matcher.find()) {
                    String currentLogID = matcher.group(1);

                    // If we already matched and this is a new entry, stop reading
                    if (matchedEntry) {
                        break;  // done with the desired log entry
                    }

                    if (currentLogID.equals(selectedLogID)) {
                        entryStarted = true;
                        matchedEntry = true;
                        // Skip appending this header line
                    } else {
                        entryStarted = false;
                    }

                } else if (entryStarted && line.startsWith("\t")) {
                    String trimmedLine = line.trim();
                    // Optional: format with fixed-width columns by splitting at first colon
                    String[] parts = trimmedLine.split(":", 2);
                    if (parts.length == 2) {
                        String label = parts[0].trim();
                        String value = parts[1].trim();
                        output.append(String.format("%-23s %s%n", label + ":", value));
                    } else {
                        output.append(trimmedLine).append("\n");
                    }
                }
            }

            if (output.length() > 0) {
                JTextArea textArea = new JTextArea(output.toString());
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Log Details for ID " + selectedLogID, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No details found for Log ID " + selectedLogID, "No Details", JOptionPane.WARNING_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void tableRowSelection(JTable table, String[] logIDs) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if(selectedRow != -1 && logIDs != null && selectedRow < logIDs.length) {
                    String selectedLogID = logIDs[selectedRow];
                    displayLogDetails(selectedLogID);
                    System.out.println("Log ID: " + selectedLogID);
                    System.out.println(SetUI.lastID);
                }
            }
        });
    }
    
    public static JButton refreshButton(JTable table, JScrollPane currentPanel, JFrame parenFrame) {
    //SetUI ui = new SetUI();
    JButton refreshButton = new JButton("Refresh");

    refreshButton.addActionListener(e -> {
        try {
            JTable newTable = SetUI.createTable(); // New table instance
            currentPanel.setViewportView(newTable); // Replace the table in the scroll pane
            currentPanel.revalidate(); // Refresh layout
            currentPanel.repaint();    // Repaint UI
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(parenFrame, "Failed to refresh logs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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