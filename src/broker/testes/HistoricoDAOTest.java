package broker.testes;

import broker.modelos.Historico;
import broker.persistencia.EntidadeDAO;
import broker.persistencia.PersistenceException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class HistoricoDAOTest {
    private EntidadeDAO<Historico> dao;
    private static final String ARQUIVO_TESTE = "data/test_historicos.dat";

    @BeforeEach
    void setUp() {
        dao = new EntidadeDAO<>(ARQUIVO_TESTE);
    }

    @AfterEach
    void tearDown() {
        new java.io.File(ARQUIVO_TESTE).delete();
    }

    @Test
    void salvarComIdNovo() throws PersistenceException {
        Historico h = new Historico(1);
        dao.salvar(h);
        assertEquals(h, dao.carregar(1));
    }

    @Test
    void salvarComIdExistente() throws PersistenceException {
        dao.salvar(new Historico(1));
        assertThrows(PersistenceException.class, () -> dao.salvar(new Historico(1)));
        assertEquals(1, dao.carregarTodos().length);
    }

    @Test
    void atualizarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.atualizar(new Historico(99)));
    }

    @Test
    void atualizarComIdExistente() throws PersistenceException {
        dao.salvar(new Historico(1));
        dao.atualizar(new Historico(1));
        assertNotNull(dao.carregar(1));
    }

    @Test
    void apagarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagarComIdExistente() throws PersistenceException {
        Historico h = new Historico(1);
        dao.salvar(h);
        Historico removido = dao.apagar(1);
        assertEquals(h, removido);
        assertThrows(PersistenceException.class, () -> dao.carregar(1));
    }

    @Test
    void carregarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(99));
    }

    @Test
    void carregarComIdExistente() throws PersistenceException {
        Historico h = new Historico(1);
        dao.salvar(h);
        assertEquals(h, dao.carregar(1));
    }
}
