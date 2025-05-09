import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;

public class PDFProcessor {
  private static final String MODEL_ID = "prebuilt-layout";
  private static final String FILE_NAME = "document.pdf"; // My TCC :)

  private static String getEnvOrThrow(String name) {
    String value = System.getenv(name);
    if (value == null || value.isBlank()) {
      throw new RuntimeException("Variável de ambiente não encontrada: " + name);
    }
    return value;
  }

  public static void main(String[] args) throws Exception {

    String ENDPOINT = getEnvOrThrow("AZURE_ENDPOINT");
    String API_KEY = getEnvOrThrow("AZURE_API_KEY");

    if (!ENDPOINT.endsWith("/")) {
      ENDPOINT += "/";
    }

    byte[] fileBytes = Files.readAllBytes(Paths.get(FILE_NAME));

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest analyzeRequest = HttpRequest.newBuilder()
        .uri(URI.create(ENDPOINT + "/formrecognizer/documentModels/" + MODEL_ID + ":analyze?api-version=2023-07-31"))
        .header("Content-Type", "application/pdf")
        .header("Ocp-Apim-Subscription-Key", API_KEY)
        .POST(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
        .timeout(Duration.ofMinutes(2))
        .build();

    HttpResponse<String> analyzeResponse = client.send(
        analyzeRequest,
        HttpResponse.BodyHandlers.ofString());

    System.out.println("Resposta inicial: " + analyzeResponse.statusCode());
    switch (analyzeResponse.statusCode()) {
      case 202 -> {
        System.out.println("Chave API aceita - Documento em processamento");
      }
      case 401 -> {
        System.err.println("ERRO: Chave API inválida ou expirada");
        System.exit(1);
      }
      case 429 -> {
        System.err.println("Limite de requisições excedido - Tente novamente mais tarde");
        System.exit(1);
      }
      default -> {
        System.err.println("Erro inesperado: " + analyzeResponse.statusCode());
        System.err.println("Resposta completa: " + analyzeResponse.body());
        System.exit(1);
      }
    }

    String operationLocation = analyzeResponse.headers()
        .firstValue("Operation-Location")
        .orElseThrow();

    HttpRequest resultRequest = HttpRequest.newBuilder()
        .uri(URI.create(operationLocation))
        .header("Ocp-Apim-Subscription-Key", API_KEY)
        .GET()
        .build();

    HttpResponse<String> resultResponse;
    do {
      Thread.sleep(5000);
      resultResponse = client.send(
          resultRequest,
          HttpResponse.BodyHandlers.ofString());
    } while (resultResponse.statusCode() != 200);

    saveResponseToFile(resultResponse.body(), "resultado.json");
    System.out.println("Análise concluída!");

  }

  private static void saveResponseToFile(String content, String filename) throws Exception {
    Files.writeString(
        Paths.get(filename),
        content,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }
}
