import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Color primaryColor = new Color(66, 134, 244);
    private Color accentColor = new Color(33, 67, 122);

    public TestLogin() {
        setTitle("OWSB Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Logo/Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("OWSB System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(accentColor);
        titlePanel.add(titleLabel);
        
        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(userLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(passLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(passwordField, gbc);
        
        // Login Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginPanel.add(loginButton, gbc);
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        loginButton.addActionListener(e -> performLogin());
        
        // Add key listener for Enter key
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            List<String> users = readUserFile("users.txt");
            boolean authenticated = false;
            
            for (String user : users) {
                String[] parts = user.split(",");
                if (parts.length >= 6 && parts[1].equals(username) && parts[2].equals(password)) {
                    authenticated = true;
                    SwingUtilities.invokeLater(() -> {
                        if (parts[5].equals("Admin")) {
                            TestAdminDashboard dashboard = new TestAdminDashboard();
                            dashboard.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this,
                                "Non-admin login not implemented yet.",
                                "Feature Not Available",
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                    break;
                }
            }
            
            if (!authenticated) {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Try admin/admin123",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error during login: " + e.getMessage(),
                "System Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<String> readUserFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            TestLogin login = new TestLogin();
            login.setVisible(true);
        });
    }
} 