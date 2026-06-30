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
        Ordem original = new Ordem(1);
        original.setCampos(new String[]{"1", "1", "COMPRA", "10", "50.0"});
        dao.salvar(original);

        Ordem atualizada = new Ordem(1);
        atualizada.setCampos(new String[]{"2", "2", "VENDA", "20", "100.0"});
        dao.atualizar(atualizada);

        Ordem recuperada = dao.carregar(1);
        assertEquals("VENDA", recuperada.getTipo());
        assertEquals(20, recuperada.getQuantidade());
        assertEquals(100.0, recuperada.getPrecoLimite());
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
    void carregarTodosVazio() {
        assertThrows(PersistenceException.class, () -> dao.carregarTodos());
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
