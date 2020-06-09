package Client;

import javafx.stage.Stage;

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
    JButton regBtn;
    JTextField login;
    JPasswordField password;
    JTextArea systemMsgs;
    JTextField nick;

    public AuthField(DataOutputStream out) {
        this.out = out;
        setBounds(WIN_POS_X, WIN_POS_Y, WIN_WIDTH, WIN_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Authentication");
        setResizable(false);

        authPanel = new JPanel();
        authBtn = new JButton("login");
        regBtn = new JButton("reg");
        login = new JTextField("login", 6);
        password = new JPasswordField("password", 6);
        systemMsgs = new JTextArea("Системные сообщения\n");
        systemMsgs.setLineWrap(true);
        nick = new JTextField("nick");

        authBtn.addActionListener(e -> {
            sendLogPas();
        });
        login.addActionListener(e -> {
            sendLogPas();
        });
        password.addActionListener(e -> {
            sendLogPas();
        });
        regBtn.addActionListener(e -> {
            try {
                register();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        authPanel.add(login, BorderLayout.WEST);
        authPanel.add(password, BorderLayout.CENTER);
        authPanel.add(authBtn, BorderLayout.CENTER);
        authPanel.add(regBtn, BorderLayout.EAST);
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

    public void register() throws IOException {
       /*if(nick.getText().equals("nick")){
           add(nick);
           nick.setText(login.getText());
       }
       else*/
       nick.setText(login.getText());
       String str = "/register " + login.getText() + " " + password.getText() + " " + nick.getText();
       out.writeUTF("/register " + login.getText() + " " + password.getText() + " " + nick.getText());
        System.out.println("register try " + str);
    }
}