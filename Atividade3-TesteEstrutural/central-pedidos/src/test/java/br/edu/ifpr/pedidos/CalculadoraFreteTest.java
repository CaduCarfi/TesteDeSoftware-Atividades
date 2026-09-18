package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {

    private final CalculadoraFrete calculadora = new CalculadoraFrete();

    private ItemPedido item(int pesoGramas, boolean fragil) {
        return new ItemPedido("SKU-1", 10_000, 1, 5, pesoGramas, fragil);
    }

    private Pedido pedido(String uf, boolean expresso, int pesoGramas, boolean fragil) {
        return new Pedido(List.of(item(pesoGramas, fragil)), uf, expresso, null);
    }

    private Cliente comum() {
        return new Cliente(false, false, 1);
    }

    private Cliente vip() {
        return new Cliente(true, false, 1);
    }

    @Test
    void valorLiquidoNegativoLancaExcecao() {
        Pedido pedido = pedido("PR", false, 1_000, false);
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcular(pedido, comum(), -1));
    }

    @Test
    void freteBaseParaUfPr() {
        Pedido pedido = pedido("PR", false, 1_000, false);
        assertEquals(1_200L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void freteBaseParaUfSp() {
        Pedido pedido = pedido("SP", false, 1_000, false);
        assertEquals(2_000L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void freteBaseParaUfRj() {
        Pedido pedido = pedido("RJ", false, 1_000, false);
        assertEquals(2_000L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void freteBaseParaUfDemais() {
        Pedido pedido = pedido("MG", false, 1_000, false);
        assertEquals(3_000L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void semAdicionalDePesoAteDoisQuilosExatos() {
        Pedido pedido = pedido("PR", false, 2_000, false);
        assertEquals(1_200L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void umaFracaoAcimaDeDoisQuilosSomaUmaParcela() {
        Pedido pedido = pedido("PR", false, 2_001, false);
        assertEquals(1_200L + 300L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void umQuiloExcedenteExatoSomaUmaParcela() {
        Pedido pedido = pedido("PR", false, 3_000, false);
        assertEquals(1_200L + 300L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void variasParcelasDePesoExcedenteComFracaoFinal() {
        // excedente = 5_200 - 2_000 = 3_200 -> 4 iterações (1000,1000,1000,200)
        Pedido pedido = pedido("PR", false, 5_200, false);
        assertEquals(1_200L + 4 * 300L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void freteGratisComValorLiquidoNoLimiteEEntregaNormal() {
        Pedido pedido = pedido("PR", false, 1_000, false);
        assertEquals(0L, calculadora.calcular(pedido, comum(), 30_000));
    }

    @Test
    void freteNaoZeraComValorLiquidoAbaixoDoLimite() {
        Pedido pedido = pedido("PR", false, 1_000, false);
        assertEquals(1_200L, calculadora.calcular(pedido, comum(), 29_999));
    }

    @Test
    void freteNaoZeraQuandoEntregaExpressaMesmoComValorAlto() {
        Pedido pedido = pedido("PR", true, 1_000, false);
        assertEquals(1_200L + 1_500L, calculadora.calcular(pedido, comum(), 30_000));
    }

    @Test
    void vipPagaMetadeDoFreteBase() {
        Pedido pedido = pedido("PR", false, 1_000, false);
        assertEquals(600L, calculadora.calcular(pedido, vip(), 1_000));
    }

    @Test
    void vipPagaMetadeMesmoComPesoExcedente() {
        Pedido pedido = pedido("PR", false, 3_000, false);
        assertEquals((1_200L + 300L) / 2, calculadora.calcular(pedido, vip(), 1_000));
    }

    @Test
    void adicionalExpressoSomaQuinzeReais() {
        Pedido pedido = pedido("PR", true, 1_000, false);
        assertEquals(1_200L + 1_500L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void adicionalFragilSomaCincoReaisUmaUnicaVez() {
        Pedido pedido = pedido("PR", false, 1_000, true);
        assertEquals(1_200L + 500L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void adicionaisDeExpressoEFragilSaoCombinaveis() {
        Pedido pedido = pedido("PR", true, 1_000, true);
        assertEquals(1_200L + 1_500L + 500L, calculadora.calcular(pedido, comum(), 1_000));
    }

    @Test
    void adicionalFragilIncideMesmoComFreteGratuito() {
        Pedido pedido = pedido("PR", false, 1_000, true);
        assertEquals(500L, calculadora.calcular(pedido, comum(), 30_000));
    }
}