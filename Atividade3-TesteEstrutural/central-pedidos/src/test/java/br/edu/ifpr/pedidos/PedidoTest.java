package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private ItemPedido item(String sku, long preco, int quantidade, int estoque, int peso, boolean fragil) {
        return new ItemPedido(sku, preco, quantidade, estoque, peso, fragil);
    }

    @Test
    void deveRejeitarListaDeItensNula() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(null, "PR", false, null));
    }

    @Test
    void deveRejeitarElementoNuloNaLista() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item("SKU-1", 1_000, 1, 5, 500, false));
        itens.add(null);
        assertThrows(NullPointerException.class, () -> new Pedido(itens, "PR", false, null));
    }

    @Test
    void deveRejeitarListaComMaisDeCemItens() {
        List<ItemPedido> itens = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            itens.add(item("SKU-" + i, 1_000, 1, 5, 500, false));
        }
        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, "PR", false, null));
    }

    @Test
    void deveAceitarListaComExatamenteCemItens() {
        List<ItemPedido> itens = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            itens.add(item("SKU-" + i, 1_000, 1, 5, 500, false));
        }
        assertDoesNotThrow(() -> new Pedido(itens, "PR", false, null));
    }

    @Test
    void deveAceitarListaVaziaNaConstrucao() {
        assertDoesNotThrow(() -> new Pedido(List.of(), "PR", false, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"P", "PRO", "pr", "P1", "1R"})
    void deveRejeitarUfInvalida(String uf) {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 1, 5, 500, false));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, uf, false, null));
    }

    @Test
    void deveRejeitarUfNula() {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 1, 5, 500, false));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, null, false, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PR", "SP", "RJ", "MG", "BA"})
    void deveAceitarUfValidaDeDuasLetrasMaiusculas(String uf) {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 1, 5, 500, false));
        assertDoesNotThrow(() -> new Pedido(itens, uf, false, null));
    }

    @Test
    void deveCopiarListaDefensivamente() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item("SKU-1", 1_000, 1, 5, 500, false));
        Pedido pedido = new Pedido(itens, "PR", false, null);
        itens.add(item("SKU-2", 1_000, 1, 5, 500, false));
        assertEquals(1, pedido.itens().size());
    }

    @Test
    void subtotalIgnoraItensInativos() {
        List<ItemPedido> itens = List.of(
                item("SKU-1", 1_000, 2, 5, 500, false),
                item("SKU-2", 5_000, 0, 5, 500, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertEquals(2_000L, pedido.subtotalCentavos());
    }

    @Test
    void subtotalComListaVaziaEhZero() {
        Pedido pedido = new Pedido(List.of(), "PR", false, null);
        assertEquals(0L, pedido.subtotalCentavos());
    }

    @Test
    void pesoGramasSomaTodosOsItensAtivos() {
        List<ItemPedido> itens = List.of(
                item("SKU-1", 1_000, 2, 5, 500, false),
                item("SKU-2", 1_000, 3, 5, 200, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertEquals(2 * 500 + 3 * 200, pedido.pesoGramas());
    }

    @Test
    void pesoGramasIgnoraContribuicaoDeItemInativo() {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 0, 5, 500, false));
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertEquals(0, pedido.pesoGramas());
    }

    @Test
    void temFragilTrueQuandoItemAtivoFragil() {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 1, 5, 500, true));
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertTrue(pedido.temFragil());
    }

    @Test
    void temFragilFalseQuandoItemFragilEstaInativo() {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 0, 5, 500, true));
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertFalse(pedido.temFragil());
    }

    @Test
    void temFragilFalseSemItensFrageis() {
        List<ItemPedido> itens = List.of(item("SKU-1", 1_000, 1, 5, 500, false));
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertFalse(pedido.temFragil());
    }

    @Test
    void estoqueSuficienteTrueQuandoTodosOsItensDisponiveis() {
        List<ItemPedido> itens = List.of(
                item("SKU-1", 1_000, 2, 5, 500, false),
                item("SKU-2", 1_000, 1, 1, 500, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertTrue(pedido.estoqueSuficiente());
    }

    @Test
    void estoqueInsuficienteQuandoPrimeiroItemFaltaEstoque() {
        List<ItemPedido> itens = List.of(
                item("SKU-1", 1_000, 6, 5, 500, false),
                item("SKU-2", 1_000, 1, 1, 500, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertFalse(pedido.estoqueSuficiente());
    }

    @Test
    void estoqueInsuficienteQuandoUltimoItemFaltaEstoque() {
        List<ItemPedido> itens = List.of(
                item("SKU-1", 1_000, 1, 5, 500, false),
                item("SKU-2", 1_000, 6, 5, 500, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);
        assertFalse(pedido.estoqueSuficiente());
    }

    @Test
    void estoqueSuficienteComListaVazia() {
        Pedido pedido = new Pedido(List.of(), "PR", false, null);
        assertTrue(pedido.estoqueSuficiente());
    }
}