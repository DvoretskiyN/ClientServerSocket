import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.*;

public class ServerApplication {
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен ");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключился: " + clientSocket);

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String login = reader.readLine();

                while (true) {
                    String jsonMessage = reader.readLine();
                    Message message = gson.fromJson(jsonMessage, Message.class);

                    if (message.getBody().equals("\\exit")) {
                        System.out.println("Клиент отключился: " + clientSocket);
                        break;
                    }

                    System.out.printf("%s %s: %s\n", "Принято сообщение от клиента: " + message.getUser().getLogin(), ", отправлено: " + message.getTimestamp(), ", текст: " + message.getBody());
                    writer.println("Сообщение получено");
                }

                writer.close();
                reader.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка в работе сервера");
        }
    }

    public static class Message {
        private User user;
        private String body;
        private String timestamp;

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

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }
}
