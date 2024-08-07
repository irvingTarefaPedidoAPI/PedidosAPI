package com.pedidosapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedidosapi.model.Pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
    Optional<Pedido> findByNumeroControle(String numeroControle);
    
    List<Pedido> findByDataCadastro(LocalDate dataCadastro);
    
    List<Pedido> findAll();
    
}