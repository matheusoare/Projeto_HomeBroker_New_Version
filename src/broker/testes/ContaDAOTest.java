package broker.testes;

import broker.modelos.Conta;
import broker.persistencia.EntidadeDAO;
import broker.persistencia.PersistenceException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ContaDAOTest {
    private EntidadeDAO<Conta> dao;
    private static final String ARQUIVO_TESTE = "data/test_contas.dat";

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
        Conta c = new Conta(1);
        dao.salvar(c);
        assertEquals(c, dao.carregar(1));
    }

    @Test
    void salvarComIdExistente() throws PersistenceException {
        dao.salvar(new Conta(1));
        assertThrows(PersistenceException.class, () -> dao.salvar(new Conta(1)));
        assertEquals(1, dao.carregarTodos().length);
    }

    @Test
    void atualizarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.atualizar(new Conta(99)));
    }

    @Test
    void atualizarComIdExistente() throws PersistenceException {
        dao.salvar(new Conta(1));
        dao.atualizar(new Conta(1));
        assertNotNull(dao.carregar(1));
    }

    @Test
    void apagarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagarComIdExistente() throws PersistenceException {
        Conta c = new Conta(1);
        dao.salvar(c);
        Conta removida = dao.apagar(1);
        assertEquals(c, removida);
        assertThrows(PersistenceException.class, () -> dao.carregar(1));
    }

    @Test
    void carregarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(99));
    }

    @Test
    void carregarComIdExistente() throws PersistenceException {
        Conta c = new Conta(1);
        dao.salvar(c);
        assertEquals(c, dao.carregar(1));
    }
}
