package Client;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.management.BufferPoolMXBean;

public class AuthField extends JFrame {
    private int WIN_POS_X = 600;
    private int WIN_POS_Y = 400;
    private int WIN_WIDTH = 300;
    private int WIN_HEIGHT = 400;

    DataOutputStream out;

    JPanel authPanel;
    JButton authBtn;
    JTextField login;
    JPasswordField password;
    JTextArea systemMsgs;

    public AuthField(DataOutputStream out) {
        this.out = out;
        setBounds(WIN_POS_X, WIN_POS_Y, WIN_WIDTH, WIN_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Authentication");
        setResizable(false);

        authPanel = new JPanel();
        authBtn = new JButton("login");
        login = new JTextField("login", 8);
        password = new JPasswordField("password", 8);
        systemMsgs = new JTextArea("Системные сообщения\n");
        systemMsgs.setLineWrap(true);

        authBtn.addActionListener(e -> {
            sendLogPas();
        });
        login.addActionListener(e -> {
            sendLogPas();
        });
        password.addActionListener(e -> {
            sendLogPas();
        });

        authPanel.add(login, BorderLayout.WEST);
        authPanel.add(password, BorderLayout.CENTER);
        authPanel.add(authBtn, BorderLayout.EAST);
        add(authPanel, BorderLayout.NORTH);
        add(systemMsgs);
        setVisible(true);
    }

    public void sendLogPas(){
        try {
            out.writeUTF("/auth " + login.getText() + " " + password.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}