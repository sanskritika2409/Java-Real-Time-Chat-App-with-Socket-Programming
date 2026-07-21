# 💬 Java Socket Programming Chat Application

## 📌 Project Title
**Real-Time Multi-Client Chat Application using Java Socket Programming**

---

# 📖 Project Overview

This project is a real-time chat application built using **Java Socket Programming** and **Multithreading**.

The application follows a **Client-Server Architecture** where multiple clients connect to a central server and exchange messages in real time.

Users can:
- Connect using a username
- Send public messages
- Receive messages instantly
- See join/leave notifications
- Communicate with multiple users simultaneously

---

# 🎯 Problem Statement

Modern applications require real-time communication between multiple users.

This project demonstrates how messaging platforms work internally using:

- Network communication
- TCP sockets
- Client-server architecture
- Multithreading
- Concurrent user handling

---

# 🚀 Features

## Core Features

✅ Multi-client communication  
✅ Real-time messaging  
✅ Username based connection  
✅ Server-client architecture  
✅ Thread-based client handling  
✅ Join notification  
✅ Leave notification  
✅ Graceful disconnect  
✅ Error handling  


## Advanced Features

✅ Java Swing GUI  
✅ Online users panel  
✅ Message timestamps  
✅ Private messaging support  
✅ Chat logging support  
✅ Multiple clients simulation  


---

# 🏗️ Architecture


             Client 1
                |
                |

Client 2 ---- Java Chat Server ---- Client 3
|
|
Client 4

Client Connection
|
↓
ServerSocket
|
↓
Client Handler Thread
|
↓
Message Processing
|
↓
Broadcasting



---

# 🛠️ Technologies Used


| Technology | Purpose |
|---|---|
| Java | Programming Language |
| Socket Programming | Network Communication |
| TCP/IP | Data Transfer |
| Multithreading | Multiple Client Handling |
| Swing | GUI Development |
| Collections | User Management |
| Exception Handling | Error Control |


---

# 📂 Project Structure



Java-Chat-App-Socket-Programming

│
├── src
│
├── server
│ ├── ChatServer.java
│ └── ClientHandler.java
│
├── client
│ └── ChatClient.java
│
├── common
│ └── ChatLogger.java
│
├── gui
│ └── ChatClientGUI.java
│
├── logs
│
├── screenshots
│
├── docs
│
└── README.md



---

# ⚙️ How It Works


## Server Side

1. Server creates a ServerSocket.
2. Server waits for client connections.
3. Every client gets a separate thread.
4. Server receives messages.
5. Server broadcasts messages to connected users.


## Client Side

1. Client connects using Socket.
2. User enters username.
3. Client sends messages.
4. Client receives messages asynchronously.


---

# ▶️ How To Run


## Step 1: Open Terminal

Go to source folder:


cd src



---

## Step 2: Compile Project



javac server*.java client*.java common*.java gui*.java



---

## Step 3: Start Server



java server.ChatServer



Output:


Starting Chat Server on port 12345

Server bound successfully.
Waiting for clients...



---

## Step 4: Start Client


Open another terminal:


java gui.ChatClientGUI



Enter:


Server IP:
127.0.0.1

Port:
12345

Username:
Sanskritika



---

# 💻 Sample Output


Server:


[INFO] Starting Chat Server

[INFO] Registered client:
Sanskritika

[INFO] Registered client:
Priyanshi



Client:


----- Connected to Server -----

Priyanshi joined the chat

Hello everyone!

Sanskritika:
Hello Priyanshi



---

# 🧵 Multithreading Implementation


The server uses multiple threads to handle users.


Example:



Main Server Thread

    |
    |

Client Handler Thread 1
Client Handler Thread 2
Client Handler Thread 3



Benefits:

- Multiple users can chat simultaneously
- One user does not block another
- Better performance


---

# 🌐 Socket Programming Concepts Used


## Socket

Represents communication endpoint between client and server.


## ServerSocket

Used by server to listen for incoming connections.


## InputStream

Receives data.


## OutputStream

Sends data.


## Thread

Handles multiple clients simultaneously.


---

# 🧪 Testing


Test cases performed:


| Test | Result |
|-|-|
| Single client connection | Passed |
| Multiple clients | Passed |
| Message broadcasting | Passed |
| Client disconnect | Passed |
| Server restart | Passed |
| Invalid connection | Handled |


---

# 📸 Screenshots Checklist


Add screenshots:


- Server running
- Client 1 connected
- Client 2 connected
- Multiple user chat
- GUI interface
- Online users panel
- GitHub repository


---

# 🔮 Future Improvements


Future upgrades:


- User authentication
- Database storage
- End-to-end encryption
- File sharing
- Multiple chat rooms
- Cloud deployment
- Mobile application
- Message notifications


---

# 📚 Learning Outcomes


Through this project I learned:


- Java networking
- TCP communication
- Socket programming
- Multithreading
- GUI development
- Client-server architecture
- Exception handling
- Real-time application development


---

# 💼 Industry Relevance


This project demonstrates concepts used in:


- Messaging applications
- Customer support systems
- Multiplayer games
- Collaboration platforms
- Real-time notification systems


---

# 👩‍💻 Author


**Sanskritika Awasthi**

B.Tech Computer Science Engineering  
Specialization: Data Science & Artificial Intelligence