package org.example.OWM;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: <API_KEY> <BASE_URL>");
            System.exit(1);
        }
        String apiKey  = args[0];
        String baseUrl = args[1];

        // Main solo crea el controller, sin más lógica:
        Controller controller = new Controller(baseUrl, apiKey);

        // Y dispara la consulta:
        controller.fetchAndPrint();
    }
}