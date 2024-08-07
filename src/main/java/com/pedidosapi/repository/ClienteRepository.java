package com.pedidosapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pedidosapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	
}