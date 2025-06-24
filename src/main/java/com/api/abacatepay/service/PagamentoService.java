package com.api.abacatepay.service;


import com.api.abacatepay.dto.AbacatePayResponse;
import com.api.abacatepay.dto.PagamentoRequest;
import com.api.abacatepay.dto.ProdutoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class PagamentoService {

    private final WebClient webClient;

    public PagamentoService(@Value("${abacatepay.token}") String token) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.abacatepay.com/v1")
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public AbacatePayResponse criarPagamentoPix(PagamentoRequest req) {
        int valorCentavos = (int) (req.valor * 100);

        return webClient.post()
                .uri("/pixQrCode/create")
                .bodyValue(Map.of(
                        "amount", valorCentavos,
                        "expiresIn", 600,
                        "description", "Compra de: " + req.produto,
                        "customer", req.cliente
                ))
                .retrieve()
                .bodyToMono(AbacatePayResponse.class)
                .block();
    }

    public AbacatePayResponse verificarStatus(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pixQrCode/check")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(AbacatePayResponse.class)
                .block();
    }

    public Object listarPagamentos() {
        return webClient.get()
                .uri("/billing/list")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public Object criarFatura(PagamentoRequest req) {
        int valorCentavos = (int) (req.valor * 100);
        ProdutoDTO produto = new ProdutoDTO();
        produto.externalId = "prod_" + req.produto.replace(" ", "_");
        produto.name = req.produto;
        produto.description = "Compra unitária de " + req.produto;
        produto.quantity = 1;
        produto.price = valorCentavos;

        return webClient.post()
                .uri("/billing/create")
                .bodyValue(Map.of(
                        "frequency", "ONE_TIME",
                        "methods", List.of("PIX"),
                        "products", List.of(produto),
                        "returnUrl", "http://127.0.0.1:5501", //rota ficticia
                        "completionUrl", "http://127.0.0.1:5501/frontend/", 
                        "customer", req.cliente
                ))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res ->
                        res.bodyToMono(String.class).map(msg -> new RuntimeException("Erro da AbacatePay: " + msg))
                )
                .bodyToMono(Object.class)
                .block();
    }

    public Object listarClientes() {
        return webClient.get()
                .uri("/customer/list")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
