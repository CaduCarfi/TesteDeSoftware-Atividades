package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {

    private final AnaliseRisco risco = new AnaliseRisco();

    @Test
    void totalNegativoLancaExcecao() {
        Cliente cliente = new Cliente(false, false, 0);
        assertThrows(IllegalArgumentException.class, () -> risco.avaliar(cliente, -1, false));
    }

    @Test
    void clienteBloqueadoEhSempreRecusado() {
        Cliente cliente = new Cliente(false, true, 10);
        assertEquals("RECUSADO", risco.avaliar(cliente, 1, false));
    }

    @Test
    void semHistoricoComTotalAcimaDoLimiteVaiParaRevisao() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("REVISAO", risco.avaliar(novo, 100_001, false));
    }

    @Test
    void semHistoricoComEntregaExpressaVaiParaRevisaoMesmoComTotalBaixo() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("REVISAO", risco.avaliar(novo, 1, true));
    }

    @Test
    void semHistoricoComTotalNoLimiteENormalEhAprovado() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("APROVADO", risco.avaliar(novo, 100_000, false));
    }

    @Test
    void comHistoricoNaoVipComTotalAcimaDoLimiteVaiParaRevisao() {
        Cliente antigo = new Cliente(false, false, 5);
        assertEquals("REVISAO", risco.avaliar(antigo, 500_001, false));
    }

    @Test
    void comHistoricoVipComTotalAltoEhAprovado() {
        Cliente vip = new Cliente(true, false, 5);
        assertEquals("APROVADO", risco.avaliar(vip, 500_001, false));
    }

    @Test
    void comHistoricoComTotalNoLimiteEhAprovado() {
        Cliente antigo = new Cliente(false, false, 5);
        assertEquals("APROVADO", risco.avaliar(antigo, 500_000, false));
    }
}