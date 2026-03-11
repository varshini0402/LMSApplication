package org.example.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// This class creates a simple GUI to display active students from the LMS API
public class LMSGui extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton fetchButton;
    private JButton clearButton;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JTextField searchField;
    private JLabel countLabel;
    private TableRowSorter<DefaultTableModel> sorter;

    // Set up the GUI window with title, size and components
    public LMSGui() {
        setTitle("LMS - Active Students");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a title label at the top of the window
        titleLabel = new JLabel("Active Students List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Create a search panel with a text field and search button
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search by name: "));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudent());
        searchPanel.add(searchButton);

        // Create a top panel to hold title and search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Create a table with ID and Name columns to show students
        tableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);

        // Center the ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Set up sorter for search functionality
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Create a label to show total number of students
        countLabel = new JLabel("Total Students: 0", SwingConstants.CENTER);
        countLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create a button panel at the bottom
        JPanel buttonPanel = new JPanel();

        // Button to load students from the API
        fetchButton = new JButton("Load Students");
        fetchButton.addActionListener(e -> fetchStudents());

        // Button to clear the table
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            // Clear all rows from the table
            tableModel.setRowCount(0);
            countLabel.setText("Total Students: 0");
            statusLabel.setText("Table cleared");
            searchField.setText("");
        });

        // Add buttons to the panel
        buttonPanel.add(fetchButton);
        buttonPanel.add(clearButton);

        // Create a label at the bottom to show status messages
        statusLabel = new JLabel("Click the button to load students", SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Add status label and buttons to bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(countLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add all components to the window layout
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // This method filters the table based on the name typed in the search field
    private void searchStudent() {
        String searchText = searchField.getText();
        if (searchText.trim().isEmpty()) {
            // If search field is empty show all students
            sorter.setRowFilter(null);
            statusLabel.setText("Showing all students");
        } else {
            // Filter table rows by name column
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 1));
            statusLabel.setText("Searching for: " + searchText);
        }
    }

    // This method connects to the API and loads the active students into the table
    private void fetchStudents() {
        try {
            // Reset search filter when loading new data
            searchField.setText("");
            sorter.setRowFilter(null);

            // Connect to the Spring Boot API endpoint for active students
            URL url = new URL("http://localhost:8080/api/students/active");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response line by line
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Clear old data from the table before adding new data
            tableModel.setRowCount(0);

            // Parse the JSON response and add each student as a row in the table
            String json = response.toString();
            String[] objects = json.replace("[", "").replace("]", "").split("\\},\\{");
            for (String obj : objects) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",");
                String id = fields[0].split(":")[1];
                String name = fields[1].split(":")[1].replace("\"", "");
                tableModel.addRow(new Object[]{id, name});
            }

            // Update the total student count label
            countLabel.setText("Total Students: " + objects.length);

            // Show how many students were loaded
            statusLabel.setText("Loaded " + objects.length + " students successfully!");

        } catch (Exception ex) {
            // Show a friendly error message if the server is not running
            statusLabel.setText("Error: Make sure the server is running!");
            JOptionPane.showMessageDialog(this,
                    "Could not connect to the server.\nPlease make sure Spring Boot is running.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to launch the GUI application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LMSGui().setVisible(true);
        });
    }
}