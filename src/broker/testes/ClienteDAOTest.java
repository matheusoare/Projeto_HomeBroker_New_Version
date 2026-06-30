package broker.testes;

import broker.modelos.Cliente;
import broker.persistencia.EntidadeDAO;
import broker.persistencia.PersistenceException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOTest {
    private EntidadeDAO<Cliente> dao;
    private static final String ARQUIVO_TESTE = "data/test_clientes.dat";

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
        Cliente c = new Cliente(1);
        dao.salvar(c);
        assertEquals(c, dao.carregar(1));
    }

    @Test
    void salvarComIdExistente() throws PersistenceException {
        dao.salvar(new Cliente(1));
        assertThrows(PersistenceException.class, () -> dao.salvar(new Cliente(1)));
        assertEquals(1, dao.carregarTodos().length);
    }

    @Test
    void atualizarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.atualizar(new Cliente(99)));
    }

    @Test
    void atualizarComIdExistente() throws PersistenceException {
        Cliente original = new Cliente(1);
        original.setCampos(new String[]{"João", "111.111.111-11"});
        dao.salvar(original);

        Cliente atualizado = new Cliente(1);
        atualizado.setCampos(new String[]{"Maria", "222.222.222-22"});
        dao.atualizar(atualizado);

        Cliente recuperado = dao.carregar(1);
        assertEquals("Maria", recuperado.getNome());
        assertEquals("222.222.222-22", recuperado.getCpf());
    }

    @Test
    void apagarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagarComIdExistente() throws PersistenceException {
        Cliente c = new Cliente(1);
        dao.salvar(c);
        Cliente removido = dao.apagar(1);
        assertEquals(c, removido);
        assertThrows(PersistenceException.class, () -> dao.carregar(1));
    }

    @Test
    void carregarTodosVazio() {
        assertThrows(PersistenceException.class, () -> dao.carregarTodos());
    }

    @Test
    void carregarComIdNaoExistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(99));
    }

    @Test
    void carregarComIdExistente() throws PersistenceException {
        Cliente c = new Cliente(1);
        dao.salvar(c);
        assertEquals(c, dao.carregar(1));
    }
}
