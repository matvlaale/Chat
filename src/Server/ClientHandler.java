package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class ClientHandler {
    private ThisServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String name;
    private boolean authorization;

    public String getName() {
        return name;
    }

    public ClientHandler(ThisServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {
                try {
                    authorization = false;
                    socket.setSoTimeout(5000);
                    authentication();
                    readMessages();
                } catch (SocketTimeoutException e) {
                    System.out.println("Аутентификация окончена");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null && !myServer.isLoginBusy(parts[1])) {
                    if (!myServer.isNickBusy(nick)) {
                        sendMsg("/authok " + nick);
                        authorization = true;
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        myServer.subscribe(this);
                        socket.setSoTimeout(0);
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль (" + str + ")");
                }
            }
            if (str.startsWith("/register")) {
                String[] parts = str.split("\\s");
                if (myServer.isNickBusy(parts[3]) || myServer.isLoginBusy(parts[1])) {
                    sendMsg("Логин или ник уже заняты");
                } else {
                    myServer.register(parts[1], parts[2], parts[3]);
                    name = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                    sendMsg("/authok " + name);
                    authorization = true;
                    myServer.broadcastMsg(parts[3] + socket.getRemoteSocketAddress() + " зашел в чат");
                    myServer.subscribe(this);
                    socket.setSoTimeout(0);
                    return;
                }
            }
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();
            System.out.println(name + ": " + strFromClient);
            if (strFromClient.equals("/end")) {
                out.writeUTF("/end");
                return;
            }
            if (strFromClient.startsWith("/w ")) {
                String[] parts = strFromClient.split("\\s");
                String getter = parts[1];
                strFromClient = strFromClient.substring(2 + 1 + getter.length() + 1);
                myServer.privateMsg(name, getter, name + "(личное): " + strFromClient);
            } else myServer.broadcastMsg(name + ": " + strFromClient);
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
