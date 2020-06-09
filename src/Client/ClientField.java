package Client;

import javafx.scene.layout.BorderRepeat;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientField extends JFrame {
    private int WIN_POS_X = 600;
    private int WIN_POS_Y = 400;
    private int WIN_WIDTH = 300;
    private int WIN_HEIGHT = 400;

    JPanel sendFrame;
    JPanel areasFrame;
    JButton sendBtn;
    JTextField sendFld;
    JTextArea chatArea;
    JTextArea clientArea;

    DataOutputStream out;

    public ClientField(DataOutputStream out) {
        this.out = out;
        setBounds(WIN_POS_X, WIN_POS_Y, WIN_WIDTH, WIN_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Interset");
        setResizable(false);
        sendFrame = new JPanel();
        areasFrame = new JPanel();
        sendBtn = new JButton("Send");
        sendFld = new JTextField("", 19);
        chatArea = new JTextArea(20, 16);
        clientArea = new JTextArea(20, 5);

        sendBtn.addActionListener(e -> {
            sendMsg();
        });
        sendFld.addActionListener(e -> {
            sendMsg();
        });

        chatArea.setLineWrap(true);
        clientArea.setLineWrap(true);
        sendFrame.add(sendFld, BorderLayout.CENTER);
        sendFrame.add(sendBtn, BorderLayout.EAST);
        add(sendFrame, BorderLayout.SOUTH);
        add(areasFrame, BorderLayout.CENTER);
        areasFrame.add(new JScrollPane(chatArea), BorderLayout.WEST);
        areasFrame.add(clientArea, BorderLayout.EAST);
        setVisible(true);
    }

    public void sendMsg(){
        try {
            if(!sendFld.getText().equals("")) out.writeUTF(sendFld.getText());
            sendFld.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
