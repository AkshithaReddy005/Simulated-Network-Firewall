import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

// Packet class for tracking network traffic
class Packet {
    String time;
    String protocol;
    String sourceIP;
    String destIP;
    String port;
    String status;
    
    public Packet(String time, String protocol, String src, String dst, String port, String status) {
        this.time = time;
        this.protocol = protocol;
        this.sourceIP = src;
        this.destIP = dst;
        this.port = port;
        this.status = status;
    }
}

// Firewall Rule class
class FirewallRule {
    String protocol;
    String port;
    String action;
    
    public FirewallRule(String protocol, String port, String action) {
        this.protocol = protocol;
        this.port = port;
        this.action = action;
    }
}

// Firewall Simulator Engine
class FirewallSimulator {
    private List<FirewallRule> rules;
    private List<Packet> trafficLog;
    private int totalPackets;
    private int allowedPackets;
    private int blockedPackets;
    private SimpleDateFormat timeFormat;
    
    public FirewallSimulator() {
        rules = new ArrayList<>();
        trafficLog = new ArrayList<>();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        // Add default rule from image
        rules.add(new FirewallRule("HTTP", "60", "BLOCK"));
        
        totalPackets = 1;
        allowedPackets = 0;
        blockedPackets = 1;
    }
    
    public void addRule(String protocol, String port, String action) {
        rules.add(new FirewallRule(protocol, port, action));
    }
    
    public void removeRule(int index) {
        if (index >= 0 && index < rules.size()) {
            rules.remove(index);
        }
    }
    
    public List<FirewallRule> getRules() {
        return new ArrayList<>(rules);
    }
    
    public String sendPacket(String protocol, String srcIP, String destIP, String port) {
        String time = timeFormat.format(new Date());
        String status = "ALLOWED";
        
        // Check rules - exact match
        for (FirewallRule rule : rules) {
            if (rule.protocol.equalsIgnoreCase(protocol) && rule.port.equals(port)) {
                status = rule.action;
                break;
            }
        }
        
        Packet packet = new Packet(time, protocol, srcIP, destIP, port, status);
        trafficLog.add(0, packet); // Add to beginning for reverse chronological order
        
        totalPackets++;
        if (status.equals("ALLOWED")) {
            allowedPackets++;
        } else {
            blockedPackets++;
        }
        
        return status;
    }
    
    public List<Packet> getTrafficLog() {
        return new ArrayList<>(trafficLog);
    }
    
    public void clearTrafficLog() {
        trafficLog.clear();
        totalPackets = 0;
        allowedPackets = 0;
        blockedPackets = 0;
    }
    
    public int getTotalPackets() { return totalPackets; }
    public int getAllowedPackets() { return allowedPackets; }
    public int getBlockedPackets() { return blockedPackets; }
    public double getBlockRate() { 
        return totalPackets > 0 ? (blockedPackets * 100.0 / totalPackets) : 0; 
    }
}

// Main UI Class
public class NetworkFirewallSimulator extends JFrame {
    private FirewallSimulator firewall;
    private DefaultTableModel rulesModel;
    private DefaultTableModel trafficModel;
    private SimpleDateFormat timeFormat;
    
    // UI Components
    private JLabel totalLabel, allowedLabel, blockedLabel, blockRateLabel;
    private JTable rulesTable, trafficTable;
    private JTextArea logArea;
    
    public NetworkFirewallSimulator() {
        firewall = new FirewallSimulator();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        setTitle("Advanced Network Firewall Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
        
        initializeComponents();
        loadInitialData();
        updateStatistics();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        
        JLabel titleLabel = new JLabel("Advanced Network Firewall Simulator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 120, 212));
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Statistics Panel
        JPanel statsPanel = createStatisticsPanel();
        titlePanel.add(statsPanel, BorderLayout.EAST);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Center Panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        
        // Rules Tab
        JPanel rulesTab = createRulesTab();
        tabbedPane.addTab("Firewall Rules", rulesTab);
        
        // Traffic Tab
        JPanel trafficTab = createTrafficTab();
        tabbedPane.addTab("Live Traffic Monitor", trafficTab);
        
        // Log Tab
        JPanel logTab = createLogTab();
        tabbedPane.addTab("System Log", logTab);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Control Panel - ONLY BUTTONS, no input fields
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        totalLabel = new JLabel();
        allowedLabel = new JLabel();
        blockedLabel = new JLabel();
        blockRateLabel = new JLabel();
        
        panel.add(createStatPanel(totalLabel, "Totals"));
        panel.add(createStatPanel(allowedLabel, "Allowed"));
        panel.add(createStatPanel(blockedLabel, "Blocked"));
        panel.add(createStatPanel(blockRateLabel, "Block Rate"));
        
        return panel;
    }
    
    private JPanel createStatPanel(JLabel valueLabel, String title) {
        JPanel statPanel = new JPanel(new BorderLayout(0, 5));
        statPanel.setBackground(Color.WHITE);
        statPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(new Color(0, 120, 212));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        statPanel.add(valueLabel, BorderLayout.CENTER);
        statPanel.add(titleLabel, BorderLayout.SOUTH);
        
        return statPanel;
    }
    
    private JPanel createRulesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Table for rules
        String[] columns = {"Protocol", "Port", "Action"};
        rulesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        rulesTable = new JTable(rulesModel);
        rulesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rulesTable.setRowHeight(30);
        rulesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        rulesTable.getTableHeader().setBackground(new Color(240, 240, 240));
        rulesTable.getTableHeader().setForeground(Color.DARK_GRAY);
        rulesTable.setGridColor(new Color(220, 220, 220));
        rulesTable.setShowGrid(true);
        rulesTable.setSelectionBackground(new Color(0, 120, 212, 50));
        rulesTable.setSelectionForeground(Color.BLACK);
        
        // Center the text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < rulesTable.getColumnCount(); i++) {
            rulesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Custom renderer for action column
        rulesTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                if (value != null) {
                    String action = value.toString();
                    if (action.equals("ALLOW")) {
                        c.setForeground(new Color(46, 204, 113)); // Green
                    } else {
                        c.setForeground(new Color(231, 76, 60)); // Red
                    }
                }
                
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(rulesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTrafficTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Table for traffic - exactly as in image: Time, Protocol, Status
        String[] columns = {"Time", "Protocol", "Status"};
        trafficModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        trafficTable = new JTable(trafficModel);
        trafficTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        trafficTable.setRowHeight(28);
        trafficTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        trafficTable.getTableHeader().setBackground(new Color(240, 240, 240));
        trafficTable.getTableHeader().setForeground(Color.DARK_GRAY);
        trafficTable.setGridColor(new Color(220, 220, 220));
        trafficTable.setShowGrid(true);
        
        // Custom renderer for status column
        trafficTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                
                if (value != null) {
                    String status = value.toString();
                    if (status.contains("BLOCKED") || status.equals("BLOCK")) {
                        c.setForeground(Color.RED);
                        ((JLabel) c).setText("✗ BLOCKED");
                    } else if (status.contains("ALLOWED") || status.equals("ALLOW")) {
                        c.setForeground(new Color(46, 204, 113));
                        ((JLabel) c).setText("✓ ALLOWED");
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Center align other columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        trafficTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        trafficTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(trafficTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLogTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        logArea = new JTextArea();
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        logArea.setEditable(false);
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        // Button Panel - ONLY BUTTONS, no input fields
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        // Create buttons with modern style
        String[] buttonLabels = {
            "Add Rule", "Remove Rule", "View Rules", "Send Packet",
            "View Routing Table", "Simulate Attack", "Export Rules",
            "Statistics", "Clear Log"
        };
        
        for (String label : buttonLabels) {
            JButton button = createModernButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(label);
                }
            });
            buttonPanel.add(button);
        }
        
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(new Color(0, 120, 212));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 200), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(0, 100, 180));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(0, 120, 212));
            }
        });
        
        return button;
    }
    
    private void loadInitialData() {
        // Clear any existing data
        rulesModel.setRowCount(0);
        trafficModel.setRowCount(0);
        
        // Load initial rule from image
        rulesModel.addRow(new Object[]{"HTTP", "60", "BLOCK"});
        
        // Load initial traffic from image
        trafficModel.addRow(new Object[]{"09:03:08", "HTTP", "BLOCKED"});
        
        // Load initial log from image
        logArea.setText(""); // Clear first
        logArea.append("[09:03:24] System firewall simulator initialized\n");
        logArea.append("[09:03:49] Rule Added: HTTP on port 60 = BLOCK\n");
        logArea.append("[09:03:08] Packet Sent: 172.16.0.10 → 172.16.0.26 | HTTP:60 [BLOCKED]\n");
        logArea.append("[09:03:22] Rules List: Displaying all firewall rules\n");
        logArea.append("... HTTP | Port 60 | BLOCK\n");
        logArea.append("[09:03:28] Routing Table: Displaying routing configuration\n");
        logArea.append("... 10.0.0.0/8 - Internal Network\n");
        logArea.append("... 33.0.0.0/8 - Internet Gateway\n");
        logArea.append("... 0.0.0.0/0 - Default Route\n");
        logArea.setCaretPosition(0);
    }
    
    private void updateStatistics() {
        totalLabel.setText(String.valueOf(firewall.getTotalPackets()));
        allowedLabel.setText(String.valueOf(firewall.getAllowedPackets()));
        blockedLabel.setText(String.valueOf(firewall.getBlockedPackets()));
        blockRateLabel.setText(String.format("%.1f%%", firewall.getBlockRate()));
    }
    
    private void handleButtonClick(String action) {
        switch (action) {
            case "Add Rule":
                addRule();
                break;
            case "Remove Rule":
                removeRule();
                break;
            case "View Rules":
                viewRules();
                break;
            case "Send Packet":
                sendPacket();
                break;
            case "View Routing Table":
                viewRoutingTable();
                break;
            case "Simulate Attack":
                simulateAttack();
                break;
            case "Export Rules":
                exportRules();
                break;
            case "Statistics":
                showStatistics();
                break;
            case "Clear Log":
                clearLog();
                break;
        }
    }
    
    private void addRule() {
        // Create a custom dialog for adding rules
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JTextField protocolField = new JTextField("HTTP");
        JTextField portField = new JTextField("60");
        JComboBox<String> actionCombo = new JComboBox<>(new String[]{"ALLOW", "BLOCK"});
        actionCombo.setSelectedItem("BLOCK");
        
        panel.add(new JLabel("Protocol (e.g., HTTP):"));
        panel.add(protocolField);
        panel.add(new JLabel("Port (1-65535):"));
        panel.add(portField);
        panel.add(new JLabel("Action:"));
        panel.add(actionCombo);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Firewall Rule",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String protocol = protocolField.getText().trim().toUpperCase();
            String port = portField.getText().trim();
            String action = (String) actionCombo.getSelectedItem();
            
            if (protocol.isEmpty() || port.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate port
            try {
                int portNum = Integer.parseInt(port);
                if (portNum < 1 || portNum > 65535) {
                    JOptionPane.showMessageDialog(this, "Port must be between 1 and 65535!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid port number! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Add to model
            rulesModel.addRow(new Object[]{protocol, port, action});
            
            // Add to firewall engine
            firewall.addRule(protocol, port, action);
            
            // Log
            String time = timeFormat.format(new Date());
            logArea.append("[" + time + "] Rule Added: " + protocol + " on port " + port + " = " + action + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
            
            updateStatistics();
            JOptionPane.showMessageDialog(this, "Rule added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void removeRule() {
        int selectedRow = rulesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String protocol = (String) rulesModel.getValueAt(selectedRow, 0);
            String port = (String) rulesModel.getValueAt(selectedRow, 1);
            
            rulesModel.removeRow(selectedRow);
            firewall.removeRule(selectedRow);
            
            String time = timeFormat.format(new Date());
            logArea.append("[" + time + "] Rule Removed: " + protocol + " on port " + port + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
            
            updateStatistics();
            JOptionPane.showMessageDialog(this, "Rule removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please select a rule to remove.\nClick on a row in the Firewall Rules table.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void viewRules() {
        String time = timeFormat.format(new Date());
        logArea.append("\n[" + time + "] Rules List: Displaying all firewall rules\n");
        
        StringBuilder rulesText = new StringBuilder();
        if (rulesModel.getRowCount() == 0) {
            rulesText.append("... No rules configured\n");
        } else {
            for (int i = 0; i < rulesModel.getRowCount(); i++) {
                rulesText.append("... ")
                         .append(rulesModel.getValueAt(i, 0))
                         .append(" | Port ")
                         .append(rulesModel.getValueAt(i, 1))
                         .append(" | ")
                         .append(rulesModel.getValueAt(i, 2))
                         .append("\n");
            }
        }
        
        logArea.append(rulesText.toString());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private void sendPacket() {
        // Create a dialog for packet details
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        JTextField protocolField = new JTextField("HTTP");
        JTextField srcIPField = new JTextField("192.168.1.1");
        JTextField destIPField = new JTextField("10.0.0.1");
        JTextField portField = new JTextField("80");
        
        panel.add(new JLabel("Protocol (e.g., HTTP, TCP):"));
        panel.add(protocolField);
        panel.add(new JLabel("Source IP (e.g., 192.168.1.1):"));
        panel.add(srcIPField);
        panel.add(new JLabel("Destination IP (e.g., 10.0.0.1):"));
        panel.add(destIPField);
        panel.add(new JLabel("Port (1-65535):"));
        panel.add(portField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Send Packet",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String protocol = protocolField.getText().trim().toUpperCase();
            String srcIP = srcIPField.getText().trim();
            String destIP = destIPField.getText().trim();
            String port = portField.getText().trim();
            
            // Validate inputs
            if (protocol.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a protocol!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (srcIP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a source IP!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (destIP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a destination IP!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (port.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a port number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate IP addresses
            if (!isValidIP(srcIP)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Source IP address format!\nExample: 192.168.1.1", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidIP(destIP)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Destination IP address format!\nExample: 10.0.0.1", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate port
            try {
                int portNum = Integer.parseInt(port);
                if (portNum < 1 || portNum > 65535) {
                    JOptionPane.showMessageDialog(this, 
                        "Port must be between 1 and 65535!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid port number!\nPlease enter a numeric value.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Send packet through firewall
            String status = firewall.sendPacket(protocol, srcIP, destIP, port);
            
            // Add to traffic table
            String time = timeFormat.format(new Date());
            trafficModel.insertRow(0, new Object[]{
                time, 
                protocol, 
                status
            });
            
            // Log with full details
            logArea.append("[" + time + "] Packet Sent: " + srcIP + " → " + 
                         destIP + " | " + protocol + ":" + port + " [" + status + "]\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
            
            updateStatistics();
            
            // Show result in dialog
            String message = String.format("📦 Packet Sent Successfully!\n\n" +
                "Source:      %s\n" +
                "Destination: %s\n" +
                "Protocol:    %s\n" +
                "Port:        %s\n" +
                "Status:      %s",
                srcIP, destIP, protocol, port, status);
            
            int messageType = status.equals("ALLOWED") ? 
                JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
            
            String title = status.equals("ALLOWED") ? 
                "✓ Packet Allowed" : "✗ Packet Blocked";
            
            JOptionPane.showMessageDialog(this, message, title, messageType);
        }
    }
    
    private void viewRoutingTable() {
        String time = timeFormat.format(new Date());
        logArea.append("\n[" + time + "] Routing Table: Displaying routing configuration\n");
        logArea.append("... 10.0.0.0/8 - Internal Network\n");
        logArea.append("... 33.0.0.0/8 - Internet Gateway\n");
        logArea.append("... 0.0.0.0/0 - Default Route\n");
        logArea.append("... 172.16.0.0/16 - Corporate Network\n");
        logArea.append("... 192.168.0.0/16 - Private Network\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private void simulateAttack() {
        String[] attackTypes = {"DDoS Attack", "Port Scan", "Malicious Payload", "Brute Force"};
        String attack = (String) JOptionPane.showInputDialog(this,
            "Select Attack Type:", "Simulate Attack",
            JOptionPane.QUESTION_MESSAGE, null, attackTypes, attackTypes[0]);
        
        if (attack == null) return;
        
        String time = timeFormat.format(new Date());
        logArea.append("\n[" + time + "] Simulating " + attack + "\n");
        
        Random rand = new Random();
        int packetsToGenerate = 5;
        int newTrafficCount = 0;
        
        switch (attack) {
            case "DDoS Attack":
                for (int i = 0; i < packetsToGenerate; i++) {
                    String srcIP = "192.168." + rand.nextInt(256) + "." + rand.nextInt(256);
                    firewall.sendPacket("HTTP", srcIP, "172.16.0.26", "80");
                    newTrafficCount++;
                }
                break;
                
            case "Port Scan":
                int[] ports = {21, 22, 23, 25, 80, 443, 3389};
                for (int port : ports) {
                    firewall.sendPacket("TCP", "10.0.0." + rand.nextInt(256), 
                                      "172.16.0.26", String.valueOf(port));
                    newTrafficCount++;
                }
                break;
                
            case "Malicious Payload":
                for (int i = 0; i < packetsToGenerate; i++) {
                    firewall.sendPacket("HTTP", "203.0.113." + rand.nextInt(256), 
                                      "172.16.0.26", "8080");
                    newTrafficCount++;
                }
                break;
                
            case "Brute Force":
                for (int i = 0; i < packetsToGenerate; i++) {
                    firewall.sendPacket("SSH", "198.51.100." + rand.nextInt(256), 
                                      "172.16.0.26", "22");
                    newTrafficCount++;
                }
                break;
        }
        
        // Update display with simulated attacks
        List<Packet> newTraffic = firewall.getTrafficLog();
        for (int i = 0; i < Math.min(newTrafficCount, newTraffic.size()); i++) {
            Packet p = newTraffic.get(i);
            // Only add if not already in table (check by time)
            boolean exists = false;
            for (int j = 0; j < trafficModel.getRowCount(); j++) {
                if (trafficModel.getValueAt(j, 0).equals(p.time)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                trafficModel.insertRow(0, new Object[]{p.time, p.protocol, p.status});
            }
        }
        
        updateStatistics();
        logArea.append("[" + timeFormat.format(new Date()) + "] Attack simulation completed. Generated " + newTrafficCount + " packets.\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
        
        JOptionPane.showMessageDialog(this, 
            "Attack simulation completed!\nGenerated " + newTrafficCount + " packets.", 
            "Simulation Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportRules() {
        if (rulesModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No rules to export!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder exportText = new StringBuilder();
        exportText.append("═══════════════════════════════════════\n");
        exportText.append("       FIREWALL RULES EXPORT\n");
        exportText.append("═══════════════════════════════════════\n\n");
        exportText.append("Export Time: ").append(new Date()).append("\n\n");
        exportText.append("Protocol    Port    Action\n");
        exportText.append("═══════════════════════════════════════\n");
        
        for (int i = 0; i < rulesModel.getRowCount(); i++) {
            exportText.append(String.format("%-12s%-8s%-10s\n",
                rulesModel.getValueAt(i, 0),
                rulesModel.getValueAt(i, 1),
                rulesModel.getValueAt(i, 2)));
        }
        
        exportText.append("\n═══════════════════════════════════════\n");
        exportText.append("Total Rules: ").append(rulesModel.getRowCount()).append("\n");
        
        JTextArea textArea = new JTextArea(exportText.toString(), 20, 40);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(this, scrollPane, "Export Rules", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showStatistics() {
        String stats = String.format(
            "═══════════════════════════════════════\n" +
            "         FIREWALL STATISTICS\n" +
            "═══════════════════════════════════════\n\n" +
            "Total Packets:   %d\n" +
            "Allowed:         %d\n" +
            "Blocked:         %d\n" +
            "Block Rate:      %.1f%%\n\n" +
            "Active Rules:    %d\n" +
            "Traffic Log:     %d entries\n\n" +
            "═══════════════════════════════════════",
            firewall.getTotalPackets(),
            firewall.getAllowedPackets(),
            firewall.getBlockedPackets(),
            firewall.getBlockRate(),
            rulesModel.getRowCount(),
            trafficModel.getRowCount()
        );
        
        JTextArea textArea = new JTextArea(stats);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(this, scrollPane, "Statistics", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearLog() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Clear all log entries?", "Confirm Clear", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            logArea.setText("");
            String time = timeFormat.format(new Date());
            logArea.append("[" + time + "] Log cleared\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }
    
    private boolean isValidIP(String ip) {
        if (ip == null || ip.trim().isEmpty()) return false;
        
        String pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(pattern);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new NetworkFirewallSimulator();
        });
    }
}