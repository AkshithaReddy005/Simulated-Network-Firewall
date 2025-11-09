import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

class Packet {
    String sourceIP;
    String destIP;
    String protocol;
    int port;

    Packet(String sourceIP, String destIP, String protocol, int port) {
        this.sourceIP = sourceIP;
        this.destIP = destIP;
        this.protocol = protocol.toUpperCase();
        this.port = port;
    }

    @Override
    public String toString() {
        return protocol + ":" + port + " | " + sourceIP + " → " + destIP;
    }
}

class FirewallRule {
    String protocol;
    int port;
    boolean allow;

    FirewallRule(String protocol, int port, boolean allow) {
        this.protocol = protocol.toUpperCase();
        this.port = port;
        this.allow = allow;
    }

    @Override
    public String toString() {
        return (allow ? "ALLOW" : "BLOCK") + " " + protocol + " (port " + port + ")";
    }
}

class Firewall {
    private List<FirewallRule> rules = new ArrayList<>();
    private File logFile = new File("firewall_log1.txt");
    private Map<String, Integer> threatCount = new HashMap<>();

    Firewall() {
        try {
            if (!logFile.exists()) logFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating log file.");
        }
    }

    void addRule(FirewallRule rule) {
        rules.add(rule);
        System.out.println("✅ Rule added: " + rule);
    }

    void removeRule(String protocol, int port) {
        boolean removed = rules.removeIf(r -> r.protocol.equalsIgnoreCase(protocol) && r.port == port);
        if (removed) System.out.println("🗑️ Rule for " + protocol + " port " + port + " removed.");
        else System.out.println("⚠️ No such rule found.");
    }

    void viewRules() {
        if (rules.isEmpty()) {
            System.out.println("🚫 No firewall rules set.");
            return;
        }
        System.out.println("\n🔒 Current Firewall Rules:");
        for (FirewallRule r : rules)
            System.out.println("   ➤ " + r);
    }

    boolean isAllowed(Packet packet) {
        for (FirewallRule rule : rules) {
            if (rule.protocol.equalsIgnoreCase(packet.protocol) && rule.port == packet.port) {
                log(packet, rule.allow);
                detectThreat(packet, rule.allow);
                return rule.allow;
            }
        }
        log(packet, true);
        return true; // default allow
    }

    void detectThreat(Packet packet, boolean allowed) {
        if (!allowed) {
            String key = packet.sourceIP;
            threatCount.put(key, threatCount.getOrDefault(key, 0) + 1);
            if (threatCount.get(key) >= 3) {
                System.out.println("🚨 ALERT: Repeated blocked attempts detected from " + key);
            }
        }
    }

    void log(Packet packet, boolean allowed) {
        String status = allowed ? "ALLOWED" : "BLOCKED";
        String entry = LocalDateTime.now() + " | " + packet + " | " + status;
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(entry + "\n");
        } catch (IOException e) {
            System.out.println("Logging error: " + e.getMessage());
        }
    }
}

class Router {
    Firewall firewall;
    Map<String, String> routingTable = new HashMap<>();

    Router(Firewall firewall) {
        this.firewall = firewall;
        routingTable.put("192.168.1.0/24", "LAN");
        routingTable.put("10.0.0.0/8", "Internal Network");
        routingTable.put("0.0.0.0/0", "Internet Gateway");
    }

    void showRoutingTable() {
        System.out.println("\n🗺️ Routing Table:");
        routingTable.forEach((k, v) -> System.out.println("   " + k + " → " + v));
    }

    void forwardPacket(Packet packet) {
        System.out.print("📦 Packet [" + packet + "] -> ");
        if (!isValidIP(packet.sourceIP) || !isValidIP(packet.destIP)) {
            System.out.println("\u001B[31mInvalid IP Address!\u001B[0m");
            return;
        }

        if (firewall.isAllowed(packet)) {
            System.out.println("\u001B[32m✅ Allowed and Routed via "
                    + getRoute(packet.destIP) + "\u001B[0m");
        } else {
            System.out.println("\u001B[31m❌ Blocked by Firewall\u001B[0m");
        }
    }

    String getRoute(String destIP) {
        if (destIP.startsWith("192.168")) return "LAN";
        else if (destIP.startsWith("10.")) return "Internal Network";
        else return "Internet Gateway";
    }

    boolean isValidIP(String ip) {
        return Pattern.matches(
                "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\."
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\."
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\."
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", ip);
    }
}

public class Advance_SimulatedNetworkFirewall {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Firewall firewall = new Firewall();
        Router router = new Router(firewall);

        System.out.println("========== ADVANCED NETWORK FIREWALL SIMULATOR ==========");
        System.out.println("             (Developed by Amruta Kajuluri)\n");

        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Firewall Rule");
            System.out.println("2. Remove Firewall Rule");
            System.out.println("3. View Firewall Rules");
            System.out.println("4. View Routing Table");
            System.out.println("5. Send Packet");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addRule(firewall);
                case 2 -> removeRule(firewall);
                case 3 -> firewall.viewRules();
                case 4 -> router.showRoutingTable();
                case 5 -> sendPacket(router);
                case 6 -> {
                    System.out.println("\n📁 Logs saved in 'firewall_log1.txt'");
                    System.out.println("🚪 Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void addRule(Firewall firewall) {
        System.out.print("Enter protocol (e.g. HTTP, FTP, SSH): ");
        String proto = sc.nextLine();
        System.out.print("Enter port number (e.g. 80 for HTTP, 21 for FTP): ");
        int port = sc.nextInt();
        sc.nextLine();
        System.out.print("Allow or Block this protocol? (A/B): ");
        String action = sc.nextLine().trim().toUpperCase();
        boolean allow = action.equals("A");
        firewall.addRule(new FirewallRule(proto, port, allow));
    }

    static void removeRule(Firewall firewall) {
        System.out.print("Enter protocol: ");
        String proto = sc.nextLine();
        System.out.print("Enter port number: ");
        int port = sc.nextInt();
        sc.nextLine();
        firewall.removeRule(proto, port);
    }

    static void sendPacket(Router router) {
        System.out.print("Enter source IP: ");
        String src = sc.nextLine();
        System.out.print("Enter destination IP: ");
        String dest = sc.nextLine();
        System.out.print("Enter protocol (HTTP/FTP/SSH/etc): ");
        String proto = sc.nextLine();
        System.out.print("Enter port number: ");
        int port = sc.nextInt();
        sc.nextLine();

        Packet packet = new Packet(src, dest, proto, port);
        router.forwardPacket(packet);
    }
}
