🔥 Advanced Simulated Network Firewall

🧠 Overview:

This project is a Computer Networks simulation of an Advanced Network Firewall implemented in Java.
It demonstrates packet filtering, rule-based access control, and traffic inspection — the core functions of modern firewalls.
The system allows users to define, remove, and view firewall rules, simulate packet transfers, and observe how the firewall filters network traffic.

🎯 Objective:

To design and implement a simulated firewall that showcases how rule-based filtering and routing protect networks from unauthorized or malicious access.

⚙️ Features:

1. Add and remove firewall rules (Allow or Block traffic)

2. Simulate packets with protocol, source, destination, and port

3. Real-time packet filtering and logging

4. View routing table

5. Interactive GUI using Java Swing

🧩 Project Structure:

├── Advance_SimulatedNetworkFirewall.java   # Core backend logic (Firewall, Router, Packet classes)
├── FirewallUI.java                         # Java Swing user interface
├── firewall_log.txt                        # Log file generated during execution
├── README.md                               # Project documentation

🖥️ Modules / Components:

---> Firewall Module – Handles rule creation, modification, and packet filtering.

---> Router Module – Forwards or blocks packets based on firewall decisions.

---> Packet Module – Defines packet properties like protocol, port, and IPs.

---> User Interface (UI) – Interactive GUI built with Java Swing for user control.

🧮 Workflow: 

---> User defines firewall rules (Allow/Block specific protocols and ports).

---> Packets are simulated with source, destination, protocol, and port values.

---> The firewall checks each packet against rules.

---> Router forwards allowed packets and blocks the rest.

---> Actions are logged in the output window and log file.

🧰 Tools & Technologies Used:

Programming Language: Java (JDK 17+ recommended)

Libraries: Java Swing, AWT

IDE: IntelliJ IDEA / Eclipse / VS Code

Platform: Windows / Linux

🚀 How to Run :

Compile both files:

javac -encoding UTF-8 Advance_SimulatedNetworkFirewall.java FirewallUI.java


Run the GUI:

java FirewallUI


Interact with the interface:

Add Rule: Define protocol and port to allow/block.

Send Packet: Input packet details to test filtering.

View Routing Table: Displays routing information.

Logs: Check console or firewall_log.txt.

🧪 Example Inputs

Add Rule: Protocol = HTTP, Port = 80, Action = Allow

Send Packet: Source = 192.168.0.2, Destination = 10.0.0.5, Protocol = HTTP, Port = 80

Expected Output:

✅ Added Rule: HTTP port 80 (ALLOW)
📦 Sent Packet: ALLOWED (Forwarded by Router)

📊 Results:

1. The firewall successfully simulates real-world packet filtering:

2. Packets are analyzed and routed based on security rules.

3. Unauthorized access attempts are blocked.

4. Log files maintain a record of all firewall actions.

🔒 Conclusion:

This project effectively demonstrates how network firewalls protect systems using rule-based packet filtering. It helps understand the internal mechanisms of network security at the transport and network layers.
