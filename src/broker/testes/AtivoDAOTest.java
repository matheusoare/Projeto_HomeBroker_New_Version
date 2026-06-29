package broker.testes;

import broker.modelos.Ativo;
import broker.persistencia.EntidadeDAO;
import broker.persistencia.PersistenceException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AtivoDAOTest {
    private EntidadeDAO<Ativo> dao;
    private static final String ARQUIVO_TESTE = "data/test_ativos.dat";

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
        Ativo a = new Ativo(1);
        dao.salvar(a);
        assertEquals(a, dao.carregar(1));
    }

    @Test
    void salvarComIdExistente() throws PersistenceException {
        dao.salvar(new Ativo(1));
        assertThrows(PersistenceException.class, () -> dao.salvar(new Ativo(1)));
        assertEquals(1, dao.carregarTodos().length);
    }

    @Test
    void atualizarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.atualizar(new Ativo(99)));
    }

    @Test
    void atualizarComIdExistente() throws PersistenceException {
        dao.salvar(new Ativo(1));
        dao.atualizar(new Ativo(1));
        assertNotNull(dao.carregar(1));
    }

    @Test
    void apagarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagarComIdExistente() throws PersistenceException {
        Ativo a = new Ativo(1);
        dao.salvar(a);
        Ativo removido = dao.apagar(1);
        assertEquals(a, removido);
        assertThrows(PersistenceException.class, () -> dao.carregar(1));
    }

    @Test
    void carregarTodosVazio() throws PersistenceException {
        assertEquals(0, dao.carregarTodos().length);
    }

    @Test
    void carregarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(99));
    }

    @Test
    void carregarComIdExistente() throws PersistenceException {
        Ativo a = new Ativo(1);
        dao.salvar(a);
        assertEquals(a, dao.carregar(1));
    }
}
