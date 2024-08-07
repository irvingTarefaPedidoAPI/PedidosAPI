package com.pedidosapi.controller;

import com.pedidosapi.Exception.ErrorResponse;
import com.pedidosapi.model.Pedido;
import com.pedidosapi.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<?> criarPedidos(@RequestBody List<Pedido> pedidos) {
        try {
            List<Pedido> novosPedidos = pedidoService.criarPedidos(pedidos);
            return new ResponseEntity<>(novosPedidos, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("Erro de Validação", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{numeroControle}")
    public ResponseEntity<Pedido> getPedidoByNumeroControle(@PathVariable String numeroControle) {
        Pedido pedido = pedidoService.getPedidoByNumeroControle(numeroControle);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/dataCadastro")
    public ResponseEntity<List<Pedido>> getPedidosByDataCadastro(@RequestParam("dataCadastro") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCadastro) {
        List<Pedido> pedidos = pedidoService.getPedidosByDataCadastro(dataCadastro);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        List<Pedido> pedidos = pedidoService.getAllPedidos();
        return ResponseEntity.ok(pedidos);
    }
}
