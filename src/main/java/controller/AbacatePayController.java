package controller;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api")

public class AbacatePayController {
    private final String BASE_URL = "https://api.abacatepay.com/v1";
    private final String AUTH_TOKEN = "Bearer abc_dev_BwWsjJrj1SrtjcyQdGRHUJux";

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/pagar")
    public ResponseEntity<?> criarCobranca(@RequestBody Map<String, Object> payload) {
        try {
            // Headers com token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", AUTH_TOKEN);

            // Montar dados do cliente
            Map<String, Object> cliente = (Map<String, Object>) payload.get("cliente");

            // Montar produto
            Map<String, Object> produto = new HashMap<>();
            produto.put("externalId", UUID.randomUUID().toString());
            produto.put("name", payload.get("produto"));
            produto.put("description", "Compra no site Frutas Prime");
            produto.put("quantity", 1);
            Number valor = (Number) payload.get("valor");
            produto.put("price", (int) (valor.doubleValue() * 100));
            // centavos

            // Montar cobrança
            Map<String, Object> cobranca = new HashMap<>();
            cobranca.put("frequency", "ONE_TIME");
            cobranca.put("methods", List.of("PIX"));
            cobranca.put("returnUrl", "http://localhost:5500/finalizar.html"); //retorno ficticio
            cobranca.put("completionUrl", "http://localhost:5500/sucesso.htm"); //retorno ficticio
            cobranca.put("products", List.of(produto));
            cobranca.put("customer", cliente); // cria o cliente se ele não existir

            // Enviar requisição
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(cobranca, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    BASE_URL + "/billing/create",
                    request,
                    Map.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("mensagem", "Erro ao processar cobrança");
            erro.put("erro", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
   }
}