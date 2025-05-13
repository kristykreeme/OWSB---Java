package Admin;

import utils.Constants;
import utils.FileHandler;
import utils.AuditLogger;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.File;

public class SystemSettingsManager extends JFrame {
    private Map<String, JComponent> settingFields;
    private Properties systemSettings;
    private static final String[] BOOLEAN_SETTINGS = {
        "enable_user_registration",
        "enable_password_reset",
        "enable_audit_logging",
        "enable_export_features",
        "enable_auto_backup"
    };
    
    private static final String[] STRING_SETTINGS = {
        "company_name",
        "admin_email",
        "backup_directory",
        "export_directory",
        "date_format"
    };
    
    private static final String[] NUMBER_SETTINGS = {
        "session_timeout_minutes",
        "max_login_attempts",
        "password_expiry_days",
        "backup_retention_days",
        "max_export_rows"
    };

    public SystemSettingsManager() {
        setTitle("System Settings - OWSB");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        settingFields = new HashMap<>();
        systemSettings = new Properties();
        
        initializeComponents();
        loadSettings();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Create tabbed pane for different setting categories
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // General Settings Panel
        JPanel generalPanel = createSettingsPanel(
            new String[]{"company_name", "admin_email", "date_format"},
            "General Settings"
        );
        tabbedPane.addTab("General", generalPanel);
        
        // Security Settings Panel
        JPanel securityPanel = createSettingsPanel(
            new String[]{"enable_user_registration", "enable_password_reset", 
                        "session_timeout_minutes", "max_login_attempts", 
                        "password_expiry_days"},
            "Security Settings"
        );
        tabbedPane.addTab("Security", securityPanel);
        
        // Backup Settings Panel
        JPanel backupPanel = createSettingsPanel(
            new String[]{"enable_auto_backup", "backup_directory", 
                        "backup_retention_days"},
            "Backup Settings"
        );
        tabbedPane.addTab("Backup", backupPanel);
        
        // Export Settings Panel
        JPanel exportPanel = createSettingsPanel(
            new String[]{"enable_export_features", "export_directory", 
                        "max_export_rows"},
            "Export Settings"
        );
        tabbedPane.addTab("Export", exportPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Settings");
        JButton resetButton = new JButton("Reset to Defaults");
        
        saveButton.addActionListener(e -> saveSettings());
        resetButton.addActionListener(e -> resetToDefaults());
        
        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSettingsPanel(String[] settings, String title) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        for (String setting : settings) {
            gbc.gridy = row++;
            
            // Label
            gbc.gridx = 0;
            String labelText = formatSettingName(setting);
            panel.add(new JLabel(labelText + ":"), gbc);
            
            // Input field
            gbc.gridx = 1;
            JComponent field = createInputField(setting);
            settingFields.put(setting, field);
            panel.add(field, gbc);
        }
        
        // Add vertical glue
        gbc.gridy = row;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }

    private JComponent createInputField(String setting) {
        if (Arrays.asList(BOOLEAN_SETTINGS).contains(setting)) {
            return new JCheckBox();
        } else if (Arrays.asList(NUMBER_SETTINGS).contains(setting)) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
            spinner.setPreferredSize(new Dimension(100, 25));
            return spinner;
        } else {
            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(200, 25));
            if (setting.contains("directory")) {
                JPanel panel = new JPanel(new BorderLayout(5, 0));
                panel.add(textField, BorderLayout.CENTER);
                JButton browseButton = new JButton("Browse");
                browseButton.addActionListener(e -> browsePath(textField));
                panel.add(browseButton, BorderLayout.EAST);
                return panel;
            }
            return textField;
        }
    }

    private void browsePath(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Directory");
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private String formatSettingName(String setting) {
        String[] words = setting.split("_");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            formatted.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return formatted.toString().trim();
    }

    private void loadSettings() {
        try {
            // Load existing settings
            List<String> lines = FileHandler.readLines(Constants.SETTINGS_FILE);
            for (String line : lines) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    systemSettings.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
            
            // Apply settings to UI
            for (Map.Entry<String, JComponent> entry : settingFields.entrySet()) {
                String key = entry.getKey();
                JComponent component = entry.getValue();
                String value = systemSettings.getProperty(key, getDefaultValue(key));
                
                if (component instanceof JCheckBox) {
                    ((JCheckBox) component).setSelected(Boolean.parseBoolean(value));
                } else if (component instanceof JSpinner) {
                    ((JSpinner) component).setValue(Integer.parseInt(value));
                } else if (component instanceof JPanel) { // Directory field
                    ((JTextField) ((JPanel) component).getComponent(0)).setText(value);
                } else if (component instanceof JTextField) {
                    ((JTextField) component).setText(value);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading settings: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getDefaultValue(String key) {
        return switch (key) {
            case "company_name" -> "OWSB Company";
            case "admin_email" -> "admin@owsb.com";
            case "date_format" -> "yyyy-MM-dd HH:mm:ss";
            case "session_timeout_minutes" -> "30";
            case "max_login_attempts" -> "3";
            case "password_expiry_days" -> "90";
            case "backup_retention_days" -> "30";
            case "max_export_rows" -> "1000";
            case "backup_directory" -> new File(System.getProperty("user.home"), "owsb_backup").getPath();
            case "export_directory" -> new File(System.getProperty("user.home"), "owsb_exports").getPath();
            default -> Arrays.asList(BOOLEAN_SETTINGS).contains(key) ? "true" : "";
        };
    }

    private void saveSettings() {
        try {
            Properties newSettings = new Properties();
            
            // Collect all settings from UI
            for (Map.Entry<String, JComponent> entry : settingFields.entrySet()) {
                String key = entry.getKey();
                JComponent component = entry.getValue();
                String value;
                
                if (component instanceof JCheckBox) {
                    value = Boolean.toString(((JCheckBox) component).isSelected());
                } else if (component instanceof JSpinner) {
                    value = ((JSpinner) component).getValue().toString();
                } else if (component instanceof JPanel) { // Directory field
                    value = ((JTextField) ((JPanel) component).getComponent(0)).getText();
                } else if (component instanceof JTextField) {
                    value = ((JTextField) component).getText();
                } else {
                    continue;
                }
                
                newSettings.setProperty(key, value);
            }
            
            // Save to file
            List<String> lines = new ArrayList<>();
            for (String key : new TreeSet<>(newSettings.stringPropertyNames())) {
                lines.add(key + "=" + newSettings.getProperty(key));
            }
            
            FileHandler.writeToFile(Constants.SETTINGS_FILE, String.join("\n", lines), false);
            AuditLogger.log("A001", "UPDATE", "System settings updated");
            JOptionPane.showMessageDialog(this, "Settings saved successfully!");
            
            systemSettings = newSettings;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving settings: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetToDefaults() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset all settings to their default values?",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            for (Map.Entry<String, JComponent> entry : settingFields.entrySet()) {
                String key = entry.getKey();
                JComponent component = entry.getValue();
                String defaultValue = getDefaultValue(key);
                
                if (component instanceof JCheckBox) {
                    ((JCheckBox) component).setSelected(Boolean.parseBoolean(defaultValue));
                } else if (component instanceof JSpinner) {
                    ((JSpinner) component).setValue(Integer.parseInt(defaultValue));
                } else if (component instanceof JPanel) { // Directory field
                    ((JTextField) ((JPanel) component).getComponent(0)).setText(defaultValue);
                } else if (component instanceof JTextField) {
                    ((JTextField) component).setText(defaultValue);
                }
            }
        }
    }

    public static String getSetting(String key) {
        try {
            Properties props = new Properties();
            List<String> lines = FileHandler.readLines(Constants.SETTINGS_FILE);
            for (String line : lines) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    props.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
            return props.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 