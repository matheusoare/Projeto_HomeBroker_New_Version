package broker.testes;

import broker.modelos.Ordem;
import broker.persistencia.EntidadeDAO;
import broker.persistencia.PersistenceException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class OrdemDAOTest {
    private EntidadeDAO<Ordem> dao;
    private static final String ARQUIVO_TESTE = "data/test_ordens.dat";

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
        Ordem o = new Ordem(1);
        dao.salvar(o);
        assertEquals(o, dao.carregar(1));
    }

    @Test
    void salvarComIdExistente() throws PersistenceException {
        dao.salvar(new Ordem(1));
        assertThrows(PersistenceException.class, () -> dao.salvar(new Ordem(1)));
        assertEquals(1, dao.carregarTodos().length);
    }

    @Test
    void atualizarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.atualizar(new Ordem(99)));
    }

    @Test
    void atualizarComIdExistente() throws PersistenceException {
        dao.salvar(new Ordem(1));
        dao.atualizar(new Ordem(1));
        assertNotNull(dao.carregar(1));
    }

    @Test
    void apagarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagarComIdExistente() throws PersistenceException {
        Ordem o = new Ordem(1);
        dao.salvar(o);
        Ordem removida = dao.apagar(1);
        assertEquals(o, removida);
        assertThrows(PersistenceException.class, () -> dao.carregar(1));
    }

    @Test
    void carregarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(99));
    }

    @Test
    void carregarComIdExistente() throws PersistenceException {
        Ordem o = new Ordem(1);
        dao.salvar(o);
        assertEquals(o, dao.carregar(1));
    }
}
