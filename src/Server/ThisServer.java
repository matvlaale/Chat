package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ThisServer {
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public ThisServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isLoginBusy(String login) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public synchronized void privateMsg(String name, String getter, String msg) {
        int check = 0;
        for (ClientHandler o : clients)
            if (o.getName().equals(getter) || o.getName().equals(name)) {
                o.sendMsg(msg);
                check++;
            }
        if (check >= 2 || name.equals(getter)) return;
        for (ClientHandler o : clients)
            if (o.getName().equals(name)) o.sendMsg("Пользователь не в чате");
    }

    public synchronized void broadcastClientsList() {
        StringBuilder sb = new StringBuilder("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getName() + " ");
        }
        broadcastMsg(sb.toString());
    }

    public void register(String login, String pass, String nick){
        ((BaseAuthService) authService).register(login, pass, nick);
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
        broadcastClientsList();
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
        broadcastClientsList();
    }

}
