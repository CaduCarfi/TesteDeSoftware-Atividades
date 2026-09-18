package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void deveCriarItemValido() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 2, 5, 500, false);
        assertEquals(2_000L, item.totalCentavos());
        assertTrue(item.disponivel());
    }

    @Test
    void deveRejeitarSkuNulo() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido(null, 1_000, 1, 5, 500, false));
    }

    @Test
    void deveRejeitarSkuEmBranco() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("   ", 1_000, 1, 5, 500, false));
    }

    @ParameterizedTest
    @CsvSource({"0", "-1", "1000001"})
    void deveRejeitarPrecoForaDoLimite(long preco) {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU-1", preco, 1, 5, 500, false));
    }

    @ParameterizedTest
    @CsvSource({"1", "1000000"})
    void deveAceitarPrecoNosLimites(long preco) {
        assertDoesNotThrow(() -> new ItemPedido("SKU-1", preco, 1, 5, 500, false));
    }

    @ParameterizedTest
    @CsvSource({"-1", "101"})
    void deveRejeitarQuantidadeForaDoLimite(int quantidade) {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU-1", 1_000, quantidade, 200, 500, false));
    }

    @ParameterizedTest
    @CsvSource({"0", "100"})
    void deveAceitarQuantidadeNosLimites(int quantidade) {
        assertDoesNotThrow(() -> new ItemPedido("SKU-1", 1_000, quantidade, 200, 500, false));
    }

    @Test
    void deveRejeitarEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU-1", 1_000, 1, -1, 500, false));
    }

    @ParameterizedTest
    @CsvSource({"0", "-1", "100001"})
    void deveRejeitarPesoForaDoLimite(int peso) {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU-1", 1_000, 1, 5, peso, false));
    }

    @ParameterizedTest
    @CsvSource({"1", "100000"})
    void deveAceitarPesoNosLimites(int peso) {
        assertDoesNotThrow(() -> new ItemPedido("SKU-1", 1_000, 1, 5, peso, false));
    }

    @Test
    void itemInativoTemTotalZeroENaoAfetaEstoque() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 0, 5, 500, false);
        assertEquals(0L, item.totalCentavos());
        assertTrue(item.disponivel());
    }

    @Test
    void itemIndisponivelQuandoQuantidadeMaiorQueEstoque() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 6, 5, 500, false);
        assertFalse(item.disponivel());
    }

    @Test
    void itemDisponivelQuandoQuantidadeIgualEstoque() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 5, 5, 500, false);
        assertTrue(item.disponivel());
    }
}
