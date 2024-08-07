# PedidosAPI
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo INTEGER NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_controle VARCHAR(255) NOT NULL UNIQUE,
    data_cadastro DATE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    quantidade INTEGER DEFAULT 1,
    valor_total DECIMAL(10, 2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Inserir 10 clientes para teste
INSERT INTO cliente (codigo, nome) VALUES
(1, 'Cliente A'),
(2, 'Cliente B'),
(3, 'Cliente C'),
(4, 'Cliente D'),
(5, 'Cliente E'),
(6, 'Cliente F'),
(7, 'Cliente G'),
(8, 'Cliente H'),
(9, 'Cliente I'),
(10, 'Cliente J');

###########################################################################################################################################

1. Testar a Criação de Pedidos
URL: http://localhost:8080/pedidos

Método: POST

Cabeçalhos:

Content-Type: application/json

[
  {
    "numeroControle": "123456",
    "dataCadastro": "2024-08-06",
    "nome": "Produto X",
    "valor": 100.00,
    "quantidade": 10,
    "cliente": {
      "id": 1
    }
  },
  {
    "numeroControle": "654321",
    "nome": "Produto Y",
    "valor": 200.00,
    "cliente": {
      "id": 2
    }
  }
]

Content-Type: application/xml

<List>
    <item>
        <numeroControle>123456</numeroControle>
        <dataCadastro>2024-08-06</dataCadastro>
        <nome>Produto X</nome>
        <valor>100.00</valor>
        <quantidade>10</quantidade>
        <cliente>
            <id>1</id>
            <nome>Cliente 1</nome>
        </cliente>
    </item>
    <item>
        <numeroControle>654321</numeroControle>
        <nome>Produto Y</nome>
        <valor>200.00</valor>
        <cliente>
            <id>2</id>
            <nome>Cliente 2</nome>
        </cliente>
    </item>
</List>

###########################################################################################################################################
2. Testar a Consulta de Pedidos por Número
URL: http://localhost:8080/pedidos/{numeroControle}
Método: GET
Cabeçalhos:
Accept: application/json
Nota: Substitua {numeroControle} pelo número de controle do pedido que você deseja consultar.
###########################################################################################################################################

3. Testar a Consulta de Pedidos por Data
URL: http://localhost:8080/pedidos/data/{dataCadastro}
Método: GET
Cabeçalhos:
Accept: application/json
Nota: Substitua {dataCadastro} pela data de cadastro no formato yyyy-MM-dd.
###########################################################################################################################################

4. Testar a Consulta de Todos os Pedidos
URL: http://localhost:8080/pedidos
Método: GET
Cabeçalhos:
Accept: application/json
###########################################################################################################################################
