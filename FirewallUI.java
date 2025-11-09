/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class FirewallUI extends JFrame {
    private Firewall firewall;
    private Router router;
    private DefaultTableModel rulesModel;
    private JTextArea outputArea;

    public FirewallUI() {
        firewall = new Firewall();
        router = new Router(firewall);

        setTitle("Advanced Network Firewall Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLayout(new BorderLayout());

        // --- Title ---
        JLabel title = new JLabel("🔥 Advanced Network Firewall Simulator 🔥", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // --- Table for Rules ---
        rulesModel = new DefaultTableModel(new Object[]{"Protocol", "Port", "Action"}, 0);
        JTable rulesTable = new JTable(rulesModel);
        JScrollPane tableScroll = new JScrollPane(rulesTable);

        // --- Buttons ---
        JButton addRuleBtn = new JButton("Add Rule");
        JButton removeRuleBtn = new JButton("Remove Rule");
        JButton sendPacketBtn = new JButton("Send Packet");
        JButton routingTableBtn = new JButton("View Routing Table");
        JButton clearOutputBtn = new JButton("Clear Output");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRuleBtn);
        buttonPanel.add(removeRuleBtn);
        buttonPanel.add(sendPacketBtn);
        buttonPanel.add(routingTableBtn);
        buttonPanel.add(clearOutputBtn);

        // --- Output Area ---
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        // --- Add components ---
        add(tableScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(outputScroll, BorderLayout.EAST);

        // --- Actions ---
        addRuleBtn.addActionListener(e -> addRule());
        removeRuleBtn.addActionListener(e -> removeRule());
        sendPacketBtn.addActionListener(e -> sendPacket());
        routingTableBtn.addActionListener(e -> showRoutingTable());
        clearOutputBtn.addActionListener(e -> outputArea.setText(""));

        setVisible(true);
    }

    private void addRule() {
        JTextField protoField = new JTextField();
        JTextField portField = new JTextField();
        String[] options = {"Allow", "Block"};
        JComboBox<String> actionBox = new JComboBox<>(options);

        Object[] fields = {
            "Protocol (e.g., HTTP, FTP):", protoField,
            "Port Number:", portField,
            "Action:", actionBox
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Firewall Rule", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String proto = protoField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());
                boolean allow = actionBox.getSelectedItem().equals("Allow");

                firewall.addRule(new FirewallRule(proto, port, allow));
                rulesModel.addRow(new Object[]{proto.toUpperCase(), port, allow ? "ALLOW" : "BLOCK"});
                outputArea.append("✅ Added Rule: " + proto.toUpperCase() + " port " + port + " (" + (allow ? "ALLOW" : "BLOCK") + ")\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
            }
        }
    }

    private void removeRule() {
        JTextField protoField = new JTextField();
        JTextField portField = new JTextField();

        Object[] fields = {
            "Protocol:", protoField,
            "Port Number:", portField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Remove Firewall Rule", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String proto = protoField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());
                firewall.removeRule(proto, port);
                outputArea.append("🗑️ Removed rule for " + proto + " port " + port + "\n");

                // Remove from table view
                for (int i = 0; i < rulesModel.getRowCount(); i++) {
                    if (rulesModel.getValueAt(i, 0).equals(proto.toUpperCase()) &&
                        (int) rulesModel.getValueAt(i, 1) == port) {
                        rulesModel.removeRow(i);
                        break;
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
            }
        }
    }

    private void sendPacket() {
        JTextField srcField = new JTextField();
        JTextField dstField = new JTextField();
        JTextField protoField = new JTextField();
        JTextField portField = new JTextField();

        Object[] fields = {
            "Source IP:", srcField,
            "Destination IP:", dstField,
            "Protocol:", protoField,
            "Port:", portField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Send Packet", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String src = srcField.getText().trim();
                String dst = dstField.getText().trim();
                String proto = protoField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());

                Packet packet = new Packet(src, dst, proto, port);
                router.forwardPacket(packet);
                outputArea.append("📦 Sent Packet: " + packet + "\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check all fields.");
            }
        }
    }

    private void showRoutingTable() {
        outputArea.append("\n🗺️ Routing Table:\n");
        router.routingTable.forEach((k, v) -> outputArea.append("   " + k + " → " + v + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FirewallUI::new);
    }
}*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class FirewallUI extends JFrame {
    private Firewall firewall;
    private Router router;
    private DefaultTableModel rulesModel;
    private JTextArea outputArea;

    public FirewallUI() {
        firewall = new Firewall();
        router = new Router(firewall);

        setTitle("Advanced Network Firewall Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // --- Title ---
        JLabel title = new JLabel("🔥 Advanced Network Firewall Simulator 🔥", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // --- Table for Rules ---
        rulesModel = new DefaultTableModel(new Object[]{"Protocol", "Port", "Action"}, 0);
        JTable rulesTable = new JTable(rulesModel);
        JScrollPane tableScroll = new JScrollPane(rulesTable);

        // --- Buttons ---
        JButton addRuleBtn = new JButton("Add Rule");
        JButton removeRuleBtn = new JButton("Remove Rule");
        JButton viewRulesBtn = new JButton("View Rules"); // 👈 Added button
        JButton sendPacketBtn = new JButton("Send Packet");
        JButton routingTableBtn = new JButton("View Routing Table");
        JButton clearOutputBtn = new JButton("Clear Output");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRuleBtn);
        buttonPanel.add(removeRuleBtn);
        buttonPanel.add(viewRulesBtn); // 👈 Add to panel
        buttonPanel.add(sendPacketBtn);
        buttonPanel.add(routingTableBtn);
        buttonPanel.add(clearOutputBtn);

        // --- Output Area ---
        outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        // --- Add components ---
        add(tableScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(outputScroll, BorderLayout.EAST);

        // --- Actions ---
        addRuleBtn.addActionListener(e -> addRule());
        removeRuleBtn.addActionListener(e -> removeRule());
        viewRulesBtn.addActionListener(e -> viewRules()); // 👈 Action
        sendPacketBtn.addActionListener(e -> sendPacket());
        routingTableBtn.addActionListener(e -> showRoutingTable());
        clearOutputBtn.addActionListener(e -> outputArea.setText(""));

        setVisible(true);
    }

    private void addRule() {
        JTextField protoField = new JTextField();
        JTextField portField = new JTextField();
        String[] options = {"Allow", "Block"};
        JComboBox<String> actionBox = new JComboBox<>(options);

        Object[] fields = {
            "Protocol (e.g., HTTP, FTP):", protoField,
            "Port Number:", portField,
            "Action:", actionBox
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Firewall Rule", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String proto = protoField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());
                boolean allow = actionBox.getSelectedItem().equals("Allow");

                firewall.addRule(new FirewallRule(proto, port, allow));
                rulesModel.addRow(new Object[]{proto.toUpperCase(), port, allow ? "ALLOW" : "BLOCK"});
                outputArea.append("✅ Added Rule: " + proto.toUpperCase() + " port " + port + " (" + (allow ? "ALLOW" : "BLOCK") + ")\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
            }
        }
    }

    private void removeRule() {
        JTextField protoField = new JTextField();
        JTextField portField = new JTextField();

        Object[] fields = {
            "Protocol:", protoField,
            "Port Number:", portField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Remove Firewall Rule", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String proto = protoField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());
                firewall.removeRule(proto, port);
                outputArea.append("🗑️ Removed rule for " + proto + " port " + port + "\n");

                // Remove from table view
                for (int i = 0; i < rulesModel.getRowCount(); i++) {
                    if (rulesModel.getValueAt(i, 0).equals(proto.toUpperCase()) &&
                        (int) rulesModel.getValueAt(i, 1) == port) {
                        rulesModel.removeRow(i);
                        break;
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
            }
        }
    }

    private void viewRules() {
        outputArea.append("\n📋 Current Firewall Rules:\n");
        if (rulesModel.getRowCount() == 0) {
            outputArea.append("   (No rules configured)\n");
            return;
        }
        for (int i = 0; i < rulesModel.getRowCount(); i++) {
            outputArea.append("   " + rulesModel.getValueAt(i, 0) + " | Port " +
                              rulesModel.getValueAt(i, 1) + " | " +
                              rulesModel.getValueAt(i, 2) + "\n");
        }
    }

    private void sendPacket() {
        JTextField srcField = new JTextField();
        JTextField dstField = new JTextField();
        JTextField protoField = new JTextField();
        JTextField portField = new JTextField();

        Object[] fields = {
            "Source IP:", srcField,
            "Destination IP:", dstField,
            "Protocol:", protoField,
            "Port:", portField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Send Packet", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String src = srcField.getText().trim();
                String dst = dstField.getText().trim();
                String proto = protoField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());

                Packet packet = new Packet(src, dst, proto, port);
                router.forwardPacket(packet);
                outputArea.append("📦 Sent Packet: " + packet + "\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check all fields.");
            }
        }
    }

    private void showRoutingTable() {
        outputArea.append("\n🗺️ Routing Table:\n");
        router.routingTable.forEach((k, v) -> outputArea.append("   " + k + " → " + v + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FirewallUI::new);
    }
}

