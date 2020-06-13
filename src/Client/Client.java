package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private ClientField field;
    private AuthField authField;
    private boolean authorization = false;

    public Client() {
        try {
            Socket socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            authField = new AuthField(out);
            setAuthorized(false);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String strFromServer = in.readUTF();
                            if (strFromServer.startsWith("/authok")) {
                                setAuthorized(true);
                                authField.dispose();
                                field = new ClientField(out);
                                field.chatArea.append("Добро пожаловать!\n");
                                break;
                            }
                            authField.systemMsgs.append(strFromServer + "\n");
                        }
                        while (true) {
                            String strFromServer = in.readUTF();
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                break;
                            }
                            if (strFromServer.startsWith("/clients")) {
                                field.clientArea.setText(strFromServer);
                            } else {
                                field.chatArea.append(strFromServer);
                                field.chatArea.append("\n");
                            }
                        }
                    } catch (EOFException e) {
                        authField.dispose();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAuthorized(boolean status) {
        authorization = status;
    }

    public static void main(String[] args) {
        new Client();
    }

    private boolean isAuthorization(){
        return authorization;
    }
}

