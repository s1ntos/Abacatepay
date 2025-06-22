package com.api.abacatepay.controller;

import com.api.abacatepay.dto.PagamentoRequest;
import com.api.abacatepay.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/pagar")
    public Object pagar(@RequestBody PagamentoRequest req) {
        return pagamentoService.criarFatura(req);
    }

    @GetMapping("/status/{id}")
    public Object status(@PathVariable String id) {
        return pagamentoService.verificarStatus(id);
    }

    @GetMapping("/pagamentos")
    public Object listarPagamentos() {
        return pagamentoService.listarPagamentos();
    }

    @GetMapping("/clientes")
    public Object listarClientes() {
        return pagamentoService.listarClientes();
    }
}


