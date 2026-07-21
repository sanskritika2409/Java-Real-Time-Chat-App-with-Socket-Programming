package gui;

import client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClientGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;

    private JButton sendButton;
    private JButton disconnectButton;

    private JLabel statusLabel;
    private DefaultListModel<String> userModel;
    private JList<String> userList;

    private ChatClient client;

    private String username;


    public ChatClientGUI() {

        setTitle("💬 Java Socket Chat Application");

        setSize(850,600);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();

        addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent e){

                if(client != null){
                    client.disconnect();
                }

            }

        });

    }



    private void createUI(){


        JPanel mainPanel = new JPanel(new BorderLayout(10,10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(10,10,10,10)
        );


        // TOP BAR

        JPanel topPanel = new JPanel(new BorderLayout());


        JLabel title =
                new JLabel("💬 Real Time Java Chat");

        title.setFont(
                new Font("Arial",Font.BOLD,22)
        );


        statusLabel =
                new JLabel("Disconnected");


        statusLabel.setFont(
                new Font("Arial",Font.BOLD,14)
        );


        topPanel.add(
                title,
                BorderLayout.WEST
        );


        topPanel.add(
                statusLabel,
                BorderLayout.EAST
        );


        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );



        // CHAT AREA


        chatArea = new JTextArea();

        chatArea.setEditable(false);

        chatArea.setLineWrap(true);

        chatArea.setWrapStyleWord(true);


        chatArea.setFont(
                new Font("Consolas",Font.PLAIN,15)
        );


        JScrollPane chatScroll =
                new JScrollPane(chatArea);



        // USER LIST


        userModel =
                new DefaultListModel<>();


        userList =
                new JList<>(userModel);


        userList.setFont(
                new Font("Arial",Font.PLAIN,14)
        );


        JScrollPane userScroll =
                new JScrollPane(userList);


        userScroll.setPreferredSize(
                new Dimension(170,0)
        );


        JPanel centerPanel =
                new JPanel(new BorderLayout());


        centerPanel.add(
                chatScroll,
                BorderLayout.CENTER
        );


        centerPanel.add(
                userScroll,
                BorderLayout.EAST
        );


        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        // BOTTOM AREA


        inputField =
                new JTextField();


        inputField.setFont(
                new Font("Arial",Font.PLAIN,15)
        );


        sendButton =
                new JButton("Send");


        disconnectButton =
                new JButton("Disconnect");


        JPanel buttonPanel =
                new JPanel(new BorderLayout(5,5));


        buttonPanel.add(
                inputField,
                BorderLayout.CENTER
        );


        JPanel buttons =
                new JPanel();


        buttons.add(sendButton);

        buttons.add(disconnectButton);



        buttonPanel.add(
                buttons,
                BorderLayout.EAST
        );


        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );



        add(mainPanel);



        sendButton.addActionListener(e ->
                sendMessage()
        );


        inputField.addActionListener(e ->
                sendMessage()
        );


        disconnectButton.addActionListener(e ->
                disconnect()
        );

    }
        private void startConnection(){


        String host =
                JOptionPane.showInputDialog(
                        this,
                        "Server IP",
                        "127.0.0.1"
                );


        if(host == null)
            return;



        String portText =
                JOptionPane.showInputDialog(
                        this,
                        "Port",
                        "12345"
                );


        if(portText == null)
            return;



        username =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Username"
                );


        if(username == null || username.trim().isEmpty())
            return;



        int port =
                Integer.parseInt(portText);



        client =
                new ChatClient(
                        host,
                        port
                );



        client.setMessageListener(
                message -> {

                    SwingUtilities.invokeLater(() -> {


                        chatArea.append(
                                message + "\n"
                        );


                        chatArea.setCaretPosition(
                                chatArea.getDocument().getLength()
                        );



                        updateUsers(message);


                    });

                }
        );



        boolean connected =
                client.connect(username);



        if(connected){


            statusLabel.setText(
                    "🟢 Connected : " + username
            );


            chatArea.append(
                    "----- Connected to Server -----\n"
            );


            userModel.addElement(username);


        }

        else{


            JOptionPane.showMessageDialog(
                    this,
                    "Connection Failed"
            );

        }


    }




    private void sendMessage(){


        String message =
                inputField.getText().trim();



        if(message.isEmpty())
            return;



        if(client != null && client.isConnected()){


            client.sendMessage(message);


            inputField.setText("");

        }

    }




    private void disconnect(){


        if(client != null){


            client.disconnect();


            statusLabel.setText(
                    "🔴 Disconnected"
            );


            chatArea.append(
                    "Disconnected from server\n"
            );

        }


    }




    private void updateUsers(String message){


        if(message.contains("joined")){


            String[] parts =
                    message.split(" ");


            if(parts.length > 1){


                String user =
                        parts[1];


                if(!userModel.contains(user)){

                    userModel.addElement(user);

                }

            }


        }



        if(message.contains("left")){


            String[] parts =
                    message.split(" ");


            if(parts.length > 1){


                userModel.removeElement(
                        parts[1]
                );

            }

        }


    }




    public static void main(String[] args){


        SwingUtilities.invokeLater(() -> {


            ChatClientGUI gui =
                    new ChatClientGUI();


            gui.setVisible(true);


            gui.startConnection();


        });


    }


}