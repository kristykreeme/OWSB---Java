package Admin;

import utils.Constants;
import utils.FileHandler;
import utils.AuditLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class AuditLogViewer extends JFrame {
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterBox;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AuditLogViewer() {
        setTitle("Audit Log Viewer - OWSB");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        loadAuditLogs();
    }

    private void initializeComponents() {
        // Top Panel with Search and Filter
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search field
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 25));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterLogs(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterLogs(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterLogs(); }
        });

        // Filter dropdown
        filterBox = new JComboBox<>(new String[]{"All", "LOGIN", "LOGOUT", "CREATE", "UPDATE", "DELETE"});
        filterBox.addActionListener(e -> filterLogs());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Action Type: "));
        filterPanel.add(filterBox);

        topPanel.add(filterPanel, BorderLayout.CENTER);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Timestamp", "User ID", "Action", "Details"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        logTable = new JTable(tableModel);
        logTable.setAutoCreateRowSorter(true);
        
        // Set column widths
        logTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        logTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        logTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        logTable.getColumnModel().getColumn(3).setPreferredWidth(400);

        JScrollPane scrollPane = new JScrollPane(logTable);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton exportButton = new JButton("Export Logs");
        JButton clearButton = new JButton("Clear Logs");

        refreshButton.addActionListener(e -> loadAuditLogs());
        exportButton.addActionListener(e -> exportLogs());
        clearButton.addActionListener(e -> clearLogs());

        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(clearButton);

        // Add components to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAuditLogs() {
        tableModel.setRowCount(0);
        try {
            List<String> lines = FileHandler.readLines(Constants.AUDIT_LOG_FILE);
            for (String line : lines) {
                String[] parts = line.split(",", 4); // Split into max 4 parts
                if (parts.length >= 4) {
                    try {
                        long timestamp = Long.parseLong(parts[0]);
                        String formattedDate = dateFormat.format(new Date(timestamp));
                        tableModel.addRow(new Object[]{formattedDate, parts[1], parts[2], parts[3]});
                    } catch (NumberFormatException e) {
                        // Skip invalid entries
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading audit logs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterLogs() {
        String searchText = searchField.getText().toLowerCase();
        String selectedAction = (String) filterBox.getSelectedItem();
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        logTable.setRowSorter(sorter);
        
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                boolean matchesSearch = searchText.isEmpty() ||
                    entry.getStringValue(1).toLowerCase().contains(searchText) || // User ID
                    entry.getStringValue(3).toLowerCase().contains(searchText);   // Details
                
                boolean matchesAction = "All".equals(selectedAction) ||
                    entry.getStringValue(2).equals(selectedAction);
                
                return matchesSearch && matchesAction;
            }
        });
    }

    private void exportLogs() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "audit_logs_" + timestamp + ".csv";
            
            List<String> exportLines = new ArrayList<>();
            exportLines.add("Timestamp,User ID,Action,Details");
            
            for (int i = 0; i < logTable.getRowCount(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < logTable.getColumnCount(); j++) {
                    if (j > 0) line.append(",");
                    Object value = logTable.getValueAt(i, j);
                    line.append(value != null ? value.toString().replace(",", ";") : "");
                }
                exportLines.add(line.toString());
            }
            
            FileHandler.writeToFile(filename, String.join("\n", exportLines), false);
            JOptionPane.showMessageDialog(this, "Logs exported to " + filename);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting logs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearLogs() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all audit logs?\nThis action cannot be undone.",
                "Confirm Clear Logs",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                FileHandler.writeToFile(Constants.AUDIT_LOG_FILE, "", false);
                loadAuditLogs();
                JOptionPane.showMessageDialog(this, "Audit logs cleared successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error clearing logs: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 