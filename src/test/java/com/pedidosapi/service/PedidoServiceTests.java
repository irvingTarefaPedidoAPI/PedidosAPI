package com.pedidosapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.pedidosapi.Exception.ResourceNotFoundException;
import com.pedidosapi.model.Pedido;
import com.pedidosapi.repository.PedidoRepository;

@SpringBootTest
public class PedidoServiceTests {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPedidoByNumeroControle() {
        Pedido pedido = new Pedido();
        pedido.setNumeroControle("123456");
        when(pedidoRepository.findByNumeroControle("123456")).thenReturn(Optional.of(pedido));

        Pedido foundPedido = pedidoService.getPedidoByNumeroControle("123456");

        assertEquals("123456", foundPedido.getNumeroControle());
    }

    @Test
    public void testGetPedidosByDataCadastro() {
        LocalDate dataCadastro = LocalDate.of(2024, 8, 6);
        Pedido pedido = new Pedido();
        pedido.setDataCadastro(dataCadastro);
        when(pedidoRepository.findByDataCadastro(dataCadastro)).thenReturn(new ArrayList<>(Arrays.asList(pedido)));

        List<Pedido> pedidos = pedidoService.getPedidosByDataCadastro(dataCadastro);

        assertFalse(pedidos.isEmpty());
        assertEquals(dataCadastro, pedidos.get(0).getDataCadastro());
    }

    @Test
    public void testGetAllPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        when(pedidoRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(pedido1, pedido2)));

        List<Pedido> pedidos = pedidoService.getAllPedidos();

        assertEquals(2, pedidos.size());
    }

    @Test
    public void testCriarPedidosComMaisDe10Pedidos() {
        List<Pedido> pedidos = new ArrayList<>(Arrays.asList(new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido()));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> 
            pedidoService.criarPedidos(pedidos)
        );

        assertEquals("Número máximo de pedidos é 10.", thrown.getMessage());
    }

    @Test
    public void testCriarPedidosComNumeroControleDuplicado() {
        Pedido pedido = new Pedido();
        pedido.setNumeroControle("123456");
        List<Pedido> pedidos = new ArrayList<>(Arrays.asList(pedido));

        when(pedidoRepository.findByNumeroControle("123456")).thenReturn(Optional.of(pedido));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> 
            pedidoService.criarPedidos(pedidos)
        );

        assertEquals("Número de controle já cadastrado: 123456", thrown.getMessage());
    }

    @Test
    public void testCriarPedidosSemDataCadastro() {
        Pedido pedido = new Pedido();
        pedido.setNumeroControle("654321");
        pedido.setDataCadastro(null);
        pedido.setQuantidade(1);
        pedido.setValor(BigDecimal.valueOf(100.00));
        List<Pedido> pedidos = new ArrayList<>(Arrays.asList(pedido));

        when(pedidoRepository.findByNumeroControle("654321")).thenReturn(Optional.empty());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido result = pedidoService.criarPedidos(pedidos).get(0);

        assertEquals(LocalDate.now(), result.getDataCadastro());
    }

    @Test
    public void testCriarPedidosComDesconto() {
        Pedido pedido = new Pedido();
        pedido.setNumeroControle("987654");
        pedido.setDataCadastro(LocalDate.now());
        pedido.setQuantidade(10);
        pedido.setValor(BigDecimal.valueOf(100.00));
        List<Pedido> pedidos = new ArrayList<>(Arrays.asList(pedido));

        when(pedidoRepository.findByNumeroControle("987654")).thenReturn(Optional.empty());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido result = pedidoService.criarPedidos(pedidos).get(0);

        assertEquals(BigDecimal.valueOf(900.00), result.getValorTotal());
    }
}
