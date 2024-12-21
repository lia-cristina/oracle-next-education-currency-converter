import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorDeMoedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        // Cores para o Terminal
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BACKGROUND_BLACK = "\u001B[40m";

        // Menu de opções
        System.out.println("---------------------------------------");
        System.out.println(ANSI_BACKGROUND_BLACK + "Bem-vindo ao Conversor de Moedas!" + ANSI_RESET);
        System.out.println("---------------------------------------");
        System.out.println("Escolha um modelo de conversão:");
        System.out.println(ANSI_YELLOW + "1. Dólar (USD) para Euro (EUR)" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "2. Euro (EUR) para Dólar (USD)" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "3. Libra Esterlina (GBP) para Dólar (USD)" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "4. Dólar (USD) para Libra Esterlina (GBP)" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "5. Yen Japonês (JPY) para Dólar (USD)" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "6. Dólar (USD) para Yen Japonês (JPY)" + ANSI_RESET);
        System.out.println("---------------------------------------");

        int opcao = scanner.nextInt();

        // Definição das moedas base e destino
        String baseMoeda = "USD"; // Default (caso não altere)
        String destinoMoeda = "EUR"; // Default (caso não altere)

        switch (opcao) {
            case 1:
                destinoMoeda = "EUR";
                break;
            case 2:
                baseMoeda = "EUR";
                destinoMoeda = "USD";
                break;
            case 3:
                baseMoeda = "GBP";
                destinoMoeda = "USD";
                break;
            case 4:
                baseMoeda = "USD";
                destinoMoeda = "GBP";
                break;
            case 5:
                baseMoeda = "JPY";
                destinoMoeda = "USD";
                break;
            case 6:
                baseMoeda = "USD";
                destinoMoeda = "JPY";
                break;
            default:
                System.out.println("---------------------------------------");
                System.out.println(ANSI_RED + "Opção inválida." + ANSI_RESET);
                return;
        }

        // Endereço da API
        String apiKey = "6ad1948cb40f246d212660a9";  // Verifique se sua chave está correta
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseMoeda;

        // Requisição à API
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificação da validade da resposta
            if (response.statusCode() == 200) {
                String respostaJson = response.body();
                JsonObject jsonResponse = JsonParser.parseString(respostaJson).getAsJsonObject();
                JsonObject taxas = jsonResponse.getAsJsonObject("conversion_rates");

                // Extraição da taxa de câmbio
                if (taxas.has(destinoMoeda)) {
                    double taxa = taxas.get(destinoMoeda).getAsDouble();
                    System.out.println("---------------------------------------");
                    System.out.println(ANSI_BACKGROUND_BLACK + ANSI_GREEN + "A taxa de câmbio de " + baseMoeda + " para " + destinoMoeda + " é: " + taxa + ANSI_RESET);

                    // Solicitação do valor a ser convertido
                    System.out.println("---------------------------------------");
                    System.out.println("Digite o valor a ser convertido:");
                    double valor = scanner.nextDouble();
                    double resultado = valor * taxa;
                    System.out.println("---------------------------------------");
                    System.out.println(ANSI_BACKGROUND_BLACK + ANSI_GREEN + valor + " " + baseMoeda + " = " + resultado + " " + destinoMoeda + ANSI_RESET);
                } else {
                    System.out.println("---------------------------------------");
                    System.out.println(ANSI_RED + "Não foi possível encontrar a taxa de câmbio para " + destinoMoeda + ANSI_RESET);
                }
            } else {
                System.out.println("---------------------------------------");
                System.out.println(ANSI_RED + "Erro na requisição. Código de status: " + response.statusCode() + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println("---------------------------------------");
            System.out.println(ANSI_RED + "Ocorreu um erro: " + e.getMessage() + ANSI_RESET);
        }
    }
}
