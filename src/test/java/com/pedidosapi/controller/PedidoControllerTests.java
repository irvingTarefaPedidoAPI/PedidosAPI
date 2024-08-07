package com.pedidosapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pedidosapi.model.Pedido;
import com.pedidosapi.service.PedidoService;

public class PedidoControllerTests {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();
    }

    @Test
    public void testCriarPedidos() throws Exception {
        Pedido pedido1 = new Pedido();
        pedido1.setNumeroControle("123456");
        pedido1.setDataCadastro(LocalDate.now());
        pedido1.setNome("Produto X");
        pedido1.setValor(BigDecimal.valueOf(100.00));
        pedido1.setQuantidade(10);
        pedido1.setValorTotal(BigDecimal.valueOf(900.00));

        Pedido pedido2 = new Pedido();
        pedido2.setNumeroControle("654321");
        pedido2.setDataCadastro(LocalDate.now());
        pedido2.setNome("Produto Y");
        pedido2.setValor(BigDecimal.valueOf(200.00));
        pedido2.setQuantidade(1);
        pedido2.setValorTotal(BigDecimal.valueOf(200.00));

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoService.criarPedidos(anyList())).thenReturn(pedidos);

        mockMvc.perform(post("/pedidos")
                .contentType("application/json")
                .content("[{\"numeroControle\":\"123456\",\"dataCadastro\":\"2024-08-06\",\"nome\":\"Produto X\",\"valor\":100.00,\"quantidade\":10,\"codigoCliente\":1},{\"numeroControle\":\"654321\",\"nome\":\"Produto Y\",\"valor\":200.00,\"codigoCliente\":2}]"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].numeroControle").value("123456"))
                .andExpect(jsonPath("$[1].numeroControle").value("654321"));

        verify(pedidoService, times(1)).criarPedidos(anyList());
    }

    @Test
    public void testGetPedidoByNumeroControle() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setNumeroControle("123456");
        pedido.setDataCadastro(LocalDate.now());
        pedido.setNome("Produto X");
        pedido.setValor(BigDecimal.valueOf(100.00));
        pedido.setQuantidade(10);
        pedido.setValorTotal(BigDecimal.valueOf(900.00));

        when(pedidoService.getPedidoByNumeroControle(anyString())).thenReturn(pedido);

        mockMvc.perform(get("/pedidos/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroControle").value("123456"));

        verify(pedidoService, times(1)).getPedidoByNumeroControle("123456");
    }

    @Test
    public void testGetPedidosByDataCadastro() throws Exception {
        LocalDate dataCadastro = LocalDate.of(2024, 8, 6);
        Pedido pedido = new Pedido();
        pedido.setDataCadastro(dataCadastro);
        pedido.setNumeroControle("123456");
        pedido.setNome("Produto X");
        pedido.setValor(BigDecimal.valueOf(100.00));
        pedido.setQuantidade(10);
        pedido.setValorTotal(BigDecimal.valueOf(900.00));

        List<Pedido> pedidos = Arrays.asList(pedido);

        when(pedidoService.getPedidosByDataCadastro(any(LocalDate.class))).thenReturn(pedidos);

        mockMvc.perform(get("/pedidos/dataCadastro").param("dataCadastro", "2024-08-06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dataCadastro").value("2024-08-06"))
                .andExpect(jsonPath("$[0].numeroControle").value("123456"));

        verify(pedidoService, times(1)).getPedidosByDataCadastro(any(LocalDate.class));
    }

    @Test
    public void testGetAllPedidos() throws Exception {
        Pedido pedido1 = new Pedido();
        pedido1.setNumeroControle("123456");
        pedido1.setDataCadastro(LocalDate.now());
        pedido1.setNome("Produto X");
        pedido1.setValor(BigDecimal.valueOf(100.00));
        pedido1.setQuantidade(10);
        pedido1.setValorTotal(BigDecimal.valueOf(900.00));

        Pedido pedido2 = new Pedido();
        pedido2.setNumeroControle("654321");
        pedido2.setDataCadastro(LocalDate.now());
        pedido2.setNome("Produto Y");
        pedido2.setValor(BigDecimal.valueOf(200.00));
        pedido2.setQuantidade(1);
        pedido2.setValorTotal(BigDecimal.valueOf(200.00));

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoService.getAllPedidos()).thenReturn(pedidos);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroControle").value("123456"))
                .andExpect(jsonPath("$[1].numeroControle").value("654321"));

        verify(pedidoService, times(1)).getAllPedidos();
    }
}
