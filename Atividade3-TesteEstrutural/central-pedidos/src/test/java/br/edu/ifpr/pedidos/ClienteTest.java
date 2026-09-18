package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveCriarClienteComHistoricoZero() {
        Cliente cliente = new Cliente(false, false, 0);
        assertEquals(0, cliente.comprasAnteriores());
        assertFalse(cliente.vip());
        assertFalse(cliente.bloqueado());
    }

    @Test
    void deveCriarClienteVipBloqueadoComHistoricoPositivo() {
        Cliente cliente = new Cliente(true, true, 42);
        assertTrue(cliente.vip());
        assertTrue(cliente.bloqueado());
        assertEquals(42, cliente.comprasAnteriores());
    }

    @Test
    void deveRejeitarHistoricoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente(false, false, -1));
    }
}
