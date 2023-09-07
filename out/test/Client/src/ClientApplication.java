import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.google.gson.Gson;

public class ClientApplication {

    private static String CONFIG_FILE = "C:\\Users\\Сергей\\IdeaProjects\\TZServerClientSocket\\Client\\src\\resources\\config.properties";
    private static String SERVER_IP;
    private static int SERVER_PORT;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        loadConfig();
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Успешно подключено к серверу");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Введите ваш логин: ");
            String login = reader.readLine();
            writer.println(login);

            while (true) {
                System.out.println("Введите сообщение: ");
                String body = reader.readLine();
                String timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
                Message message = new Message(new User(login), body, timestamp);
                String jsonMessage = gson.toJson(message);
                writer.println(jsonMessage);

                if (body.equals("\\exit")) {
                    break;
                }

                //String response = reader.readLine();
                System.out.println("Сообщение доставлено");
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            System.err.println("Произошла ошибка в работе клиента");
        }
    }
    private static void loadConfig() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
            SERVER_IP = properties.getProperty("server.address");
            SERVER_PORT = Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке конфигурационного файла");
            System.out.println(SERVER_IP);
            System.out.println(SERVER_PORT);
            System.exit(1);
        }
    }
    public static class Message {
        private User user;
        private String body;
        private String timestamp;

        public Message(User user, String body, String timestamp) {
            this.user = user;
            this.body = body;
            this.timestamp = timestamp;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class User {
        private String login;

        public User(String login) {
            this.login = login;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }
}
