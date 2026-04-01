package org.example.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// This class creates a simple GUI to display students enrolled in courses from the LMS API
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
        setTitle("LMS - Enrolled Students");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a title label at the top of the window
        titleLabel = new JLabel("Students Enrolled in Courses", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Create a search panel with a text field and search button
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(15);

        // Auto-search while typing using DocumentListener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchTable(); }
            public void removeUpdate(DocumentEvent e) { searchTable(); }
            public void changedUpdate(DocumentEvent e) { searchTable(); }
        });

        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTable());
        searchPanel.add(searchButton);

        // Create a top panel to hold title and search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Create a table with Student Name and Course Name columns
        tableModel = new DefaultTableModel(new String[]{"Student Name", "Course Name"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);

        // Set up sorter for search functionality
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Center both columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // Create a label to show total number of enrollments
        countLabel = new JLabel("Total Enrollments: 0", SwingConstants.CENTER);
        countLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create a button panel at the bottom
        JPanel buttonPanel = new JPanel();

        // Button to load enrollments from the API
        fetchButton = new JButton("Load Students");
        fetchButton.addActionListener(e -> fetchStudents());

        // Button to clear the table
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            // Clear all rows from the table
            tableModel.setRowCount(0);
            countLabel.setText("Total Enrollments: 0");
            statusLabel.setText("Table cleared");
            searchField.setText("");
        });

        // Add buttons to the panel
        buttonPanel.add(fetchButton);
        buttonPanel.add(clearButton);

        // Create a label at the bottom to show status messages
        statusLabel = new JLabel("Loading students...", SwingConstants.CENTER);
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

        // Automatically load enrollments when the app starts
        fetchStudents();
    }

    // This method filters the table by both student name and course name
    private void searchTable() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            // If search field is empty show all enrollments
            sorter.setRowFilter(null);
            statusLabel.setText("Showing all enrollments");
        } else {
            try {
                // Filter by both student name (column 0) and course name (column 1)
                List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 0));
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 1));
                sorter.setRowFilter(RowFilter.orFilter(filters));
                statusLabel.setText("Searching for: " + searchText);
            } catch (Exception e) {
                sorter.setRowFilter(null);
            }
        }
    }

    // This method connects to the API and loads all enrollments into the table
    private void fetchStudents() {
        try {
            searchField.setText("");
            sorter.setRowFilter(null);

            // Connect to the Spring Boot API endpoint for all enrollments
            URL url = new URL("http://localhost:8080/api/enrollments/list");
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

            String json = response.toString();

            // Split by each enrollment object using student field
            String[] objects = json.split("\"student\":");

            int count = 0;
            for (String obj : objects) {
                if (!obj.contains("\"name\":")) continue;

                String studentName = "";
                String courseName = "";

                // Extract student name - first name field
                int nameStart = obj.indexOf("\"name\":\"") + 8;
                int nameEnd = obj.indexOf("\"", nameStart);
                studentName = obj.substring(nameStart, nameEnd);

                // Extract course name - after course field
                if (obj.contains("\"course\":")) {
                    int courseStart = obj.indexOf("\"course\":");
                    String coursePart = obj.substring(courseStart);
                    int courseNameStart = coursePart.indexOf("\"name\":\"") + 8;
                    int courseNameEnd = coursePart.indexOf("\"", courseNameStart);
                    courseName = coursePart.substring(courseNameStart, courseNameEnd);
                }

                if (!studentName.isEmpty() && !courseName.isEmpty()) {
                    tableModel.addRow(new Object[]{studentName, courseName});
                    count++;
                }
            }

            // Update the total enrollment count label
            countLabel.setText("Total Enrollments: " + count);

            // Show how many enrollments were loaded
            statusLabel.setText("Loaded " + count + " enrollments successfully!");

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