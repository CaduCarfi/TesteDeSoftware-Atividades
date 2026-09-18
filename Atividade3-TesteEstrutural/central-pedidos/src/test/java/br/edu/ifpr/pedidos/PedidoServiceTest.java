package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
                () -> assertEquals("PAGO", resultado.status()),
                () -> assertEquals(10_000L, resultado.subtotalCentavos()),
                () -> assertEquals(0L, resultado.descontoCentavos()),
                () -> assertEquals(1_200L, resultado.freteCentavos()),
                () -> assertEquals(11_200L, resultado.totalCentavos()),
                () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void construirServicoComProcessadorNuloLancaExcecao() {
        assertThrows(NullPointerException.class, () -> new PedidoService(null));
    }

    @Test
    void referenciasNulasLancamNullPointerException() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("SKU-1", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);

        assertThrows(NullPointerException.class, () -> service.fechar(null, cliente));
        assertThrows(NullPointerException.class, () -> service.fechar(pedido, null));
    }

    @Test
    void clienteBloqueadoRetornaBloqueadoComValoresZeradosSemAvaliarItensOuCupom() {
        Cliente cliente = new Cliente(false, true, 3);
        ItemPedido item = new ItemPedido("SKU-1", 10_000, 10, 1, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, "NAOEXISTE");
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
                () -> assertEquals("BLOQUEADO", resultado.status()),
                () -> assertEquals(0L, resultado.subtotalCentavos()),
                () -> assertEquals(0L, resultado.descontoCentavos()),
                () -> assertEquals(0L, resultado.freteCentavos()),
                () -> assertEquals(0L, resultado.totalCentavos()),
                () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void subtotalZeroComItensTodosInativosLancaExcecao() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido itemInativo = new ItemPedido("SKU-1", 10_000, 0, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(itemInativo), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> service.fechar(pedido, cliente));
    }

    @Test
    void estoqueInsuficienteRetornaSemEstoqueAntesDeAvaliarCupom() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("SKU-1", 10_000, 10, 1, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, "NAOEXISTE");
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
                () -> assertEquals("SEM_ESTOQUE", resultado.status()),
                () -> assertEquals(0L, resultado.subtotalCentavos()),
                () -> assertEquals(0L, resultado.descontoCentavos()),
                () -> assertEquals(0L, resultado.freteCentavos()),
                () -> assertEquals(0L, resultado.totalCentavos()),
                () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void riscoEmRevisaoNaoCobraPagamentoEMantemValoresCalculados() {
        Cliente novo = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("SKU-1", 1_000_000, 100, 100, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, novo);

        assertAll(
                () -> assertEquals("REVISAO", resultado.status()),
                () -> assertEquals(100_000_000L, resultado.subtotalCentavos()),
                () -> assertTrue(resultado.totalCentavos() > 100_000),
                () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void pagamentoRecusadoRetornaStatusPagamentoRecusado() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("SKU-1", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        PedidoService service = new PedidoService(total -> false);

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("PAGAMENTO_RECUSADO", resultado.status());
    }
}