package Server;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService{
    private List<Entry> entries;

    private class Entry {
        private String login;
        private String pass;
        private String nick;

        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }


    public BaseAuthService() {
        entries = new ArrayList<>();
        entries.add(new Entry("login1", "pass1", "Solnyško"));
        entries.add(new Entry("login2", "pass1", "Imperor"));
        entries.add(new Entry("login3", "pass1", "Lenin"));
    }

    public void register(String login, String pass, String nick){
        entries.add(new Entry(login, pass, nick));
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (Entry o : entries) {
            if (o.login.equals(login) && o.pass.equals(pass)) return o.nick;
        }
        return null;
    }
}
