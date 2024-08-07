package com.pedidosapi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedidosapi.Exception.ResourceNotFoundException;
import com.pedidosapi.model.Pedido;
import com.pedidosapi.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    
    public Pedido getPedidoByNumeroControle(String numeroControle) {
        return pedidoRepository.findByNumeroControle(numeroControle)
        		.orElseThrow(() -> new ResourceNotFoundException("Pedido com número de controle " + numeroControle + " não encontrado."));
    }
    
    public List<Pedido> getPedidosByDataCadastro(LocalDate dataCadastro) {
        return pedidoRepository.findByDataCadastro(dataCadastro);
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> criarPedidos(List<Pedido> pedidos) {
        if (pedidos.size() > 10) {
            throw new IllegalArgumentException("Número máximo de pedidos é 10.");
        }

        for (Pedido pedido : pedidos) {
        	 if (pedidoRepository.findByNumeroControle(pedido.getNumeroControle()).isPresent()) {
                throw new IllegalArgumentException("Número de controle já cadastrado: " + pedido.getNumeroControle());
            }

            if (pedido.getDataCadastro() == null) {
                pedido.setDataCadastro(LocalDate.now());
            }

            if (pedido.getQuantidade() == null) {
                pedido.setQuantidade(1);
            }

            BigDecimal valorTotal = pedido.getValor().multiply(BigDecimal.valueOf(pedido.getQuantidade()));
            if (pedido.getQuantidade() >= 10) {
                valorTotal = valorTotal.multiply(BigDecimal.valueOf(0.9)); 
            } else if (pedido.getQuantidade() > 5) {
                valorTotal = valorTotal.multiply(BigDecimal.valueOf(0.95));
            }
            pedido.setValorTotal(valorTotal);

            pedidoRepository.save(pedido);
        }

        return pedidos;
    }
}