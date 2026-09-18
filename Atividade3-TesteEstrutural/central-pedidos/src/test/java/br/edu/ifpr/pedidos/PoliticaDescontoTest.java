package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {

    private final PoliticaDesconto politica = new PoliticaDesconto();

    @Test
    void subtotalNegativoLancaExcecao() {
        Cliente comum = new Cliente(false, false, 0);
        assertThrows(IllegalArgumentException.class, () -> politica.calcular(comum, -1, null));
    }

    @Test
    void vipRecebeDezPorCentoSemCupom() {
        Cliente vip = new Cliente(true, false, 3);
        assertEquals(1_000L, politica.calcular(vip, 10_000, null));
    }

    @Test
    void comumComSubtotalAbaixoDoLimiteNaoRecebeDesconto() {
        Cliente comum = new Cliente(false, false, 3);
        assertEquals(0L, politica.calcular(comum, 49_999, null));
    }

    @Test
    void comumComSubtotalNoLimiteRecebeCincoPorCento() {
        Cliente comum = new Cliente(false, false, 3);
        assertEquals(2_500L, politica.calcular(comum, 50_000, null));
    }

    @Test
    void cupomNuloMantemDescontoBase() {
        Cliente comum = new Cliente(false, false, 3);
        assertEquals(2_500L, politica.calcular(comum, 50_000, null));
    }

    @Test
    void cupomEmBrancoMantemDescontoBase() {
        Cliente comum = new Cliente(false, false, 3);
        assertEquals(2_500L, politica.calcular(comum, 50_000, "   "));
    }

    @Test
    void bemVindoSomaVinteReaisParaClienteNovoComSubtotalSuficiente() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals(2_000L, politica.calcular(novo, 10_000, "bemvindo"));
    }

    @Test
    void bemVindoNormalizaEspacosEMaiusculas() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals(2_000L, politica.calcular(novo, 10_000, "  BemVindo  "));
    }

    @Test
    void bemVindoNaoSomaSeClienteJaComprouAntes() {
        Cliente antigo = new Cliente(false, false, 1);
        assertEquals(0L, politica.calcular(antigo, 10_000, "BEMVINDO"));
    }

    @Test
    void bemVindoNaoSomaSeSubtotalAbaixoDoLimite() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals(0L, politica.calcular(novo, 9_999, "BEMVINDO"));
    }

    @Test
    void extra10SomaDezPorCentoQuandoElegivel() {
        Cliente comum = new Cliente(false, false, 3);
        assertEquals(2_000L, politica.calcular(comum, 20_000, "EXTRA10"));
    }

    @Test
    void extra10NaoSomaSeSubtotalAbaixoDoLimite() {
        Cliente comum = new Cliente(false, false, 3);
        assertEquals(0L, politica.calcular(comum, 19_999, "EXTRA10"));
    }

    @Test
    void cupomConhecidoSemElegibilidadeNaoAcrescentaDesconto() {
        Cliente antigo = new Cliente(false, false, 5);
        assertEquals(0L, politica.calcular(antigo, 9_999, "BEMVINDO"));
    }

    @Test
    void cupomDesconhecidoLancaExcecao() {
        Cliente comum = new Cliente(false, false, 3);
        assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(comum, 10_000, "NAOEXISTE"));
    }

    @Test
    void descontoCombinadoNoLimiteDeVintePorCentoNaoEhTruncado() {
        Cliente vip = new Cliente(true, false, 3);
        long subtotal = 100_000;
        assertEquals(20_000L, politica.calcular(vip, subtotal, "EXTRA10"));
    }

    @Test
    void descontoCombinadoAcimaDoTetoEhTruncadoEmVintePorCento() {
        Cliente vipNovo = new Cliente(true, false, 0);
        long subtotal = 10_000;
        assertEquals(2_000L, politica.calcular(vipNovo, subtotal, "BEMVINDO"));
    }
}