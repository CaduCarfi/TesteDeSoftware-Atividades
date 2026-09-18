package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {

    @Test
    void processadorNuloLancaExcecaoNaConstrucao() {
        assertThrows(NullPointerException.class, () -> new PagamentoService(null));
    }

    @Test
    void totalNaoPositivoLancaExcecao() {
        PagamentoService service = new PagamentoService(total -> true);
        assertThrows(IllegalArgumentException.class, () -> service.pagar(0, 1));
    }

    @Test
    void maxTentativasForaDoLimiteLancaExcecao() {
        PagamentoService service = new PagamentoService(total -> true);
        assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 0));
        assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 4));
    }

    @Test
    void aprovacaoNaPrimeiraTentativaChamaUmaUnicaVez() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            return true;
        });
        assertTrue(service.pagar(10_000, 3));
        assertEquals(1, chamadas[0]);
    }

    @Test
    void recusaDefinitivaNaoRepeteAChamada() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            return false;
        });
        assertFalse(service.pagar(10_000, 3));
        assertEquals(1, chamadas[0]);
    }

    @Test
    void indisponibilidadeTemporariaPermiteNovaTentativaEDepoisAprova() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            if (chamadas[0] < 2) throw new IllegalStateException("indisponível");
            return true;
        });
        assertTrue(service.pagar(10_000, 3));
        assertEquals(2, chamadas[0]);
    }

    @Test
    void esgotarTentativasComIndisponibilidadeRetornaFalse() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            throw new IllegalStateException("sempre indisponível");
        });
        assertFalse(service.pagar(10_000, 3));
        assertEquals(3, chamadas[0]);
    }

    @Test
    void limiteDeUmaTentativaNaoRepeteAposIndisponibilidade() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            throw new IllegalStateException("indisponível");
        });
        assertFalse(service.pagar(10_000, 1));
        assertEquals(1, chamadas[0]);
    }

    @Test
    void outraExcecaoNaoEhCapturadaEPropaga() {
        PagamentoService service = new PagamentoService(total -> {
            throw new RuntimeException("falha inesperada");
        });
        assertThrows(RuntimeException.class, () -> service.pagar(10_000, 3));
    }
}